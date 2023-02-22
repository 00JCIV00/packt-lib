import PacktUtils.toBitList
import PacktUtils.toByteArray
import PacktUtils.toInt

/**
 * Immutable BitField Wrapper for Java's BitSet
 */
data class BitField(val bits: List<Boolean>, val size: Int) {
	companion object {
		fun from(data: Any,
				 size: Int,
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

			var bits = byteArray.toBitList()

			if (bits.size > size && truncate) {
				val lastIdx = if (size > 0) size else 1
				bits = bits.subList(0, lastIdx)
			}

			return BitField(bits, size)
		}
	}

	init {
		if (size <= 0) throw NegativeBitFieldSizeException()
		if (bits.size !in 0..(size)) throw BitsOutOfBoundsException(bitsSize = bits.size, setSize = size)
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
	@OptIn(ExperimentalStdlibApi::class)
	override fun toString(): String {
		return buildString {
			for (i in 0..<bits.size) append(if (bits[i]) 1 else 0)
		}.reversed().padStart(size, '0')
	}

	fun toByteArray(): ByteArray {
		return buildList {
			for (byteBits in bits.chunked(8)) {
				var byte = 0
				for (bit in byteBits.reversed())
					byte = byte or bit.toInt()
				add(byte.toByte())
			}
		}.toByteArray()
	}

}