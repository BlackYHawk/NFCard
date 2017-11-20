package com.hawk.nfcard

import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.hawk.nfcard.card.*
import com.hawk.nfcard.util.UIHelper


class MainActivity : AppCompatActivity() {
    private var mNFCAdapter : NfcAdapter? = null;
    private var mPendingIntent : PendingIntent? = null;
    private var iNfcCard : INfcCard? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkNFCFunction();
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent?.action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent?.action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent?.action)) {
            processIntent(intent!!)
        }
    }

    private fun checkNFCFunction() {
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
        else if (!mNFCAdapter?.isEnabled!!) {
            val confirmListener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss();
                    val setnfc = Intent(Settings.ACTION_NFC_SETTINGS)
                    startActivity(setnfc)
                }
            }
            UIHelper.showSingleDialog(this, R.string.nfc_enable, confirmListener)
        }
        else {
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        }
    }

    private fun processIntent(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.action)) {
            iNfcCard = NdefCard(intent);
        }
        else {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val techList = tag.techList;

            for (tech in techList) {
                if (tech.equals(NfcA::class.java.name)) {
                    iNfcCard = NfcACard(tag);
                } else if (tech.equals(MifareClassic::class.java.name)) {
                    iNfcCard = MifareClassicCard(tag);
                } else if (tech.equals(MifareUltralight::class.java.name)) {
                    iNfcCard = MifareUltralightCard(tag);
                } else if (tech.equals(IsoDep::class.java.name)) {
                    iNfcCard = IsoDepCard(tag);
                }

                if (iNfcCard != null) break
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mPendingIntent != null) mNFCAdapter?.enableForegroundDispatch(this, mPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        mNFCAdapter?.disableForegroundDispatch(this)
    }
}
