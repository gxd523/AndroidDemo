package com.gxd.demo.android

import com.gxd.demo.android.util.tosByteArray
import java.io.File

/**
 * 测试「ClassLoader」加载一个「class文件」
 */
class MyClassLoader : ClassLoader() {
    override fun findClass(name: String?): Class<*>? {
        val fileName = name?.split(".")?.last() ?: return super.findClass(name)
        val classFile = File("$fileName.class")
        if (!classFile.exists()) return super.findClass(name)

        val inputStream = classFile.inputStream()
        val classBytes = inputStream.tosByteArray()
        return defineClass(name, classBytes, 0, classBytes.size)
    }
}

fun main() {
    println(System.getProperty("user.dir"))
    val classLoader = MyClassLoader()
    val buildConfigClass = classLoader.loadClass("MyClass")
    val buildConfig = buildConfigClass.getDeclaredConstructor().newInstance()
    val applicationIdField = buildConfigClass.getField("APPLICATION_ID")
    val applicationId = applicationIdField.get(buildConfig)
    println("applicationId = $applicationId")
}