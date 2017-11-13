# pygments-jython

Thread-safe Java wrapper for [Pygments][pygments] via [Jython][jython]

[![](https://api.bintray.com/packages/okorz001/maven/pygments-jython/images/download.svg)
](https://bintray.com/okorz001/maven/pygments-jython/_latestVersion)

## Usage

This library provides a single class, `Pygments`, that can be safely shared
between threads.

```java
// Create a Pygments instance with a lexer and formatter.
Pygments p = Pygments.newBuilder()
    .setLexer("c")
    .setFormatter("html")
    // Set some lexer or formatter options if you want.
    .setFormatterOption("linenos", "table")
    .setFormatterOption("noclasses", true)
    .build();

// Highlight code.
String code = "int main(void) { return 0; }"
String highlighted = p.highlight(code);
```

For more information about what lexers and formatters are available and the
options they support, please consult the official Pygments documentation:

* [Lexers](http://pygments.org/docs/lexers/)
* [Formatters](http://pygments.org/docs/formatters/)

## Releases

`pygments-java` is currently published on Bintray.

The following demonstrates how to use this library in a Gradle build:

```gradle
plugins {
    id 'java'
}

repositories {
    // for pygments-jython
    maven {
        url 'https://dl.bintray.com/okorz001/maven/'
    }
    // for transitive dependencies
    jcenter()
}

dependencies {
    implementation 'org.korz.pygments:pygments-jython:1.0.0'
}
```

## Why

[Pygments][pygments] is a best-in-class code highlighting library,
but it is written in [Python][python]. There are a couple options for using 
Pygments on the JVM:

1. Shell out to `pygmentize`, the Pygments CLI.

   Pros:
    * Well-documented, officially supported use case.

   Cons:
    * Slowest method since `pygmentize` does not have support for streaming
    multiple files.
    * Requires Python and Pygments to be installed in your environment, which
     may not be possible in all scenarios, e.g. deploying to a servlet 
     container.


2. Port Pygments from Python to Java (or other JVM language).

   Pros:
    * Native support.
    * JAR-only dependencies.

   Cons:
    * Ports are typically slower to add features.
    * Will have different bugs/features than original library.

    If you are interested in a Java port, see [Jygments][jygments].

3. Run Pygments in-process with an embedded Python runtime ([Jython][jython]). 
   This is feasible because Pygments has not external dependencies.

   Pros:
    * Get all the features of Pygments.
    * No external process management or overhead.
    * JAR-only dependencies.

   Cons:
    * Jython is a large dependency.
    * Jython does not currently support AOT compilation of Python sources, so
      calling Python from a Java is not transparent.
    * It is not readily apparent how to call Python from multiple threads in an
      efficient and safe manner. (`PythonInterpreter.exec` is not safe!)

This library chooses the embedded Python approach. It attempts to mitigate the
Jython complexities by abstracting everything in an idiomatic Java API.

[pygments]: http://pygments.org/
[jython]: http://www.jython.org/
[python]: https://www.python.org/
[jygments]: https://github.com/tliron/jygments/
