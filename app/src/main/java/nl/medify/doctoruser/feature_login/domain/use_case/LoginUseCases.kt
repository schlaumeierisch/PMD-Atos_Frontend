package nl.medify.doctoruser.feature_login.domain.use_case

import nl.medify.doctoruser.feature_login.domain.use_case.get_care_provider_by_email.GetCareProviderByEmailUseCase
import nl.medify.doctoruser.feature_login.domain.use_case.get_general_practitioner_by_email.GetGeneralPractitionerByEmailUseCase

data class LoginUseCases(
    val getGeneralPractitionerByEmailUseCase: GetGeneralPractitionerByEmailUseCase,
    val getCareProviderByEmailUseCase: GetCareProviderByEmailUseCase
)