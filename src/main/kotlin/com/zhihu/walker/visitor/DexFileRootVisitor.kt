package com.zhihu.walker.visitor

import d2j.api.visitors.DexClassVisitor
import d2j.api.visitors.DexFileVisitor
import java.io.File

class DexFileRootVisitor(val file: File) : DexFileVisitor() {
    override fun visit(
        access_flags: Int,
        className: String,
        superClass: String?,
        interfaceNames: Array<out String>?
    ): DexClassVisitor {
        val cv = super.visit(access_flags, className, superClass, interfaceNames)
        return InvokeVirtualDexVisitor(file, className, cv)
    }
}