# packt-lib
A small Kotlin library to build and send basic packets based on IETF specifications.

---

# Overview
This project is an experiment to learn more about the basics of how packets by coding parts of the IETF spec in Kotlin, a language I enjoy. Optimally, the library would allow users to manipulate packets either directly or through a DSL.

# Issues
Unfortunately, using Kotlin has introduced a number of challenges in terms of packet crafting and manipulation:
- [ ] Sending packets directly to a raw socket. *(This is partially solved using [Ktor's Raw Socket for TCP and UDP](https://ktor.io/docs/servers-raw-sockets.html), but that's limited to those specific protocols and only works with JVM as far as I can tell.)*
- [ ] Intercepting packets delivered to the host machine.

# Resources
- [IP](https://datatracker.ietf.org/doc/html/rfc791)
- [ICMP](https://datatracker.ietf.org/doc/html/rfc792)
- [UDP](https://datatracker.ietf.org/doc/html/rfc768)
- [TCP](https://www.ietf.org/rfc/rfc9293.html)
