# [Bld](https://github.com/rife2/rife2/wiki/What-Is-Bld) extension to generate ANTLR4 parsers

[![License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/java-17%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![bld](https://img.shields.io/badge/2.3.0-FA9052?label=bld&labelColor=2392FF)](https://rife2.com/bld)
[![Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.rife2.com%2Freleases%2Fcom%2Fuwyn%2Frife2%2Fbld-antlr4%2Fmaven-metadata.xml)](https://repo.rife2.com/#/releases/com/uwyn/rife2/bld-antlr4)
[![GitHub CI](https://github.com/rife2/bld-antlr4/actions/workflows/bld.yml/badge.svg)](https://github.com/rife2/bld-antlr4/actions/workflows/bld.yml)

A `bld` extension for generating Java sources from ANTLR4 parsers.

The complete documentation of `Antrl4Operation` can be found in its [javadocs](https://rife2.github.io/bld-antlr4/rife/bld/extension/Antlr4Operation.html).

This is an example usage where your ANTLR4 sources would be located at
`src/main/antlr` and the parsers generated into `build/generated`. The `compile`
command then uses an adapted `CompileOperation` to include the generated
sources into the main source directories.

```java
private final Antlr4Operation antlr4Operation = new Antlr4Operation();
private final CompileOperation compileOperation = new CompileOperation();

@BuildCommand(summary = "Generates the grammar Java sources")
public void generateGrammar()
throws Exception {
    antlr4Operation.executeOnce(() -> antlr4Operation
        .sourceDirectories(List.of(new File(srcMainDirectory(), "antlr")))
        .outputDirectory(new File(buildDirectory(), "generated"))
        // these options are specific to ANTLR4, please refer to the extension
        // documentation to learn more about these and others
        .visitor()
        .longMessages());
}

public void compile()
throws Exception {
    // always generate the latest grammar before compiling the sources
    generateGrammar();

    // include the generated grammar with the main sources
    compileOperation.executeOnce(() -> compileOperation
        .fromProject(this)
        .mainSourceDirectories(List.of(antlr4Operation.outputDirectory())));
}
```
