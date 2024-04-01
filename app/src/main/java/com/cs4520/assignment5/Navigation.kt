package com.cs4520.assignment5

enum class Screen {
    LOGIN,
    PRODUCTS
}

sealed class NavigationItem(val route: String) {
    object Login: NavigationItem(Screen.LOGIN.name)
    object Products: NavigationItem(Screen.PRODUCTS.name)
}