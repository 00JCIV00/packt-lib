fun main(args: Array<String>) {
	val tcpTest = tcpPacket {
		ipHeader {
			sourceAddr = "192.168.1.1"
			destAddr = "172.16.1.1"
		}
		tcpHeader {
			sourcePort = 9999
			destPort = 11111
		}
		data("Hello World")
	}

	println(tcpTest)
}

