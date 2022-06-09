package com.slc.prototype

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_task_post.*


class TaskPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_post)
        task_post_desc.movementMethod = ScrollingMovementMethod()
    }

    fun backToHome(view: View) {
        onBackPressed()
    }
}