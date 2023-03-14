package nl.medify.doctoruser.feature_exercises.data.repository

import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_exercises.domain.model.Exercise
import nl.medify.doctoruser.feature_exercises.domain.repository.ExerciseRepository
import java.time.LocalDate
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val api: MedifyAPIService
) : ExerciseRepository {

    override suspend fun getExerciseOfPatient(id: String): ArrayList<Exercise> {
        return api.getExerciseOfPatient(id)
    }

    override suspend fun getExercisesOfMedicalRecord(
        mrId: String,
        cpId: String
    ): ArrayList<Exercise> {
        return api.getExercisesOfMedicalRecord(mrId, cpId)
    }

    override suspend fun createExercise(
        title: String,
        description: String,
        startDate: LocalDate,
        endDate: LocalDate,
        medicalRecordID: String
    ) {
        api.createExercise(title, description, startDate, endDate, medicalRecordID)
    }
}