package org.yanzuwu.live.administrator.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog

/**
 * Dialog 构建者 KTX
 *
 * 所有参数都是可选择参数，根据参数判断Builder的内容。
 * @param isShowUp 是否在创建时启动
 * @param message 设置Dialog内的消息
 * @param title 标题 不用说了
 * @param view 设置内部的view
 * @param isCancelable 是否可取消
 * @param positiveButton [Pair<String,DialogInterface.OnClickListener>] <br /> 设置积极按钮 分别为 标题和OnClickListener
 * @return [AlertDialog]
 */
fun Context.dialog(
    isShowUp:Boolean = true,
    message:String?=null,
    title:String?=null,
    view: View?=null,
    isCancelable:Boolean=true,
    positiveButton:Pair<String,DialogInterface.OnClickListener>? = null
): AlertDialog = AlertDialog.Builder(this)
    .run {
        message?.let(::setMessage)
        title?.let(::setTitle)
        view?.let(::setView)
        positiveButton?.let { setPositiveButton(it.first,it.second) }
        setCancelable(isCancelable)
        if (isShowUp) show() else create()
    }