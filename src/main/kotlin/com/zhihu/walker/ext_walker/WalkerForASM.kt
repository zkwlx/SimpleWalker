package com.zhihu.walker.ext_walker

import com.zhihu.walker.class_visitor.GetStaticVisitor
import com.zhihu.walker.class_visitor.InvokeSpecialVisitor
import com.zhihu.walker.class_visitor.InvokeVirtualVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import java.io.File
import java.io.InputStream

/**
 * 使用 ASM 遍历每个 .class
 */
abstract class WalkerForASM : Walker() {

    /**
     * @param file File 类型对象，用于获取被扫描的文件信息
     * @param input Visitor 访问的输入源
     */
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