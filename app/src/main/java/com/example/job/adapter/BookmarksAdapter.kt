package com.example.job.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.Bookmark
import com.example.job.model.DeleteBookmarkResponse
import com.example.job.model.GetAllBookmarksResponse
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.each_row_bookmaerk.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarksAdapter (
    private val bookmarks: List<Bookmark>,

): RecyclerView.Adapter<BookmarksAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.each_row_bookmaerk,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val bookmark = bookmarks[position]
        if (bookmark != null && bookmark.job != null) {
            Glide.with(holder.itemView.context)
                .load(bookmark.job.imageUrl)
                .into(holder.itemView.imageView_book)

            holder.itemView.title_book.text = bookmark.job.title
            holder.itemView.company_book.text = bookmark.job.company
            holder.itemView.location_book.text = bookmark.job.location
            holder.itemView.salary_book.text = bookmark.job.salary

            holder.itemView.imageButton_book.setOnClickListener {

                val bookmarkId = bookmark._id

                val token = AppReference.getToken(holder.itemView.context)

                RetrofitClient.instance.deleteBookmark("Bearer $token", bookmarkId)
                    .enqueue(object : Callback<DeleteBookmarkResponse>{
                        override fun onResponse(
                            call: Call<DeleteBookmarkResponse>,
                            response: Response<DeleteBookmarkResponse>
                        ) {
                            if (response.isSuccessful) {

                                val deleteResponse = response.body()
                                val message = deleteResponse?.message

//                            to remove item from recycleview

                                removeItem(position)

                                Toast.makeText(
                                    holder.itemView.context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DeleteBookmarkResponse>, t: Throwable) {
                            Toast.makeText(holder.itemView.context, "Network error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
        else{
            Toast.makeText(
                holder.itemView.context,
                "message",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    private fun removeItem(position: Int) {
        (bookmarks as MutableList).removeAt(position)
        notifyItemRemoved(position)
    }
}
