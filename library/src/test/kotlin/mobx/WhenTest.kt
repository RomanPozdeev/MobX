package mobx

import mobx.core.whenThen
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

object WhenTest : Spek({
    describe("when test") {

        describe("when with simple true predicate") {
            val testObject =
                mobx.testtools.SimpleTestObservable()
            val sideEffectCallCount = AtomicInteger(0)
            whenThen(predicate = {
                true
            }, sideEffect = {
                sideEffectCallCount.incrementAndGet()
            })

            it("should invoke sideEffect once") {
                assertThat(sideEffectCallCount.get()).isEqualTo(1)
            }

            it("should auto dispose after sideEffect ") {
                testObject.inc()
                assertThat(sideEffectCallCount.get()).isEqualTo(1)
            }
        }

        describe("when with simple false predicate") {
            val sideEffectCallCount = AtomicInteger(0)
            whenThen(predicate = {
                false
            }, sideEffect = {
                sideEffectCallCount.incrementAndGet()
            })

            it("should not invoke sideEffect") {
                assertThat(sideEffectCallCount.get()).isEqualTo(0)
            }
        }

        describe("when with observable predicate") {
            lateinit var disposable: mobx.core.Disposable
            val testObject by memoized { mobx.testtools.SimpleTestObservable() }
            val sideEffectCallCount by memoized { AtomicInteger(0) }
            val predicateCallCount by memoized { AtomicInteger(0) }

            beforeEachTest {
                testObject.reset()
                disposable = whenThen(predicate = {
                    predicateCallCount.incrementAndGet()
                    testObject.counter >= 1
                }, sideEffect = {
                    sideEffectCallCount.incrementAndGet()
                })
            }

            it("should invoke predicate on creation") {
                assertThat(predicateCallCount.get()).isEqualTo(1)
            }

            it("should not invoke side effect on creation") {
                assertThat(sideEffectCallCount.get()).isEqualTo(0)
            }

            it("should invoke predicate on test counter update") {
                assertThat(predicateCallCount.get()).isEqualTo(1)
                testObject.inc()
                assertThat(predicateCallCount.get()).isEqualTo(2)
            }

            it("should invoke side effect once when predicate is true") {
                assertThat(sideEffectCallCount.get()).isEqualTo(0)
                testObject.inc()
                assertThat(sideEffectCallCount.get()).isEqualTo(1)
                repeat(5) {
                    testObject.inc()
                }
                assertThat(sideEffectCallCount.get()).isEqualTo(1)
            }

            it("should be disposable") {
                assertThat(sideEffectCallCount.get()).isEqualTo(0)
                disposable.dispose()
                testObject.inc()
                assertThat(sideEffectCallCount.get()).isEqualTo(0)
            }
        }
    }
})
