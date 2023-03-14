package nl.medify.patientuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import nl.medify.patientuser.databinding.ActivityPatientBinding
import nl.medify.patientuser.feature_login.presentation.LoginActivity
import nl.medify.patientuser.feature_login.presentation.vm.LoginViewModel

/** So, GoogleMaps API is pretty broken apparently. So for optimal quality we use OnMapsSdkInitializedCallback.
 * With this, whenever the app starts, we can set the renderer of maps to the latest version.
 * I kindly refer to -> https://developers.google.com/maps/documentation/android-sdk/renderer */

@AndroidEntryPoint
class PatientActivity : AppCompatActivity(), OnMapsSdkInitializedCallback {

    private lateinit var binding: ActivityPatientBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)

        binding = ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.includeMain.bottomNavigation.setupWithNavController(navHostFragment.navController)

        //Remove the bottom navigation form selected pages
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.homeFragment || nd.id == R.id.calendarFragment) {
                binding.includeMain.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.includeMain.bottomNavigation.visibility = View.GONE
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()

        // check if user is logged in
        if (firebaseAuth.currentUser != null) {
            // save current logged in user (viewModel)
            val email: String = firebaseAuth.currentUser!!.email.toString()

            loginViewModel.getPatientByEmail(email)

            while (loginViewModel.loggedInPatient == null) {
                // do nothing until call is done
            }
        } else {
            // navigate to login page
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST -> Log.d("MapsDemo", "The latest version of the renderer is used.")
            MapsInitializer.Renderer.LEGACY -> Log.d("MapsDemo", "The legacy version of the renderer is used.")
        }
    }
}