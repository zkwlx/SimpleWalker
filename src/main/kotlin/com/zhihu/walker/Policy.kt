package com.zhihu.walker

data class Policy(
    /**
     * class 的指令名，目前支持 invokevirtual、invokespecial、getstatic
     */
    val instruct: String,
    /**
     * 被调用的类名
     */
    val className: String,
    /**
     * 被调用的 [className] 的方法或属性名
     */
    val secondName: String,
    /**
     * 策略描述
     */
    val desc: String,

    val key: String = "$className $desc"
)