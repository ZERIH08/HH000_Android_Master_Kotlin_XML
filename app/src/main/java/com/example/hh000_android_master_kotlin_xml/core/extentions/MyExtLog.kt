package com.example.hh000_android_master_kotlin_xml.core.extentions

import android.util.Log

object MyExtLog {
    //----------------------------------------------------------------------------------------------
    fun String.logVerbose0(tag: String = "818181") {
        Log.v(tag, this)
    }

    fun String.logInfo0(tag: String = "818181") {
        Log.i(tag, this)
    }

    fun String.logWarn0(tag: String = "818181") {
        Log.w(tag, this)
    }

    fun String.logDebug0(tag: String = "818181") {
        Log.d(tag, this)
    }

    fun String.logError0(tag: String = "818181") {
        Log.e(tag, this)
    }
    //----------------------------------------------------------------------------------------------
}