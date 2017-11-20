package com.hawk.nfcard.card

import android.nfc.Tag
import android.nfc.tech.IsoDep

/**
 * Created by heyong on 2017/11/18.
 */
@Suppress("UNREACHABLE_CODE")
class IsoDepCard(tag : Tag) : INfcCard {
    var mTag = tag

    override fun readText(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val isoDep = IsoDep.get(mTag);

        try {
            isoDep.connect()

        } catch (e: Exception) {
        } finally {
            try {
                isoDep.close()
            } catch (e: Exception) {
            }

        }

    }
}