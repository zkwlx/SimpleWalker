package com.zhihu.walker.ext_walker

import com.zhihu.walker.visitor.GetStaticVisitor
import com.zhihu.walker.visitor.InvokeSpecialVisitor
import com.zhihu.walker.visitor.InvokeVirtualVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import java.io.File
import java.io.InputStream

abstract class WalkerForASM {

    abstract fun getExt(): String

    abstract fun walk(file: File)

    internal fun accept(file: File, input: InputStream) {
        val visitor = buildClassVisitor(file)
        ClassReader(input).accept(visitor, ClassReader.EXPAND_FRAMES)
    }

    private fun buildClassVisitor(file: File): ClassVisitor {
        var visitor: ClassVisitor = InvokeVirtualVisitor(file)
        visitor = InvokeSpecialVisitor(file, visitor)
        visitor = GetStaticVisitor(file, visitor)
        return visitor
    }

}