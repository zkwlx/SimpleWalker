package com.zhihu.walker.ext_walker

import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream

object ApkWalker : WalkerForD2J() {

    private const val DOT_DEX = ".dex"

    override fun getExt(): String {
        return "apk"
    }

    override fun walk(file: File) {
        ZipInputStream(FileInputStream(file)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory && entry.name.endsWith(DOT_DEX)) {
                    accept(file, zis)
                } else {
                    // Log.i("非 DOT_DEX 文件不处理: ${entry.name}")
                }
                entry = zis.nextEntry
            }
        }
    }

}