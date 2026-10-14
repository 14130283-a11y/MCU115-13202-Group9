package com.example.foodorderingwizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _order = MutableLiveData(Order())
    val order: LiveData<Order> get() = _order

    fun setMainMeal(meal: String) {
        _order.value?.mainMeal = meal
        _order.value = _order.value
    }

    fun toggleSideDish(side: String, isChecked: Boolean) {
        val currentOrder = _order.value ?: return
        if (isChecked) {
            currentOrder.sideDishes.add(side)
        } else {
            currentOrder.sideDishes.remove(side)
        }
        _order.value = currentOrder
    }

    fun setDrink(drink: String) {
        _order.value?.drink = drink
        _order.value = _order.value
    }

    fun resetOrder() {
        _order.value = Order()
    }
}
