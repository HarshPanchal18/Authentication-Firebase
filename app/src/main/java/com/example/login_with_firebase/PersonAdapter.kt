package com.example.login_with_firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PersonAdapter(options: FirebaseRecyclerOptions<person>)
    : FirebaseRecyclerAdapter<person, PersonAdapter.personViewHolder>(options) {

    class personViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var firstname: TextView
        var lastname: TextView
        var age: TextView

        init {
            firstname = itemView.findViewById(R.id.firstname)
            lastname = itemView.findViewById(R.id.lastname)
            age = itemView.findViewById(R.id.age)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): personViewHolder {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.person,parent,false)
        return personViewHolder(v)
    }

    override fun onBindViewHolder(holder: personViewHolder, position: Int, model: person) {
        holder.firstname.text=model.firstname
        holder.lastname.text=model.lastname
        holder.age.text= model.age.toString()
    }
}
