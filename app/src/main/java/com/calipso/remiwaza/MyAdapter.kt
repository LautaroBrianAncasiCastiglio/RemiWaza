package com.calipso.remiwaza

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val listCarro: java.util.ArrayList<ListaAutos>):
    RecyclerView.Adapter<MyAdapter.CustomViewHolder>() {

    // ViewHolder: contiene las vistas para cada item
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemLastName: TextView = itemView.findViewById(R.id.itemLastName)
        val itemState: TextView = itemView.findViewById(R.id.itemState)
    }

    // Infla el diseño de cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_carro_remisero, parent, false)
        return CustomViewHolder(view)
    }

    // Conecta los datos a las vistas
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = listCarro[position]
        holder.itemName.text = currentItem.Model
        holder.itemLastName.text = currentItem.Color
        holder.itemState.text = currentItem.Name

        // Cambia el color del fondo según el estado de "isActive"
        if (currentItem.StateA) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.stateGreen
                )
            )  // Verde
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.stateRed
                )
            )  // Rojo
        }
    }

    // Retorna el tamaño de la lista de datos
    override fun getItemCount() = listCarro.size
}
