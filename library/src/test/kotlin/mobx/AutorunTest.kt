package mobx

import mobx.core.autorun
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

object AutorunTest : Spek({
    describe("autorun test") {
        val effectCallCount by memoized { AtomicInteger(0) }
        val subscribeCount by memoized { AtomicInteger(0) }
        val disposeCount by memoized { AtomicInteger(0) }
        val disposable by memoized {
            object : mobx.core.Disposable {
                override fun dispose() {
                    disposeCount.incrementAndGet()
                }
            }
        }

        val testObservable by memoized {
            mobx.testtools.TestObservable(disposable) { subscribeCount.incrementAndGet() }
        }

        lateinit var autorunEx: mobx.core.Autorun

        beforeEachTest {
            autorunEx = autorun {
                effectCallCount.incrementAndGet()
            }
        }

        afterEachTest {
            autorunEx.dispose()
        }

        it("should run on create") {
            assertThat(effectCallCount.get()).isEqualTo(1)
        }

        it("should run on each changes") {
            assertThat(effectCallCount.get()).isEqualTo(1)
            autorunEx.onChange()
            assertThat(effectCallCount.get()).isEqualTo(2)
        }

        it("should run until disposed") {
            assertThat(effectCallCount.get()).isEqualTo(1)
            autorunEx.onChange()
            assertThat(effectCallCount.get()).isEqualTo(2)
            autorunEx.dispose()
            assertThatThrownBy {
                autorunEx.onChange()
            }.isInstanceOf(IllegalStateException::class.java)
            assertThat(effectCallCount.get()).isEqualTo(2)
        }

        it("should run when tracked observable changed") {
            assertThat(effectCallCount.get()).isEqualTo(1)
            autorunEx.track(testObservable)
            testObservable.onChange()
            assertThat(effectCallCount.get()).isEqualTo(2)
        }

        it("should notify only when action complete") {
            val complex = mobx.testtools.ComplexTestObservable()
            var runCount = 0
            autorun {
                runCount++
                complex.toString()
            }
            complex.inc()
            assertThat(runCount).isEqualTo(2)
        }
    }
})
