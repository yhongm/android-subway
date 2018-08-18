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

import com.yhongm.subway.xml.*

class DataObject {
    companion object {
        var station: String? = null
        var subway: String? = null

        fun parseSvg(): ArrayList<SVG> {
            val inputStream = subway!!.byteInputStream()
            return SvgUtil.getInstance()!!.parseFile(inputStream)
        }

        fun parseFileToSubway(): ArrayList<Subway> {
            return SvgUtil.getInstance().parseFileToSubway(subway!!.byteInputStream())

        }

        fun parseStations(): ArrayList<Station> {

            return XMLUtil.readStation(DataObject.station!!.byteInputStream())!!
        }
    }
}