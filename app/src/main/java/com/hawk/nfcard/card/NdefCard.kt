package com.hawk.nfcard.card

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Parcelable
import java.nio.charset.Charset
import java.util.*
import kotlin.experimental.and


/**
 * Created by heyong on 2017/11/18.
 */
@Suppress("UNREACHABLE_CODE")
class NdefCard(intent: Intent) : INfcCard {
    var mIntent: Intent = intent

    override fun readText(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val tag: Tag = mIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val rawMsgs: Array<Parcelable>? = mIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        val msgs: Array<NdefMessage?>
        var contentSize = 0

        if (rawMsgs != null) {
            msgs = arrayOfNulls<NdefMessage>(rawMsgs.size)
            for (i in 0 until rawMsgs.size) {
                msgs[i] = rawMsgs[i] as NdefMessage
                contentSize += msgs[i]?.toByteArray()!!.size
            }
        }
        try {
            val record = msgs[0]?.getRecords()?.get(0)
            val textRecord = parseTextRecord(record)
            return "$textRecord\n\ntext\n$contentSize bytes"
        } catch (e: Exception) {
        }

    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    fun parseTextRecord(ndefRecord: NdefRecord?): String? {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord == null || ndefRecord.tnf != NdefRecord.TNF_WELL_KNOWN) {
            return null
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.type, NdefRecord.RTD_TEXT)) {
            return null
        }
        try {
            //获得字节数组，然后进行分析
            val payload = ndefRecord.payload
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            val textEncoding = if ((payload[0] and 0x80.toByte()).toInt() == 0) "UTF-8" else "UTF-16"
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            val languageCodeLength = (payload[0] and 0x3f).toInt()
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            val languageCode = String(payload, 1, languageCodeLength, Charset.forName("US-ASCII"))
            //下面开始NDEF文本数据后面的字节，解析出文本
            return String(payload, languageCodeLength + 1,
                    payload.size - languageCodeLength - 1, Charset.forName(textEncoding))
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }

    }

}