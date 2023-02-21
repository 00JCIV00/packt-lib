package PacktUtils
// STANDALONE

fun validateIP(strIP: String): String {
	class InvalidIPException(message: String = "The provided IP is invalid. IP: $strIP") : Exception(message)
	if (!Regex("^((?:(?:^|\\.)(?:\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){4})\$").containsMatchIn(strIP))
		throw InvalidIPException()
	return strIP
}


// STRING

fun String.toBits(): List<Int> {
	return buildString {
		this@toBits.forEach { char ->
			val bits = char.code.toString(2).padStart(8, '0')
			append(bits)
		}
	}.chunked(32).map { it.toInt(2) }
}

fun String.toIPBytes(): ByteArray {
	return buildList {
		this@toIPBytes.split(Regex("[. ]")).forEach { add(it.toInt().toByte()) }
	}.toByteArray()
}

fun String.toIPInt(): Int {
	return this.toIPBytes().toNumber().toInt()
}

// BYTEARRAY

fun ByteArray.toNumber(): ULong {
	class NoNumberConversionException(message: String = "No number conversion for this ByteArray. The size of the ByteArray is greater than 8 bytes.") :
		Exception(message)
	if (this.size > 8) throw NoNumberConversionException()
	var num: ULong = 0u
	this.reversed().forEachIndexed { idx, byte ->
		val uLong = byte.toULong() and 0b11111111u
		num = (uLong shl (idx * 8)) or num
	}
	return num
}

// NUMBER
fun Number.toByteArray(bytes: Int): ByteArray {
	val num = this.toLong()
	return buildList {
		for (b in 0..<bytes) {
			add((num shr (b * 8)).toByte())
		}
	}.toByteArray()
}
