package network.arkane.monitor.aeternitymonitor.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import java.io.File

class DiskSpaceHealthIndicatorConfig {

    @ConditionalOnProperty(value = "network.arkane.ethereum.disk.path", matchIfMissing = false)
    @Bean
    fun conditionallyProvideDiskspaceHealthIndicator(@Value("\${network.arkane.ethereum.disk.path}") path: String): HealthIndicator {
        if (File(path).exists()) {
            return DiskSpaceHealthIndicator(File(path), 1073741824L)
        } else {
            throw IllegalArgumentException("The provided ethereum disk path is not available")
        }
    }
}