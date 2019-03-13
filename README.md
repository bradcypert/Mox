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


### FAQ
#### What is a stub?
In Mox, a stub replaces a function implementation. If you have a `Repo` class that has a `delete` method that throws an exception, stubbing it would prevent that exception from being thrown an instead run a different function in it's place! Neat!

#### What if I want my stub to return something?
In Mox, we call that a response and a response is handled using the `respond` function. Here's an example `Mox.respond(classUnderTest, classUnderTest::read, User(name = "Brad"))`. If you need to act on parameters or other decision-making logic, you can instead provide a function as the last parameter instead of a value. For example:

```kotlin
Mox.respond(classUnderTest, classUnderTest::read) { args: Array<Any> -> User(name = "Brad") }
```
#### How do I check if a method is called?
```kotlin
var isCalled = false
val classUnderTest = UserRepo::class.mock() as Repo<*>
Mox.stub(classUnderTest, classUnderTest::read) {
    isCalled = true
}

classUnderTest.read(1)
assert(isCalled)
```

#### How do I return a specific value?
```kotlin
val classUnderTest = UserRepo::class.mock() as Repo<*>
Mox.respond(classUnderTest, classUnderTest::read, User(name = "Brad"))

val result = classUnderTest.read(1) as User

assert(result.name == "Brad")
```

#### How do I return a specific value when a certain argument is passed?
This isn't a great implementation currently, and is subject to change. If you must do this while I'm working out the API, you can do the following:

```kotlin
val classUnderTest = UserRepo::class.mock() as Repo<*>
Mox.respond(classUnderTest, classUnderTest::read) { args: Array<Any> ->
    if (args.first() == 1) {
      return@respond User(name = "Brad")
    } else {
      throw IllegalArgumentException("1 was not passed to the mocked function")
    }
}

val result = classUnderTest.read(1) as User
assert(result.name == "Brad")
```

#### How do I act upon my arguments when stubbing a function?
```kotlin
var isCalled = false
val classUnderTest = UserRepo::class.mock() as Repo<*>
Mox.stub(classUnderTest, classUnderTest::read) { args: Array<Any> ->
    if (args.first() == 1) {
        isCalled = true
    } else {
        throw IllegalArgumentException("1 was not passed to the mocked function")
    }
}
classUnderTest.read(1) as User
assert(isCalled)
```
