package com.mohsen.falehafez_new.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.util.ItemClickListener
import kotlinx.android.synthetic.main.poem_list_item.view.*

class AllPoemAdapter (val context: Context, val items: List<String>, val itemClickListener: ItemClickListener): RecyclerView.Adapter<AllPoemAdapter.ListHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.poem_list_item, parent, false)

        return ListHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(position,items[position],itemClickListener)
    }
    class ListHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title : TextView = view.poemNumberTextView
        private val poemFirstHemistich: TextView = view.poemFirstHemistich

        fun bind(firstItem:Int,secondItem:String, clickListener: ItemClickListener){
            title.text= "غزل شماره ${firstItem+1}"
            poemFirstHemistich.text=secondItem
            clickListener.onItemClicked(firstItem)

        }
    }

}