package com.zhihu.walker.visitor

import com.zhihu.walker.Log
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9

class InstanceMethodInvokeVisitor(cv: ClassVisitor? = null) : ClassVisitor(ASM9, cv) {

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
        return InternalMethodVisitor(name, descriptor)
    }

    private inner class InternalMethodVisitor internal constructor(
        val parentMethodName: String,
        val parentMethodDesc: String
    ) : MethodVisitor(ASM9) {

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
                return
            }
            if (owner == "android/telephony/TelephonyManager") {
                Log.i("SDK 中 $currentClassName.$parentMethodName$parentMethodDesc 调用了 ${owner.substringAfterLast('/')}.$name 方法获取手机信息")
            }
            super.visitMethodInsn(opcode, owner, name, desc, isInterface)
        }

    }
}