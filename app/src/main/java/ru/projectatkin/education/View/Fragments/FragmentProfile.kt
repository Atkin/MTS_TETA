package ru.projectatkin.education.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.projectatkin.education.R

const val TAG_PROFILE = "ProfileFragment"

class FragmentProfile() : Fragment() {
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        bottomNavigationBar = view.findViewById(R.id.list_bottom_navigation_profile)
        this.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_home_button -> {
                    findNavController().navigate(R.id.action_fragmentProfile_to_fragmentHome, null)
                }
                R.id.list_profile_button -> {
                    Log.d(TAG_PROFILE, "Same")
                }
                else -> {
                }
            }
            true
        }

        return view
    }
}