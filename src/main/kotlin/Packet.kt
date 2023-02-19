abstract class Packet() {
	abstract val fieldGroups: MutableList<BitFieldGroup>
	val numBits get() = fieldGroups.sumOf { it.size }
	val numBytes get() = numBits / 8

	override fun toString(): String {
		return buildString {
			append(
				"""
			     0                   1                   2                   3
			     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
			   	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			   	""".trimIndent()
			)
			append("\n")
			for (group in fieldGroups) append("$group\n")
		}
	}

	fun toByteArray(): ByteArray {
		return buildList {
			fieldGroups.forEach { group ->
				group.toByteArray().forEach { add(it) }
			}
		}.toByteArray()
	}


	fun ipHeader(init: IPHeader.() -> Unit): BitFieldGroup {
		val ipHeader = IPHeader()
		ipHeader.init()
		ipHeader.fillFields()
		fieldGroups.add(ipHeader)
		return ipHeader
	}

	class IPHeader(name: String = "ip-header"): BitFieldGroup(name) {
		var version: Int = 0
		var headerLen: Int = 0
		var serviceType: Int = 0
		var totalLen: Int = 0
		var id: Int = 0
		var flags: Int = 0
		var fragmentOffset: Int = 0
		var ttl: Int = 0
		var protocol: Int = 0
		var headerChecksum: Int = 0
		var sourceAddr: String = "0.0.0.0"
		var destAddr: String = "0.0.0.0"
		fun fillFields() {
			fields["version"] = BitField.from(version, 4)
			fields["headerLen"] = BitField.from(headerLen, 4)
			fields["serviceType"] = BitField.from(serviceType, 8)
			fields["totalLen"] = BitField.from(totalLen, 16)
			fields["id"] = BitField.from(id, 16)
			fields["flags"] = BitField.from(flags, 4)
			fields["fragmentOffset"] = BitField.from(fragmentOffset, 12)
			fields["ttl"] = BitField.from(ttl, 8)
			fields["protocol"] = BitField.from(protocol, 8)
			fields["headerChecksum"] = BitField.from(headerChecksum, 16)
			fields["sourceAddr"] = BitField.from(sourceAddr.toIPInt(), 32)
			fields["destAddr"] = BitField.from(destAddr.toIPInt(), 32)
		}
	}
}
