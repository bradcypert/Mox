
package com.bradcypert.mox

import kotlin.reflect.KClass
import java.lang.reflect.Proxy
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.HashSet
import kotlin.reflect.KFunction


object Mox {
    interface MoxMarker

    val impl = mutableMapOf<String, Map<String, (Array<Any>) -> Unit>>()

    fun<T> stub(mock: T, method: KFunction<*>, fn: (Array<Any>) -> Unit) {
        impl += mapOf("TODO UUID" to mapOf(method.name to fn))
    }

    fun<T : Any> isMock(mock: T): Boolean {
        return mock::class.java.interfaces.any { it.name == MoxMarker::class.java.name }
    }

    internal class DynamicInvocationHandler : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
            val arguments = args ?: arrayOf()
            impl["TODO UUID"]?.get(method.name)?.invoke(arguments)
            return Any()
        }
    }
}

fun<T : Any> KClass<T>.mock(): Any {
    return Proxy.newProxyInstance(
            java.classLoader,
            this.java.interfaces + Mox.MoxMarker::class.java,
            Mox.DynamicInvocationHandler())
}
