package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.powerance.denterprofessional.databinding.ActivityMenuBinding
import br.com.powerance.denterprofessional.databinding.FragmentSignInBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_menu)
        setupSmoothBottomMenu()

//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when(it.itemId){
//
//                R.id.Emergency -> replaceFragment(EmergencyFragment())
//                R.id.userProfile -> replaceFragment(UserProfileFragment())
//
//                else ->{
//
//                }
//            }
//            true
//        }
    }
    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_nav)
        val menu = popupMenu.menu
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menu,fragment)
        fragmentTransaction.commit()
    }

}
