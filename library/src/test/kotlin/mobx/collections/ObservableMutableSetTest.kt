package mobx.collections

import mobx.testtools.trackableMethod
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UsePropertyAccessSyntax")
object ObservableMutableSetTest : Spek({

    describe("observable mutable set") {
        val trackCallCount by memoized { AtomicInteger(0) }
        val originalSet by memoized { mutableSetOf(1, 2, 3, 4, 5) }
        val observableSet by memoized { originalSet.toObservableSet() }
        val subscriber by memoized {
            FakeSubscriber(observableSet)
        }

        it("should return new change value on every update") {
            val changes = mutableSetOf<Int>()
            repeat(10) {
                changes.add(observableSet.change)
                observableSet.add(6)
            }
            assertThat(changes.size).isEqualTo(10)
        }

        it("should add item on set") {
            observableSet.add(6)
            assertThat(observableSet).containsExactlyElementsOf(listOf(1, 2, 3, 4, 5, 6))
        }

        it("should notify observers after add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.add(6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should add items on set") {
            observableSet.addAll(listOf(6, 7, 8))
            assertThat(observableSet).containsExactlyElementsOf(listOf(1, 2, 3, 4, 5, 6, 7, 8))
        }

        it("should notify observers after items add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.addAll(listOf(6, 7, 8))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should clear set") {
            assertThat(observableSet.size).isNotZero()
            observableSet.clear()
            assertThat(observableSet).isEmpty()
        }

        it("contains() should work") {
            assertThat(observableSet.contains(5)).isTrue()
            assertThat(observableSet.contains(10)).isFalse()
        }

        it("contains() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableSet.contains(0)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("containsAll() should work") {
            assertThat(observableSet.containsAll(listOf(1, 2, 3))).isTrue()
            assertThat(observableSet.containsAll(listOf(1, 2, 3, 10))).isFalse()
        }

        it("containsAll() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableSet.containsAll(emptySet())
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("isEmpty should work") {
            assertThat(observableSet.isEmpty()).isFalse()
            observableSet.clear()
            assertThat(observableSet.isEmpty()).isTrue()
        }

        it("isEmpty should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableSet.isEmpty()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("remove should remove item") {
            observableSet.remove(2)
            assertThat(observableSet).containsExactlyElementsOf(listOf(1, 3, 4, 5))
        }

        it("remove should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.add(6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("remove should not notify observers if it not present") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.remove(12)
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("removeAll should remove items") {
            observableSet.removeAll(listOf(2, 3))
            assertThat(observableSet).containsExactlyElementsOf(listOf(1, 4, 5))
        }

        it("removeAll should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.removeAll(listOf(2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("removeAll should not notify observers if collection was not modified") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.removeAll(listOf(12, 13))
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("retainAll should retain items") {
            observableSet.retainAll(listOf(1, 2, 3))
            assertThat(observableSet).containsExactlyElementsOf(listOf(1, 2, 3))
        }

        it("retainAll should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.retainAll(listOf(1, 2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("retainAll should notify observers only if items removed") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableSet.retainAll(listOf(1, 2, 3, 4, 5))
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("should return size") {
            assertThat(observableSet.size).isEqualTo(5)
        }

        it("size should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableSet.size
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        describe("iterator test") {
            val iterator by memoized { observableSet.iterator() }

            it("should remove item") {
                iterator.next()
                iterator.remove()
                assertThat(observableSet).containsExactlyElementsOf(listOf(2, 3, 4, 5))
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                iterator.next()
                iterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }
    }
})
