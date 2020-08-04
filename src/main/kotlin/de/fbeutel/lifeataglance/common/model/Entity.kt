package de.fbeutel.lifeataglance.common.model

import java.util.*

abstract class Entity(open val targetDate: Date, open val previousDates: List<Date>) {
}
