package mobx.collections

import mobx.testtools.trackableMethod
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UsePropertyAccessSyntax")
object ObservableMutableListTest : Spek({

    describe("observable mutable list") {
        val trackCallCount by memoized { AtomicInteger(0) }
        val originalList by memoized { mutableListOf(1, 2, 3, 4, 5) }
        val observableList by memoized { ObservableMutableList(originalList) }
        val subscriber by memoized {
            FakeSubscriber(observableList)
        }

        it("should return new change value on every update") {
            val changes = mutableSetOf<Int>()
            repeat(10) {
                changes.add(observableList.change)
                observableList.add(6)
            }
            assertThat(changes.size).isEqualTo(10)
        }

        it("should add item on list") {
            observableList.add(6)
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 3, 4, 5, 6))
        }

        it("should notify observers after add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.add(6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should insert item on list") {
            observableList.add(0, 6)
            assertThat(observableList).containsExactlyElementsOf(listOf(6, 1, 2, 3, 4, 5))
        }

        it("should notify observers after insert") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.add(0, 6)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should add items on list") {
            observableList.addAll(listOf(6, 7, 8))
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 3, 4, 5, 6, 7, 8))
        }

        it("should notify observers after items add") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.addAll(listOf(6, 7, 8))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should insert items on list") {
            observableList.addAll(0, listOf(6, 7, 8))
            assertThat(observableList).containsExactlyElementsOf(listOf(6, 7, 8, 1, 2, 3, 4, 5))
        }

        it("should notify observers after items insert") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.addAll(0, listOf(6, 7, 8))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should clear list") {
            assertThat(observableList.size).isNotZero()
            observableList.clear()
            assertThat(observableList).isEmpty()
        }

        it("contains() should work") {
            assertThat(observableList.contains(5)).isTrue()
            assertThat(observableList.contains(10)).isFalse()
        }

        it("contains() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.contains(5)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("containsAll() should work") {
            assertThat(observableList.containsAll(listOf(1, 2, 3))).isTrue()
            assertThat(observableList.containsAll(listOf(1, 2, 3, 10))).isFalse()
        }

        it("containsAll() should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.containsAll(listOf(1, 2, 3))
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("get() should work") {
            assertThat(observableList[0]).isEqualTo(1)
            assertThat(observableList[1]).isEqualTo(2)
        }

        it("get() should notify current tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList[0]
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("isEmpty should work") {
            assertThat(observableList.isEmpty()).isFalse()
            observableList.clear()
            assertThat(observableList.isEmpty()).isTrue()
        }

        it("isEmpty should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.isEmpty()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("remove should remove item") {
            observableList.remove(2)
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 3, 4, 5))
        }

        it("remove should notify observers if it present") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.remove(1)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("remove should not notify observers if it not present") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.remove(11)
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("removeAll should remove items") {
            observableList.removeAll(listOf(2, 3))
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 4, 5))
        }

        it("removeAll should notify observers if collection was modified") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.removeAll(listOf(2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("removeAll should not notify observers if collection was not modified") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.removeAll(listOf(11, 12))
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("removeAt should remove item") {
            observableList.removeAt(1)
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 3, 4, 5))
        }

        it("removeAt should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.removeAt(1)
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("retainAll should retain items") {
            observableList.retainAll(listOf(1, 2, 3))
            assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 3))
        }

        it("retainAll should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.retainAll(listOf(1, 2, 3))
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("retainAll should notify observers only if items removed") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList.retainAll(listOf(1, 2, 3, 4, 5))
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
        }

        it("set should update list") {
            observableList[0] = 10
            assertThat(observableList).containsExactlyElementsOf(listOf(10, 2, 3, 4, 5))
        }

        it("set should notify observers") {
            assertThat(subscriber.subscriberCallCount).isEqualTo(0)
            observableList[0] = 10
            assertThat(subscriber.subscriberCallCount).isEqualTo(1)
        }

        it("should return size") {
            assertThat(observableList.size).isEqualTo(5)
        }

        it("size should notify current tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.size
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should return indexOf") {
            assertThat(observableList.indexOf(2)).isEqualTo(1)
        }

        it("indexOf should notify current tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.indexOf(1)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should return lastIndexOf") {
            observableList.add(2)
            assertThat(observableList.lastIndexOf(2)).isEqualTo(5)
        }

        it("lastIndexOf should notify current tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                observableList.lastIndexOf(2)
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        describe("list iterator test") {
            val listIterator by memoized { observableList.listIterator() }

            it("should add item") {
                listIterator.add(10)
                assertThat(observableList).containsExactlyElementsOf(listOf(10, 1, 2, 3, 4, 5))
            }

            it("should notify observers after add") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.add(6)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }

            it("should remove item") {
                listIterator.next()
                listIterator.remove()
                assertThat(observableList).containsExactlyElementsOf(listOf(2, 3, 4, 5))
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }

            it("should update item") {
                listIterator.next()
                listIterator.set(11)
                assertThat(observableList).containsExactlyElementsOf(listOf(11, 2, 3, 4, 5))
            }

            it("should notify observers after update") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.set(11)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }

        describe("list iterator index test") {
            val listIterator by memoized { observableList.listIterator(2) }

            it("should add item") {
                listIterator.add(10)
                assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 10, 3, 4, 5))
            }

            it("should notify observers after add") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.add(6)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }

            it("should remove item") {
                listIterator.next()
                listIterator.remove()
                assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 4, 5))
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }

            it("should update item") {
                listIterator.next()
                listIterator.set(11)
                assertThat(observableList).containsExactlyElementsOf(listOf(1, 2, 11, 4, 5))
            }

            it("should notify observers after update") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.set(11)
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }

        describe("iterator test") {
            val listIterator by memoized { observableList.iterator() }

            it("should remove item") {
                listIterator.next()
                listIterator.remove()
                assertThat(observableList).containsExactlyElementsOf(listOf(2, 3, 4, 5))
            }

            it("should notify observers after remove") {
                assertThat(subscriber.subscriberCallCount).isEqualTo(0)
                listIterator.next()
                listIterator.remove()
                assertThat(subscriber.subscriberCallCount).isEqualTo(1)
            }
        }
    }
})
