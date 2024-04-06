package com.chepics.chepics.feature.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun GridImagesView(imageUrlList: List<String>, onTapImage: (Int) -> Unit) {
    Column {
        if (imageUrlList.size > 1) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .height(if (imageUrlList.size == 4) (getHeight() * 2 + 48.dp) else getHeight() + 32.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(imageUrlList.size) {index ->
                    if (!(imageUrlList.size % 2 != 0 && index == imageUrlList.size - 1)) {
                        AsyncImage(
                            model = imageUrlList[index],
                            contentDescription = "$index image",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onTapImage(index) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        if (imageUrlList.size % 2 == 1) {
            if (imageUrlList.size == 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            AsyncImage(
                model = imageUrlList.last(),
                contentDescription = "last image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(getHeight())
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onTapImage(imageUrlList.size - 1) },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun getHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 48) / 2).dp
}