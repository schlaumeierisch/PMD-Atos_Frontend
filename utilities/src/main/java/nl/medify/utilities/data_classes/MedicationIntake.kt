package nl.medify.utilities.data_classes

import java.time.LocalDateTime

data class MedicationIntake(
    val id: String,
    val time: LocalDateTime,
    val amount: Int,
    val unit: String,
    val medicationId: String,
)