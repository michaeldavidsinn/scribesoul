import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.commands.Command
import com.example.scribesoul.models.ShapeItem
import java.time.LocalDate
import java.time.YearMonth

sealed class SectionType {
    object Plain : SectionType()
    object Habits : SectionType()
    object Calendar : SectionType()
    object Creation : SectionType()
    object Todo : SectionType()
    object Mood: SectionType()
    object WideLined: SectionType()
    object WideLinedSmallMargin: SectionType()
    object WideLinedLargeMargin: SectionType()
    object NarrowLined: SectionType()
    object NarrowLinedSmallMargin: SectionType()
    object NarrowLinedLargeMargin: SectionType()
    object SmallGrid: SectionType()
    object LargeGrid: SectionType()
}

data class Journal(
    val id: Int,
    val uid: Int,
    val name: String,
    val sections: SnapshotStateList<JournalSection> = mutableStateListOf() //This will be removed when connected to backend
)

data class JournalSection(
    val id: Int,
    val jid: Int,
    val type: SectionType,
    val pages: SnapshotStateList<JournalPage> = mutableStateListOf(),
    val color: Color,
)

sealed class JournalPage(
    open val id: Int,
    val undoStack: SnapshotStateList<Command> = mutableStateListOf(),
    val redoStack: SnapshotStateList<Command> = mutableStateListOf(),
    val paths: SnapshotStateList<DrawablePath> = mutableStateListOf(),
    val shapes: SnapshotStateList<ShapeItem> = mutableStateListOf()
) {
    data class PlainPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)

    data class WideLinedPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)

    data class WideLinedSmallMarginPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)

    data class WideLinedLargeMarginPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)

    data class NarrowLinedPage(
        override val id: Int,
        var name: String = "",
    ) : JournalPage(id)

    data class NarrowLinedSmallMarginsPage(
        override val id: Int,
        var name: String = ""
    ) : JournalPage(id)

    data class NarrowLinedLargeMarginsPage(
        override val id: Int,
        var name: String = ""
    ) : JournalPage(id)

    data class SmallGridPage(
        override val id: Int,
        var name: String = ""
    ) : JournalPage(id)

    data class LargeGridPage(
        override val id: Int,
        var name: String = ""
    ) : JournalPage(id)

    data class DottedPage(
        override val id: Int,
        var name: String = ""
    ) : JournalPage(id)


    data class NarrowLinedSmallMarginPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)

    data class NarrowLinedLargeMarginPage(
        override val id: Int,
        var name: String = "",

        ) : JournalPage(id)


    data class HabitsPage(
        override val id: Int,
        val habits: SnapshotStateList<String> = mutableStateListOf(),
    ) : JournalPage(id)

    data class CalendarPage(
        override val id: Int,
        val initialMonth: YearMonth = YearMonth.now(),
        val reminders: SnapshotStateMap<LocalDate, SnapshotStateList<String>> = SnapshotStateMap()
    ) : JournalPage(id){
        var currentMonth by mutableStateOf(initialMonth)
    }

    data class MoodsPage(
        override val id: Int, val moods: SnapshotStateMap<YearMonth, SnapshotStateMap<Int, Float>> = SnapshotStateMap(), var currentMonth: YearMonth
    ) : JournalPage(id)

    data class TodoPage(
        override val id: Int,
        var todoList: SnapshotStateList<Pair<String, Boolean>> = mutableStateListOf(),
    ) : JournalPage(id)


}
