package network.arkane.monitor.aeternitymonitor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["network.arkane.monitor","network.arkane.provider"])
class AeternitymonitorApplication

fun main(args: Array<String>) {
	runApplication<AeternitymonitorApplication>(*args)
}
