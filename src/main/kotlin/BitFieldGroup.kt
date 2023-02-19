open class BitFieldGroup(val name: String, val fields: MutableMap<String, BitField> = mutableMapOf()): Collection<BitField> {
	companion object {
		class BitFieldConversionException(message: String = "Could not convert the provided object to a BitFieldGroup"): Exception(message)
		fun from(name: String, data: Any): BitFieldGroup {
			return BitFieldGroup(name, buildMap {
				when(data) {
					is String -> {
						data.forEachIndexed { idx, char ->
							set(idx.toString(), BitField.from(char.code, 16))
						}
					}
					else -> throw BitFieldConversionException()
				}
			}.toMutableMap())
		}
	}
	override val size
		get() = fields.size
	override fun isEmpty() = fields.isEmpty()
	override fun iterator(): Iterator<BitField> = fields.values.iterator()
	override fun containsAll(elements: Collection<BitField>): Boolean = fields.values.containsAll(elements)
	override fun contains(element: BitField): Boolean = fields.values.contains(element)

	enum class DataOutputStyle() {
		BIN,
		DEC,
		HEX,
		STR
	}

	private val separator = "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
	override fun toString(): String {
		return buildString {
			val pad = 32 + (name.length / 2)
			append("${name.uppercase().padStart(pad)}\n")
			append("$separator\n")
			append('|')
			var bitsCount = 0
			var namesIdx = 0
			fields.values.forEachIndexed { idx, field ->
				append("${field.toString().chunked(1).joinToString(" ")}|")
				bitsCount += field.size
				if (bitsCount >= 32) {
					append(" ")
					for(ni in namesIdx..idx) append("[${fields.keys.toList()[ni]}] ")
					namesIdx = idx + 1
					append("\n")
					if (idx < fields.size - 1) append("|")
					bitsCount = 0
				}
			}
			if (bitsCount != 0) append("${ "".padEnd(32 - bitsCount, '0').chunked(1).joinToString(" ")}|\n")

			append(separator)
		}
	}

	/*fun toString(style: DataOutputStyle = DataOutputStyle.BIN, fieldNames: Boolean = false): String {
		return buildString {
			when (style) {
				DataOutputStyle.BIN ->
			}
		}
	}*/

	fun toByteArray(): ByteArray {
		return buildList {
			fields.values.forEach { field ->
				field.toByteArray().forEach { add(it) }
			}
		}.toByteArray()
	}
}
