package nl.medify.patientuser.feature_calendar.domain.repository

import nl.medify.patientuser.feature_calendar.domain.model.Appointment
import nl.medify.patientuser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.patientuser.feature_calendar.domain.model.GeoCode
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.data_classes.PlaceholderTimeSlot
import java.time.LocalDateTime

interface CalendarRepository {

    suspend fun getAllAppointmentsOfPatientByID(id: String): ArrayList<Appointment>

    suspend fun getAllCareProvidersOfPatientByID(id: String): ArrayList<DoctorOrCareProvider>

    suspend fun getDoctorByID(id: String): DoctorOrCareProvider

    suspend fun getCareProviderByID(id: String): DoctorOrCareProvider

    suspend fun getPatientByID(id: String): Patient

    suspend fun getGeoCodes(city: String, street: String, postalcode: String): ArrayList<GeoCode>

    suspend fun cancelAppointment(appointmentID: String)

    suspend fun createAppointment(
        dateTime: LocalDateTime,
        reason: String,
        patientId: String,
        gpId: String?,
        cpId: String?
    )

    suspend fun getNotAvailableDaysInThisMonth(
        yearNumber: Int,
        monthNumber: Int,
        gpId: String
    ): List<String>

    suspend fun getAvailableTimeSlotsOfParticularDay(
        yearNumber: Int,
        monthNumber: Int,
        monthDay: Int,
        gpId: String
    ): List<PlaceholderTimeSlot>
}