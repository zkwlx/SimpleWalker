package com.zhihu.walker

data class Policy(
    val instruct: String,
    val className: String,
    val secondName: String,
    val desc: String,
    val key: String = "$className $desc"
)