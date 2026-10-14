package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DrinkFragment : Fragment(R.layout.fragment_drink) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgDrink = view.findViewById<RadioGroup>(R.id.rgDrink)
        val btnDone = view.findViewById<Button>(R.id.btnDoneDrink)

        rgDrink.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            selectedRadioButton?.let {
                viewModel.setDrink(it.text.toString())
            }
        }

        btnDone.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
}