
package com.bradcypert.mox

import kotlin.reflect.KClass
import java.lang.reflect.Proxy
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.HashSet





object Mox {
    interface MoxMarker

    fun<T> stub(mock: T, method: String, fn: () -> Any) {

    }

    fun<T : Any> isMock(mock: T): Boolean {
        return mock::class.java.interfaces.filter { it.name == MoxMarker::class.java.name }.isNotEmpty()
    }

    internal class DynamicInvocationHandler : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
            return Any()
        }
    }
}

fun<T : Any> KClass<T>.mock(): Any {
    return Proxy.newProxyInstance(
            java.classLoader,
            getInterfacesForObject(this) + Mox.MoxMarker::class.java,
            Mox.DynamicInvocationHandler())
}

internal fun getInterfacesForObject(`object`: Any): Array<Class<*>> {
    val interfaceSet = HashSet<Class<*>>()

    var objectClass: Class<*> = `object`.javaClass
    while (Any::class.java != objectClass) {
        val classInterfaces = objectClass.interfaces
        classInterfaces.forEach {
            interfaceSet.add(it)
        }
        objectClass = objectClass.superclass
    }

    return interfaceSet.toTypedArray()
}
