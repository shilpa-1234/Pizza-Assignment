package com.task.pizzaassignment

import java.util.Scanner

data class Pizza(
    val name: String,
    val type: String, // Veg or Non Veg
    val prices: Map<String, Int>
)

data class Topping(
    val name: String,
    val price: Int,
    val type: String
)

data class Crust(
    val name: String
)

data class Side(
    val name: String,
    val price: Int
)

data class Order(
    val id: Int,
    val pizzas: List<PizzaOrder>,
    val sides: List<Side>,
    val totalAmount: Int,
    val isPlaced: Boolean = false
)

data class PizzaOrder(
    val pizza: Pizza,
    val size: String,
    val crust: Crust,
    val toppings: List<Topping>
)

class PizzaFactory {
    val menu = mutableListOf<Pizza>()
    val toppings = mutableListOf<Topping>()
    val crusts = mutableListOf<Crust>()
    val sides = mutableListOf<Side>()
    private val orders = mutableListOf<Order>()
    private var orderCounter = 1

    init {
        initializeMenu()
    }

    private fun initializeMenu() {
        menu.add(Pizza("Deluxe Veggie", "Veg", mapOf("Regular" to 150, "Medium" to 200, "Large" to 325)))
        menu.add(Pizza("Cheese and Corn", "Veg", mapOf("Regular" to 175, "Medium" to 375, "Large" to 475)))
        menu.add(Pizza("Paneer Tikka", "Veg", mapOf("Regular" to 160, "Medium" to 290, "Large" to 340)))
        menu.add(Pizza("Non-Veg Supreme", "Non-Veg", mapOf("Regular" to 190, "Medium" to 325, "Large" to 425)))
        menu.add(Pizza("Chicken Tikka", "Non-Veg", mapOf("Regular" to 210, "Medium" to 370, "Large" to 500)))
        menu.add(Pizza("Pepper Barbecue Chicken", "Non-Veg", mapOf("Regular" to 220, "Medium" to 380, "Large" to 525)))

        toppings.add(Topping("Black Olive", 20, "Veg"))
        toppings.add(Topping("Capsicum", 25, "Veg"))
        toppings.add(Topping("Paneer", 35, "Veg"))
        toppings.add(Topping("Mushroom", 30, "Veg"))
        toppings.add(Topping("Fresh Tomato", 10, "Veg"))
        toppings.add(Topping("Chicken Tikka", 35, "Non-Veg"))
        toppings.add(Topping("Barbecue Chicken", 45, "Non-Veg"))
        toppings.add(Topping("Grilled Chicken", 40, "Non-Veg"))

        crusts.add(Crust("New Hand Tossed"))
        crusts.add(Crust("Wheat Thin Crust"))
        crusts.add(Crust("Cheese Burst"))
        crusts.add(Crust("Fresh Pan Pizza"))

        sides.add(Side("Cold Drink", 55))
        sides.add(Side("Mousse Cake", 90))
    }

    fun placeOrder(pizzas: List<PizzaOrder>, sides: List<Side>): Order? {
        if (!validateOrderSelected(pizzas)) return null
        val total = calculateTotalAmount(pizzas, sides)
        val order = Order(orderCounter++, pizzas, sides, total, true)
        orders.add(order)
        return order
    }

    private fun validateOrderSelected(pizzas: List<PizzaOrder>): Boolean {
        pizzas.forEach { order ->
            if (order.pizza.type == "Veg" && order.toppings.any { it.type == "Non-Veg" }) return false
            if (order.pizza.type == "Non-Veg" && order.toppings.any { it.name == "Paneer" }) return false
            if (order.toppings.filter { it.type == "Non-Veg" }.size > 1) return false
        }
        return true
    }

    private fun calculateTotalAmount(pizzas: List<PizzaOrder>, sides: List<Side>): Int {
        var total = 0
        pizzas.forEach { order ->
            total += order.pizza.prices[order.size] ?: 0
            if (order.size != "Large") {
                total += order.toppings.sumOf { it.price }
            }
        }
        total += sides.sumOf { it.price }
        return total
    }
}
fun main() {
    val pizzaFactory = PizzaFactory()
    val scanner = Scanner(System.`in`)

    println("Select a pizza: ")
    pizzaFactory.menu.forEachIndexed { index, pizza -> println("$index: ${pizza.name}") }
    if(!scanner.hasNextInt()){
        println("Oops, something went wrong!")
        return
    }
    val pizzaIndex = scanner.nextInt()
    scanner.nextLine()
    if (pizzaIndex !in pizzaFactory.menu.indices) {
        println("Wrong Menu selected.")
        return
    }
    val pizza = pizzaFactory.menu[pizzaIndex]

    println("Select size (Regular, Medium, Large): ")
    val size = scanner.nextLine().trim()

    println("Select a crust: ")
    pizzaFactory.crusts.forEachIndexed { index, crust -> println("$index: ${crust.name}") }
    val crustIndex = scanner.nextInt()
    scanner.nextLine()
    if (crustIndex !in pizzaFactory.crusts.indices) {
        println("Wrong crust selected.")
        return
    }
    val crust = pizzaFactory.crusts[crustIndex]

    println("Select toppings (comma separated indices, or -1 to skip): ")
    pizzaFactory.toppings.forEachIndexed { index, topping -> println("$index: ${topping.name}") }
    val toppingInput = scanner.nextLine().trim()
    val selectedToppings = if (toppingInput == "-1") {
        emptyList()
    } else {
        toppingInput.split(",").mapNotNull { it.trim().toIntOrNull()?.let { i -> pizzaFactory.toppings.getOrNull(i) } }
    }

    println("Select sides (comma separated indices, or -1 to skip): ")
    pizzaFactory.sides.forEachIndexed { index, side -> println("$index: ${side.name}") }
    val sideInput = scanner.nextLine().trim()
    val selectedSides = if (sideInput == "-1") {
        emptyList()
    } else {
        sideInput.split(",").mapNotNull { it.trim().toIntOrNull()?.let { i -> pizzaFactory.sides.getOrNull(i) } }
    }

    val order = pizzaFactory.placeOrder(
        listOf(PizzaOrder(pizza, size, crust, selectedToppings)),
        selectedSides
    )

    println("Order placed: ${order ?: "Oops, something went wrong!"}")
}

