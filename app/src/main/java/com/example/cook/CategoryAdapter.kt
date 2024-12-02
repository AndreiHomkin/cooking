package com.example.cook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(var categories: List<Category>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.categoryImg)
        val title: TextView = view.findViewById(R.id.categoryName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_in_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.title.text = categories[position].name
        holder.image.setImageResource(category.image)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra("catName", category.name)
            context.startActivity(intent)
        }
    }
}