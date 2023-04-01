/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.extension;

import org.antlr.v4.Tool;
import rife.bld.operations.AbstractOperation;

import java.io.File;
import java.util.*;

/**
 * Generates Java sources from ANTLR grammars.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.0
 */
public class Antlr4Operation extends AbstractOperation<Antlr4Operation> {
    private final List<String> arguments_ = new ArrayList<>();
    private final List<File> sourceDirectories_ = new ArrayList<>();
    private final List<File> sourceFiles_ = new ArrayList<>();
    private File libDirectory_ = null;
    private File outputDirectory_ = null;

    private Boolean listener_ = null;
    private Boolean visitor_ = null;

    /**
     * Performs the antlr operation.
     *
     * @since 1.0
     */
    public void execute()
    throws Exception {
        var sources = new ArrayList<String>();
        for (var dir : sourceDirectories_) {
            if (dir.exists() && dir.isDirectory()) {
                var files = dir.listFiles((dir1, name) -> name.endsWith(".g") || name.endsWith(".g4"));
                if (files != null) {
                    for (var file : files) {
                        sources.add(file.getAbsolutePath());
                    }
                }
            }
        }
        for (var file : sourceFiles_) {
            if (file.exists()) {
                sources.add(file.getAbsolutePath());
            }
        }

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("ERROR: no ANTLR grammars found");
        }

        var arguments = new ArrayList<>(arguments_);

        if (libDirectory_ != null) {
            arguments.add("-lib");
            arguments.add(libDirectory_.getAbsolutePath());
        }
        if (outputDirectory_ != null) {
            arguments.add("-o");
            arguments.add(outputDirectory_.getAbsolutePath());
        }
        if (listener_ != null) {
            if (listener_) {
                arguments.add("-listener");
            } else {
                arguments.add("-no-listener");
            }
        }

        if (visitor_ != null) {
            if (visitor_) {
                arguments.add("-visitor");
            } else {
                arguments.add("-no-visitor");
            }
        }
        arguments.addAll(sources);

        var argument_array = new String[arguments.size()];
        arguments.toArray(argument_array);
        new Tool(argument_array).processGrammarsOnCommandLine();
    }

    /**
     * Provides additional arguments for the antlr operation.
     * <p>
     * The following additional arguments are supported:
     * <table>
     * <tr><td><code>-atn</code></td><td>generate rule augmented transition network diagrams</td></tr>
     * <tr><td><code>-D&lt;option&gt;=value</code></td><td>set/override a grammar-level option</td></tr>
     * <tr><td><code>-Werror</code></td><td>treat warnings as errors"</td></tr>
     * <tr><td><code>-XdbgST</code></td><td>launch StringTemplate visualizer on generated code</td></tr>
     * <tr><td><code>-XdbgSTWait</code></td><td>wait for STViz to close before continuing</td></tr>
     * <tr><td><code>-Xforce-atn</code></td><td>use the ATN simulator for all predictions</td></tr>
     * <tr><td><code>-Xlog</code></td><td>dump lots of logging info to antlr-timestamp.log</td></tr>
     * <tr><td><code>-Xexact-output-dir</code></td><td>all output goes into -o dir regardless of paths/package</td></tr>
     * </table>
     *
     * @param arguments the additional arguments
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation arguments(String... arguments) {
        arguments_.addAll(Arrays.asList(arguments));
        return this;
    }

    /**
     * Provides additional arguments for the antlr operation.
     * <p>
     * See {@link #arguments(String...)} for details.
     *
     * @param arguments the additional arguments
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation arguments(List<String> arguments) {
        arguments_.addAll(arguments);
        return this;
    }

    /**
     * Provides the source directories that will be used for the antlr operation.
     *
     * @param directories the source directories
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation sourceDirectories(List<File> directories) {
        sourceDirectories_.addAll(directories);
        return this;
    }

    /**
     * Provides the source files that will be used for the antlr operation.
     *
     * @param files the source files
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation sourceFiles(List<File> files) {
        sourceFiles_.addAll(files);
        return this;
    }

    /**
     * Provides the output directory where all output is generated.
     *
     * @param directory the output directory
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation outputDirectory(File directory) {
        outputDirectory_ = directory;
        return this;
    }

    /**
     * Provides the location of grammars and tokens files.
     *
     * @param directory the lib directory
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation libDirectory(File directory) {
        libDirectory_ = directory;
        return this;
    }

    /**
     * Provides grammar file encoding; e.g., euc-jp.
     *
     * @param encoding the encoding
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation grammarEncoding(String encoding) {
        arguments("-encoding", encoding);
        return this;
    }

    /**
     * Specify output style for messages in antlr, gnu, vs2005.
     *
     * @param format the output styke
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation msgFormat(String format) {
        arguments("-message-format", format);
        return this;
    }

    /**
     * Show exception details when available for errors and warnings.
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation longMessages() {
        arguments("-long-messages");
        return this;
    }

    /**
     * Generate parse tree listener (default).
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation listener() {
        listener_ = true;
        return this;
    }

    /**
     * Don't generate parse tree listener.
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation noListener() {
        listener_ = false;
        return this;
    }

    /**
     * Generate parse tree visitor.
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation visitor() {
        visitor_ = true;
        return this;
    }

    /**
     * Don't generate parse tree visitor (default).
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation noVisitor() {
        visitor_ = false;
        return this;
    }

    /**
     * Specify a package/namespace for the generated code
     *
     * @param pkg the package name
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation pkg(String pkg) {
        arguments("-package", pkg);
        return this;
    }

    /**
     * Generate file dependencies.
     *
     * @return this operation instance
     * @since 1.0
     */
    public Antlr4Operation depend() {
        arguments("-depend");
        return this;
    }

    /**
     * Retrieves the list of arguments that will be used for the
     * antlr operation.
     * <p>
     * This is a modifiable list that can be retrieved and changed.
     *
     * @return the arguments
     * @since 1.0
     */
    public List<String> arguments() {
        return arguments_;
    }

    /**
     * Retrieves the list of source directories that will be used for the
     * antlr operation.
     * <p>
     * This is a modifiable list that can be retrieved and changed.
     *
     * @return the source directories
     * @since 1.0
     */
    public List<File> sourceDirectories() {
        return sourceDirectories_;
    }

    /**
     * Retrieves the list of source files that will be used for the
     * antlr operation.
     * <p>
     * This is a modifiable list that can be retrieved and changed.
     *
     * @return the source files
     * @since 1.0
     */
    public List<File> sourceFiles() {
        return sourceFiles_;
    }

    /**
     * Retrieves the output directory where all output is generated.
     *
     * @return the output directory; or
     * * {@code null} if the directory wasn't specified
     * @since 1.0
     */
    public File outputDirectory() {
        return outputDirectory_;
    }

    /**
     * Retrieves the location of grammars and tokens files.
     *
     * @return the lib directory; or
     * {@code null} if the directory wasn't specified
     * @since 1.0
     */
    public File libDirectory() {
        return libDirectory_;
    }
}