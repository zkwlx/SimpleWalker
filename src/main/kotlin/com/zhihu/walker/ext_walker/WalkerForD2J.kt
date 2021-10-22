package com.zhihu.walker.ext_walker

import com.zhihu.walker.visitor.DexFileRootVisitor
import d2j.api.visitors.DexFileVisitor
import d2j.reader.DexFileReader
import java.io.File
import java.io.InputStream

/**
 * 使用 d2j 遍历每个 dex
 */
abstract class WalkerForD2J : Walker() {

    /**
     * @param file File 类型对象，用于获取被扫描的文件信息
     * @param input Visitor 访问的输入源
     */
    internal fun accept(file: File, input: InputStream) {
        val visitor = buildClassVisitor(file)
        DexFileReader(input).accept(visitor)
    }

    private fun buildClassVisitor(file: File): DexFileVisitor {
        var visitor: DexFileVisitor = DexFileRootVisitor(file)
        return visitor
    }

}