package com.example.travelmemories.ui.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.travelmemories.R
import com.example.travelmemories.databinding.FragmentContactUsBinding

class ContactUsFragment : Fragment() {
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewName.text = getString(R.string.name_label)
        binding.textViewEmail.text = getString(R.string.email_label)
        binding.textViewMessage.text = getString(R.string.message_label)

        binding.buttonSubmit.text = getString(R.string.submit_button)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}