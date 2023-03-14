package nl.medify.patientuser.feature_calendar.data.repository

import nl.medify.patientuser.common.api.PatientAPIService
import nl.medify.patientuser.feature_calendar.domain.model.Appointment
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_calendar.domain.model.GeoCode
import nl.medify.patientuser.feature_calendar.domain.repository.CalendarRepository
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.data_classes.PlaceholderTimeSlot
import java.time.LocalDateTime
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: PatientAPIService
) : CalendarRepository {

    override suspend fun getAllAppointmentsOfPatientByID(id: String): ArrayList<Appointment> {
        return api.getAllAppointmentsOfPatientByID(id)
    }

    override suspend fun getAllCareProvidersOfPatientByID(id: String): ArrayList<DoctorOrCareProvider> {
        return api.getAllCareProvidersOfPatientByID(id)
    }

    override suspend fun getDoctorByID(id: String): DoctorOrCareProvider {
        return api.getDoctorByID(id)
    }

    override suspend fun getCareProviderByID(id: String): DoctorOrCareProvider {
        return api.getCareProviderByID(id)
    }

    override suspend fun getPatientByID(id: String): Patient {
        return api.getPatientByID(id)
    }

    override suspend fun getGeoCodes(
        city: String,
        street: String,
        postalcode: String
    ): ArrayList<GeoCode> {
        return api.getGeoCodes(city, street, postalcode)
    }

    override suspend fun cancelAppointment(
        appointmentID: String
    ) {
        api.cancelAppointment(appointmentID)
    }

    override suspend fun createAppointment(
        dateTime: LocalDateTime,
        reason: String,
        patientId: String,
        gpId: String?,
        cpId: String?
    ) {
        api.createAppointment(dateTime, reason, patientId, gpId, cpId)
    }

    override suspend fun getNotAvailableDaysInThisMonth(
        yearNumber: Int,
        monthNumber: Int,
        gpId: String
    ): List<String> {
        return api.getNotAvailableDaysInThisMonth(yearNumber, monthNumber, gpId)
    }

    override suspend fun getAvailableTimeSlotsOfParticularDay(
        yearNumber: Int,
        monthNumber: Int,
        monthDay: Int,
        gpId: String
    ): List<PlaceholderTimeSlot> {
        return api.getAvailableTimeSlotsOfParticularDay(yearNumber, monthNumber, monthDay, gpId)
    }
}