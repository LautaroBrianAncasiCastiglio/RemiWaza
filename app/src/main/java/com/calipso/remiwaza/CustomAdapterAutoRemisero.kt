package com.calipso.remiwaza

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val listCarro: ArrayList<ParametrosAutos>, private val context: Context) :
    RecyclerView.Adapter<MyAdapter.CustomViewHolder>() {

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
        holder.itemModel.text = currentItem.Model
        holder.itemColor.text = currentItem.Color
        holder.itemMarca.text = currentItem.Marca

        // Configura el color de fondo según el estado de "isActive"
        if (currentItem.StateA) {
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
            intent.putExtra("model", currentItem.Model)
            intent.putExtra("marca", currentItem.Marca)
            intent.putExtra("color", currentItem.Color)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listCarro.size
    }
}
