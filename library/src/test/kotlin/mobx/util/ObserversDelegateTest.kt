package mobx.util

import mobx.core.Observable
import mobx.core.Observer
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

object ObserversDelegateTest : Spek({

    describe("observers delegate test") {
        val defaultObserversDT by memoized {
            ObserversDelegate()
        }

        val onChangeCount by memoized { AtomicInteger() }

        val defaultObserver = object : Observer {
            override fun onChange() {
                onChangeCount.incrementAndGet()
            }
        }

        it("should update id on every update") {
            val idBeforeUpdate = defaultObserversDT.change
            defaultObserversDT.notifyObservers()
            assertThat(idBeforeUpdate).isNotEqualTo(defaultObserversDT.change)
        }

        it("should notify observer on every update") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(defaultObserver)
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(1)
        }

        it("should notify observer on every update (extended subscribe)") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(0, defaultObserver)
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(1)
        }

        it("should remove observer after dispose") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(defaultObserver).dispose()
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(0)
        }

        it("should remove observer after dispose (extended subscribe)") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(0, defaultObserver).dispose()
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(0)
        }

        it("should not add observer twice") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            val dispose = defaultObserversDT.subscribe(defaultObserver)
            repeat(5) {
                defaultObserversDT.subscribe(defaultObserver)
            }
            assertThat(dispose).isSameAs(defaultObserversDT.subscribe(defaultObserver))
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(1)
        }

        it("should not add observer twice (extended subscribe)") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            val dispose = defaultObserversDT.subscribe(0, defaultObserver)
            repeat(5) {
                defaultObserversDT.subscribe(it, defaultObserver)
            }
            assertThat(dispose).isSameAs(defaultObserversDT.subscribe(defaultObserver))
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(1)
        }

        val subscribeCount by memoized { AtomicInteger() }
        val disposeCount by memoized { AtomicInteger() }

        val observable by memoized {
            object : Observable {
                override fun subscribe(observer: Observer): mobx.core.Disposable {
                    subscribeCount.incrementAndGet()
                    return object : mobx.core.Disposable {
                        override fun dispose() {
                            disposeCount.incrementAndGet()
                        }
                    }
                }
            }
        }

        it("should subscribe observer on observable updates") {
            assertThat(subscribeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(observable, defaultObserver)
            assertThat(subscribeCount.get()).isEqualTo(1)
        }

        it("should unsubscribe observable observer after dispose") {
            assertThat(subscribeCount.get()).isEqualTo(0)
            assertThat(disposeCount.get()).isEqualTo(0)
            defaultObserversDT.subscribe(observable, defaultObserver).dispose()
            assertThat(disposeCount.get()).isEqualTo(1)
        }

        it("should not add observer on observable twice") {
            assertThat(onChangeCount.get()).isEqualTo(0)
            val dispose = defaultObserversDT.subscribe(observable, defaultObserver)
            repeat(5) {
                defaultObserversDT.subscribe(observable, defaultObserver)
            }
            assertThat(dispose).isSameAs(defaultObserversDT.subscribe(observable, defaultObserver))
            defaultObserversDT.notifyObservers()
            assertThat(onChangeCount.get()).isEqualTo(1)
            assertThat(subscribeCount.get()).isEqualTo(1)
        }

        it("should return to MIN_VALUE after MAX_VALUE") {
            val observers = ObserversDelegate(counterStart = Int.MAX_VALUE)
            observers.notifyObservers()
            assertThat(observers.change).isEqualTo(Int.MIN_VALUE + 1)
        }
    }
})
