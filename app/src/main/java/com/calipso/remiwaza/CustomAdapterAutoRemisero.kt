package com.calipso.remiwaza

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterAutoRemisero(private val context: Context,private val autosList: List<ParametrosAutos>) :
    RecyclerView.Adapter<CustomAdapterAutoRemisero.CustomViewHolder>() {

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemModel: TextView = itemView.findViewById(R.id.itemModel)
        val itemMarca: TextView = itemView.findViewById(R.id.itemMarca)
        val itemColor: TextView = itemView.findViewById(R.id.itemColor)
        val itemLayout: View = itemView // El contenedor completo del ítem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_carro_remisero, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = autosList[position]
        holder.itemModel.text = currentItem.modelo
        holder.itemColor.text = currentItem.color
        holder.itemMarca.text = currentItem.marca

        // Configura el color de fondo según el estado de "isActive"
        when (currentItem.state.lowercase()) {
            "not available" -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.stateRed))
            "available" -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.stateGreen))
            else -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparentBlack))
        }

        // Agrega el OnClickListener para iniciar la nueva actividad
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityStateRemisero::class.java)
            intent.putExtra("model", currentItem.modelo)
            intent.putExtra("marca", currentItem.marca)
            intent.putExtra("color", currentItem.color)
            intent.putExtra("carId", currentItem.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return autosList.size
    }
}
