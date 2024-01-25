package com.example.newsapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.MyProfileActivity
import com.example.newsapp.R
import com.example.newsapp.SignInActivity
import com.example.newsapp.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {
    lateinit var binding:FragmentSettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSettingsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileCons.setOnClickListener {
            val intent=Intent(requireContext(),MyProfileActivity::class.java)
            startActivity(intent)

        }
        binding.logoutCons.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.supportFragmentManager?.popBackStack()
            val intent=Intent(requireContext(),SignInActivity::class.java)
            startActivity(intent)

        }

    }
}