package com.zhihu.walker.ext_walker

import java.io.File
import java.io.FileInputStream

object DexWalker : WalkerForD2J() {

    override fun getExt(): String {
        return "dex"
    }

    override fun walk(file: File) {
        FileInputStream(file).use { input ->
            accept(file, input)
        }
    }
}