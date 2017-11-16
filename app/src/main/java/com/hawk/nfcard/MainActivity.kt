package com.hawk.nfcard

import android.content.DialogInterface
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.hawk.nfcard.util.UIHelper


class MainActivity : AppCompatActivity() {
    private var mNFCAdapter : NfcAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkNFC();
    }

    fun checkNFC() {
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFCAdapter == null) {
            val confirmListener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss();
                    finish();
                }
            }
            UIHelper.showSingleDialog(this, R.string.nfc_support, confirmListener)
        }
        else if (mNFCAdapter?.isEnabled!!) {
            val confirmListener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss();
                    val setnfc = Intent(Settings.ACTION_NFC_SETTINGS)
                    startActivity(setnfc)
                }
            }
            UIHelper.showSingleDialog(this, R.string.nfc_enable, confirmListener)
        }

    }
}
