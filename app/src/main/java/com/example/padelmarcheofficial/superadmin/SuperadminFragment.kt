package com.example.padelmarcheofficial.superadmin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.padelmarcheofficial.R

class SuperadminFragment : Fragment() {

    companion object {
        fun newInstance() = SuperadminFragment()
    }

    private lateinit var viewModel: SuperadminActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SuperadminActivityViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_superadmin_home, container, false)
    }

}