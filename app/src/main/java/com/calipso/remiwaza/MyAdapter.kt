package com.calipso.remiwaza

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.calipso.remiwaza.databinding.ListviewCarroRemiseroBinding

class MyAdapter(private val context: Context, private val listCarro: java.util.ArrayList<ListaAutoRemisero>):
    BaseAdapter(){

    override fun getCount(): Int {

        return listCarro.size
    }

    override fun getItem(position: Int): Any {

        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var binding = ListviewCarroRemiseroBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        var convertView = convertView
        convertView = binding.root

        binding.textModel.text = listCarro[position].Model
        binding.textColor.text = listCarro[position].Color
        binding.textName.text = listCarro[position].Name

        return convertView
    }

}