import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.scribesoul.app.model.PostData
import com.scribesoul.app.models.Billing
import com.scribesoul.app.models.BillingDTO
import com.scribesoul.app.models.CalendarPageDTO
import com.scribesoul.app.models.Chat
import com.scribesoul.app.models.ChatDTO
import com.scribesoul.app.models.ClientInfo
import com.scribesoul.app.models.ClientInfoDTO
import com.scribesoul.app.models.DottedPageDTO
import com.scribesoul.app.models.DrawablePath
import com.scribesoul.app.models.DrawablePathDTO
import com.scribesoul.app.models.EditableText
import com.scribesoul.app.models.EditableTextDTO
import com.scribesoul.app.models.Education
import com.scribesoul.app.models.EducationDTO
import com.scribesoul.app.models.FillStyle
import com.scribesoul.app.models.FillStyleDTO
import com.scribesoul.app.models.HabitsPageDTO
import com.scribesoul.app.models.ImageLayer
import com.scribesoul.app.models.ImageLayerDTO
import com.scribesoul.app.models.JournalDTO
import com.scribesoul.app.models.JournalPageDTO
import com.scribesoul.app.models.JournalSectionDTO
import com.scribesoul.app.models.LargeGridPageDTO
import com.scribesoul.app.models.LinearGradient
import com.scribesoul.app.models.LinearGradientDTO
import com.scribesoul.app.models.MoodsPageDTO
import com.scribesoul.app.models.NarrowLinedLargeMarginPageDTO
import com.scribesoul.app.models.NarrowLinedPageDTO
import com.scribesoul.app.models.NarrowLinedSmallMarginPageDTO
import com.scribesoul.app.models.PaymentStatus
import com.scribesoul.app.models.PlainPageDTO
import com.scribesoul.app.models.PostDTO
import com.scribesoul.app.models.RadialGradient
import com.scribesoul.app.models.RadialGradientDTO
import com.scribesoul.app.models.SessionStatus
import com.scribesoul.app.models.ShapeItem
import com.scribesoul.app.models.ShapeItemDTO
import com.scribesoul.app.models.SmallGridPageDTO
import com.scribesoul.app.models.SolidColor
import com.scribesoul.app.models.SolidColorDTO
import com.scribesoul.app.models.Therapist
import com.scribesoul.app.models.TherapistDTO
import com.scribesoul.app.models.TherapySession
import com.scribesoul.app.models.TherapySessionDTO
import com.scribesoul.app.models.TodoItemDTO
import com.scribesoul.app.models.TodoPageDTO
import com.scribesoul.app.models.ToolMode
import com.scribesoul.app.models.UrgencyLevel
import com.scribesoul.app.models.WideLinedLargeMarginPageDTO
import com.scribesoul.app.models.WideLinedPageDTO
import com.scribesoul.app.models.WideLinedSmallMarginPageDTO
import java.time.LocalDate
import java.time.YearMonth

// 1. UI Model -> DTO (Called when SAVING)
fun Journal.toDTO(): JournalDTO {
    return JournalDTO(
        id = this.id,
        uid = this.uid,
        name = this.name,
        sections = this.sections.map { it.toDTO() }
    )
}

fun JournalSection.toDTO(): JournalSectionDTO {
    return JournalSectionDTO(
        id = this.id,
        jid = this.jid,
        type = this.type.name, // Converts object Plain to "Plain"
        color = this.color,
        pages = this.pages.map { it.toDTO() }
    )
}

fun JournalPage.toDTO(): JournalPageDTO {
    val p = this.paths.map { it.toDTO() }; val s = this.shapes.map { it.toDTO() }; val i = this.imageLayers.map { it.toDTO() }; val t = this.texts.map { it.toDTO() }
    return when (this) {
        is JournalPage.PlainPage -> PlainPageDTO(id, name, p, s, i, t)
        is JournalPage.WideLinedPage -> WideLinedPageDTO(id, name, p, s, i, t)
        is JournalPage.WideLinedSmallMarginPage -> WideLinedSmallMarginPageDTO(id, name, p, s, i, t)
        is JournalPage.WideLinedLargeMarginPage -> WideLinedLargeMarginPageDTO(id, name, p, s, i, t)
        is JournalPage.NarrowLinedPage -> NarrowLinedPageDTO(id, name, p, s, i, t)
        is JournalPage.NarrowLinedSmallMarginPage -> NarrowLinedSmallMarginPageDTO(id, name, p, s, i, t)
        is JournalPage.NarrowLinedLargeMarginPage -> NarrowLinedLargeMarginPageDTO(id, name, p, s, i, t)
        is JournalPage.SmallGridPage -> SmallGridPageDTO(id, name, p, s, i, t)
        is JournalPage.LargeGridPage -> LargeGridPageDTO(id, name, p, s, i, t)
        is JournalPage.DottedPage -> DottedPageDTO(id, name, p, s, i, t)
        is JournalPage.HabitsPage -> HabitsPageDTO(id, habits.toList(), p, s, i, t)
        is JournalPage.TodoPage -> TodoPageDTO(id, todoList.map { TodoItemDTO(it.first, it.second) }, p, s, i, t)
        is JournalPage.MoodsPage -> MoodsPageDTO(id, initialMonth.toString(), moods.mapKeys { it.key.toString() }.mapValues { it.value.toMap() }, p, s, i, t)
        is JournalPage.CalendarPage -> CalendarPageDTO(id, initialMonth.toString(), reminders.mapKeys { it.key.toString() }.mapValues { it.value.toList() }, p, s, i, t)
        else -> throw IllegalArgumentException("Missing DTO mapping")
    }
}

// 2. DTO -> UI Model (Called when LOADING)
fun JournalDTO.toUIModel(): Journal {
    val snapshotSections = mutableStateListOf<JournalSection>()
    snapshotSections.addAll(this.sections.map { it.toUIModel() })

    return Journal(
        id = this.id,
        uid = this.uid,
        name = this.name,
        sections = snapshotSections
    )
}

fun JournalSectionDTO.toUIModel(): JournalSection {
    val snapshotPages = mutableStateListOf<JournalPage>()
    snapshotPages.addAll(this.pages.map { it.toUIModel() })

    val sectionType = try {
        SectionType.valueOf(this.type)
    } catch (e: Exception) {
        SectionType.Plain // Fallback if something goes wrong
    }

    return JournalSection(
        id = this.id,
        jid = this.jid,
        type = sectionType,
        color = this.color,
        pages = snapshotPages
    )
}

fun JournalPageDTO.toUIModel(): JournalPage {
    // 1. Rebuild the base page with specific data
    val page = when (this) {
        is PlainPageDTO -> JournalPage.PlainPage(this.id, this.name)
        is WideLinedPageDTO -> JournalPage.WideLinedPage(this.id, this.name)
        is WideLinedSmallMarginPageDTO -> JournalPage.WideLinedSmallMarginPage(this.id, this.name)
        is WideLinedLargeMarginPageDTO -> JournalPage.WideLinedLargeMarginPage(this.id, this.name)
        is NarrowLinedPageDTO -> JournalPage.NarrowLinedPage(this.id, this.name)
        is NarrowLinedSmallMarginPageDTO -> JournalPage.NarrowLinedSmallMarginPage(this.id, this.name)
        is NarrowLinedLargeMarginPageDTO -> JournalPage.NarrowLinedLargeMarginPage(this.id, this.name)
        is SmallGridPageDTO -> JournalPage.SmallGridPage(this.id, this.name)
        is LargeGridPageDTO -> JournalPage.LargeGridPage(this.id, this.name)
        is DottedPageDTO -> JournalPage.DottedPage(this.id, this.name)
        is TodoPageDTO -> JournalPage.TodoPage(id).apply { todoList.addAll(this@toUIModel.todoList.map { it.text to it.isChecked }) }
        is HabitsPageDTO -> JournalPage.HabitsPage(this.id).apply { this.habits.addAll(this@toUIModel.habits) }

        is MoodsPageDTO -> JournalPage.MoodsPage(this.id, initialMonth = YearMonth.parse(this.initialMonthStr)).apply {
            this@toUIModel.moods.forEach { (yearMonthStr, dayMap) ->
                val innerMap = SnapshotStateMap<Int, Float>()
                innerMap.putAll(dayMap)
                this.moods[YearMonth.parse(yearMonthStr)] = innerMap
            }
        }
        is CalendarPageDTO -> JournalPage.CalendarPage(this.id, initialMonth = YearMonth.parse(this.initialMonthStr)).apply {
            this@toUIModel.reminders.forEach { (dateStr, reminderList) ->
                val snapshotList = mutableStateListOf<String>()
                snapshotList.addAll(reminderList)
                this.reminders[LocalDate.parse(dateStr)] = snapshotList
            }
        }
        else -> throw IllegalArgumentException("Unknown DTO page type")
    }

    // 2. Add all the Canvas Drawings back onto the page!
    page.paths.addAll(this.paths.map { it.toUIModel() })
    page.shapes.addAll(this.shapes.map { it.toUIModel() })
    page.imageLayers.addAll(this.imageLayers.map { it.toUIModel() })
    page.texts.addAll(this.texts.map { it.toUIModel() })

    return page
}

// Dari Firebase (DTO) ke UI (PostData)
fun PostDTO.toUIModel(currentUserId: String): PostData {
    return PostData(
        // Cukup ambil id langsung karena keduanya sekarang String
        id = this.id,
        title = this.title,
        description = this.description,
        initialLikeCount = this.initialLikeCount,
        commentCount = this.commentCount,
        date = this.date
    )
}

// Dari UI ke Firebase
fun PostData.toDTO(): PostDTO {
    return PostDTO(
        id = this.id, // Pastikan ID juga dipetakan kembali
        title = this.title,
        description = this.description,
        initialLikeCount = this.initialLikeCount,
        commentCount = this.commentCount,
        date = this.date
    )
}

fun ChatDTO.toUIModel(): Chat {
    return Chat(
        id = this.id,
        message = this.message,
        sender = this.sender,
        senderId = this.senderId,
        timestamp = this.timestamp
    )
}

// Dari UI ke Firebase (DTO)
fun Chat.toDTO(): ChatDTO {
    return ChatDTO(
        id = this.id,
        message = this.message,
        sender = this.sender,
        senderId = this.senderId,
        timestamp = this.timestamp
    )
}

// --- Canvas Mappers: UI -> DTO (Saving) ---
fun FillStyle.toDTO(): FillStyleDTO = when (this) {
    is SolidColor -> SolidColorDTO(this.color)
    is LinearGradient -> LinearGradientDTO(this.colors)
    is RadialGradient -> RadialGradientDTO(this.colors)
}

fun DrawablePath.toDTO() = DrawablePathDTO(
    offsets = this.offsets,
    toolMode = this.toolMode.name, // Enum to String
    thickness = this.thickness,
    fill = this.fill.toDTO()
)

fun ShapeItem.toDTO() = ShapeItemDTO(this.type, this.offset, this.rotation, this.size, this.fill.toDTO(), this.cornerRadius)
fun EditableText.toDTO() = EditableTextDTO(this.text, this.offset, this.rotation, this.fill.toDTO(), this.fontSize, this.size)
fun ImageLayer.toDTO() = ImageLayerDTO(this.uri.toString(), this.offset, this.rotation, this.size)

// --- Canvas Mappers: DTO -> UI (Loading) ---
fun FillStyleDTO.toUIModel(): FillStyle = when (this) {
    is SolidColorDTO -> SolidColor(this.color)
    is LinearGradientDTO -> LinearGradient(this.colors)
    is RadialGradientDTO -> RadialGradient(this.colors)
}

fun DrawablePathDTO.toUIModel() = DrawablePath(
    offsets = this.offsets,
    toolMode = ToolMode.valueOf(this.toolMode), // String back to Enum
    thickness = this.thickness,
    fill = this.fill.toUIModel()
)

fun ShapeItemDTO.toUIModel() = ShapeItem(this.type, this.offset, this.rotation, this.size, this.fill.toUIModel(), this.cornerRadius)
fun EditableTextDTO.toUIModel() = EditableText(this.text, this.offset, this.rotation, false, this.fill.toUIModel(), this.fontSize, this.size)
fun ImageLayerDTO.toUIModel() = ImageLayer(Uri.parse(this.uriStr), this.offset, this.rotation, this.size, isResizing = false)

// ==========================================================
// THERAPIST MAPPERS
// ==========================================================

fun TherapistDTO.toUIModel(): Therapist {
    return Therapist(
        id = this.id,
        name = this.name,
        title = this.title,
        birthday = this.birthday,
        strNumber = this.strNumber,
        pricePerSession = this.pricePerSession,
        experienceYears = this.experienceYears,
        totalClients = this.totalClients,
        rating = this.rating,
        reviewCount = this.reviewCount,
        biography = this.biography,
        specializations = this.specializations,
        languages = this.languages,
        therapyApproaches = this.therapyApproaches,
        educations = this.educations.map { it.toUIModel() },
        workPractices = this.workPractices
    )
}

fun Therapist.toDTO(): TherapistDTO {
    return TherapistDTO(
        id = this.id,
        name = this.name,
        title = this.title,
        strNumber = this.strNumber,
        pricePerSession = this.pricePerSession,
        experienceYears = this.experienceYears,
        totalClients = this.totalClients,
        rating = this.rating,
        reviewCount = this.reviewCount,
        biography = this.biography,
        specializations = this.specializations,
        languages = this.languages,
        therapyApproaches = this.therapyApproaches,
        educations = this.educations.map { it.toDTO() },
        workPractices = this.workPractices
    )
}

fun EducationDTO.toUIModel(): Education = Education(
    institution = this.institution,
    degree = this.degree,
    year = this.year
)

fun Education.toDTO(): EducationDTO = EducationDTO(
    institution = this.institution,
    degree = this.degree,
    year = this.year
)

// ==========================================================
// CLIENT INFO MAPPERS
// ==========================================================

fun ClientInfoDTO.toUIModel(): ClientInfo {
    val enumUrgency = try {
        UrgencyLevel.valueOf(this.urgencyLevel)
    } catch (e: Exception) {
        UrgencyLevel.MILD
    }

    return ClientInfo(
        clientId = this.clientId,
        name = this.name,
        mainCondition = this.mainCondition,
        urgencyLevel = enumUrgency,
        lastSessionDate = this.lastSessionDate,
        totalSessions = this.totalSessions,
        latestNote = this.latestNote
    )
}

fun ClientInfo.toDTO(): ClientInfoDTO {
    return ClientInfoDTO(
        clientId = this.clientId,
        name = this.name,
        mainCondition = this.mainCondition,
        urgencyLevel = this.urgencyLevel.name,
        lastSessionDate = this.lastSessionDate,
        totalSessions = this.totalSessions,
        latestNote = this.latestNote
    )
}

// ==========================================================
// SESSION MAPPERS
// ==========================================================

fun TherapySessionDTO.toUIModel(): TherapySession {
    val enumStatus = try {
        SessionStatus.valueOf(this.status)
    } catch (e: Exception) {
        SessionStatus.SCHEDULED
    }

    return TherapySession(
        sessionId = this.sessionId,
        therapistId = this.therapistId,
        clientId = this.clientId,
        dateTimestamp = this.dateTimestamp,
        durationMinutes = this.durationMinutes,
        location = this.location,
        status = enumStatus,
        therapistNote = this.therapistNote,
        billingId = this.billingId
    )
}

fun TherapySession.toDTO(): TherapySessionDTO {
    return TherapySessionDTO(
        sessionId = this.sessionId,
        therapistId = this.therapistId,
        clientId = this.clientId,
        dateTimestamp = this.dateTimestamp,
        durationMinutes = this.durationMinutes,
        location = this.location,
        status = this.status.name,
        therapistNote = this.therapistNote,
        billingId = this.billingId
    )
}

fun BillingDTO.toUIModel(): Billing {
    val enumStatus = try {
        PaymentStatus.valueOf(this.status)
    } catch (e: Exception) {
        PaymentStatus.PENDING
    }

    return Billing(
        billingId = this.billingId,
        sessionId = this.sessionId,
        paymentMethod = this.paymentMethod,
        accountIdentifier = this.accountIdentifier,
        payerFirstName = this.payerFirstName,
        payerLastName = this.payerLastName,
        amount = this.amount,
        status = enumStatus
    )
}

fun Billing.toDTO(): BillingDTO {
    return BillingDTO(
        billingId = this.billingId,
        sessionId = this.sessionId,
        paymentMethod = this.paymentMethod,
        accountIdentifier = this.accountIdentifier,
        payerFirstName = this.payerFirstName,
        payerLastName = this.payerLastName,
        amount = this.amount,
        status = this.status.name
    )
}
