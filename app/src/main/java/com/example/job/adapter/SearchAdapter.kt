package com.example.job.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.R
import com.example.job.activities.JobDetailsActivity
import com.example.job.model.SearchResult
import kotlinx.android.synthetic.main.each_row.view.*
import kotlinx.android.synthetic.main.each_row1.view.*

class SearchAdapter(
    private var searchResults: List<SearchResult>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_row1, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchResult = searchResults[position]

        Glide.with(holder.itemView.context)
            .load(searchResult.imageUrl)
            .into(holder.itemView.imageView1)

        holder.itemView.company_job1.text = searchResult.company
        holder.itemView.title_job1.text = searchResult.title
        holder.itemView.location_job1.text = searchResult.location
        holder.itemView.salary_job1.text = searchResult.salary

        holder.itemView.imageButton2.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, JobDetailsActivity::class.java)
            intent.putExtra("selectedPosition", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}