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

fun ByteArray.toNumber(): Number {
	class NoNumberConversionException(message: String = "No number conversion for this ByteArray."): Exception(message)
	var num = 0L
	this.forEach { byte -> num = (byte.toLong() shl (this.size * 8)) or num }
	return num

}