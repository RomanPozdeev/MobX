package mobx

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

object ComputedTest : Spek({
    describe("computed test") {
        val bodyWasCalled by memoized { AtomicInteger(0) }

        val computedEx by memoized {
            mobx.core.Computed {
                bodyWasCalled.incrementAndGet()
            }
        }

        it("should not call body on creation") {
            assertThat(bodyWasCalled.get()).isEqualTo(0)
        }

        it("should call body once on first") {
            assertThat(computedEx.get()).isEqualTo(1)
            assertThat(bodyWasCalled.get()).isEqualTo(1)
        }

        it("should not call body if not changed") {
            assertThat(computedEx.get()).isEqualTo(1)
            assertThat(computedEx.get()).isEqualTo(1)
            assertThat(bodyWasCalled.get()).isEqualTo(1)
        }

        it("should not call body if changed") {
            computedEx.get()
            computedEx.onChange()
            assertThat(computedEx.get()).isEqualTo(2)
            assertThat(bodyWasCalled.get()).isEqualTo(2)
        }

        it("should work with nested Computed values") {
            val comp = mobx.testtools.ComputedTestObservable()
            var bodyCalled = 0
            val computed1 = mobx.core.Computed {
                bodyCalled++
                comp.triple
            }
            assertThat(bodyCalled).isEqualTo(0)
            computed1.get()
            assertThat(bodyCalled).isEqualTo(1)
            comp.inc()
            assertThat(bodyCalled).isEqualTo(1)
            computed1.get()
            assertThat(bodyCalled).isEqualTo(2)
        }
    }
})
