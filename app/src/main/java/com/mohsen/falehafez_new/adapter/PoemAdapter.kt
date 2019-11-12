package com.mohsen.falehafez_new.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.falehafez_new.R
import kotlinx.android.synthetic.main.poem_item.view.*

class PoemAdapter(val context: Context, val items: List<String>) : RecyclerView.Adapter<PoemAdapter.ItemHolder>() {
    class ItemHolder(view: View): RecyclerView.ViewHolder(view) {

        private val firstHistich: TextView = view.firstHemistich
        private val secondHistich: TextView = view.secondHemistich

        fun bind(firstItem:String,secondItem:String){
            firstHistich.text=firstItem
            secondHistich.text=secondItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.poem_item, parent, false)
        return ItemHolder(view)
    }


    override fun getItemCount(): Int {
        return items.size/2
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val firstItem = items[position*2]
        val secondItem = items[position*2+1]
        holder.bind(firstItem,secondItem)

    }

}