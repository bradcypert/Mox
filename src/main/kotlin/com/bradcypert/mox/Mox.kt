
package com.bradcypert.mox

import kotlin.reflect.KClass
import java.lang.reflect.Proxy
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method


class Mox {
    fun someLibraryMethod(): Boolean {
        return true
    }
}

class DynamicInvocationHandler : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
       return Any()
    }
}

fun<T : Any> KClass<T>.mock(): T {
    val proxyInstance = Proxy.newProxyInstance(
            this.java.classLoader,
            arrayOf<Class<*>>(this.java),
            DynamicInvocationHandler()) as T

    return proxyInstance
}
