package com.zhihu.walker.ext_walker

import java.io.File

object ApkWalker : WalkerForD2J() {
    override fun getExt(): String {
        return "apk"
    }

    override fun walk(file: File) {
        TODO("Not yet implemented")
    }
}