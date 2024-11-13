package com.calipso.remiwaza

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomAdapterEmpleados(private var dataList: List<ParametrosEmpleados>) :
    RecyclerView.Adapter<CustomAdapterEmpleados.CustomViewHolder>() {

    // ViewHolder: contiene las vistas para cada item
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemLastName: TextView = itemView.findViewById(R.id.itemLastName)
        val itemState: TextView = itemView.findViewById(R.id.itemState)
    }

    // Infla el diseño de cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_empleado_agencia, parent, false)
        return CustomViewHolder(view)
    }

    // Conecta los datos a las vistas
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = dataList[position]

        // Convertir el estado del remisero (String) a Boolean
        val stateBoolean = currentItem.state == "available"  // True si "available", false si "not available"

        holder.itemName.text = currentItem.name
        holder.itemLastName.text = currentItem.lastName
        holder.itemState.text = currentItem.state

        // Cambiar el color de fondo basado en el estado
        if (stateBoolean) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.stateGreen) // Verde si está disponible
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.stateRed) // Rojo si no está disponible
            )
        }
    }


    // Retorna el tamaño de la lista de datos
    override fun getItemCount() = dataList.size

    // Función para actualizar la lista de datos
    fun updateData(newDataList: List<ParametrosEmpleados>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}
