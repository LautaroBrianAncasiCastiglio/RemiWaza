package com.calipso.remiwaza

data class ParametrosEmpleados(
    val id: String = "", // Si tienes un ID explícito
    val name: String = "",
    val lastName: String = "",
    val state: String = "",
    val modelo: String = "", // Si necesitas este campo
    val marca: String = "", // Si necesitas este campo
) {
    // Constructor sin argumentos que Firebase necesita ya está implícito al usar valores predeterminados
}
