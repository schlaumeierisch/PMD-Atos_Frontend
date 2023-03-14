package nl.medify.patientuser.feature_calendar.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_calendar.data.repository.CalendarRepositoryImpl
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.patientuser.feature_calendar.domain.use_case.CalendarUseCases
import nl.medify.patientuser.feature_calendar.domain.use_case.cancel_appointment.CancelAppointmentUseCase
import nl.medify.patientuser.feature_calendar.domain.use_case.get_all_appointments_of_patient.GetAllAppointmentsOfPatientUseCase
import nl.medify.patientuser.feature_calendar.domain.use_case.get_all_doctors_and_careproviders_of_patient.GetAllDoctorsAndCareProvidersOfPatientUseCase
import nl.medify.patientuser.feature_calendar.domain.use_case.get_available_timeslots.GetAvailableTimeSlotsOfParticularDay
import nl.medify.patientuser.feature_calendar.domain.use_case.get_geo_codes.GetGeoCodesUseCase
import nl.medify.patientuser.feature_calendar.domain.use_case.get_not_available_days.GetNotAvailableDaysInThisMonth
import nl.medify.patientuser.feature_calendar.domain.use_case.post_create_appointment.PostCreateAppointmentUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {

    @Provides
    @Singleton
    fun provideCalendarRepository(api: PatientAPIService): CalendarRepository {
        return CalendarRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCalendarUseCases(repository: CalendarRepository): CalendarUseCases {
        return CalendarUseCases(
            getAllAppointmentsOfPatientUseCase = GetAllAppointmentsOfPatientUseCase(repository),
            getAllDoctorsAndCareProvidersOfPatientUseCase = GetAllDoctorsAndCareProvidersOfPatientUseCase(repository),
            getGeoCodesUseCase = GetGeoCodesUseCase(repository),
            postCreateAppointmentUseCase = PostCreateAppointmentUseCase(repository),
            cancelAppointmentUseCase = CancelAppointmentUseCase(repository),
            getAvailableTimeSlotsOfParticularDay = GetAvailableTimeSlotsOfParticularDay(repository),
            getNotAvailableDaysInThisMonth = GetNotAvailableDaysInThisMonth(repository)
        )
    }
}