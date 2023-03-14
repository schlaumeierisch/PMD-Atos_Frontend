package nl.medify.doctoruser.feature_profile.presentation

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import nl.medify.doctoruser.R
import nl.medify.doctoruser.databinding.FragmentProfileBinding
import nl.medify.doctoruser.feature_patient_list.presentation.vm.PatientListViewModel
import nl.medify.doctoruser.feature_profile.domain.model.Image
import nl.medify.doctoruser.feature_profile.presentation.vm.ImageViewModel
import nl.medify.utilities.data_classes.Patient

class PatientProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val imageViewModel: ImageViewModel by activityViewModels()
    private val patientListViewModel: PatientListViewModel by activityViewModels()
    private lateinit var selectedPatient: Patient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedPatient = patientListViewModel.selectedPatient!!
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.patient_profile)

        // set navigation(s)
        binding.header.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnMedicalRecords.setOnClickListener {
            findNavController().navigate(R.id.action_patientProfileFragment_to_medicalRecordFragment)
        }

        if (patientListViewModel.selectedPatient != null) {
            selectedPatient = patientListViewModel.selectedPatient!!

            binding.tvName.text = buildString {
                append(selectedPatient.firstName)
                append(" ")
                append(selectedPatient.lastName)
            }
            binding.tvBirthdateValue.text = selectedPatient.birthDate
            binding.tvGenderValue.text = selectedPatient.gender
        }

        //Get patient's image
        imageViewModel.getImagesByPatientId(selectedPatient.id)

        //Load image from database
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imageViewModel.imageList.collect {
                    val images: List<Image> = it.imageList
                    initDisplayImages(images)
                }
            }
        }
    }

    private fun initDisplayImages(images: List<Image>){
        if (images.isNotEmpty()) {
            val byteArray = Base64.decode(images[0].image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val drawable = BitmapDrawable(resources, bitmap)
            binding.ivProfile.setImageDrawable(drawable)
        }
    }
}