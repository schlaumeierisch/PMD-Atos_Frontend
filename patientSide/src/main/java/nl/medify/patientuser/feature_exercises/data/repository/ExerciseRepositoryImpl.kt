package nl.medify.patientuser.feature_exercises.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_exercises.domain.repository.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : ExerciseRepository {


    override suspend fun getExerciseOfPatient(id: String): ArrayList<Exercise> {
        return api.getExerciseOfPatient(id)
    }
}