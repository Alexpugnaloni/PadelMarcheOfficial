package com.example.padelmarcheofficial.ui.amici

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.databinding.FragmentAmiciBinding

class AmiciFragment : Fragment()/*,RecyclerAdapter.ViewHolder.ClickListener */{

    private var _binding: FragmentAmiciBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val amiciViewModel =
            ViewModelProvider(this).get(AmiciViewModel::class.java)

        _binding = FragmentAmiciBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        amiciViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}