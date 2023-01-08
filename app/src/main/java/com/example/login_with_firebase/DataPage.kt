package com.example.login_with_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_data_page.*

class DataPage : AppCompatActivity() {

    private lateinit var adapter:PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_page)

        // Create a instance of the database and get its reference
        val mbase:DatabaseReference=FirebaseDatabase.getInstance().reference

        personRecycler.layoutManager= LinearLayoutManager(this)

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        val options = FirebaseRecyclerOptions.Builder<person>()
            .setQuery(mbase,person::class.java).build()

        // connecting object of required adapter class to the adapter class itself
        adapter= PersonAdapter(options)
        personRecycler.adapter=adapter
    }

    override fun onStart() {
        // tell the app to start getting data from database on starting of the activity
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        // tell the app to stop getting data from database on stopping of the activity
        super.onStop()
        adapter.stopListening()
    }
}
