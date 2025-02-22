package com.gxd.demo.android.util

import android.content.Context
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Array

/**
 * 「热修复」、「插件化」示例，「插件化」如何不合并「classLoader」就得通过
 * 将「plugin.apk」的「dex」转为「classLoader」
 * 并获取「element数组」
 * 合并到应用的「dexPathList」的「element数组」中
 * 最终与应用的「dex」合并
 */
@Throws(ClassNotFoundException::class, NoSuchFieldException::class, IllegalAccessException::class)
fun Context.mergeDex(pluginFileName: String = "plugin.apk") {
    val pluginApk = File(getExternalFilesDir(null), pluginFileName)
    if (!pluginApk.exists()) {
        FileOutputStream(pluginApk).use { outputStream ->
            // 「Android 14」要求动态加载的文件标记为只读
            // https://developer.android.com/about/versions/14/behavior-changes-14?hl=zh-cn#safer-dynamic-code-loading
            pluginApk.setReadOnly()
            assets.open(pluginFileName).use { inputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            }
        }
    }

    val pluginClassLoader: BaseDexClassLoader = DexClassLoader(
        pluginApk.absolutePath,
        getExternalFilesDir("apk_dex")!!.absolutePath,
        null,
        classLoader
    )

    // 获取获取「应用」和「plugin」的「dexPathList对象」
    val pathListFiled = BaseDexClassLoader::class.java.getDeclaredField("pathList")
    pathListFiled.isAccessible = true

    val pathList = pathListFiled.get(classLoader)
    val pluginPathList = pathListFiled.get(pluginClassLoader)

    // 获取获取「应用」和「plugin」的「element数组对象」
    val dexPathListClass = Class.forName("dalvik.system.DexPathList")
    val dexElementsField = dexPathListClass.getDeclaredField("dexElements")
    dexElementsField.isAccessible = true

    val dexElements = dexElementsField.get(pathList) ?: throw Exception("get dexElements failed")
    val pluginDexElements = dexElementsField.get(pluginPathList) ?: throw Exception("get pluginDexElements failed")

    // 合并「应用」和「plugin」的「element数组」
    val dexElementsSize = Array.getLength(dexElements)
    val pluginDexElementsSize = Array.getLength(pluginDexElements)
    val totalDexElementsSize = pluginDexElementsSize + dexElementsSize

    val elementClass = dexElements.javaClass.componentType ?: throw Exception("get elementClass failed")
    val mergeElementArray = Array.newInstance(elementClass, totalDexElementsSize)

    for (i in 0 until totalDexElementsSize) {
        val elementValue = if (i < pluginDexElementsSize) { // 先把「plugin」的「element元素」放入「新element数组」，顺序不能错!!!
            Array.get(pluginDexElements, i)
        } else {// 再放「应用」的「element元素」，顺序不能错!!!
            Array.get(dexElements, i - pluginDexElementsSize)
        }
        Array.set(mergeElementArray, i, elementValue)
    }

    // 最后将「新的element数组」替换「应用dexPathList」中的「element数组」
    dexElementsField.set(pathList, mergeElementArray)
}