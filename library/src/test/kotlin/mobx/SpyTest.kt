package mobx

import mobx.core.action
import mobx.core.spy
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SpyTest : Spek({
    describe("spy test") {
        var spyPayload: Any? = null
        val disposable = spy {
            spyPayload = it
        }
        it("should receive payload from actions until disposed") {
            action("testPayload") {}
            assertThat(spyPayload).isEqualTo("testPayload")
            disposable.dispose()
            action("new test payload") {}
            assertThat(spyPayload).isEqualTo("testPayload")
        }
    }
})
