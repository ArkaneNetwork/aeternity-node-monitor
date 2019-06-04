package network.arkane.monitor.ethereummonitor

import com.kryptokrauts.aeternity.generated.model.KeyBlock
import com.kryptokrauts.aeternity.sdk.service.chain.ChainService
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class AeternityNodeHealthIndicator(val chainService: ChainService) : AbstractReactiveHealthIndicator() {

    private val latestBlock: Optional<KeyBlock>
        get() {
            return try {
                Optional.ofNullable(chainService.currentKeyBlock.blockingGet())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Optional.empty()
            }
        }

    override fun doHealthCheck(builder: Health.Builder): Mono<Health> {
        try {
            val block = latestBlock
            return block
                    .map {
                        val date = Date(block.get().time.toLong() * 1000)
                        val blockTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                        if (blockTime.plus(10, ChronoUnit.MINUTES).isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                            Mono.just(builder.status(Status.DOWN)
                                    .withDetail("aeternitynode", """last block is older than 30 minutes.($blockTime, block: ${block.get().height})""")
                                    .build())
                        } else {
                            Mono.just(builder.up().withDetail("aeternitynode", """latest block is ${block.get().height}""").build())
                        }
                    }.orElseGet {
                        Mono.just(
                                builder.down()
                                        .withDetail("aeternitynode.down", "Unable to fetch status for aeternity node").build())
                    }
        } catch (ex: Exception) {
            return Mono.just(
                    builder.down()
                            .withDetail("aeternitynode.exception", ex.message).build())
        }
    }
}