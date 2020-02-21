package mobx

import mobx.core.Observable
import mobx.core.ObservableTracker
import mobx.core.ObservableTrackerHolder
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ObservableTrackerHolderTest : Spek({
    describe("ObservableTrackerHolderTest") {
        val get = ObservableTrackerHolder.currentTracker

        it("should have null tracker") {
            assertThat(get).isNull()
        }

        var transactionCall = 0

        val tracker = object : ObservableTracker {
            override fun track(observable: Observable) {
            }
        }

        ObservableTrackerHolder.replaceTo(tracker, transaction = {
            transactionCall++
        })

        it("should call transaction on replace") {
            assertThat(transactionCall).isEqualTo(1)
        }

        it("should return prev tracker") {
            ObservableTrackerHolder.replaceTo(tracker, transaction = {
                ObservableTrackerHolder.replaceTo(tracker, transaction = {
                    transactionCall++
                })
                assertThat(ObservableTrackerHolder.currentTracker).isEqualTo(tracker)
                transactionCall++
            })
            assertThat(ObservableTrackerHolder.currentTracker).isNull()
        }
    }
})
