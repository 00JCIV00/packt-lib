package Packets

import BitField
import BitFieldGroup

/**
 * A simple representation of an ICMP Packet.
 *
 * Resources:
 * - [IETF](https://datatracker.ietf.org/doc/html/rfc792)
 * - [Wikipedia](https://en.wikipedia.org/wiki/Internet_Control_Message_Protocol)
 */
class ICMPPacket: BasePacket() {
	override val fieldGroups: MutableList<BitFieldGroup> = mutableListOf()

	/**
	 * DSL method to create an [ICMPHeader].
	 */
	fun icmpHeader(init: ICMPHeader.() -> Unit): BitFieldGroup {
		val icmpHeader = ICMPHeader()
		icmpHeader.init()
		icmpHeader.fillFields()
		fieldGroups.add(icmpHeader)
		return icmpHeader
	}

	/**
	 * DSL method to add raw [data] as a [BitFieldGroup] to this [ICMPPacket].
	 */
	fun data(data: Any) {
		fieldGroups.add(BitFieldGroup.from("data", data))
	}

	/**
	 * A [BitFieldGroup] representation of ICMP Header data.
	 */
	class ICMPHeader(name: String = "icmp-header"): BitFieldGroup(name) {
		var type: UByte = 0u
		var code: UByte = 0u
		var checksum: Short = 0

		fun fillFields() {
			fields["type"] = BitField.from(type, 8)
			fields["code"] = BitField.from(code, 8)
			fields["checksum"] = BitField.from(checksum, 16)
			fields["unused"] = BitField.from(0, 32)
		}
	}
}

/**
 * DSL method to create an [ICMPPacket].
 */
fun icmpPacket(init: ICMPPacket.() -> Unit): ICMPPacket {
	val ICMPPacket = ICMPPacket()
	ICMPPacket.init()
	return ICMPPacket
}

/**
 * A collection of ICMP Types and their associated [value]s.
 */
enum class ICMPTypes(val value: UByte) {
	DEST_UNREACHABLE(3u),
	TIME_EXCEEDED(11u),
	PARAM_PROB(12u),
	SOURCE_QUENCH(4u),
	REDIRECT(5u),
	ECHO(8u),
	ECHO_REPLY(0u),
	TIMESTAMP(13u),
	TIMESTAMP_REPLY(14u),
	INFO_REQUEST(15u),
	INFO_REPLY(16u);
}

/**
 * A collection of ICMP Codes for various ICMP Types along with their associated values.
 */
class ICMPCodes() {
	companion object {
		enum class DestUnreachable(val value: UByte) {
			NET_UNREACHABLE(0u),
			HOST_UNREACHABLE(1u),
			PROTOCOL_UNREACHABLE(2u),
			PORT_UNREACHABLE(3u),
			FRAG_NEEDED(4u),
			SOURCE_ROUTE_FAILED(5u)
		}

		enum class TimeExceeded(val value: UByte) {
			TTL_EXCEEDED_IN_TRANSIT(0u),
			FRAG_REASSEMBLY_TIME_EXCEEDED(1u)
		}

		enum class Redirect(val value: UByte) {
			NETWORK(0u),
			HOST(1u),
			TYPE_OF_SERVICE_AND_NETWORK(2u),
			TYPE_OF_SERVICE_AND_HOST(3u)
		}
	}
}