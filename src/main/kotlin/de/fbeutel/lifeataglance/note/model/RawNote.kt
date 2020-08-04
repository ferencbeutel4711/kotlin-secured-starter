package de.fbeutel.lifeataglance.note.model

import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

data class RawNote(
        @NotBlank
        val text: String,
        @NotNull
        val targetDate: Date,
        val previousDates: List<Date> = ArrayList(),
        val cancellationDate: Date?,
        val isImportant: Boolean = false
)
