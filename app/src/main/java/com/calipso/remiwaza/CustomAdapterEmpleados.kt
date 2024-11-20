package com.calipso.remiwaza

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomAdapterEmpleados(private val context: Context, private val dataList: List<ParametrosEmpleados>) :
    RecyclerView.Adapter<CustomAdapterEmpleados.CustomViewHolder>() {

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemCar: TextView = itemView.findViewById(R.id.itemCar)
        val itemColorCar: TextView = itemView.findViewById(R.id.itemColorCar)
        val itemLayout: View = itemView // El contenedor completo del ítem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listview_empleado_agencia, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val empleado = dataList[position]

        // Asigna los valores del objeto ParametrosEmpleados a los TextView correspondientes
        holder.itemName.text = "${empleado.name} ${empleado.lastName}"
        holder.itemName.text = "${empleado.name} ${empleado.lastName}"

        // Cambia el color de fondo según el estado
        when (empleado.state.lowercase()) {
            "available" -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.stateRed))
            "not available" -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.stateGreen))
            else -> holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparentBlack))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

