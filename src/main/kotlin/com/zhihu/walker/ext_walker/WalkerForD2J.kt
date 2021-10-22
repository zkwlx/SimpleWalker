package com.zhihu.walker.ext_walker

import com.zhihu.walker.dex_visitor.GetStaticDexVisitor
import com.zhihu.walker.dex_visitor.InvokeSpecialDexVisitor
import com.zhihu.walker.utils.StrUtils
import com.zhihu.walker.dex_visitor.InvokeVirtualDexVisitor
import d2j.api.visitors.DexClassVisitor
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
        return DexFileRootVisitor(file)
    }

    inner class DexFileRootVisitor(val file: File) : DexFileVisitor() {

        override fun visit(
            access_flags: Int,
            className: String,
            superClass: String?,
            interfaceNames: Array<out String>?
        ): DexClassVisitor {
            var cv = super.visit(access_flags, className, superClass, interfaceNames)
            val trimmedClassName = StrUtils.trimClassName(className)
            cv = InvokeVirtualDexVisitor(file, trimmedClassName, cv)
            cv = InvokeSpecialDexVisitor(file, trimmedClassName, cv)
            cv = GetStaticDexVisitor(file, trimmedClassName, cv)
            return cv
        }
    }

}