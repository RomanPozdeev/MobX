package mobx

import mobx.core.action
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ActionTest : Spek({
    describe("action invoke test") {
        var bodyWasCalled = 0
        it("should call body() on create") {
            assertThat(bodyWasCalled).isEqualTo(0)
            action { bodyWasCalled++ }
            assertThat(bodyWasCalled).isEqualTo(1)
        }
    }
})
