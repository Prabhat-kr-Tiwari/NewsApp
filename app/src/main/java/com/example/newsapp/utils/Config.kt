package com.example.newsapp.utils

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import com.example.newsapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {

    private var dialog:AlertDialog?=null
    fun showDialog(context: Context,message:String){
        val view=LayoutInflater.from(context).inflate(R.layout.loading_layout,null)
        val progressBar=view.findViewById<ProgressBar>(R.id.progressBar)
        val textView=view.findViewById<TextView>(R.id.textView)
        textView.text=message
        dialog=MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()
        dialog!!.show()
    }
    fun hideDialog(){
        dialog?.dismiss()
    }
}