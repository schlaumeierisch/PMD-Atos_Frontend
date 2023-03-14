package nl.medify.doctoruser.feature_exercises.domain.repository

import nl.medify.doctoruser.feature_exercises.domain.model.Exercise
import java.time.LocalDate

interface ExerciseRepository {
    suspend fun getExerciseOfPatient(id: String): ArrayList<Exercise>

    suspend fun getExercisesOfMedicalRecord(mrId: String, cpId: String): ArrayList<Exercise>

    suspend fun createExercise(
        title: String,
        description: String,
        startDate: LocalDate,
        endDate: LocalDate,
        medicalRecordID : String
    )
}