package com.calipso.remiwaza

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterAutoRemisero(private val listCarro: ArrayList<ParametrosAutos>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapterAutoRemisero.CustomViewHolder>() {

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemModel: TextView = itemView.findViewById(R.id.itemModel)
        val itemMarca: TextView = itemView.findViewById(R.id.itemMarca)
        val itemColor: TextView = itemView.findViewById(R.id.itemColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_carro_remisero, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = listCarro[position]
        holder.itemModel.text = currentItem.model
        holder.itemColor.text = currentItem.color
        holder.itemMarca.text = currentItem.marca

        // Configura el color de fondo según el estado de "isActive"
        if (currentItem.state) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.stateGreen)
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.stateRed)
            )
        }

        // Agrega el OnClickListener para iniciar la nueva actividad
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityStateRemisero::class.java)
            intent.putExtra("model", currentItem.model)
            intent.putExtra("marca", currentItem.marca)
            intent.putExtra("color", currentItem.color)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listCarro.size
    }
}
