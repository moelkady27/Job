package com.example.job

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object AppReference {

    fun setLoginState(context: Activity?, state: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("login", state)
        editor.apply()
    }

    fun getLoginState(context: Activity?): Boolean {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("login", false)
    }

    fun setToken(context: Activity?, state: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("token", state)
        editor.apply()
    }

    fun getToken(context: Context?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "")!!
    }

    fun setUserId(context: Activity?, userId: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    fun getUserId(context: Activity?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "")!!
    }

    fun setIsAdmin(context: Activity?, isAdmin: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isAdmin", isAdmin)
        editor.apply()
    }

    fun getIsAdmin(context: Activity?): Boolean {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isAdmin", false)
    }

    fun setImageUrl(context: Activity?, imageUrl: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getImageUrl(context: Activity?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")!!
    }

    fun setImageJob(context: Activity?, imageUrl: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("imageJob", imageUrl)
        editor.apply()
    }

    fun getImageJob(context: Activity?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("imageJob", "")!!
    }
}