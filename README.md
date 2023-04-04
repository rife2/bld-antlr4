# [Bld](https://github.com/rife2/rife2/wiki/What-Is-Bld) extension to generate ANTLR4 parsers

[![License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/java-17%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Release](https://img.shields.io/github/release/rife2/bld-antrl4.svg)](https://github.com/rife2/bld-antlr4/releases/latest)
[![GitHub CI](https://github.com/rife2/bld-antrl4/actions/workflows/bld.yml/badge.svg)](https://github.com/rife2/bld-antrl4/actions/workflows/bld.yml)

An extension for generating Java sources from ANTLR4 parsers.

This is an example usage:

```java
private final Antlr4Operation antlr4Operation_ = new Antlr4Operation();
@BuildCommand
public void generateGrammar()
throws Exception {
    antlr4Operation_.executeOnce(o -> o
        .sourceDirectories(List.of(new File(srcMainDirectory(), "antlr")))
        .outputDirectory(new File(buildDirectory(), "generated"))
        .visitor()
        .longMessages());
}

// compileOperation_ is part of the main project
public void compile()
throws Exception {
    generateGrammar();
    compileOperation_.executeOnce(o -> o
        .fromProject(this)
        .mainSourceDirectories(List.of(antlr4Operation_.outputDirectory())));
}
```

The complete document of `Antrl4Operation` can be found in its [javadocs](https://rife2.github.io/bld-antlr4/rife/bld/extension/Antlr4Operation.html).