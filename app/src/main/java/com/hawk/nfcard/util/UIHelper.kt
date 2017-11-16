package com.hawk.nfcard.util

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import com.hawk.nfcard.R




/**
 * Created by heyong on 2017/11/16.
 */
class UIHelper {
    companion object {
        private var alertDialog: AlertDialog? = null

        fun showSingleDialog(context : Context, @StringRes msgId : Int, confirmListener : DialogInterface.OnClickListener) {
            val msg: String = context.getString(msgId)
            showSingleDialog(context, msg, confirmListener)
        }

        fun showAlertDialog(context : Context, @StringRes msgId : Int, confirmListener : DialogInterface.OnClickListener) {
            val msg: String = context.getString(msgId)
            showAlertDialog(context, msg, confirmListener)
        }

        fun showSingleDialog(context: Context, msg: String, confirmListener: DialogInterface.OnClickListener) {
            cancelAlertDialog()

            val builder = AlertDialog.Builder(context)
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm, confirmListener)
            alertDialog = builder.create()
            alertDialog?.show()
        }

        fun showAlertDialog(context: Context, msg: String, confirmListener: DialogInterface.OnClickListener) {
            cancelAlertDialog()

            val builder = AlertDialog.Builder(context)
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm, confirmListener)
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            alertDialog = builder.create()
            alertDialog?.show()
        }

        fun cancelAlertDialog() {
            alertDialog?.dismiss()
            alertDialog = null
        }

    }
}