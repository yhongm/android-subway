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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yhongm.gm.JniSm4
import com.yhongm.subway.view.WaitDialog

class SplashActivity : AppCompatActivity() {
    var waitDialog: WaitDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        waitDialog = WaitDialog(this@SplashActivity, "数据加载中...")
        waitDialog!!.show()
        initData()
    }

    private fun initData() {
        Thread(Runnable {
            var startTime = System.currentTimeMillis();
            val stationEncBa = JniSm4.assetContentDec(assets, "stationenc.txt")
            val subwayEncBa = JniSm4.assetContentDec(assets, "subwayenc.txt")
            DataObject.station = String(stationEncBa)
            DataObject.subway = String(subwayEncBa)
            val endTime = System.currentTimeMillis()

            val totalTime = endTime - startTime
            var intent = Intent(this@SplashActivity, MainActivity::class.java)

            startActivity(intent)
            finish()
            waitDialog!!.dismiss()

//            Log.i("SplashActivity", "10:57,initData totalTime:$totalTime ,threadName:${Thread.currentThread().name}")// 2018/6/8,yhongm
        }).start()


    }


}