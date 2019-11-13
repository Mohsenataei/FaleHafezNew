package com.mohsen.falehafez_new.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.model.FavedPoem
import com.mohsen.falehafez_new.util.ItemClickListener
import kotlinx.android.synthetic.main.poem_list_item.view.*

class FavedPoemAdapter (val context: Context, val items: List<FavedPoem>, private val itemClickListener: ItemClickListener): RecyclerView.Adapter<FavedPoemAdapter.FavedHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavedHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.poem_list_item, parent, false)

        return FavedHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FavedHolder, position: Int) {
        holder.bind(items[position].index, items[position].title,itemClickListener)
    }

    class FavedHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title : TextView = view.poemNumberTextView
        private val poemFirstHemistich: TextView = view.poemFirstHemistich

        fun bind(firstItem:String,secondItem:String, clickListener: ItemClickListener){
            val index = firstItem.toInt().plus(1)
            title.text= "غزل شماره $index"
            poemFirstHemistich.text=secondItem
            itemView.setOnClickListener {
                clickListener.onItemClicked(firstItem.toInt())
            }

        }
    }

}