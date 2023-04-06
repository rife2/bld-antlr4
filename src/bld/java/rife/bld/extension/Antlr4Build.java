/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.extension;

import rife.bld.Project;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishLicense;
import rife.bld.publish.PublishScm;

import java.io.File;
import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.JavadocOptions.DocLinkOption.*;

public class Antlr4Build extends Project {
    public Antlr4Build() {
        pkg = "rife.bld.extension";
        name = "Antlr4";
        version = version(0,9,7,"SNAPSHOT");

        javaRelease = 17;
        downloadSources = true;
        autoDownloadPurge = true;

        repositories = List.of(MAVEN_CENTRAL, repository("https://repo.rife2.com/snapshots"), RIFE2);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", version(1,5,18,"SNAPSHOT")))
            .include(dependency("org.antlr", "antlr4", version(4,11,1)));
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,2)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,2)));

        javadocOperation()
            .javadocOptions()
                .docLint(NO_MISSING)
                .link("https://rife2.github.io/rife2/");

        publishOperation()
            .repository(version.isSnapshot() ? repository("rife2-snapshots") : repository("rife2-releases"))
            .info()
                .groupId("com.uwyn.rife2")
                .artifactId("bld-antlr4")
                .description("bld extension for generating ANTLR4 grammars")
                .url("https://github.com/rife2/bld-antlr4")
                .developer(new PublishDeveloper()
                    .id("gbevin")
                    .name("Geert Bevin")
                    .email("gbevin@uwyn.com")
                    .url("https://github.com/gbevin"))
                .license(new PublishLicense()
                    .name("The Apache License, Version 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
                .scm(new PublishScm()
                    .connection("scm:git:https://github.com/rife2/bld-antlr4.git")
                    .developerConnection("scm:git:git@github.com:rife2/bld-antlr4.git")
                    .url("https://github.com/rife2/bld-antlr4"))
                .signKey(property("sign.key"))
                .signPassphrase(property("sign.passphrase"));
    }

    @Override
    public List<String> testClasspath() {
        var classpath = super.testClasspath();
        classpath.add(new File(srcTestDirectory(), "antlr").toString());
        return classpath;
    }

    public static void main(String[] args) {
        new Antlr4Build().start(args);
    }
}