package com.nassafy.aro.ui.view.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentAroServiceSelectBinding
import com.nassafy.aro.ui.view.BaseFragment

class AroServiceSelectFragment: BaseFragment<FragmentAroServiceSelectBinding>(FragmentAroServiceSelectBinding::inflate) {

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_aroServiceSelectFragment_to_aroCountryPlaceSelectFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}