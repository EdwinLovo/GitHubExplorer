package edwinlovo.githubexplorer.presentation.utils.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtTest {

    @Test
    fun toFormattedDateReturnsNonBlankForValidIsoTimestamp() {
        val formatted = "2025-11-01T12:34:56Z".toFormattedDate()

        assertThat(formatted).isNotEmpty()
    }

    @Test
    fun toFormattedDateReturnsEmptyForMalformedInput() {
        val formatted = "not-a-date".toFormattedDate()

        assertThat(formatted).isEmpty()
    }
}
