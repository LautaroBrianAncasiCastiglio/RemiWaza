package com.calipso.remiwaza

data class ParametrosEmpleados(
    val id: String = "",
    val name: String = "",
    val lastName: String = "",
    val state: String = "",
    val modelo: String = "",
    val marca: String = ""
)
 {
    // Constructor sin argumentos que Firebase necesita ya está implícito al usar valores predeterminados
}
