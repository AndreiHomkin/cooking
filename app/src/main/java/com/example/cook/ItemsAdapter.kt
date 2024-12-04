@file:Suppress("DEPRECATION")

package com.example.cook

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemsAdapter(private var items: List<Item>): RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val title: TextView = view.findViewById(R.id.item_list_title)
        val desc: TextView = view.findViewById(R.id.item_list_desk)
        val button: Button = view.findViewById(R.id.item_list_button)
        val context = view.context!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = items[position].name
        holder.desc.text = items[position].subcategory
        if (!item.image.isNullOrEmpty()) {
            val imageUri = Uri.parse(item.image)
            Glide.with(holder.context)
                .load(imageUri)
                .thumbnail(0.05f)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.dinner)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.dinner)
        }
        holder.button.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ViewItemActivity::class.java)
            intent.putExtra("itemTitle", items[position].name)

            context.startActivity(intent)
        }
    }
}