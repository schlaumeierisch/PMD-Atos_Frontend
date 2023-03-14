package nl.medify.patientuser.common.api

import nl.medify.patientuser.feature_calendar.domain.model.*
import nl.medify.patientuser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.patientuser.feature_exercises.domain.model.Exercise
import nl.medify.patientuser.feature_medication.domain.model.Medication
import nl.medify.utilities.data_classes.*
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalDateTime

interface PatientAPIService {

    /*******************************************************************************************/
    /** medical-record-rest-controller */

    @POST("/rest/permissions/createNotePermission")
    suspend fun createNotePermission(
        @Query("noteId") noteId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: LocalDate,
    )

    @POST("/rest/permissions/createMedicationPermission")
    suspend fun createMedicationPermission(
        @Query("medicationId") medicationId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: LocalDate,
    )

    @POST("/rest/permissions/createExercisePermission")
    suspend fun createExercisePermission(
        @Query("exerciseId") exerciseId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: LocalDate,
    )

    @POST("/rest/permissions/createDiagnosisPermission")
    suspend fun createDiagnosisPermission(
        @Query("noteId") noteId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: LocalDate,
    )

    // TODO: When permissions done add the rest of permission-rest-controller

    /*******************************************************************************************/
    /** medical-record-rest-controller */

    @POST("/rest/medicalRecords/notes/createNote")
    suspend fun createNote(
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("medicalRecordId") medicalRecordId: String,
    )

    @POST("/rest/medicalRecords/medication/createMedication")
    suspend fun createMedication(
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate,
        @Query("medicalRecordId") medicalRecordId: String,
    )

    @POST("/rest/medicalRecords/exercises/createExercise")
    suspend fun createExercise(
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate,
        @Query("medicalRecordId") medicalRecordId: String,
    )

    @POST("/rest/medicalRecords/diagnoses/createDiagnosis")
    suspend fun createDiagnosis(
        @Query("title") title: String,
        @Query("diagnosisType") diagnosisType: String,
        @Query("dateDiagnosed") dateDiagnosed: LocalDate,
        @Query("cause") cause: String,
        @Query("treatment") treatment: String,
        @Query("advice") advice: String,
        @Query("medicalRecordId") medicalRecordId: String,
    )

    @GET("/rest/medicalRecords/notes/getAllNotes/{id}")
    suspend fun getNotesOfPatient(@Path("id") id: String): ArrayList<Note>

    @GET("/rest/medicalRecords/medication/getIntakeByMedicationId/{id}")
    suspend fun getIntakeByMedicationId(@Path("id") id: String): ArrayList<MedicationIntake>

    @GET("/rest/medicalRecords/medication/getAllMedication/{id}")
    suspend fun getAllMedication(@Path("id") id: String): ArrayList<Medication>

    @GET("/rest/medicalRecords/exercises/getAllExercises/{id}")
    suspend fun getAllExercises(@Path("id") id: String): ArrayList<Exercise>

    @GET("/rest/medicalRecords/diagnoses/getAllDiagnoses/{id}")
    suspend fun getAllDiagnoses(@Path("id") id: String): ArrayList<Diagnosis>

    @GET("/rest/medicalRecords/exercises/getAllExercises/{id}")
    suspend fun getExerciseOfPatient(@Path("id") id: String): ArrayList<Exercise>

    @DELETE("/rest/medicalRecords/note/deleteNote")
    suspend fun deleteNote(@Query("noteId") noteId: String)

    /*******************************************************************************************/
    /** calendar-rest-controller */

    @POST("/rest/calendar/createAppointment")
    suspend fun createAppointment(
        @Query("dateTime") dateTime: LocalDateTime,
        @Query("reason") reason: String,
        @Query("patientId") patientId: String,
        @Query("gpId") gpId: String? = null,
        @Query("cpId") cpId: String? = null
    )

    @POST("/rest/calendar/cancelAppointment/{id}")
    suspend fun cancelAppointment(
        @Path("id") appointmentID: String
    )

    @GET("/rest/calendar/getNotAvailableDaysInThisMonth/{yearNumber}/{monthNumber}/{gpId}")
    suspend fun getNotAvailableDaysInThisMonth(
        @Path("yearNumber") yearNumber: Int,
        @Path("monthNumber") monthNumber: Int,
        @Path("gpId") gpId: String
    ): List<String>

    @GET("/rest/calendar/getAvailableTimeSlotsOfParticularDay/{yearNumber}/{monthNumber}/{monthDay}/{gpId}")
    suspend fun getAvailableTimeSlotsOfParticularDay(
        @Path("yearNumber") yearNumber: Int,
        @Path("monthNumber") monthNumber: Int,
        @Path("monthDay") monthDay: Int,
        @Path("gpId") gpId: String
    ): List<PlaceholderTimeSlot>

    @GET("/rest/calendar/getAllAppointmentsByPatientId/{id}")
    suspend fun getAllAppointmentsOfPatientByID(@Path("id") id: String): ArrayList<Appointment>

    @GET("/rest/calendar/getAllAppointmentsByGeneralPractitionerId/{id}")
    suspend fun getAllAppointmentsOfDoctorByID(@Path("id") id: String): ArrayList<Appointment>

    @GET("/rest/calendar/getAllAppointmentsByCareProviderId/{id}")
    suspend fun getAllAppointmentsOfCareProviderByID(@Path("id") id: String): ArrayList<Appointment>

    /*******************************************************************************************/
    /** account-rest-controller */

    @GET("/rest/accounts/patients/getByEmail/{email}")
    suspend fun getPatientByEmail(@Path("email") email: String): Patient

    @GET("/rest/accounts/patients/getCareProvidersOfPatientById/{id}")
    suspend fun getAllCareProvidersOfPatientByID(@Path("id") id: String): ArrayList<DoctorOrCareProvider>

    @GET("/rest/accounts/patients/getById/{id}")
    suspend fun getPatientByID(@Path("id") id: String): Patient

    @GET("/rest/accounts/patients/getAll")
    suspend fun getAllPatients(): ArrayList<Patient>

    @GET("/rest/accounts/patients/createPatientCareProviderRelation")
    suspend fun createPatientCareProviderRelation(
        @Path("careProviderId") careProviderId: String,
        @Path("patientId") patientId: String
    )

    @GET("/rest/accounts/generalPractitioners/getPatientsOfGeneralPractitionerById/{id}")
    suspend fun getPatientsOfGeneralPractitioner(@Path("id") id: String): ArrayList<Patient>

    @GET("/rest/accounts/generalPractitioners/getById/{id}")
    suspend fun getDoctorByID(@Path("id") id: String): DoctorOrCareProvider

    @GET("/rest/accounts/generalPractitioners/getAll")
    suspend fun getAllDoctors(): ArrayList<DoctorOrCareProvider>

    @GET("/rest/accounts/careProviders/getById/{id}")
    suspend fun getCareProviderByID(@Path("id") id: String): DoctorOrCareProvider

    @GET("/rest/accounts/careProviders/getAll")
    suspend fun getAllCareProviders(): ArrayList<DoctorOrCareProvider>

    /*******************************************************************************************/
    /** used to convert streetname of patient/cp/gp... into latitude and longitude coordinates for the mapView */

    @GET("https://nominatim.openstreetmap.org/search.php?")
    suspend fun getGeoCodes(
        @Query("city") city: String,
        @Query("street") street: String,
        @Query("postalcode") postalcode: String,
        @Query("limit") limit: String = "1",
        @Query("format") format: String = "jsonv2",
    ): ArrayList<GeoCode>

    /*******************************************************************************************/
    /** permissions-rest-controller */

    @POST("/rest/permissions/createNotePermission")
    suspend fun createNotePermission(
        @Query("noteId") noteId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: String
    )

    @POST("/rest/permissions/createMedicationPermission")
    suspend fun createMedicationPermission(
        @Query("medicationId") medicationId: String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: String
    )

    @POST("/rest/permissions/createExercisePermission")
    suspend fun createExercisePermissionPermission(
        @Query("exerciseId") exerciseId : String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: String
    )

    @POST("/rest/permissions/createDiagnosisPermission")
    suspend fun createDiagnosisPermission(
        @Query("diagnosisId") diagnosisId : String,
        @Query("careProviderId") careProviderId: String,
        @Query("validDate") validDate: String
    )
}