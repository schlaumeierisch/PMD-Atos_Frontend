package nl.medify.doctoruser.feature_calendar.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.feature_calendar.data.repository.CalendarRepositoryImpl
import nl.medify.doctoruser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.doctoruser.feature_calendar.domain.use_case.CalendarUseCases
import nl.medify.doctoruser.feature_calendar.domain.use_case.cancel_appointment.CancelAppointmentUseCase
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_all_appointments_of_careprovider.GetAllAppointmentsOfCareProviderUseCase
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_all_appointments_of_doctor.GetAllAppointmentsOfDoctorUseCase
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_all_patients_of_doctor.GetAllPatientsOfDoctorUseCase
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_available_timeslots.GetAvailableTimeSlotsOfParticularDay
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_geo_codes.GetGeoCodesUseCase
import nl.medify.doctoruser.feature_calendar.domain.use_case.get_not_available_days.GetNotAvailableDaysInThisMonth
import nl.medify.doctoruser.feature_calendar.domain.use_case.post_create_appointment.PostCreateAppointmentUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {

    @Provides
    @Singleton
    fun provideCalendarRepository(api: MedifyAPIService): CalendarRepository {
        return CalendarRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCalendarUseCases(repository: CalendarRepository): CalendarUseCases {
        return CalendarUseCases(
            getAllAppointmentsOfDoctorUseCase = GetAllAppointmentsOfDoctorUseCase(repository),
            getAllPatientsOfDoctorUseCase = GetAllPatientsOfDoctorUseCase(repository),
            getGeoCodesUseCase = GetGeoCodesUseCase(repository),
            getAllAppointmentsOfCareProviderUseCase = GetAllAppointmentsOfCareProviderUseCase(repository),
            postCreateAppointmentUseCase = PostCreateAppointmentUseCase(repository),
            cancelAppointmentUseCase = CancelAppointmentUseCase(repository),
            getAvailableTimeSlotsOfParticularDay = GetAvailableTimeSlotsOfParticularDay(repository),
            getNotAvailableDaysInThisMonth = GetNotAvailableDaysInThisMonth(repository)
        )
    }
}