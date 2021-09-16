package com.zhihu.walker

import com.zhihu.walker.visitor.InstanceMethodInvokeVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import java.io.File
import java.io.InputStream

abstract class WalkerForASM {

    abstract fun getExt(): String

    abstract fun walk(file: File)

    internal fun accept(file: File, input: InputStream) {
        val visitor = buildClassVisitor()
        ClassReader(input).accept(visitor, ClassReader.EXPAND_FRAMES)
    }

    private fun buildClassVisitor(): ClassVisitor {
        var visitor = InstanceMethodInvokeVisitor()
        return visitor
    }

}