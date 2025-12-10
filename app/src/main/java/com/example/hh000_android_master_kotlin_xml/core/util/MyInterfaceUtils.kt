package com.example.hh000_android_master_kotlin_xml.core.util

object MyInterfaceUtils {
    interface MyCallbackRun {

        fun onCallbackRun()
    }

    interface MyCallbackBoolean {

        fun onCallbackBoolean(bool: Boolean)
    }

    interface MyCallbackInt {

        fun onCallbackInt(int: Int)
    }

    interface MyCallbackString {

        fun onCallbackString(string: String)
    }
}