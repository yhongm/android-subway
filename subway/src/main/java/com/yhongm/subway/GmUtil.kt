/**
 *                   Copyright [yhongm]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at

 *         http://www.apache.org/licenses/LICENSE-2.0

 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

package com.yhongm.subway

import android.util.Log
import com.yhongm.gm.GmUtil
import java.io.File

class Gm4ContentUtil {
    init {
        val key = byteArrayOf(0x31.toByte(), 0x39.toByte(), 0x39.toByte(), 0x30.toByte(), 0x31.toByte(), 0x31.toByte(), 0x31.toByte(), 0x36.toByte(), 0x31.toByte(), 0x39.toByte(), 0x39.toByte(), 0x30.toByte(), 0x31.toByte(), 0x31.toByte(), 0x31.toByte(), 0x36.toByte())
        val gmUtil = GmUtil.getGmUtil()
    }

    private fun decContent(gmUtil: GmUtil, key: ByteArray, content: ByteArray): String {
        try {
            var bytes = content
            var newBytes: ByteArray = ByteArray(0)
            var tempIndex = 0
            do {
                val range = bytes.copyOfRange(tempIndex, tempIndex + 16)
                val decSm4 = gmUtil.decSm4(key, range, GmUtil.CalcMode.Sm4Cpp)
                tempIndex += 16
                newBytes += decSm4

            } while (tempIndex <= bytes.size)
            if (tempIndex > bytes.size) {
                tempIndex -= 16
            }
            val rem = bytes.size % 16
            if (rem != 0) {
                val copyOfRange11 = bytes.copyOfRange(tempIndex, tempIndex + rem)
                newBytes += gmUtil.decSm4(key, copyOfRange11, GmUtil.CalcMode.Sm4Cpp)
            }
            var str = String(newBytes)
            return str
        } catch (e: Exception) {
            Log.i("TestActivity", "17:39,onCreate e:: $e")// 2018/5/21,yhongm
        }
        return ""
    }

    private fun encContent(gmUtil: GmUtil, key: ByteArray, content: ByteArray, outFile: File) {
        try {
            val bytes = content
            val size = bytes.size
            var tempIndex = 0
            val outFile = outFile

            outFile.createNewFile()
            var outByteArray = ByteArray(0)
            do {
                val range = bytes.copyOfRange(tempIndex, tempIndex + 16)
                tempIndex += 16
                val sr = gmUtil.encSm4(key, range, GmUtil.CalcMode.Sm4Cpp)
                outByteArray += sr

            } while (tempIndex <= size)
            if (tempIndex > size) {
                tempIndex -= 16
            }
            val rem = size % 16
            val range1 = bytes.copyOfRange(tempIndex, tempIndex + rem)
            val remContnt = gmUtil.encSm4(key, range1, GmUtil.CalcMode.Sm4Cpp)
            outByteArray += remContnt

            outFile.writeBytes(outByteArray)

        } catch (e: Exception) {

        }
    }
}