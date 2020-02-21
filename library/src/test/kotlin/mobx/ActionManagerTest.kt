package mobx

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ActionManagerTest : Spek({
    describe("action manager test") {
        var actionsPayload: Any? = null
        mobx.core.ActionManager.listenActions {
            actionsPayload = it
        }

        val nesting = mutableListOf<Int>()
        mobx.core.ActionManager.listenNestingChanges {
            nesting.add(0)
        }

        it("should notify actions listeners") {
            var actionRunCount = 0
            mobx.core.ActionManager.runAsAction("TestPayload") {
                actionRunCount++
            }

            assertThat(actionRunCount).isEqualTo(1)
            assertThat(actionsPayload).isEqualTo("TestPayload")
            assertThat(nesting).containsOnly(0)
        }

        it("should work with null actions") {
            mobx.core.ActionManager.runAsAction(null, null)
        }
    }
})
