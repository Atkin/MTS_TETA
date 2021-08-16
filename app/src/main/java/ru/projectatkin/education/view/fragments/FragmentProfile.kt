package ru.projectatkin.education.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Profile.Profile
import ru.projectatkin.education.R
import ru.projectatkin.education.ViewModels.ProfileViewModel

const val TAG_PROFILE = "ProfileFragment"

class FragmentProfile : Fragment() {
    private lateinit var bottomNavigationBar: BottomNavigationView
    private var profileId = 0
    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

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


        view.account_exit_button.setOnClickListener {
            addDataToDatabase()
            activity?.onBackPressed()
        }

        return view
    }

    private fun addDataToDatabase() {
        val name = account_edit_name.text.toString()
        val surname = account_edit_surname.text.toString()
        val password = account_edit_password.text.toString()
        val email = account_edit_email.text.toString()
        val telephone = account_edit_telephone.text.toString()

        if (inputCheck(name, surname, password, email, telephone)) {
            val profile = Profile(name, surname, password, email, telephone, profileId)
            profileViewModel.addProfile(profile)
            Toast.makeText(requireContext(), "Successully added!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(
        name: String,
        surname: String,
        password: String,
        email: String,
        telephone: String
    ): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) && TextUtils.isEmpty(password) && TextUtils.isEmpty(
            email
        ) && TextUtils.isEmpty(
            telephone
        ))
    }
}