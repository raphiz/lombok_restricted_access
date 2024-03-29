# What's this?
The idea is to build something like [Kohsuke Kawaguchi's  Custom Access Modifier](http://www.kohsuke.org/access-modifier/) 
as a custom [lombok](http://projectlombok.org/) handler. When using lombok, no Maven-Plugins or any other build magic is required - just
adding the dependencies and it works.

You annotate the methods that shall not be called outside of your Framework with the RestrictedAccess annotation.
At compile time, lombok's magic injects a check if the caller of the method/constructor is in the allowed package.
If not, an RuntimeException is thrown.

```java
   package my.private.api.internal;
   public class PrivateAPI {
    @RestrictedAccess("my.private.api")
    public PrivateAPI() {
        // DO private stuff
    }
}
```

```java
    package third.party.app;
    ...
       new PrivateApi().doStuff()
    ...
    }
```

A working example can be found in the sub project *example_usage*

**This is only a proof of concept. Production usage is not recommended!**

# What's next?
* Add support for Eclipse
* Add support for delombok
* Get rid of the compiler warnings (```warning: [options] bootstrap class path not set in conjunction with -source 1.6```)
* Provide a lot more tests
* Improve this README
* ...
* Upload to Maven Central

# Build the project

Open your terminal, change directory to the root of the project and run

    ./gradlew clean build -q

# License
[MIT](http://opensource.org/licenses/mit-license.php)
