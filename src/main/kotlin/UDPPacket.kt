class UDPPacket: Packet() {
	override val fieldGroups: MutableList<BitFieldGroup> = mutableListOf()
}

fun udpPacket(init: UDPPacket.() -> Unit): UDPPacket {
	val udpPacket = UDPPacket()
	udpPacket.init()
	return udpPacket
}