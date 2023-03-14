package nl.medify.patientuser.feature_exercises.domain.repository

import nl.medify.patientuser.feature_exercises.domain.model.Exercise

interface ExerciseRepository {
    suspend fun getExerciseOfPatient(id: String): ArrayList<Exercise>
}