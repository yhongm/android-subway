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

package com.yhongm.subway.fragment

import android.support.v4.app.Fragment
import android.content.pm.PackageManager
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import android.content.Intent
import com.yhongm.subway.R


class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_about, container, false)
        val tvVersion = view.findViewById<TextView>(R.id.about_tv_version)
        val tvDeveloper = view.findViewById<TextView>(R.id.about_tv_dv)
        val tvOpenSource = view.findViewById<TextView>(R.id.about_tv_opensource)
        val versionName = activity!!.application.packageManager.getPackageInfo(activity!!.packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT).versionName
        tvVersion.text = "版本$versionName"
        tvDeveloper.text = "开发者:yhongm"
        tvOpenSource.text = "开源许可"
        tvOpenSource.setOnClickListener({

            OssLicensesMenuActivity.setActivityTitle("开源许可")

            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))


        })
        return view
    }
}