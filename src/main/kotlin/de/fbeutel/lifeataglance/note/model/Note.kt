package de.fbeutel.lifeataglance.note.model

import de.fbeutel.lifeataglance.common.model.Entity
import java.util.*

data class Note(val text: String, override val targetDate: Date, override val previousDates: List<Date>) : Entity(targetDate, previousDates) {
}
