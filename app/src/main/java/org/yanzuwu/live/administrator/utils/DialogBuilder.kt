package org.yanzuwu.live.administrator.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

fun Context.dialog(
    isShowUp:Boolean = true,
    message:String?=null,
    title:String?=null,
    view: View?=null,
    isCancelable:Boolean=true,
    positiveButton:Pair<String,DialogInterface.OnClickListener>? = null

): AlertDialog =
    AlertDialog.Builder(this).run {
        message?.let(::setMessage)
        title?.let(::setTitle)
        view?.let(::setView)
        positiveButton?.let { setPositiveButton(it.first,it.second) }
        setCancelable(isCancelable)
        if (isShowUp) show() else create()
    }