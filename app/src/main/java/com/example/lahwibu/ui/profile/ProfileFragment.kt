package com.example.lahwibu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lahwibu.databinding.FragmentProfileBinding
import com.example.lahwibu.ui.login.LoginActivity
import com.example.lahwibu.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        auth = Firebase.auth

        binding.btnLogout.setOnClickListener {
            signOut()
        }
        setProfileUser()
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireActivity())
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            requireActivity().run {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }
    }

    private fun setProfileUser(){
        val user = auth.currentUser
        user?.let {
            with(binding){
                userName.text = it.displayName
                Glide.with(binding.root)
                    .load(it.photoUrl)
                    .circleCrop()
                    .into(userProfile)

            }
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

}