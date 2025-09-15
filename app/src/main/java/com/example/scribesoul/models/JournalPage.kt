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
    val pages: MutableList<JournalPage> = mutableListOf(),
    val color: Color,
)

sealed class JournalPage {
    data class PlainPage(
        val id: Int,
        val paths: MutableList<DrawablePath> = mutableListOf(),
        val undoStack: MutableList<Command> = mutableListOf(),
        val redoStack: MutableList<Command> = mutableListOf()
    ) : JournalPage()

    data class HabitsPage(val id: Int) : JournalPage()

    data class CalendarPage(val id: Int) : JournalPage()
}
