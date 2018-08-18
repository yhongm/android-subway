package com.yhongm.subway.svg

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

import com.yhongm.subway.xml.Circle
import com.yhongm.subway.xml.Ellipse
import com.yhongm.subway.xml.InnerSvg
import com.yhongm.subway.xml.SVG
import com.yhongm.subway.xml.Text

import java.text.ParseException
import java.util.ArrayList

class RenderSvgPathView
(context: Context, attr: AttributeSet) : View(context, attr) {

    private var strokeColor: Int = 0
    private var strokeWidth: Int = 0
    private var originalWidth: Int = 0
    private var originalHeight: Int = 0

    private var pathDatas: ArrayList<PathData>? = null
    private var stokePaint: Paint? = null

    private var viewWidth: Float = 0f
    private var viewHeight: Float = 0f

    private var mContext: Context = context

    var parser: SvgPathParser? = null

    var ellipses: ArrayList<Ellipse>? = null

    var texts: ArrayList<Text>? = null

    var svgs: ArrayList<SVG>? = null

    var innerSvgs: ArrayList<InnerSvg>? = null

    var isShowStationName: Boolean = true
    var isShowStationLocation: Boolean = true


    var maxWidth: Int = 3466

    var maxHeight = 1821
    var minWidth: Int = maxWidth / 2
    var minHeight = maxHeight / 2

    var rsl: RenderSvgLister? = null

    private var isFirst = true

    var isNeedUpdateLocation = true

    var pointMapText: HashMap<Point, String>? = null

    private var minLeft = 0
    private var maxTop = 0
    private var maxRight = 0
    private var minBottom = 0

    private val pathParser: SvgPathParser
        get() {
            val builder = ConstrainedSvgPathParser.Builder()
            return builder.originalWidth(originalWidth)
                    .originalHeight(originalHeight)
                    .viewWidth(viewWidth.toInt())
                    .viewHeight(viewHeight.toInt())
                    .build()
        }

    init {
        strokeColor = Color.YELLOW
        strokeWidth = 1
        originalWidth = 3486
        originalHeight = 1821

        pointMapText = HashMap()
        initD()
    }


    private fun initD() {

        pathDatas = ArrayList()
        ellipses = ArrayList()
        texts = ArrayList()

        innerSvgs = ArrayList()

        initDashPaint()

//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun initDashPaint() {
        stokePaint = Paint()
        stokePaint!!.style = Paint.Style.STROKE
        stokePaint!!.isAntiAlias = true
        stokePaint!!.strokeWidth = strokeWidth.toFloat()
        stokePaint!!.color = strokeColor
    }

    override fun invalidate() {
        super.invalidate()
        isNeedUpdateLocation = true
    }

    fun start() {
        checkOriginalDimensions()
        if (parser != null) {
            initSvgs()
        }
        invalidate()
    }


    private fun checkOriginalDimensions() {
        if (originalWidth <= 0 || originalHeight <= 0) {
            throw IllegalArgumentException(
                    "You must provide the original image dimensions in order map the coordinates properly.")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
//        if (isFirst) {
//            isFirst = false
//            minWidth = oldw / 2
//            minHeight = oldh / 2
//            maxWidth = oldw
//            maxHeight = oldh
//        }
        parser = pathParser
        initSvgs()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var mDownX: Float = 0f
        var mDownY: Float = 0f
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {

                mDownX = event.x

                mDownY = event.y
            }
            MotionEvent.ACTION_UP -> {
                var mUpX = event.x
                var mUpY = event.y
                pointMapText!!.forEach {


                    var r = returnRect(it.key, 50)

                    if (r.contains(mUpX.toInt(), mUpY.toInt())) {

                        if (rsl != null) {
                            rsl!!.clickStationName(name = it.value, view = this, x = it.key.x, y = it.key.y)
                        }
                    }


                }

            }

        }
        return true
    }

    private fun returnRect(point: Point, radius: Int): Rect {
        return Rect(point.x - radius, point.y - radius, point.x + radius, point.y + radius)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (pathData in pathDatas!!) {
            val pathColor = pathData.pathColor
            if (pathColor != null) {
                stokePaint!!.color = pathColor
            }
            val strokeWidth = pathData.pathStrokeWidth

            stokePaint!!.strokeWidth = strokeWidth!! * viewWidth / originalWidth
            canvas.drawPath(pathData.path!!, stokePaint!!)
        }

        if (isShowStationLocation) {
            val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            ellipses!!.forEach {
                val cx = it.cx
                val cy = it.cy
                val fillColor = it.fill
                val strokeColor = it.stroke
                val radius = it.rx
                drawCircle(cx, cy, radius, fillColor, strokeColor, canvas, circlePaint)

            }
        }
        if (isShowStationLocation) {
            val cPaint = Paint()
            cPaint.color = Color.GREEN
            cPaint.flags = Paint.ANTI_ALIAS_FLAG
            cPaint.style = Paint.Style.FILL
            innerSvgs!!.forEach continuing@{ innerSvg ->

                var circle: Circle? = innerSvg.circle ?: return@continuing
                drawInnerSvg(innerSvg, canvas, circle!!, cPaint)


            }
        }
        if (isShowStationName) {
            val cTextPaint = TextPaint()
            cTextPaint.color = Color.WHITE

            cTextPaint.isAntiAlias = true
            texts!!.withIndex().forEach {
                var text = it.value
                val x = text.x
                val y = text.y
                val dy = text.dy
                val fillColor = text.fill
                val content = text.content
                val fontSize = text.fontSize
                cTextPaint.textSize = fontSize * viewWidth / originalWidth
                var index = it.index
                drawContent(x, y, dy, fillColor, content, canvas, index, cTextPaint)
            }
        }


    }

    private fun drawInnerSvg(innerSvg: InnerSvg, canvas: Canvas, circle: Circle, cPaint: Paint) {

        val viewBox = innerSvg.viewBox
        //            if (viewBox != null) {
        val vbWidth = viewBox!!.width
        val svgWidth = innerSvg.width
        val vbHeight = viewBox.height
        val svgHeight = innerSvg.height


        val x = innerSvg.x * (viewWidth / (originalWidth + 0.5f))
        val y = innerSvg.y * (viewHeight / (originalHeight + 0.5f))
        val cx = circle!!.cx * svgWidth / vbWidth * (viewWidth / (originalWidth + 0.5f))
        val cy = circle!!.cy * svgHeight / vbHeight * (viewWidth / (originalWidth + 0.5f))


        val fillColor = circle.fill
        val strokeColor = circle.stroke
        val radius = circle.r


        val cRadius = (radius * (svgWidth / vbWidth)) * (viewWidth / (originalWidth + (0.5f)))

        canvas.drawCircle((x + cx), (y + cy), cRadius, cPaint)
    }

    fun drawCircle(cx: Float?, cy: Float?, radius: Float?, fillColor: Int, strokeColor: Int, canvas: Canvas, circlePaint: Paint) {
        val dcx: Float = cx!! * viewWidth / originalWidth
        val dcy: Float = cy!! * viewHeight / originalHeight

        circlePaint.color = fillColor
        canvas.drawCircle(dcx, dcy, radius!! * viewWidth / originalWidth, circlePaint)
    }

    fun drawContent(cx: Float, cy: Float, dy: Float, color: String?, content: String, canvas: Canvas, index: Int, cTextPaint: TextPaint) {
        val dcx = cx * viewWidth / originalWidth
        val dcy = cy * viewHeight / originalHeight
        val ddy = dy * viewHeight / originalHeight

        val dcxInt = dcx.toInt()
        val dcyInt = dcy.toInt()
        val ddyInt = ddy.toInt()
        var point = Point(dcxInt, dcyInt)
        if (index == 0) {
            minLeft = dcxInt
            maxRight = dcxInt
            minBottom = dcyInt + ddyInt
            maxTop = dcyInt + ddyInt
        }
        if (dcx < minLeft) {
            minLeft = dcxInt
        }
        if (dcx > maxRight) {
            maxRight = dcxInt
        }
        if ((dcy + ddy) < minBottom) {
            minBottom = dcyInt + ddyInt
        }
        if ((dcy + ddy) > maxTop) {
            maxTop = dcyInt + ddyInt
        }
        pointMapText!!.put(point, content)
        if (index == texts!!.size - 1) {
            if (rsl != null) {
                if (isNeedUpdateLocation) {
                    val rect = Rect(minLeft, minBottom, maxRight, maxTop)
//
                    rsl!!.firstContent(rect.centerX(), rect.centerY())
                    isNeedUpdateLocation = false
                }
            }

        }


        canvas.drawText(content, dcx, (dcy + ddy), cTextPaint)
    }


    fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
    }


    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth
    }

    fun setOriginalDimensions(originalWidth: Int, originalHeight: Int) {
        this.originalWidth = originalWidth
        this.originalHeight = originalHeight
    }


    private fun setSvgPath(svgPath: String?): PathData {
        if (svgPath == null || svgPath.isEmpty()) {
            throw IllegalArgumentException(
                    "must not empty path .")
        }
        return buildPathData(svgPath)

    }


    private fun buildPathData(svgPath: String): PathData {


        val pathData = PathData()
        try {
            pathData.path = parser!!.parsePath(svgPath)
        } catch (e: ParseException) {
            pathData.path = Path()
        }

        val pm = PathMeasure(pathData.path, true)
        while (true) {
            pathData.length = Math.max(pathData.length, pm.length)
            if (!pm.nextContour()) {
                break
            }
        }
        return pathData
    }

    fun setPvgs(svgs: ArrayList<SVG>) {
        this.svgs = svgs

        start()
    }

    fun initSvgs() {
        clickCacheDatas()
        svgs?.forEach loop@{ svg ->

            if (!svg.isSelect) {
                return@loop
            }
            val viewBox = svg.svgAttribute!!.viewBox
            setOriginalDimensions(viewBox!!.width, viewBox.height)
            val path = svg.g!!.path
            val pathData = setSvgPath(path)
            pathData.pathColor = Color.parseColor(svg.color)
            pathData.pathStrokeWidth = svg.g!!.pathStrokeWidth
            pathDatas!!.add(pathData)

            ellipses!!.addAll(svg!!.g!!.ellipses!!)

            texts!!.addAll(svg!!.g!!.texts!!)

            innerSvgs!!.addAll(svg!!.g!!.innerSvgs!!)
        }
    }

    private fun clickCacheDatas() {
        pathDatas!!.clear()
        ellipses!!.clear()
        texts!!.clear()
        innerSvgs!!.clear()
    }


    fun setScale(d: Double) {
        var lp = layoutParams

        Log.i("RenderSvgPathView", "18:53,setScale d:$d ")// 2018/6/11,yhongm
//        Log.i("RenderSvgPathView", "18:31,setScale lpWidth:${lp.width} lp.height:${lp.height} ")// 2018/6/11,yhongm
        when (d) {

            0.5 -> {
                lp.width = minWidth
                lp.height = minHeight
                layoutParams = lp
                isFirst = true

            }
            2.0 -> {
                Log.i("RenderSvgPathView", "18:54,setScale 2.0: ")// 2018/6/11,yhongm
                lp.width = maxWidth
                lp.height = maxHeight
                layoutParams = lp
                isFirst = true
            }


        }
//            lp.width in (minWidth + 1)..(maxWidth - 1) -> {
//                lp.width = (lp.width * d).toInt()
//                lp.height = (lp.height * d).toInt()
//                layoutParams = lp
//                isFirst = true
//            }
//            lp.width == maxWidth -> {
//                if (d > 0) {
//                    Toast.makeText(mContext, "已放到到最大", Toast.LENGTH_SHORT).show()
//                }else{
//                    lp.width = (lp.width * d).toInt()
//                    lp.height = (lp.height * d).toInt()
//                    layoutParams = lp
//                }
//            }
//            lp.width == minWidth -> {
//                Toast.makeText(mContext, "已缩小到最小", Toast.LENGTH_SHORT).show()
//            }

    }

    fun setIsShowStationName(isShow: Boolean) {
        isShowStationName = isShow
        invalidate()
    }

    fun setIsShowStationLocation(isShow: Boolean) {
        isShowStationLocation = isShow
        invalidate()
    }


    fun setListener(l: RenderSvgLister) {
        rsl = l
    }

    interface RenderSvgLister {
        fun firstContent(x: Int, y: Int)
        fun clickStationName(name: String, view: View, x: Int, y: Int)
    }
}
