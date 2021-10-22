package com.zhihu.walker.visitor

import com.zhihu.walker.Context
import com.zhihu.walker.Log
import com.zhihu.walker.Policy
import d2j.api.Method
import d2j.api.MethodHandle
import d2j.api.Proto
import d2j.api.reader.Op
import d2j.api.visitors.DexClassVisitor
import d2j.api.visitors.DexCodeVisitor
import d2j.api.visitors.DexFileVisitor
import d2j.api.visitors.DexMethodVisitor
import org.json.JSONArray
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9
import java.io.File

class InvokeVirtualDexVisitor(val file: File, val className: String, cv: DexClassVisitor? = null) :
    DexClassVisitor(cv) {

    private val policyList: List<Policy> = Context.policyList.filter {
        it.instruct.toUpperCase() == "INVOKEVIRTUAL"
    }

    private lateinit var currentClassName: String


    override fun visitMethod(
        access: Int,
        method: Method
    ): DexMethodVisitor {
        val vm = super.visitMethod(access, method)
        return InternalMethodVisitor(vm, method)
    }

    private inner class InternalMethodVisitor constructor(
        mv: DexMethodVisitor?,
        val method: Method
    ) : DexMethodVisitor(mv) {

        override fun visitCode(): DexCodeVisitor {
            val cv = super.visitCode()
            return InternalCodeVisitor(cv, method)
        }

    }

    private inner class InternalCodeVisitor constructor(cv: DexCodeVisitor?, val parentMethod: Method) :
        DexCodeVisitor(cv) {

        /**
         * OP_INVOKE_VIRTUAL
         * OP_INVOKE_SUPER
         * OP_INVOKE_DIRECT
         * OP_INVOKE_STATIC
         * OP_INVOKE_INTERFACE
         */
        override fun visitMethodStmt(op: Op?, args: IntArray?, method: Method) {
            if (op != Op.INVOKE_VIRTUAL) {
                super.visitMethodStmt(op, args, method)
                return
            }
            val owner = method.owner
            val name = method.name

            policyList.filter {
                owner == it.className
            }.filter {
                "all" == it.secondName || name == it.secondName
            }.forEach {
                val array = Context.outputJson[it.key] as JSONArray
                array.put(
                    "文件 ${file.name} 中的 $currentClassName.$parentMethod() 方法调用了" +
                            " ${owner.substringAfterLast('/')}.$name()，${it.desc}"
                )
            }
            super.visitMethodStmt(op, args, method)
        }

    }
}