package com.example.bibliotecaroom.domain.model

sealed class Pantalla(val route: String) {
    object Main : Pantalla("main")
    object GetAll : Pantalla("get_all")
    object Insert : Pantalla("insert")
    object Update : Pantalla("update")
    object GetById : Pantalla("get_by_id")
}