package com.zhihu.walker

import java.io.File
import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.util.zip.ZipInputStream

object JarWalker : WalkerForASM() {

    private const val DOT_CLASS = ".class"

    override fun getExt(): String {
        return "jar"
    }

    override fun walk(jarFile: File) {
        if (!jarFile.name.endsWith(getExt())) {
            throw IllegalArgumentException("文件类型不是 jar：${jarFile.absoluteFile}")
        }
        ZipInputStream(FileInputStream(jarFile)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory && entry.name.endsWith(DOT_CLASS)) {
                    accept(jarFile, zis)
                } else {
                    // Do not copy resources
                    Log.i("非 $DOT_CLASS 文件不处理: ${entry.name}")
                }
                entry = zis.nextEntry
            }
        }
    }

}