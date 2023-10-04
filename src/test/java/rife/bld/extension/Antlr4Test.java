/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.extension;

import org.junit.jupiter.api.Test;
import rife.resources.ResourceFinderClasspath;
import rife.tools.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Antlr4Test {
    @Test
    void testInstantiation() {
        var operation = new Antlr4Operation();
        assertTrue(operation.arguments().isEmpty());
        assertTrue(operation.sourceDirectories().isEmpty());
        assertTrue(operation.sourceFiles().isEmpty());
        assertNull(operation.libDirectory());
        assertNull(operation.outputDirectory());
    }

    @Test
    void testGenerateGrammar()
    throws Exception {
        var tmp = Files.createTempDirectory("test").toFile();
        try {
            assertEquals("", FileUtils.generateDirectoryListing(tmp));
            new Antlr4Operation()
                .sourceFiles(List.of(new File(ResourceFinderClasspath.instance().getResource("ArrayInit.g4").toURI())))
                .outputDirectory(tmp)
                .execute();
            assertEquals("""
                /ArrayInit.interp
                /ArrayInit.tokens
                /ArrayInitBaseListener.java
                /ArrayInitLexer.interp
                /ArrayInitLexer.java
                /ArrayInitLexer.tokens
                /ArrayInitListener.java
                /ArrayInitParser.java""", FileUtils.generateDirectoryListing(tmp));
        } finally {
            FileUtils.deleteDirectory(tmp);
        }
    }
}
