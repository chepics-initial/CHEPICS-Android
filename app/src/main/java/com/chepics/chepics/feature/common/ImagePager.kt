package com.chepics.chepics.feature.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePager(
    index: Int,
    imageUrls: List<String>,
    onDismiss: () -> Unit
) {
    // 垂直方向へのスワイプで画面を閉じるために `VerticalPager` も用いている
    val verticalPagerState = rememberPagerState(1, pageCount = { 3 })
    val horizontalPagerState = rememberPagerState(index, pageCount = { imageUrls.size })
    VerticalPager(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        state = verticalPagerState,
    ) { verticalPage ->
        if (verticalPage == 1) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = horizontalPagerState
            ) { horizontalPage ->
                ImagePagerPage(imageUrl = imageUrls[horizontalPage])
            }
        }
    }

    LaunchedEffect(verticalPagerState.currentPage) {
        if (verticalPagerState.currentPage != 1) {
            onDismiss()
        }
    }
}

@Composable
private fun ImagePagerPage(imageUrl: String) {
    val gestureState = remember { GestureState() }
    val scope = rememberCoroutineScope()
    AsyncImage(
        model = imageUrl,
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { size ->
                gestureState.layoutSize = size.toSize()
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGestureStart = { gestureState.onGestureStart() },
                    onGesture = { centroid, pan, zoom, uptimeMillis ->
                        // 画像端を超えてドラッグしようとしている場合は false を返すことでタッチイベントを消費しない
                        val canConsume = gestureState.canConsumeGesture(pan = pan, zoom = zoom)
                        if (canConsume) {
                            scope.launch {
                                gestureState.applyGesture(
                                    pan = pan,
                                    zoom = zoom,
                                    position = centroid,
                                    uptimeMillis = uptimeMillis,
                                )
                            }
                        }
                        canConsume
                    },
                    onGestureEnd = { scope.launch { gestureState.onGestureEnd() } }
                )
            }
            .graphicsLayer {
                scaleX = gestureState.scale
                scaleY = gestureState.scale
                translationX = gestureState.offsetX
                translationY = gestureState.offsetY
            },
        onState = { state ->
            if (state is AsyncImagePainter.State.Success) {
                gestureState.imageSize = state.painter.intrinsicSize
            }
        },
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}

@Stable
private class GestureState {
    private var _scale = Animatable(1f).apply { updateBounds(1f, 2.5f) }
    val scale: Float
        get() = _scale.value

    private var _offsetX = Animatable(0f)
    val offsetX: Float
        get() = _offsetX.value

    private var _offsetY = Animatable(0f)
    val offsetY: Float
        get() = _offsetY.value

    var layoutSize = Size.Zero
    var imageSize = Size.Zero

    private var eventConsumingState = EventConsumingState.Idle

    private val velocityTracker = VelocityTracker()
    private val velocityDecay = exponentialDecay<Float>()
    private var shouldFling = true
    private var animationJob: Job? = null

    private val fitImageSize: Size
        get() = if (imageSize == Size.Zero || layoutSize == Size.Zero) {
            Size.Zero
        } else {
            val imageAspectRatio = imageSize.width / imageSize.height
            val layoutAspectRatio = layoutSize.width / layoutSize.height
            if (imageAspectRatio > layoutAspectRatio) {
                imageSize * (layoutSize.width / imageSize.width)
            } else {
                imageSize * (layoutSize.height / imageSize.height)
            }
        }

    fun onGestureStart() {
        eventConsumingState = EventConsumingState.Idle
    }

    fun canConsumeGesture(pan: Offset, zoom: Float) = when (eventConsumingState) {
        EventConsumingState.Idle -> {
            if (zoom != 1f) {
                // ズーム操作のジェスチャーは常にハンドリングする
                eventConsumingState = EventConsumingState.Active
                true
            } else if (scale == 1f) {
                // ズームしていない場合はタッチイベントを常に消費しない
                eventConsumingState = EventConsumingState.Ignore
                false
            } else {
                // 画像端を超えてドラッグしようとした場合はタッチイベントを消費しない
                // 明確に水平 or 垂直方向に動かしていない場合は無視するために、移動量の縦横の比率が充分に高い場合だけハンドリングする
                val isPanningHorizontally = abs(pan.x) / abs(pan.y) > 3
                val isPanningHorizontallyOverLowerBound = pan.x < 0 && offsetX == _offsetX.lowerBound
                val isPanningHorizontallyOverUpperBound = pan.x > 0 && offsetX == _offsetX.upperBound
                val isPanningVertically = abs(pan.y) / abs(pan.x) > 3
                val isPanningVerticallyOverLowerBound = pan.y < 0 && offsetY == _offsetY.lowerBound
                val isPanningVerticallyOverUpperBound = pan.y > 0 && offsetY == _offsetY.upperBound
                if (isPanningHorizontally && (isPanningHorizontallyOverLowerBound || isPanningHorizontallyOverUpperBound)) {
                    eventConsumingState = EventConsumingState.Ignore
                    false
                } else if (isPanningVertically && (isPanningVerticallyOverLowerBound || isPanningVerticallyOverUpperBound)) {
                    eventConsumingState = EventConsumingState.Ignore
                    false
                } else {
                    eventConsumingState = EventConsumingState.Active
                    true
                }
            }
        }
        EventConsumingState.Active -> true
        EventConsumingState.Ignore -> false
    }

    suspend fun applyGesture(pan: Offset, zoom: Float, position: Offset, uptimeMillis: Long) = coroutineScope {
        animationJob?.cancel()
        animationJob = launch {
            launch { _scale.snapTo(scale * zoom) }

            val boundX = max((fitImageSize.width * scale - layoutSize.width), 0f) / 2f
            _offsetX.updateBounds(-boundX, boundX)
            launch { _offsetX.snapTo(offsetX + pan.x) }

            val boundY = max((fitImageSize.height * scale - layoutSize.height), 0f) / 2f
            _offsetY.updateBounds(-boundY, boundY)
            launch { _offsetY.snapTo(offsetY + pan.y) }

            velocityTracker.addPosition(uptimeMillis, position)

            if (zoom != 1f) {
                shouldFling = false
            }
        }
    }

    suspend fun onGestureEnd() = coroutineScope {
        animationJob?.cancel()
        animationJob = launch {
            if (shouldFling) {
                val velocity = velocityTracker.calculateVelocity()

                val boundX = max((fitImageSize.width * scale - layoutSize.width), 0f) / 2f
                _offsetX.updateBounds(-boundX, boundX)
                launch { _offsetX.animateDecay(velocity.x, velocityDecay) }

                val boundY = max((fitImageSize.height * scale - layoutSize.height), 0f) / 2f
                _offsetY.updateBounds(-boundY, boundY)
                launch { _offsetY.animateDecay(velocity.y, velocityDecay) }
            }

            shouldFling = true

            if (scale < 1f) {
                launch { _scale.animateTo(1f) }
            }
        }
    }

    private enum class EventConsumingState {
        Idle, Active, Ignore
    }
}

private suspend fun PointerInputScope.detectTransformGestures(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, uptimeMillis: Long) -> Boolean,
    onGestureStart: () -> Unit = {},
    onGestureEnd: () -> Unit = {},
) {
    awaitEachGesture {
        val touchSlop = TouchSlop(viewConfiguration.touchSlop)
        awaitFirstDown(requireUnconsumed = false)
        onGestureStart()
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.any { it.isConsumed }
            if (!canceled) {
                val zoomChange = event.calculateZoom()
                val panChange = event.calculatePan()

                if (touchSlop.isPast(event)) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    if (zoomChange != 1f || panChange != Offset.Zero) {
                        val uptimeMillis = event.changes[0].uptimeMillis

                        // コールバックの戻り値が true である場合のみタッチイベントを消費する
                        val isConsumed = onGesture(centroid, panChange, zoomChange, uptimeMillis)
                        if (isConsumed) {
                            event.changes.forEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.any { it.pressed })

        onGestureEnd()
    }
}

/**
 * ジェスチャーの移動量がごく少量の場合は `onGesture` のコールバックを発火しないようにするための実装
 */
private class TouchSlop(private val threshold: Float) {
    private var zoom = 1f
    private var pan = Offset.Zero
    private var isPast = false

    fun isPast(event: PointerEvent): Boolean {
        if (isPast) {
            return true
        }

        zoom *= event.calculateZoom()
        pan += event.calculatePan()
        val zoomMotion = abs(1 - zoom) * event.calculateCentroidSize(useCurrent = false)
        val panMotion = pan.getDistance()
        isPast = zoomMotion > threshold || panMotion > threshold
        return isPast
    }
}