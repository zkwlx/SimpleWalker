package com.zhihu.walker.class_visitor

import com.zhihu.walker.Context
import com.zhihu.walker.Policy
import org.json.JSONArray
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9
import java.io.File

class GetStaticVisitor(val file: File, cv: ClassVisitor? = null) : ClassVisitor(ASM9, cv) {

    private val policyList: List<Policy> = Context.policyList.filter {
        it.instruct.toUpperCase() == "GETSTATIC"
    }

    private lateinit var currentClassName: String

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        currentClassName = name
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)
        return InternalMethodVisitor(vm, name, descriptor)
    }

    private inner class InternalMethodVisitor internal constructor(
        cw: MethodVisitor?,
        val parentMethodName: String,
        val parentMethodDesc: String
    ) : MethodVisitor(ASM9, cw) {

        override fun visitFieldInsn(opcode: Int, owner: String, name: String?, descriptor: String?) {
            if (opcode != Opcodes.GETSTATIC) {
                super.visitFieldInsn(opcode, owner, name, descriptor)
                return
            }
            policyList.filter {
                owner == it.className
            }.filter {
                "all" == it.secondName || name == it.secondName
            }.forEach {
                val array = Context.outputJson[it.key] as JSONArray
                array.put(
                    "文件 ${file.name} 中 $currentClassName.$parentMethodName() 方法使用了" +
                            " ${owner.substringAfterLast('/')}.$name，${it.desc}"
                )
            }
            super.visitFieldInsn(opcode, owner, name, descriptor)
        }

    }
}