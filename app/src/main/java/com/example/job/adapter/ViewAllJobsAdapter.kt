package com.example.job.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.activities.JobDetailsActivity
import com.example.job.R
import com.example.job.activities.CreateJobActivity
import com.example.job.activities.UpdateJobActivity
import com.example.job.model.DeleteJobResponse
import com.example.job.model.Job
import com.example.job.model.JobXX
import com.example.job.request.DeleteJobRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.each_row1.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllJobsAdapter(
    private val list: List<JobXX>
): RecyclerView.Adapter<ViewAllJobsAdapter.MyViewHolder>() {


    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_row1 , parent ,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val x = list[position]

        Glide.with(holder.itemView.context)
            .load(x.imageUrl)
            .into(holder.itemView.imageView1)

        holder.itemView.company_job1.text = x.company
        holder.itemView.title_job1.text = x.title
        holder.itemView.location_job1.text = x.location
        holder.itemView.salary_job1.text = x.salary

        holder.itemView.imageButton2.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, JobDetailsActivity::class.java)
            context.startActivity(intent)
        }

        val isAdmin = AppReference.getIsAdmin(holder.itemView.context as Activity?)

        if (isAdmin){
            holder.itemView.imageViewEdit.visibility = View.VISIBLE
            holder.itemView.imageViewDelete.visibility = View.VISIBLE
            holder.itemView.imageViewEdit.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, UpdateJobActivity::class.java)
                intent.putExtra("id", x._id)
                intent.putExtra("title", x.title)
                intent.putExtra("location", x.location)
                intent.putExtra("company", x.company)
                intent.putExtra("description", x.description)
                intent.putExtra("salary", x.salary)
                intent.putExtra("workHours", x.workHours)
                intent.putExtra("contractPeriod", x.contractPeriod)
                intent.putExtra("imageUrl", x.imageUrl)
                intent.putStringArrayListExtra("requirements", ArrayList(x.requirements))

                context.startActivity(intent)
            }
            holder.itemView.imageViewDelete.setOnClickListener {

                val token = AppReference.getToken(holder.itemView.context)
                val jobId = x._id

                RetrofitClient.instance.deleteJob("Bearer $token", jobId)
                    .enqueue(object : Callback<DeleteJobResponse>{
                        override fun onResponse(
                            call: Call<DeleteJobResponse>,
                            response: Response<DeleteJobResponse>
                        ) {
                            if (response.isSuccessful) {

                                val deleteResponse = response.body()
                                val message = deleteResponse?.message

                                Toast.makeText(
                                    holder.itemView.context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()

                                removeItem(position)
//                                list.toMutableList().removeAt(position)
                            } else {
                                val error = response.errorBody()?.string()
                                Toast.makeText(
                                    holder.itemView.context,
                                    "$error",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e("error is ", "$error")
                            }
                        }

                        override fun onFailure(call: Call<DeleteJobResponse>, t: Throwable) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else{
            holder.itemView.imageViewEdit.visibility = View.GONE
            holder.itemView.imageViewDelete.visibility = View.GONE
        }
    }

    private fun removeItem(position: Int) {
        (list as MutableList).removeAt(position)
        notifyItemRemoved(position)
    }
}