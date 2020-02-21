package mobx.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@Suppress("UsePropertyAccessSyntax")
object DefaultCompareKtTest : Spek({
    describe("default comparator test") {
        val firstChangeable = object : mobx.core.Changeable {
            private var id = 0
            override val change: Int
                get() = id++
        }

        val secondChangeable = object : mobx.core.Changeable {
            private var id = 0
            override val change: Int
                get() = id++
        }

        it("should compare by reference") {
            val first = listOf(0)
            val second = listOf(0)
            assertThat(notSame<List<Int>>().invoke(first, second, 0)).isTrue()
            assertThat(notSame<List<Int>>().invoke(first, first, 0)).isFalse()
        }

        it("should compare by reference and Changeable by id") {
            assertThat(
                notSame<mobx.core.Changeable>().invoke(
                    firstChangeable,
                    secondChangeable,
                    0
                )
            ).isTrue()
            assertThat(
                notSame<mobx.core.Changeable>().invoke(
                    firstChangeable,
                    firstChangeable,
                    0
                )
            ).isFalse()
            assertThat(
                notSame<mobx.core.Changeable>().invoke(
                    firstChangeable,
                    firstChangeable,
                    2
                )
            ).isTrue()
        }
    }
})
