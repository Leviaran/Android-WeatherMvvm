package com.robotboy.weathermvvm.helpers

import android.content.Context
import com.robotboy.weathermvvm.R
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


class MessageHelpers {

    companion object {
        private fun showMessageWithListener(context: Context, title: String, message: String?, positive: String, okListener: DialogInterface.OnClickListener?) {
            if (message == null) return
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
                    .setMessage(message)
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setNegativeButton(positive, okListener)
            val alert = builder.create()
            alert.show()
        }

        fun showMessage(context: Context, message: String, okListener: DialogInterface.OnClickListener?) {
            showMessageWithListener(context, context.getString(R.string.app_name), message, context.getString(R.string.ok), okListener)
        }

        fun showExceptionError(context: Context, error: Throwable, okListener: DialogInterface.OnClickListener?) {
            showMessageWithListener(context, context.getString(R.string.app_name), error.message, context.getString(R.string.ok), okListener)
        }
    }
}
