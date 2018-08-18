package com.yhongm.subway.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.*
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.yhongm.subway.R
import kotlinx.android.synthetic.main.wait_dialog.*

class WaitDialog constructor(context: Context, title: String) : Dialog(context, R.style.WaitDialog) {
    var tvTitle: String? = title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wait_dialog)

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val animator = ObjectAnimator.ofFloat(wait_dialog_iv, "rotation", 0f, 90f, 180f, 270f, 360f)

        wait_dialog_tv_title.text = tvTitle
        animator.repeatCount = INFINITE
        animator.repeatMode = RESTART
        animator.start()
    }


}