package com.slc.prototype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

internal class ClassAdapter(private var classList : List<ClassData>):
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    internal inner class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.class_name_card)
        val teacher : TextView = view.findViewById(R.id.teacher_name)
        val image : ImageView = view.findViewById(R.id.class_image)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.class_card_layout, parent, false)
        return ClassViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classData = classList[position]
        holder.name.text = classData.getName()
        holder.teacher.text = classData.getTeacher()
        Glide.with(holder.image.context).load(classData.getImage()).into(holder.image)
    }

    override fun getItemCount(): Int {
        return classList.size
    }
}
