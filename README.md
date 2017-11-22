# pygments-jython

Thread-safe Java wrapper for [Pygments][pygments] via [Jython][jython]

[![](https://api.bintray.com/packages/okorz001/maven/pygments-jython/images/download.svg)
](https://bintray.com/okorz001/maven/pygments-jython/_latestVersion)

## Releases

`pygments-java` is currently published on JCenter.

The following demonstrates how to use this library in a Gradle build:

```gradle
plugins {
    id 'java'
}

repositories {
    jcenter()
}

dependencies {
    implementation 'org.korz.pygments:pygments-jython:2.0.0'
}
```

## Usage

Complete documentation is available in Javadoc format.

The main entry point, `PygmentsContext`, highlights text and can be shared
between threads.

```java
// Create a PygmentsContext instance with a lexer and formatter.
PygmentsContext p = PygmentsContext.newContext()
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

### Sharing Lexers and Formatters

`Lexer` and `Formatter` instances are also thread-safe and it is possible to
share them between `PygmentsContext` instances.

```java
Lexer l = Lexer.byName("c")
    // Set some options if you want.
    .build();
Formatter f = Formatter.byName("html")
    // Set some options if you want.
    .build();
// ... later ...
PygmentsContext p = PygmentsContext.newContext()
    .withLexer(l)
    .withFormatter(f)
    .build();
```

Using `withLexer` or `withFormatter` allows creating `Lexer` or `Formatter`
instances with builders other than `Lexer.byName` and `Formatter.byName`.

### Low-Level API

The main classes in his library, `PygmentsContext`, `Lexer`, and `Formatter`,
are designed to be idiomatic and easy to use in Java. However, it may be easier
to use the Python API directly in languages with map literals and/or without
static type checking. The Python API is wrapped in a single class, `Pygments`,
that simply wraps the interaction with the Python runtime without adding any
additional abstractions.

```groovy
def code = "int main(void) { return 0; }"
def lexer = Pygments.getLexer("c", [:])
def formatter = Pygments.getFormatter("html", [linenos: "table"])
def highlighted = Pygments.highlight(code, lexer, formatter)
```

The idiomatic Java API is implemented directly on top of this low-level API.

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
