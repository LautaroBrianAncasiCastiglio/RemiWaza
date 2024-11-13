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

    // ViewHolder que contiene las vistas del ítem
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemLastName: TextView = itemView.findViewById(R.id.itemLastName)
        val itemState: TextView = itemView.findViewById(R.id.itemState)
    }

    // Se infla el diseño del ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listview_empleado_agencia, parent, false)
        return CustomViewHolder(view)
    }

    // Se asignan los datos a cada ítem
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val empleado = dataList[position]

        // Asigna los valores del objeto ParametrosEmpleados a los TextView correspondientes
        holder.itemName.text = empleado.name
        holder.itemLastName.text = empleado.lastName
        holder.itemState.text = empleado.state
    }

    // Retorna el número de elementos en la lista
    override fun getItemCount(): Int {
        return dataList.size
    }
}
