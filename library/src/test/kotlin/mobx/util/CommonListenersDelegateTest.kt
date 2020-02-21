package mobx.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.concurrent.atomic.AtomicInteger

class CommonListenersDelegateTest : Spek({
    describe("common listeners delegate test") {
        val delegate by memoized { CommonListenersDelegate<mobx.core.Listener>() }
        val invokeCount by memoized { AtomicInteger() }
        val listener by memoized {
            object : mobx.core.Listener {
                override fun invoke() {
                    invokeCount.incrementAndGet()
                }
            }
        }

        it("should not add observer on observable twice") {
            val disposable = delegate.add(listener)
            assertThat(delegate.add(listener)).isSameAs(disposable)
        }

        it("should notify listeners until disposed") {
            assertThat(invokeCount.get()).isEqualTo(0)
            val disposable = delegate.add(listener)
            delegate.notifyListeners {
                it()
            }
            assertThat(invokeCount.get()).isEqualTo(1)
            disposable.dispose()
            delegate.notifyListeners {
                it()
            }
            assertThat(invokeCount.get()).isEqualTo(1)
        }
    }
})
