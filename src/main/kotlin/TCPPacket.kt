/**
 * https://www.ietf.org/rfc/rfc9293.html
 */

class TCPPacket(): Packet() {
	override val fieldGroups: MutableList<BitFieldGroup> = mutableListOf()

	fun tcpHeader(init: TCPHeader.() -> Unit): BitFieldGroup {
		val tcpHeader = TCPHeader()
		tcpHeader.init()
		tcpHeader.fillFields()
		fieldGroups.add(tcpHeader)
		return tcpHeader
	}

	fun data(data: Any) {
		fieldGroups.add(BitFieldGroup.from("data", data))
	}

	class TCPHeader(name: String = "tcp-header"): BitFieldGroup(name) {
		var sourcePort: Int = 0
		var destPort: Int = 0
		var seqNum: Int = 0
		var ackNum: Int = 0
		var dataOffset: Int = 0
		var reserved: Int = 0
		var flags: Int = TCPFlags.FIN.bits
		var window: Int = 0
		var checksum: Int = 0
		var urgPointer: Int = 0
		var options: Int = 0
		fun fillFields() {
			fields["sourcePort"] = BitField.from(sourcePort, 16)
			fields["destPort"] = BitField.from(destPort, 16)
			fields["seqNum"] = BitField.from(seqNum, 32)
			fields["ackNum"] = BitField.from(ackNum, 32)
			fields["dataOffset"] = BitField.from(dataOffset, 4)
			fields["reserved"] = BitField.from(reserved, 4)
			fields["flags"] = BitField.from(flags, 8)
			fields["window"] = BitField.from(window, 16)
			fields["checksum"] = BitField.from(checksum, 16)
			fields["urgPointer"] = BitField.from(urgPointer, 16)
			fields["options"] = BitField.from(options, 32)
		}
	}
}

fun tcpPacket(init: TCPPacket.() -> Unit): TCPPacket {
	val tcpPacket = TCPPacket()
	tcpPacket.init()
	return tcpPacket
}

enum class TCPFlags(val bits: Int) {
	FIN(0b00000001),
	SYN(0b00000010),
	RST(0b00000100),
	PSH(0b00001000),
	ACK(0b00010000),
	URG(0b00100000),
	ECE(0b01000000),
	CWR(0b10000000)
}