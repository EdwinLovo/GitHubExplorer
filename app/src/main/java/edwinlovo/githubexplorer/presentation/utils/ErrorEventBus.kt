package edwinlovo.githubexplorer.presentation.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object ErrorEventBus {
    private val _events = Channel<UiText>(Channel.BUFFERED)
    val events: Flow<UiText> = _events.receiveAsFlow()

    suspend fun send(error: UiText) {
        _events.send(error)
    }

    // Drains buffered events left over from a previous test so the channel is clean.
    fun drain() {
        generateSequence { _events.tryReceive().getOrNull() }.count()
    }
}
