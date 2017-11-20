package com.hawk.nfcard.card

import android.nfc.Tag
import android.nfc.tech.NfcA

/**
 * Created by heyong on 2017/11/18.
 */
@Suppress("UNREACHABLE_CODE")
class NfcACard(tag : Tag) : INfcCard {
    var mTag = tag

    override fun readText(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val nfcA = NfcA.get(mTag);

        try {
            nfcA.connect()

        } catch (e: Exception) {
        } finally {
            try {
                nfcA.close()
            } catch (e: Exception) {
            }
        }

    }
}