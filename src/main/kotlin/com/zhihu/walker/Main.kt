package com.zhihu.walker

import java.io.File


class Main {


    companion object {

        private val walkerMap = mapOf(
            create(JarWalker),
            create(AarWalker)
        )

        private fun create(walker: WalkerForASM): Pair<String, WalkerForASM> {
            return walker.getExt() to walker
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val inputPath = args[0]
            val inputFile = File(inputPath)
            if (inputFile.isDirectory) {
                inputFile.walk().filter { !it.isDirectory }.forEach { file ->
                    doWalk(file)
                }
            } else {
                doWalk(inputFile)
            }

        }

        private fun doWalk(file: File) {
            walkerMap[file.extension].also {
                if (it != null) {
                    it.walk(file)
                } else {
                    Log.i("不支持文件格式 ${file.absolutePath}")
                }
            }
        }
    }
}