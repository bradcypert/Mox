/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.bradcypert.mox

import kotlin.test.Test
import kotlin.test.assertTrue

data class User(val name: String)

interface Repo<T> {
    fun create(item: T)
    fun read(id: Int): T
    fun update(id: Int, item: T)
    fun delete(id: Int)
    fun delete(item: T)
}

class UserRepo : Repo<User> {
    override fun create(item: User) {
        throw Exception("CALLED ACTUAL CREATE")
    }

    override fun read(id: Int): User {
        throw Exception("CALLED ACTUAL READ")
    }

    override fun update(id: Int, item: User) {
        throw Exception("CALLED ACTUAL UPDATE")
    }

    override fun delete(id: Int) {
        throw Exception("CALLED ACTUAL DELETE ID")
    }

    override fun delete(item: User) {
        throw Exception("CALLED ACTUAL DELETE ITEM")
    }
}

class ViewModel(val repo: Repo<User>) {
    fun getUser(id: Int): User {
        return repo.read(id)
    }
}

class MoxTest {
    @Test fun testSomeLibraryMethod() {
        val classUnderTest = UserRepo::class.mock()
    }

    @Test fun isMock() {
        val classUnderTest = UserRepo::class.mock()
        assertTrue(Mox.isMock(classUnderTest))
    }
}