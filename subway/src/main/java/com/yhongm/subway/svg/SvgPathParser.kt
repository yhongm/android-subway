package com.yhongm.subway.svg

import android.graphics.Path
import android.graphics.PointF
import android.util.Log

import java.text.ParseException

open class SvgPathParser {

    private var mCurrentToken: Int = 0
    private val mCurrentPoint = PointF()
    private var mLength: Int = 0
    private var mIndex: Int = 0
    private var mPathString: String? = null

    protected open fun transformX(x: Float): Float {
        return x
    }

    protected open fun transformY(y: Float): Float {
        return y
    }

    @Throws(ParseException::class)
    fun parsePath(s: String): Path {
        mCurrentPoint.set(java.lang.Float.NaN, java.lang.Float.NaN)
        mPathString = s
        mIndex = 0
        mLength = mPathString!!.length

        val tempPoint1 = PointF()
        val tempPoint2 = PointF()
        val tempPoint3 = PointF()

        val p = Path()
        p.fillType = Path.FillType.WINDING

        var firstMove = true
        while (mIndex < mLength) {
            val command = consumeCommand()
            val relative = mCurrentToken == TOKEN_RELATIVE_COMMAND
            when (command) {
                'M', 'm' -> {
                    // move command
                    var firstPoint = true
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative && mCurrentPoint.x != java.lang.Float.NaN)
                        if (firstPoint) {

                            p.moveTo(tempPoint1.x, tempPoint1.y)
                            firstPoint = false
                            if (firstMove) {
                                mCurrentPoint.set(tempPoint1)
                                firstMove = false
                            }
                        } else {
                            p.lineTo(tempPoint1.x, tempPoint1.y)
                        }
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'C', 'c' -> {
                    // curve command
                    if (mCurrentPoint.x == java.lang.Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }

                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative)
                        consumeAndTransformPoint(tempPoint2, relative)
                        consumeAndTransformPoint(tempPoint3, relative)
                        p.cubicTo(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y, tempPoint3.x,
                                tempPoint3.y)
                    }
                    mCurrentPoint.set(tempPoint3)
                }
                'Q', 'q' -> {
                    // q curve command
                    if (mCurrentPoint.x == java.lang.Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }

                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative)
                        consumeAndTransformPoint(tempPoint2, relative)

                        p.quadTo(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y)
                    }
                    mCurrentPoint.set(tempPoint2)
                }

                'L', 'l' -> {
                    // line command
                    if (mCurrentPoint.x == java.lang.Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }

                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative)
                        p.lineTo(tempPoint1.x, tempPoint1.y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'H', 'h' -> {
                    // horizontal line command
                    if (mCurrentPoint.x == java.lang.Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }

                    while (advanceToNextToken() == TOKEN_VALUE) {
                        var x = transformX(consumeValue())
                        if (relative) {
                            x += mCurrentPoint.x
                        }
                        p.lineTo(x, mCurrentPoint.y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'V', 'v' -> {
                    // vertical line command
                    if (mCurrentPoint.x == java.lang.Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }

                    while (advanceToNextToken() == TOKEN_VALUE) {
                        var y = transformY(consumeValue())
                        if (relative) {
                            y += mCurrentPoint.y
                        }
                        p.lineTo(mCurrentPoint.x, y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'Z', 'z' -> {
                    // close command
                    p.close()
                }
            }
        }

        return p
    }

    private fun advanceToNextToken(): Int {
        while (mIndex < mLength) {
            val c = mPathString!![mIndex]
            if ('a' <= c && c <= 'z') {
                mCurrentToken = TOKEN_RELATIVE_COMMAND
                return mCurrentToken
            } else if ('A' <= c && c <= 'Z') {
                mCurrentToken = TOKEN_ABSOLUTE_COMMAND
                return mCurrentToken
            } else if ('0' <= c && c <= '9' || c == '.' || c == '-') {
                mCurrentToken = TOKEN_VALUE
                return mCurrentToken
            }

            // skip unrecognized character
            ++mIndex
        }
        mCurrentToken = TOKEN_EOF
        return mCurrentToken
    }

    @Throws(ParseException::class)
    private fun consumeCommand(): Char {
        advanceToNextToken()
        if (mCurrentToken != TOKEN_RELATIVE_COMMAND && mCurrentToken != TOKEN_ABSOLUTE_COMMAND) {
            throw ParseException("Expected command", mIndex)
        }

        return mPathString!![mIndex++]
    }

    @Throws(ParseException::class)
    private fun consumeAndTransformPoint(out: PointF, relative: Boolean) {
        out.x = transformX(consumeValue())
        out.y = transformY(consumeValue())
        if (relative) {
            out.x += mCurrentPoint.x
            out.y += mCurrentPoint.y
        }
    }

    @Throws(ParseException::class)
    private fun consumeValue(): Float {
        advanceToNextToken()
        if (mCurrentToken != TOKEN_VALUE) {
            throw ParseException("Expected value", mIndex)
        }

        var start = true
        var seenDot = false
        var index = mIndex
        while (index < mLength) {
            val c = mPathString!![index]
            if (!('0' <= c && c <= '9') && (c != '.' || seenDot) && (c != '-' || !start)) {
                // end of value
                break
            }
            if (c == '.') {
                seenDot = true
            }
            start = false
            ++index
        }

        if (index == mIndex) {
            throw ParseException("Expected value", mIndex)
        }

        val str = mPathString!!.substring(mIndex, index)
        try {
            val value = java.lang.Float.parseFloat(str)
            mIndex = index
            return value
        } catch (e: NumberFormatException) {
            throw ParseException("Invalid  value '$str'.", mIndex)
        }

    }

    companion object {
        private val TOKEN_ABSOLUTE_COMMAND = 1
        private val TOKEN_RELATIVE_COMMAND = 2
        private val TOKEN_VALUE = 3
        private val TOKEN_EOF = 4
    }
}
