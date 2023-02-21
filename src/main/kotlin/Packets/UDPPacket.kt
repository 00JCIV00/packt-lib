package Packets

import BitField
import BitFieldGroup

class UDPPacket: BasePacket() {
	override val fieldGroups: MutableList<BitFieldGroup> = mutableListOf()

	fun udpHeader(init: UDPHeader.() -> Unit): BitFieldGroup {
		val udpHeader = UDPHeader()
		udpHeader.init()
		udpHeader.fillFields()
		fieldGroups.add(udpHeader)
		return udpHeader
	}

	fun data(data: Any) {
		fieldGroups.add(BitFieldGroup.from("data", data))
	}

	class UDPHeader(name: String = "udp-header"): BitFieldGroup(name) {
		var sourcePort: Short = 0
		var destPort: Short = 0
		var length: Short = 0
		var checksum: Short = 0

		fun fillFields() {
			fields["sourcePort"] = BitField.from(sourcePort, 16)
			fields["destPort"] = BitField.from(destPort, 16)
			fields["length"] = BitField.from(length, 16)
			fields["checksum"] = BitField.from(checksum, 16)
		}
	}
}

fun udpPacket(init: UDPPacket.() -> Unit): UDPPacket {
	val udpPacket = UDPPacket()
	udpPacket.init()
	return udpPacket
}