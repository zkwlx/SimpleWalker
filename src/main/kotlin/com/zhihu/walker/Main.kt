package com.zhihu.walker

import com.beust.jcommander.JCommander
import com.zhihu.walker.ext_walker.*
import com.zhihu.walker.utils.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*


class Main {

    companion object {

        private val walkerMap = mapOf(
//            create(JarWalker),
//            create(AarWalker),
            create(DexWalker)
        )

        private fun create(walker: Walker): Pair<String, Walker> {
            return walker.getExt() to walker
        }

        @JvmStatic
        fun main(vararg args: String) {
            val myArgs = Args()
            val commander = JCommander.newBuilder().addObject(myArgs).build()
            commander.setProgramName("SimpleWalker", "一个简单的静态代码扫描工具，可通过文件配置，支持目录、jar、aar 格式")
            try {
                commander.parse(*args)
            } catch (e: Throwable) {
                commander.usage()
                return
            }
            if (myArgs.help) {
                commander.usage()
                return
            }
            parsePolicy(myArgs.policyPath!!)
            val inputFile = File(myArgs.inputPath!!)
            if (inputFile.isDirectory) {
                inputFile.walk().filter { !it.isDirectory }.forEach { file ->
                    doWalk(file)
                }
            } else {
                doWalk(inputFile)
            }

            onFinish(myArgs.outputPath)
        }

        private fun parsePolicy(policyPath: String) {
            val policyList = LinkedList<Policy>()
            File(policyPath).readLines()
                .filter {
                    it.trim().isNotEmpty()
                }.filter {
                    !it.startsWith("#")
                }.forEach { line ->
                    val list = line.split(",")
                    val instruct = list[0]
                    val className = list[1].replace(".", "/")
                    val secondName = list[2]
                    val desc = list[3]
                    policyList.add(Policy(instruct, className, secondName, desc))
                }
            Context.policyList = policyList
            createOutput()
        }

        private fun createOutput() {
            val json = JSONObject()
            Context.policyList.forEach {
                if (!json.has(it.key)) {
                    val array = JSONArray()
                    json.put(it.key, array)
                }
            }
            Context.outputJson = json
        }

        private fun doWalk(file: File) {
            walkerMap[file.extension].also {
                if (it != null && it.verifyExt(file)) {
                    it.walk(file)
                } else {
                    Log.i("不支持文件格式 ${file.absolutePath}")
                }
            }
        }

        private fun onFinish(outputPath: String) {
            val outputFile = File(outputPath)
            outputFile.writeText(Context.outputJson.toString(4))
            Log.i("报告文件：${outputFile.absolutePath}")
        }
    }
}