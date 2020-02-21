package mobx.collections

import mobx.testtools.trackableMethod
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UsePropertyAccessSyntax")
object ObservableMutableListIteratorTest : Spek({
    describe("observable list iterator") {
        val trackCallCount by memoized { AtomicInteger(0) }
        val originalList by memoized { mutableListOf(1, 2, 3, 4, 5) }
        val originalIterator by memoized { originalList.listIterator() }
        val iterator by memoized { ObservableMutableListIterator(originalIterator) }

        it("should not have previous on start") {
            assertThat(iterator.hasPrevious()).isFalse()
        }

        it("hasPrevious should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                iterator.hasPrevious()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should have previous after next") {
            assertThat(originalList.size).isGreaterThan(1)
            iterator.next()
            assertThat(iterator.hasPrevious()).isTrue()
        }

        it("next should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                iterator.next()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should be next index 0 on start") {
            assertThat(iterator.nextIndex()).isEqualTo(0)
        }

        it("nextIndex should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                iterator.nextIndex()
            }
            assertThat(trackCallCount.get()).isEqualTo(1)
        }

        it("should be next index 1 after next") {
            assertThat(originalList.size).isGreaterThan(1)
            iterator.next()
            assertThat(iterator.nextIndex()).isEqualTo(1)
        }

        it("should not have previous on start") {
            assertThatThrownBy { iterator.previous() }.isInstanceOf(NoSuchElementException::class.java)
        }

        it("should have previous after next") {
            assertThat(originalList.size).isGreaterThan(1)
            iterator.next()
            assertThat(iterator.previous()).isEqualTo(1)
        }

        it("previous should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                iterator.next()
                iterator.previous()
            }
            assertThat(trackCallCount.get()).isEqualTo(2)
        }

        it("previousIndex should notify tracker") {
            assertThat(trackCallCount.get()).isEqualTo(0)
            trackableMethod(trackCallCount) {
                iterator.next()
                iterator.previousIndex()
            }
            assertThat(trackCallCount.get()).isEqualTo(2)
        }

        it("should be -1 on start") {
            assertThat(iterator.previousIndex()).isEqualTo(-1)
        }

        it("should be 0 after next") {
            assertThat(originalList.size).isGreaterThan(1)
            iterator.next()
            assertThat(iterator.previousIndex()).isEqualTo(0)
        }

        it("should has next on start") {
            assertThat(originalList.size).isGreaterThan(1)
            assertThat(iterator.hasNext()).isTrue()
        }

        it("should not has next on end") {
            assertThat(originalList.size).isGreaterThan(1)
            while (iterator.hasNext()) {
                iterator.next()
            }
            assertThat(iterator.hasNext()).isFalse()
        }
    }
})
