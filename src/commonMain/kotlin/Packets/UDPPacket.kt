package Packets

import BitField
import BitFieldGroup

/**
 * A simple representation of a UDP Packet.
 *
 * Resources:
 * - [IETF](https://www.ietf.org/rfc/rfc768.html)
 */
class UDPPacket: BasePacket() {
	override val fieldGroups: MutableList<BitFieldGroup> = mutableListOf()

	/**
	 * DSL method to create an [UDPHeader].
	 */
	fun udpHeader(init: UDPHeader.() -> Unit): BitFieldGroup {
		val udpHeader = UDPHeader()
		udpHeader.init()
		udpHeader.fillFields()
		fieldGroups.add(udpHeader)
		return udpHeader
	}

	/**
	 * DSL method to add raw [data] as a [BitFieldGroup] to this [UDPPacket].
	 */
	fun data(data: Any) {
		fieldGroups.add(BitFieldGroup.from("data", data))
	}

	/**
	 * A [BitFieldGroup] representation of UDP Header data.
	 */
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

/**
 * DSL method to create a [UDPPacket].
 */
fun udpPacket(init: UDPPacket.() -> Unit): UDPPacket {
	val udpPacket = UDPPacket()
	udpPacket.init()
	return udpPacket
}