# Mox
- No Bytecode Manipulation
- No massive library (even for test dependencies)
- Yes to best practices.

That's Mox. A mocking library/pattern that does what it says it does.

**Note**: The reason this works is because you use interfaces properly. If you do not use interfaces properly, you're going to want a library like Mockito. If you do use interfaces properly, you can use this extremely lightweight, non-bytecode-manipulating library to help with quick and easy mocking/stubbing.

#### Are we there yet?
- [x] Mocking Creates a Proxy Class
- [x] Stub a mock to run a function
- [x] Specify return value when a function is called
- [ ] Metadata regarding function calls (nice, but should be achievable with Stubs)
- [ ] Retaining the generic when proxying an interface that uses a generic
- [ ] More elaborate tests

#### Can you use Mox?
Yes. Although not fully feature, it does provide value as is and most desired behavior for mocks can be accomplished with `Mox.stub`. Do be aware that the API contract is subject to change (and likely will) as I find simpler/more efficient ways to handle the objectives of this project.

#### Give me a real-world example

```kotlin
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

class MoxTest {
    @Test fun stubsWork() {
        var isCalled = false
        val classUnderTest = UserRepo::class.mock() as Repo<*>
        Mox.stub(classUnderTest, classUnderTest::read) {
            isCalled = true
        }

        classUnderTest.read(1)

        assert(isCalled)
    }
}
```

This example is pulled directly from the tests! If you'd like to find more examples, check out the tests!
