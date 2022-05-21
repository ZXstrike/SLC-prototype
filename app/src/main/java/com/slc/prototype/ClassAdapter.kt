package com.slc.prototype

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ClassAdapter(private var classList : List<ClassData>, private val clickListener: (ClassData) -> Unit):
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(view: View, clickAtPosition: (Int) -> Unit ) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.class_name_card)
        val teacher : TextView = view.findViewById(R.id.teacher_name)
        val image : ImageView = view.findViewById(R.id.class_image)

        init {
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val vh =  ClassViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.class_card_layout,
            parent, false)){
                clickListener(classList[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classData = classList[position]
        holder.name.text = classData.getName()
        holder.teacher.text = classData.getTeacher()
        Glide.with(holder.image.context).load(classData.getImage()).into(holder.image)
    }

    override fun getItemCount() = classList.size
}
