package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {

    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        btnConfirm.setOnClickListener {
            val order = viewModel.order.value

            if (order?.isComplete() != true) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${order.mainMeal}\n" +
                            "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${order.drink}\n\nSubmit this order?"
                )
                .setPositiveButton("Submit") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()
                    viewModel.resetOrder()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}