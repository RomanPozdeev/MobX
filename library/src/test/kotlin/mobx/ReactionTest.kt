package mobx

import mobx.core.reaction
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ReactionTest : Spek({
    describe("reaction test") {
        it("should invoke data callback & effect on creation") {
            var dataCallbackCalled = 0
            var effectCalled = 0

            reaction(dataCallback = {
                dataCallbackCalled++
                true
            }, sideEffect = {
                effectCalled++
            })

            assertThat(dataCallbackCalled).isEqualTo(1)
            assertThat(effectCalled).isEqualTo(1)
        }

        it("should invoke data callback on every observable update") {
            var dataCallbackCalled = 0
            var effectCalled = 0
            val test = mobx.testtools.SimpleTestObservable()

            reaction(dataCallback = {
                dataCallbackCalled++
                test.counter
            }, sideEffect = {
                effectCalled++
            })

            test.inc()
            test.inc()
            // on creation + 2 on inc
            assertThat(dataCallbackCalled).isEqualTo(3)
            assertThat(effectCalled).isEqualTo(3)
        }

        it("should invoke only when not disposed") {
            var effectCalled = 0
            val test = mobx.testtools.SimpleTestObservable()

            reaction(dataCallback = {
                test.counter
            }, sideEffect = {
                effectCalled++
            }).dispose()

            test.inc()
            test.inc()
            assertThat(effectCalled).isEqualTo(1)
        }

        it("should invoke data callback on every changeable observable update") {
            var dataCallbackCalled = 0
            var effectCalled = 0
            val test = mobx.testtools.ChangeableTestObservable()

            reaction(dataCallback = {
                dataCallbackCalled++
                test.counter
            }, sideEffect = {
                effectCalled++
            })

            test.inc()
            test.inc()
            // on creation + 2 on inc
            assertThat(dataCallbackCalled).isEqualTo(3)
            assertThat(effectCalled).isEqualTo(3)
        }
    }
})
