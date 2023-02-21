import Packets.*

fun main(args: Array<String>) {
	val tcpTest = tcpPacket {
		ipHeader {
			version = 3
			sourceAddr = "192.168.1.1"
			destAddr = "172.16.1.1"
		}
		tcpHeader {
			sourcePort = 9999
			destPort = 11111
			flags = TCPFlags.ACK.value
		}
		data("Hello World")
	}

	val udpTest =
		udpPacket {
			ipHeader {
				version = 2
				sourceAddr = "10.10.10.1"
				destAddr = "10.10.10.2"
			}
			udpHeader {
				sourcePort = 5555
				destPort = 6666
			}
			data("Data Payload")
		}

	val icmpTest =
		icmpPacket {
			ipHeader {
				version = 1
				sourceAddr = "192.168.0.1"
				destAddr = "192.168.0.2"
			}
			icmpHeader {
				type = ICMPTypes.ECHO.value
			}

		}

	println("TCP TEST\n$tcpTest")
	println("UDP TEST\n$udpTest")
	println("ICMP TEST\n$icmpTest")
}


