import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.commands.Command

sealed class SectionType {
    object Plain : SectionType()
    object Habits : SectionType()
    object Calendar : SectionType()
    object Creation: SectionType()
}

data class JournalSection(
    val id: Int,
    val type: SectionType,
    val pages: SnapshotStateList<JournalPage> = mutableStateListOf(),
    val color: Color,
)

sealed class JournalPage(
    val undoStack: SnapshotStateList<Command> = mutableStateListOf(),
    val redoStack: SnapshotStateList<Command> = mutableStateListOf()
) {
    data class PlainPage(
        val id: Int,
        val name: String = "",
        val paths: SnapshotStateList<DrawablePath> = mutableStateListOf(),

    ) : JournalPage()

    data class HabitsPage(val id: Int) : JournalPage()

    data class CalendarPage(val id: Int) : JournalPage()
}
