package mobx.collections

import mobx.testtools.trackableMethod
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UsePropertyAccessSyntax")
object ObservableMutableMapTest : Spek({
    describe("observable mutable map") {
        val trackCallCount by memoized { AtomicInteger(0) }
        val originalMap by memoized { mutableMapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5) }
        val observableMap by memoized { originalMap.toObservableMap() }
        val subscriber by memoized {
            FakeSubscriber(observableMap)
        }

        it("should return new change value on every update") {
            val changes = mutableSetOf<Int>()
            repeat(10) {
                changes.add(observableMap.change)
                observableMap[6] = 6
            }
            assertThat(changes.size).isEqualTo(10)
        }

        it("should add item on map") {
            observableMap[6] = 6
            assertThat(observableMap).containsOnlyKeys(1, 2, 3, 4, 5, 6)
            assertThat(observableMap).containsValues(1, 2, 3, 4, 5, 6)
        }

        it("should notify observers after add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableMap[6] = 6
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should get item from map") {
            assertThat(observableMap[1]).isEqualTo(1)
        }

        it("get should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableMap[1]
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should put items on map") {
            observableMap.putAll(mapOf(6 to 6, 7 to 7, 8 to 8))
            assertThat(observableMap).containsOnlyKeys(1, 2, 3, 4, 5, 6, 7, 8)
            assertThat(observableMap).containsValues(1, 2, 3, 4, 5, 6, 7, 8)
        }

        it("should notify observers after items add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableMap.putAll(mapOf(6 to 6, 7 to 7, 8 to 8))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should clear list") {
            assertThat(observableMap.size).isNotZero()
            observableMap.clear()
            assertThat(observableMap).isEmpty()
        }

        it("contains() should work") {
            assertThat(observableMap.containsKey(5)).isTrue()
            assertThat(observableMap.containsKey(10)).isFalse()
        }

        it("contains() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableMap.contains(1)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("isEmpty should work") {
            assertThat(observableMap.isEmpty()).isFalse()
            observableMap.clear()
            assertThat(observableMap.isEmpty()).isTrue()
        }

        it("isEmpty should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableMap.isEmpty()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("remove should remove item") {
            observableMap.remove(2)
            assertThat(observableMap).containsOnlyKeys(1, 3, 4, 5)
            assertThat(observableMap).containsValues(1, 3, 4, 5)
        }

        it("remove should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableMap.remove(1)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("remove should notify observers only if item is removed") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableMap.remove(8)
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("set should update list") {
            observableMap[10] = 10
            assertThat(observableMap).containsOnlyKeys(1, 2, 3, 4, 5, 10)
            assertThat(observableMap).containsValues(1, 2, 3, 4, 5, 10)
        }

        it("set should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableMap[10] = 10
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should return size") {
            assertThat(observableMap.size).isEqualTo(5)
        }

        it("size should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableMap.size
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        describe("iterator (entries) test") {
            val listIterator by memoized { observableMap.iterator() }

            it("should remove item") {
                listIterator.next()
                listIterator.remove()
                assertThat(observableMap).containsOnlyKeys(2, 3, 4, 5)
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }

        describe("iterator (keys) test") {
            val keys by memoized { observableMap.keys }

            it("should remove item") {
                keys.remove(1)
                assertThat(observableMap).containsOnlyKeys(2, 3, 4, 5)
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                keys.remove(1)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }

        describe("iterator (values) test") {
            val values by memoized { observableMap.values }

            it("should remove item") {
                values.remove(1)
                assertThat(observableMap).containsValues(2, 3, 4, 5)
                assertThat(observableMap).doesNotContainValue(1)
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                values.remove(1)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }
    }
})
