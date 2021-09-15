package com.zhihu.walker

import com.zhihu.walker.CmdLineParser.IllegalOptionValueException
import com.zhihu.walker.CmdLineParser.Option.StringOption
import com.zhihu.walker.CmdLineParser.UnknownOptionException
import java.io.File
import kotlin.system.exitProcess


class Main {

    private class MyOptionsParser : CmdLineParser() {

        companion object {
            val INPUT_FILE: Option<String> = StringOption('i', "input")
            val OUTPUT_FILE: Option<String> = StringOption('o', "output")
        }

        init {
            addOption(INPUT_FILE)
            addOption(OUTPUT_FILE)
        }

        fun printUsage() {
            System.err.println(
                "usage: walker [{-i,--input} input file] [{-o,--output} output file]"
            )
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val myOptions = MyOptionsParser()

            try {
                myOptions.parse(args)
            } catch (e: UnknownOptionException) {
                System.err.println(e.message)
                myOptions.printUsage()
                exitProcess(2)
            } catch (e: IllegalOptionValueException) {
                System.err.println(e.message)
                myOptions.printUsage()
                exitProcess(2)
            }

            val inputFilePath = myOptions.getOptionValue(MyOptionsParser.INPUT_FILE)
            val outputFilePath = myOptions.getOptionValue(MyOptionsParser.OUTPUT_FILE)

            val inputFile = File(inputFilePath)
            if (inputFile.isDirectory) {
                inputFile.walk().forEach {

                }
            }

        }
    }
}