package com.zhihu.walker

import com.beust.jcommander.Parameter

class Args {

    @Parameter(names = ["-i", "--input"], description = "被扫描的文件，支持 jar、aar 和存放这些文件的目录", required = true)
    var inputPath: String? = null

    @Parameter(names = ["-o", "--output"], description = "Json 报告输出文件名，默认为当前目录的 result.json")
    var outputPath: String = "./result.json"

    @Parameter(names = ["-p", "--policy"], description = "规则配置文件，根据规则进行扫描", required = true)
    var policyPath: String? = null

    @Parameter(names = ["-h", "--help"], help = true)
    var help: Boolean = false
}