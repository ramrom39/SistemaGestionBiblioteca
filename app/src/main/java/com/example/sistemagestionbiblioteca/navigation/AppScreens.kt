package com.example.sistemagestionbiblioteca.navigation

sealed class AppScreens(val route:String) {

    object Login : AppScreens("login")


    object Home : AppScreens("home")


    object Register : AppScreens("register")


    object History : AppScreens("History")


    object Shelves : AppScreens("shelves")


}