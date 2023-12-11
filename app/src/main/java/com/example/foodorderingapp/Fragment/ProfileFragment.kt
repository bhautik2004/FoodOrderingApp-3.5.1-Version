package com.example.foodorderingapp.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.LoginActivity
import com.example.foodorderingapp.R


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    interface OnFragmentInteractionListener {
    }

    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"
    private var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView: TextView = view.findViewById(R.id.nameTextView)

        val emailTextView: TextView = view.findViewById(R.id.emailTextView)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences?.getString("user_email", "")
        val databaseHelper = DatabaseHelper(requireContext())
        val userName = databaseHelper.getUserNameByEmail(userEmail?: "");

        nameTextView.text = userName
        emailTextView.text = userEmail
        logoutButton.setOnClickListener {
            sharedPreferences?.edit()?.apply {
                clear()
                apply()
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
