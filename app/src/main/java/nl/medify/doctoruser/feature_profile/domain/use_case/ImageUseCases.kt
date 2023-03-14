package nl.medify.doctoruser.feature_profile.domain.use_case

import nl.medify.doctoruser.feature_profile.domain.use_case.get_image_by_patient_id.GetImageByPatientIdUseCase

data class ImageUseCases(
    val getImageByPatientIdUseCase: GetImageByPatientIdUseCase,
)