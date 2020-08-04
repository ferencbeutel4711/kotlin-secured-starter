package de.fbeutel.lifeataglance.common.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

abstract class Entity(
        @Id
        @Field("parentId")
        open val id: String?,
        @Field("parentCreatorId")
        open val creatorId: String,
        @Field("parentTargetDate")
        open val targetDate: Date,
        @Field("parentPreviousDates")
        open val previousDates: List<Date>,
        @Field("parentCancellationDate")
        open val cancellationDate: Date?,
        @Field("parentIsImportant")
        open val isImportant: Boolean
)
