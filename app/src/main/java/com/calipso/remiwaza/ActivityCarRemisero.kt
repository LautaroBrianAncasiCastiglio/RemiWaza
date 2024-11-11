package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ActivityCarRemisero : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: MyAdapter
    private lateinit var dataList: ArrayList<ParametrosAutos>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_remisero)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userButton: LinearLayout = findViewById(R.id.btnCount)
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilRemisero::class.java)
            startActivity(intent)
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnAgencia)
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioRemisero::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.listaDerecha)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = ArrayList()
        database = FirebaseDatabase.getInstance().getReference("autos")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val auto = dataSnapshot.getValue(ParametrosAutos::class.java)
                    if (auto != null) {
                        dataList.add(auto)
                    }
                }
                customAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores
            }
        })

        customAdapter = MyAdapter(dataList, this)
        recyclerView.adapter = customAdapter
    }
}
