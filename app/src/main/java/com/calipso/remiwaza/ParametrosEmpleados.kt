package com.calipso.remiwaza

data class ParametrosEmpleados(
    val id: String = "", // Si tienes un ID explícito
    val name: String = "",
    val lastName: String = "",
    val state: String = "",
    val email: String = "", // Si necesitas este campo
    val type: String = "", // Si necesitas este campo
    val password: String = "" // Si necesitas este campo
) {
    // Constructor sin argumentos que Firebase necesita ya está implícito al usar valores predeterminados
}
