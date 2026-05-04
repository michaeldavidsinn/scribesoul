package com.scribesoul.app.models


import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Color) = encoder.encodeLong(value.value.toLong())
    override fun deserialize(decoder: Decoder): Color = Color(decoder.decodeLong().toULong())
}

object OffsetSerializer : KSerializer<Offset> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Offset", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Offset) = encoder.encodeString("${value.x},${value.y}")
    override fun deserialize(decoder: Decoder): Offset {
        val parts = decoder.decodeString().split(",")
        return Offset(parts[0].toFloat(), parts[1].toFloat())
    }
}

object SizeSerializer : KSerializer<Size> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Size", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Size) = encoder.encodeString("${value.width},${value.height}")
    override fun deserialize(decoder: Decoder): Size {
        val parts = decoder.decodeString().split(",")
        return Size(parts[0].toFloat(), parts[1].toFloat())
    }
}

object UriSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Uri) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Uri = Uri.parse(decoder.decodeString())
}