# oop-unit-tests
Repository for sharing unit tests for OOP course

# How to contribute with tests

## Git

1. If you don't have the repo on your machine:
    1. Fork the repository on Github
    2. Clone your repository into your machine
    3. Add remote upstream `git remote add upstream https://github.com/mhorod/oop-unit-tests`


2. Sync local repository with remote
    - If you haven't commited your changes:
        1. Switch to `main` by running `git checkout main`
        2. Execute `git pull upstream main` to sync changes
    - If you commited your changes rebase instead of merging:
        1. `git fetch`
        2. `git rebase origin/main`
        
3. Create branch for your changes and switch to it
    1. `git branch {branch_name}`
    2. `git checkout {branch_name}`

4. Add your tests to directory `oop-unit-tests/satori/{task}/`
    Don't create new directories there.

5. Make sure your tests compile by executing `bash check.sh`
6. Commit your changes and push them
7. Go to your repository on Github and create a pull request

## code style
This is recommended style guide for writing tests. While not necessary, following it will be highly appreciated.

### Classes
In single java project there can exist only one class of given name. In order to avoid name conflicts prefix your test class with name and surname., e.g. `class JohnDoeTests {}`

With `JUnit` you can group your tests in nested classes with `RunWith(@Enclosed.class)`

Example test with nested classes:
```java
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class ExampleTest
{
    public static class ExampleGroupTest 
    {
        @Test
        public void example_test_method() {}
    }
}
```

### Methods
Use `snake_case` for naming test methods and use names that convey expected behavior.
e.g. `size` is a bad name for a test while `size_of_new_list_is_zero` is better.

Try to make your tests concise and focused on single thing.
Test behavior rather than interface.
If you have many assertions consider splling the test into more functions.

# Running tests in IntelliJ

Put files with unit tests in directory `src/test/java/` in your solution project.

1. In toolbar click between `Build` and `Run` icons
2. `Edit Configurations`
3. `Add new configuration > JUnit`
4. `JUnit > Add new run configuration`
5. Select `Class` to run single test file
6. Select `All in directory` and select directory to run all tests
7. Press `Run` icon to run tests
