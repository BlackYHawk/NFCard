package com.hawk.nfcard.card

import android.nfc.Tag
import android.nfc.tech.MifareUltralight


/**
 * Created by heyong on 2017/11/18.
 */
@Suppress("UNREACHABLE_CODE")
class MifareUltralightCard(tag : Tag) : INfcCard {
    var mTag = tag

    override fun readText(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val mifareUltralight = MifareUltralight.get(mTag);

        try {
            mifareUltralight.connect()

        } catch (e: Exception) {
        } finally {
            try {
                mifareUltralight.close()
            } catch (e: Exception) {
            }
        }

    }
}