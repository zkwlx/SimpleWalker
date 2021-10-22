package com.zhihu.walker.dex_visitor

import com.zhihu.walker.Context
import com.zhihu.walker.Policy
import com.zhihu.walker.utils.Log
import com.zhihu.walker.utils.StrUtils
import d2j.api.Field
import d2j.api.Method
import d2j.api.reader.Op
import d2j.api.visitors.DexClassVisitor
import d2j.api.visitors.DexCodeVisitor
import d2j.api.visitors.DexMethodVisitor
import org.json.JSONArray
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9
import java.io.File

class GetStaticDexVisitor(val file: File, val className: String, cv: DexClassVisitor? = null) : DexClassVisitor(cv) {

    private val policyList: List<Policy> = Context.policyList.filter {
        it.instruct.toUpperCase() == "GETSTATIC"
    }

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
         * OP_IGETX a,b field
         * OP_IPUTX a,b field
         * OP_SGETX a field
         * OP_SPUTX a field
         */
        override fun visitFieldStmt(op: Op?, a: Int, b: Int, field: Field) {
            if (op != Op.SGET_OBJECT) {
                super.visitFieldStmt(op, a, b, field)
                return
            }

            // field.owner = Ljava/lang/Object;
            val owner = StrUtils.trimClassName(field.owner)
            val name = field.name

            policyList.filter {
                owner == it.className
            }.filter {
                "all" == it.secondName || name == it.secondName
            }.forEach {
                val array = Context.outputJson[it.key] as JSONArray
                array.put(
                    "文件 ${file.name} 中 $className.${parentMethod.name}() 方法使用了" +
                            " ${owner.substringAfterLast('/')}.$name，${it.desc}"
                )
            }
            super.visitFieldStmt(op, a, b, field)

        }

    }

}