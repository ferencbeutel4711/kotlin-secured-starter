package de.fbeutel.lifeataglance.note.model

import de.fbeutel.lifeataglance.common.model.Entity
import java.util.*
data class Note(
        val text: String,
        override val id: String,
        override val creatorId: String,
        override val targetDate: Date,
        override val previousDates: List<Date>,
        override val cancellationDate: Date?,
        override val isImportant: Boolean
) : Entity(id, creatorId, targetDate, previousDates, cancellationDate, isImportant)