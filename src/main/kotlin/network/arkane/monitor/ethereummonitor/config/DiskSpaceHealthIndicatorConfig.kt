package network.arkane.monitor.ethereummonitor.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.util.unit.DataSize
import org.springframework.util.unit.DataUnit
import java.io.File
import java.lang.IllegalArgumentException

class DiskSpaceHealthIndicatorConfig {

    @ConditionalOnProperty(value = "network.arkane.ethereum.disk.path", matchIfMissing = false)
    @Bean
    fun conditionallyProvideDiskspaceHealthIndicator(@Value("\${network.arkane.ethereum.disk.path}") path: String): HealthIndicator {
        if (File(path).exists()) {
            return DiskSpaceHealthIndicator(File(path), DataSize.of(1, DataUnit.GIGABYTES))
        } else {
            throw IllegalArgumentException("The provided ethereum disk path is not available")
        }
    }
}