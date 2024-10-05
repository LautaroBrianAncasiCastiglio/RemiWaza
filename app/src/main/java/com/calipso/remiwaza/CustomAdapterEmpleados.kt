package com.calipso.remiwaza

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
class CustomAdapterEmpleados(private val dataList: List<ListEmpleados>) :
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
        holder.itemName.text = currentItem.Name
        holder.itemLastName.text = currentItem.LastName
        holder.itemState.text = currentItem.State

        // Cambia el color del fondo según el estado de "isActive"
        if (currentItem.StateC) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.stateGreen))  // Verde
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.stateRed))  // Rojo
        }
    }

    // Retorna el tamaño de la lista de datos
    override fun getItemCount() = dataList.size
}