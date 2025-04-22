package com.example.sistemagestionbiblioteca.navigation

sealed class AppScreens(val route:String) {

    object Login : AppScreens("login")

    object Register : AppScreens("register")

    object Home   : AppScreens("home/{userId}") {
        fun createRoute(userId: Int) = "home/$userId"
    }

    object History : AppScreens("history/{userId}") {
        fun createRoute(userId: Int) = "history/$userId"
    }
    object Shelves : AppScreens("shelves/{userId}") {
        fun createRoute(userId: Int) = "shelves/$userId"
    }

}