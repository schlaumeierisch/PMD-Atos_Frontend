package nl.medify.doctoruser.feature_calendar.domain.repository

import nl.medify.doctoruser.feature_calendar.domain.model.Appointment
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_calendar.domain.model.GeoCode
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.data_classes.TimeSlot
import java.time.LocalDate
import java.time.LocalDateTime

interface CalendarRepository {

    suspend fun getAllAppointmentsOfDoctorByID(id: String): ArrayList<Appointment>

    suspend fun getAllAppointmentsOfCareProviderByID(id: String): ArrayList<Appointment>

    suspend fun getAllCareProvidersOfPatientByID(id: String): ArrayList<DoctorOrCareProvider>

    suspend fun getDoctorByID(id: String): DoctorOrCareProvider

    suspend fun getCareProviderByID(id: String): DoctorOrCareProvider

    suspend fun getPatientByID(id: String): Patient

    suspend fun getGeoCodes(city: String, street: String, postalcode: String): ArrayList<GeoCode>

    suspend fun getAllPatientsOfDoctor(id: String): ArrayList<Patient>

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
    ): List<LocalDate>

    suspend fun getAvailableTimeSlotsOfParticularDay(
        yearNumber: Int,
        monthNumber: Int,
        monthDay: Int,
        gpId: String
    ): List<TimeSlot>
}