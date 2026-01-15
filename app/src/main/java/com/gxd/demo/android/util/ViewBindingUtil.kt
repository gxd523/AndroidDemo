package com.gxd.demo.android.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

object ViewBindingUtil {
    fun <T : ViewBinding> getViewBindingClass(any: Any): Class<T>? {
        val superClass = any::class.java.genericSuperclass
        if (superClass is ParameterizedType) {
            for (typeArgument in superClass.actualTypeArguments) {
                if (typeArgument is Class<*> && ViewBinding::class.java in typeArgument.interfaces) {
                    @Suppress("UNCHECKED_CAST")
                    return typeArgument as Class<T>?
                }
            }
        }
        return null
    }
}

fun <T : ViewBinding> Class<T>.callInflate(inflater: LayoutInflater, parent: ViewGroup? = null, attachToParent: Boolean? = null): T? {
    val parameterTypes = attachToParent?.let {
        arrayOf(LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    } ?: arrayOf(LayoutInflater::class.java, ViewGroup::class.java)

    val args = attachToParent?.let { arrayOf(inflater, parent, attachToParent) } ?: arrayOf(inflater, parent)

    @Suppress("UNCHECKED_CAST")
    return getMethod("inflate", *parameterTypes).invoke(null, *args) as T?
}