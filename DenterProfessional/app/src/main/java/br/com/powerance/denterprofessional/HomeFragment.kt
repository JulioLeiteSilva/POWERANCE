package br.com.powerance.denterprofessional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.powerance.denterprofessional.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val dataSetDeEmergencias = emergenciesList()
        val emergencyAdapter = EmergencyAdapter(dataSetDeEmergencias)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.rvEmergencies)

        if (recyclerView != null) {
            recyclerView.adapter = emergencyAdapter
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}