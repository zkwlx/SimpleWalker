package com.zhihu.walker.ext_walker

import com.zhihu.walker.utils.Log
import java.io.File

abstract class Walker {
    abstract fun getExt(): String

    abstract fun walk(file: File)

    fun verifyExt(file: File): Boolean {
        val ext = getExt()
        return if (file.name.endsWith(ext)) {
            true
        } else {
            Log.i("文件类型不是 $ext：${file.absoluteFile}")
            false
        }
    }
}