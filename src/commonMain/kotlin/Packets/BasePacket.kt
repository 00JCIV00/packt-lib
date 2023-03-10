package Packets

import BitField
import BitFieldGroup
import PacktUtils.toIPInt
import PacktUtils.validateIP

/**
 * A simple representation of an IP Packet.
 *
 * Resources:
 * - [IETF](https://datatracker.ietf.org/doc/html/rfc791)
 * - [Wikipedia](https://en.wikipedia.org/wiki/Internet_Protocol_version_4#Header)
 */
abstract class BasePacket() {
	/**
	 * The List of [BitFieldGroup]s comprising this [BasePacket].
	 */
	abstract val fieldGroups: MutableList<BitFieldGroup>

	/**
	 * The total Number of Bits in this [BasePacket].
	 */
	val numBits get() = fieldGroups.sumOf { it.size }

	/**
	 * The total Number of Bytes in this [BasePacket].
	 */
	val numBytes get() = numBits / 8

	/**
	 * Returns the String representation of this [BasePacket] in Big Endian. Formatted to an IETF-like style.
	 */
	override fun toString(): String {
		return buildString {
			append(
				"""
				               |               |               |               |	
			     0             v     1         v         2     v             3 v
			     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
			   	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			   	""".trimIndent()
			)
			append("\n")
			for (group in fieldGroups) append("$group\n")
		}
	}

	/**
	 * Returns the [ByteArray] representation of this [BasePacket] in Big Endian.
	 */
	fun toByteArray(): ByteArray {
		return buildList {
			fieldGroups.forEach { group ->
				group.toByteArray().forEach { add(it) }
			}
		}.toByteArray()
	}

	/**
	 * DSL method to create an [IPHeader].
	 */
	fun ipHeader(init: IPHeader.() -> Unit): BitFieldGroup {
		val ipHeader = IPHeader()
		ipHeader.init()
		ipHeader.fillFields()
		fieldGroups.add(ipHeader)
		return ipHeader
	}

	/**
	 * A [BitFieldGroup] representation of IP Header data.
	 */
	class IPHeader(name: String = "ip-header"): BitFieldGroup(name) {
		var version: Byte = 0
		var headerLen: Byte = 0
		var serviceType: Byte = 0
		var totalLen: Short = 0
		var id: Short = 0
		var flags: Byte = 0
		var fragmentOffset: Short = 0
		var ttl: Byte = 0
		var protocol: Byte = 0
		var headerChecksum: Short = 0
		var sourceAddr: String = "0.0.0.0"
			set (strIP) { field = validateIP(strIP) }
		var destAddr: String = "0.0.0.0"
			set (strIP) { field = validateIP(strIP) }
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

/**
 * A collection of IP Protocols and their associated [value]s.
 */
enum class IPProtocols(val value: UByte) {
	ICMP(1u),
	IGMP(2u),
	TCP(6u),
	UDP(17u),
	ENCAP(41u),
	OSPF(88u),
	SCTP(132u)
}