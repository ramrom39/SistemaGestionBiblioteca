package com.example.sistemagestionbiblioteca.navigation
/**
 * Sealed class que define las rutas de navegación de la aplicación.
 *
 * Cada pantalla es un objeto que hereda de esta clase, proporcionando su ruta
 * y, cuando es necesario, un método auxiliar para generar rutas parametrizadas.
 *
 * @property route Ruta asociada a la pantalla.
 */
sealed class AppScreens(val route:String) {
    /** Pantalla de inicio de sesión. */
    object Login : AppScreens("login")

    /** Pantalla de registro de nuevos usuarios. */
    object Register : AppScreens("register")

    /**
     * Pantalla principal de la app, parametrizada con el ID del usuario.
     *
     * @param userId ID del usuario autenticado.
     */
    object Home : AppScreens("home/{userId}") {
        fun createRoute(userId: Int) = "home/$userId"
    }
    /**
     * Pantalla de historial de acciones del usuario, parametrizada con el ID.
     *
     * @param userId ID del usuario.
     */
    object History : AppScreens("history/{userId}") {
        fun createRoute(userId: Int) = "history/$userId"
    }
    /**
     * Pantalla de estanterías del usuario, parametrizada con el ID.
     *
     * @param userId ID del usuario.
     */
    object Shelves : AppScreens("shelves/{userId}") {
        fun createRoute(userId: Int) = "shelves/$userId"
    }

}