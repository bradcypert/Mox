
package com.bradcypert.mox

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


object Mox {
    interface MoxMarker

    val impl = mutableMapOf<String, Map<String, (Array<Any>) -> Any>>()

    fun<T : Any> stub(mock: T, method: KFunction<*>, fn: (Array<Any>) -> Unit) {
        if (isMock(mock)) {
            val handler = Proxy.getInvocationHandler(mock) as DynamicInvocationHandler
            impl += mapOf(handler.uuid.toString() to mapOf(method.name to fn))
        }
    }

    fun<T : Any, X: Any> respond(mock: T, method: KFunction<*>, fn: (Array<Any>) -> X) {
        if (isMock(mock)) {
            val handler = Proxy.getInvocationHandler(mock) as DynamicInvocationHandler
            impl += mapOf(handler.uuid.toString() to mapOf(method.name to fn))
        }
    }

    fun<T: Any, X: Any> respond(mock: T, method: KFunction<*>, response: X) {
        if (isMock(mock)) {
            val handler = Proxy.getInvocationHandler(mock) as DynamicInvocationHandler
            impl += mapOf(handler.uuid.toString() to mapOf(method.name to fun(_: Array<Any>): X = response))
        }
    }

    fun<T : Any> isMock(mock: T): Boolean {
        return mock::class.java.interfaces.any { it.name == MoxMarker::class.java.name }
    }

    internal class DynamicInvocationHandler(val uuid: UUID) : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
            val arguments = args ?: arrayOf()
            return impl[uuid.toString()]?.get(method.name)?.invoke(arguments) as Any
        }
    }
}

fun<T : Any> KClass<T>.mock(): Any {
    return Proxy.newProxyInstance(
            java.classLoader,
            this.java.interfaces + Mox.MoxMarker::class.java,
            Mox.DynamicInvocationHandler(UUID.randomUUID()))
}
