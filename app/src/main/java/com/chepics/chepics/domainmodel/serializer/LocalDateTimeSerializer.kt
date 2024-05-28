package com.chepics.chepics.domainmodel.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    private const val SERIALIZE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val zone = ZoneId.of("Asia/Tokyo")
        val zoneOffSet = zone.rules.getOffset(value)
        val offsetDateTime = value.atOffset(zoneOffSet)
        val formatter = DateTimeFormatter.ofPattern(SERIALIZE_PATTERN)
        encoder.encodeString(offsetDateTime?.format(formatter) ?: "")
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val decodeStr = decoder.decodeString()

        // Offset有り/無しどちらもdeserializeできるよう,DateTimeFormatter.ISO_DATE_TIMEを使用
        val parsedDateTime = LocalDateTime.parse(decodeStr, DateTimeFormatter.ISO_DATE_TIME)

        // Offset無しの場合はOffsetを付与したLocalDateTimeに変換
        val zone = ZoneId.of("Asia/Tokyo")
        val zoneOffSet = zone.rules.getOffset(parsedDateTime)
        return parsedDateTime.atOffset(zoneOffSet).toLocalDateTime()
    }
}