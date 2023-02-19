import java.util.BitSet
import kotlin.reflect.full.memberProperties

/**
 * Immutable BitField Wrapper for Java's BitSet
 */
data class BitField(val bits: BitSet, val size: Int) {
	companion object {
		fun from(data: Any,
				 size: Int = (data::class.memberProperties.firstOrNull { it.name == "SIZE_BITS" } ?: throw NoByteArrayConversionException()) as Int,
				 truncate: Boolean = true): BitField {
			val byteArray = when (data) {
				is ByteArray -> data
				is Byte -> listOf(data).toByteArray()
				is UByte -> listOf(data.toByte()).toByteArray()
				is Short  -> data.toByteArray(2)
				is UShort -> data.toShort().toByteArray(2)
				is Int  -> data.toByteArray(4)
				is UInt  -> data.toInt().toByteArray(4)
				is Long -> data.toByteArray(8)
				is ULong -> data.toLong().toByteArray(8)
				is String -> data.toByteArray()
				else -> throw NoByteArrayConversionException()
			}

			var bits = BitSet.valueOf(byteArray)

			if (bits.length() > size && truncate) {
				val lastIdx = if (size > 0) size else 1
				bits = bits[0, lastIdx]
			}

			return BitField(bits, size)
		}

		private fun Number.toByteArray(bytes: Int): ByteArray {
			val num = this.toLong()
			//println(num.toString(2))
			return buildList {
				for (b in 0..<bytes) {
					add((num shr (b * 8)).toByte())
				}
				/*this.forEachIndexed { idx, byte: Byte ->
					val ubyte = byte.toUByte()
					println("$idx: ${ubyte.toString(2).padStart(8, '0')}")
				}*/
			}.toByteArray()
		}
	}

	init {
		if (size <= 0) throw NegativeBitFieldSizeException()
		if (bits.length() !in 0..(size)) throw BitsOutOfBoundsException(bitsSize = bits.length(), setSize = size)
	}
	class NoByteArrayConversionException(message: String = "The provided object has no ByteArray conversion."): Exception(message)
	class BitsOutOfBoundsException(message: String = "The provided number of bits is greater than the provided size.", bitsSize: Int = -1, setSize: Int = -1): Exception(
		buildString {
			append(message)
			if(bitsSize >= 0) append(" Bits Size: $bitsSize.")
			if(setSize >= 0) append(" Set Size: $setSize.")
		}
	)
	class NegativeBitFieldSizeException(message: String = "The provided size is negative."): Exception(message)

	override fun toString(): String {
		return buildString {
			for (i in 0..<bits.length()) append(if (bits[i]) 1 else 0)
		}.reversed().padStart(size, '0')
	}

	fun toByteArray(): ByteArray {
		return bits.toByteArray()
	}

}