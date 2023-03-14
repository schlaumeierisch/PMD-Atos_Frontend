package nl.medify.doctoruser.common.api

import nl.medify.doctoruser.feature_calendar.domain.model.Appointment
import nl.medify.doctoruser.feature_calendar.domain.model.DoctorOrCareProvider
import nl.medify.doctoruser.feature_calendar.domain.model.GeoCode
import nl.medify.doctoruser.feature_diagnosis.domain.model.Diagnosis
import nl.medify.doctoruser.feature_exercises.domain.model.Exercise
import nl.medify.doctoruser.feature_medication.domain.model.Medication
import nl.medify.doctoruser.feature_profile.domain.model.Image
import nl.medify.utilities.data_classes.MedicationIntake
import nl.medify.utilities.data_classes.Note
import nl.medify.utilities.data_classes.Patient
import nl.medify.utilities.data_classes.TimeSlot
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalDateTime

interface MedifyAPIService {

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

    @GET("/rest/permissions/getMedicationOfMedicalRecord")
    suspend fun getMedicationOfMedicalRecord(
        @Query("mrId") mrId: String,
        @Query("cpId") cpId: String
    ): ArrayList<Medication>

    @GET("/rest/permissions/getDiagnosisOfMedicalRecord")
    suspend fun getDiagnosisOfMedicalRecord(
        @Query("mrId") mrId: String,
        @Query("cpId") cpId: String
    ): ArrayList<Diagnosis>

    @GET("/rest/permissions/getExercisesOfMedicalRecord")
    suspend fun getExercisesOfMedicalRecord(
        @Query("mrId") mrId: String,
        @Query("cpId") cpId: String
    ): ArrayList<Exercise>

    @GET("/rest/permissions/getNotesOfMedicalRecord")
    suspend fun getNotesOfMedicalRecord(
        @Query("mrId") mrId: String,
        @Query("cpId") cpId: String
    ): ArrayList<Note>

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
    ): List<LocalDate>

    @GET("/rest/calendar/getAvailableTimeSlotsOfParticularDay/{yearNumber}/{monthNumber}/{monthDay}/{gpId}")
    suspend fun getAvailableTimeSlotsOfParticularDay(
        @Path("yearNumber") yearNumber: Int,
        @Path("monthNumber") monthNumber: Int,
        @Path("monthNumber") monthDay: Int,
        @Path("gpId") gpId: String
    ): List<TimeSlot>

    @GET("/rest/calendar/getAllAppointmentsByPatientId/{id}")
    suspend fun getAllAppointmentsOfPatientByID(@Path("id") id: String): ArrayList<Appointment>

    @GET("/rest/calendar/getAllAppointmentsByGeneralPractitionerId/{id}")
    suspend fun getAllAppointmentsOfDoctorByID(@Path("id") id: String): ArrayList<Appointment>

    @GET("/rest/calendar/getAllAppointmentsByCareProviderId/{id}")
    suspend fun getAllAppointmentsOfCareProviderByID(@Path("id") id: String): ArrayList<Appointment>

    /*******************************************************************************************/
    /** account-rest-controller */

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
    suspend fun getGeneralPractitionerById(@Path("id") id: String): DoctorOrCareProvider

    @GET("/rest/accounts/generalPractitioners/getByEmail/{email}")
    suspend fun getGeneralPractitionerByEmail(@Path("email") email: String): DoctorOrCareProvider

    @GET("/rest/accounts/generalPractitioners/getPatientsOfGeneralPractitionerById/{id}")
    suspend fun getPatientsOfGeneralPractitionerById(@Path("id") id: String): ArrayList<Patient>

    @GET("/rest/accounts/patients/getAll")
    suspend fun getAllPatients(@Path("id") id: String): ArrayList<Patient>

    @GET("/rest/accounts/patients/getById/{id}")
    suspend fun getPatientById(@Path("id") id: String): Patient

    @GET("/rest/accounts/generalPractitioners/getById/{id}")
    suspend fun getDoctorByID(@Path("id") id: String): DoctorOrCareProvider

    @GET("/rest/accounts/generalPractitioners/getAll")
    suspend fun getAllDoctors(): ArrayList<DoctorOrCareProvider>

    @GET("/rest/accounts/careProviders/getById/{id}")
    suspend fun getCareProviderByID(@Path("id") id: String): DoctorOrCareProvider

    @GET("/rest/accounts/careProviders/getByEmail/{email}")
    suspend fun getCareProviderByEmail(@Path("email") email: String): DoctorOrCareProvider

    @GET("/rest/accounts/careProviders/getAll")
    suspend fun getAllCareProviders(): ArrayList<DoctorOrCareProvider>

    @GET("/rest/accounts/careProviders/getPatientsOfCareProviderById/{id}")
    suspend fun getPatientsOfCareProviderById(@Path("id") id: String): ArrayList<Patient>

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
    /** image-rest-controller */

    @GET("/rest/images/getImageByPatientId/{patientId}")
    suspend fun getImageByPatientId(@Path("patientId") patientId: String): ArrayList<Image>

}