package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkBoxes = listOf(
            view.findViewById<CheckBox>(R.id.cbFries),
            view.findViewById<CheckBox>(R.id.cbSalad),
            view.findViewById<CheckBox>(R.id.cbCornCup)
        )

        checkBoxes.forEach { cb ->
            cb.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.toggleSideDish(buttonView.text.toString(), isChecked)
            }
        }

        val btnDone = view.findViewById<Button>(R.id.btnDoneSideDishes)
        btnDone.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
}