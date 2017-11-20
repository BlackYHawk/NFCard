package com.hawk.nfcard.card

import android.nfc.Tag
import android.nfc.tech.MifareClassic

/**
 * Created by heyong on 2017/11/18.
 */
@Suppress("UNREACHABLE_CODE")
class MifareClassicCard(tag: Tag) : INfcCard {
    var mTag = tag

    override fun readText(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val mifareClassic = MifareClassic.get(mTag);

        try {
            mifareClassic.connect()

        } catch (e: Exception) {
        } finally {
            try {
                mifareClassic.close()
            } catch (e: Exception) {
            }
        }

    }
}