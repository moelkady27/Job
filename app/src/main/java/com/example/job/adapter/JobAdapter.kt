package com.example.job.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.R
import com.example.job.activities.JobDetailsActivity
import com.example.job.model.Job
import com.example.job.model.JobXX
import kotlinx.android.synthetic.main.each_row.view.*
import kotlinx.android.synthetic.main.each_row1.view.*

class JobAdapter(
    private val list: List<JobXX>

) : RecyclerView.Adapter<JobAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_row , parent ,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val x = list[position]

        Glide.with(holder.itemView.context)
            .load(x.imageUrl)
            .into(holder.itemView.imageView)

        holder.itemView.company_job.text = x.company
        holder.itemView.title_job.text = x.title
        holder.itemView.location_job.text = x.location
        holder.itemView.salary_job.text = x.salary
        holder.itemView.imageButton3.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, JobDetailsActivity::class.java)
            intent.putExtra("selectedPosition", position)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

}