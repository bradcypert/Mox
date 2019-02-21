# Mox
- No Bytecode Manipulation
- No massive library (even for test dependencies)
- Yes to best practices.

That's Mox. A mocking library/pattern that does what it says it does.

**Note**: The reason this works is because you use interfaces properly. If you do not use interfaces properly, you're going to want a library like Mockito. If you do use interfaces properly, you can use this extremely lightweight, non-bytecode-manipulating library to help with quick and easy mocking/stubbing.

#### Are we there yet?
- [x] Mocking Creates a Proxy Class
- [x] Stub a mock to run a function
- [ ] Specify return value when a function is called
- [ ] Metadata regarding function calls (nice, but should be achievable with Stubs)
- [ ] Retaining the generic when proxying an interface that uses a generic
- [ ] More elaborate tests

#### Can you use Mox?
Yes. Although not fully feature, it does provide value as is and most desired behavior for mocks can be accomplished with `Mox.stub`. Do be aware that the API contract is subject to change (and likely will) as I find simpler/more efficient ways to handle the objectives of this project.
