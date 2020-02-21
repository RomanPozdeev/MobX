package mobx.collections

import mobx.testtools.trackableMethod
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UsePropertyAccessSyntax")
object ObservableMutableCollectionTest : Spek({

    describe("observable mutable map") {
        val trackCallCount by memoized { AtomicInteger(0) }
        val originalCollection by memoized { mutableListOf(1, 2, 3, 4, 5) }
        val observableCollection by memoized { ObservableMutableCollection(originalCollection) }
        val subscriber by memoized {
            FakeSubscriber(observableCollection)
        }

        it("should return new change value on every update") {
            val changes = mutableSetOf<Int>()
            repeat(10) {
                changes.add(observableCollection.change)
                observableCollection.add(6)
            }
            assertThat(changes.size).isEqualTo(10)
        }

        it("should add item on collection") {
            observableCollection.add(6)
            assertThat(observableCollection).containsExactlyElementsOf(listOf(1, 2, 3, 4, 5, 6))
        }

        it("should notify observers after add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.add(6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should add items on collection") {
            observableCollection.addAll(listOf(6, 7, 8))
            assertThat(observableCollection).containsExactlyElementsOf(
                listOf(
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                )
            )
        }

        it("should notify observers after items add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.addAll(listOf(6, 7, 8))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should clear collection") {
            assertThat(observableCollection.size).isNotZero()
            observableCollection.clear()
            assertThat(observableCollection).isEmpty()
        }

        it("contains() should work") {
            assertThat(observableCollection.contains(5)).isTrue()
            assertThat(observableCollection.contains(10)).isFalse()
        }

        it("contains() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableCollection.contains(5)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("containsAll() should work") {
            assertThat(observableCollection.containsAll(listOf(1, 2, 3))).isTrue()
            assertThat(observableCollection.containsAll(listOf(1, 2, 3, 10))).isFalse()
        }

        it("contains() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableCollection.containsAll(emptyList())
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("isEmpty should work") {
            assertThat(observableCollection.isEmpty()).isFalse()
            observableCollection.clear()
            assertThat(observableCollection.isEmpty()).isTrue()
        }

        it("isEmpty should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableCollection.isEmpty()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("remove should remove item") {
            observableCollection.remove(2)
            assertThat(observableCollection).containsExactlyElementsOf(listOf(1, 3, 4, 5))
        }

        it("remove should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.add(6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("removeAll should remove items") {
            observableCollection.removeAll(listOf(2, 3))
            assertThat(observableCollection).containsExactlyElementsOf(listOf(1, 4, 5))
        }

        it("removeAll should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.removeAll(listOf(2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("retainAll should retain items") {
            observableCollection.retainAll(listOf(1, 2, 3))
            assertThat(observableCollection).containsExactlyElementsOf(listOf(1, 2, 3))
        }

        it("retainAll should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.retainAll(listOf(1, 2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("retainAll should notify observers only if items removed") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableCollection.retainAll(listOf(1, 2, 3, 4, 5))
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("should return size") {
            assertThat(observableCollection.size).isEqualTo(5)
        }

        it("size should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableCollection.size
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        describe("iterator test") {
            val iterator by memoized { observableCollection.iterator() }

            it("should remove item") {
                iterator.next()
                iterator.remove()
                assertThat(observableCollection).containsExactlyElementsOf(listOf(2, 3, 4, 5))
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                iterator.next()
                iterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }

            it("hasNext should notify tracker") {
                assertThat(trackCallCount.get()).isEqualTo(0)
                trackableMethod(trackCallCount) {
                    iterator.hasNext()
                }
                assertThat(trackCallCount.get()).isEqualTo(1)
            }

            it("next should notify tracker") {
                assertThat(trackCallCount.get()).isEqualTo(0)
                trackableMethod(trackCallCount) {
                    iterator.next()
                }
                assertThat(trackCallCount.get()).isEqualTo(1)
            }
        }
    }
})
