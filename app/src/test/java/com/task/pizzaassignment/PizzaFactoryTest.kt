package com.task.pizzaassignment

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PizzaFactoryTest {

    private lateinit var pizzaFactory: PizzaFactory

    @Before
    fun setUp() {
        pizzaFactory = PizzaFactory()
    }

    @Test
    fun testMenuIsNotEmpty() {
        assertTrue("Menu should have pizzas", pizzaFactory.menu.isNotEmpty())
    }

    @Test
    fun testInvalidToppingOrderReturnsNull() {
        val pizza = pizzaFactory.menu.first()
        val invalidTopping = Topping("Chicken Tikka", 35, "Non-Veg")
        val crust = pizzaFactory.crusts.first()

        val order = pizzaFactory.placeOrder(
            listOf(PizzaOrder(pizza, "Medium", crust, listOf(invalidTopping))),
            emptyList()
        )

        assertNull("Order should be null for invalid topping", order)
    }

    @Test
    fun testValidOrderPlacement() {
        val pizza = pizzaFactory.menu.first()
        val crust = pizzaFactory.crusts.first()

        val order = pizzaFactory.placeOrder(
            listOf(PizzaOrder(pizza, "Medium", crust, emptyList())),
            emptyList()
        )

        assertNotNull("Order should be placed successfully", order)
        assertTrue("Order should be marked as placed", order!!.isPlaced)
    }

    @Test
    fun testTotalAmountCalculation() {
        val pizza = pizzaFactory.menu.first()
        val crust = pizzaFactory.crusts.first()
        val side = pizzaFactory.sides.first()

        val order = pizzaFactory.placeOrder(
            listOf(PizzaOrder(pizza, "Medium", crust, emptyList())),
            listOf(side)
        )

        val expectedTotal = pizza.prices["Medium"]!! + side.price
        assertEquals("Total amount calculated", expectedTotal, order?.totalAmount)
    }
}
