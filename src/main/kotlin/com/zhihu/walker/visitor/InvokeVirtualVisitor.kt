package com.zhihu.walker.visitor

import com.zhihu.walker.Context
import com.zhihu.walker.Policy
import org.json.JSONArray
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9
import java.io.File

class InvokeVirtualVisitor(val file: File, cv: ClassVisitor? = null) : ClassVisitor(ASM9, cv) {

    private val policyList: List<Policy> = Context.policyList.filter {
        it.instruct.toUpperCase() == "INVOKEVIRTUAL"
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

        /**
         * 遍历对象方法的调用
         */
        override fun visitMethodInsn(
            opcode: Int,
            owner: String,
            name: String,
            desc: String,
            isInterface: Boolean
        ) {
            if (opcode != Opcodes.INVOKEVIRTUAL) {
                super.visitMethodInsn(opcode, owner, name, desc, isInterface)
                return
            }
            policyList.filter {
                owner == it.className
            }.filter {
                "all" == it.secondName || name == it.secondName
            }.forEach {
                val array = Context.outputJson[it.key] as JSONArray
                array.put(
                    "文件 ${file.name} 中的 $currentClassName.$parentMethodName() 方法调用了" +
                            " ${owner.substringAfterLast('/')}.$name()，${it.desc}"
                )
            }
            super.visitMethodInsn(opcode, owner, name, desc, isInterface)
        }

    }
}