package com.zhihu.walker.utils

class StrUtils {

    companion object {

        /**
         * Ljava/lang/Object; => java/lang/Object
         */
        @JvmStatic
        fun trimClassName(name: String): String {
            return name.substring(1 - name.length - 1)
        }
    }
}