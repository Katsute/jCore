<div align="center">
    <h1 align="center">jCore - Java library for GitHub workflow commands</h3>
    <p align="center">
        <a href="https://jCore.katsute.dev/">Documentation</a>
        •
        <a href="https://github.com/Katsute/jCore/issues">Issues</a>
    </p>
</div>

<div align="center">
    <a href="https://github.com/Katsute/jCore/actions/workflows/java_ci.yml"><img alt="Java CI" src="https://github.com/Katsute/jCore/workflows/Java%20CI/badge.svg"></a>
    <a href="https://github.com/Katsute/jCore/actions/workflows/codeql.yml"><img alt="CodeQL" src="https://github.com/Katsute/jCore/actions/workflows/codeql.yml/badge.svg"></a>
    <a href="https://github.com/Katsute/jCore/actions/workflows/release.yml"><img alt="Deploy" src="https://github.com/Katsute/jCore/actions/workflows/release.yml/badge.svg"></a>
    <a href="https://mvnrepository.com/artifact/com.kttdevelopment/jCore"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.kttdevelopment/jCore"></a>
    <a href="https://github.com/Katsute/jCore/releases"><img alt="version" src="https://img.shields.io/github/v/release/Katsute/jCore"></a>
    <a href="https://github.com/Katsute/jCore/blob/main/LICENSE"><img alt="license" src="https://img.shields.io/github/license/Katsute/jCore"></a>
</div>

## Overview

jCore supports most GitHub workflow commands.
 - Inputs
   ```java
   Workflow.getInput("input")
   ```
 - Logging
   ```java
   Workflow.info("info");
   Workflow.debug("debug");
   Workflow.warning("warning");
   Workflow.error("error");
   ```
 - Grouos
   ```java
   Workflow.startGroup("my-group");
   ```
 - Secret masking
   ```java
   Workflow.addMask("secret value");
   ```
View all features in the [documentation](https://jCore.katsute.dev/)

## JUnit Integration

Add jCore to your JUnit test cases in order to see failures as annotations.

### ❌ without jCore

```java
Assertions.assertTrue(false, "expected expression to be true");

Assumptions.assumeTrue(false, "expected expression to be true");
```

 - no warning messages
 - requires reading maven log to find warnings and errors

<div align="center">
    <a href="https://github.com/Katsute/jCore/blob/main/before.png">
        <img src="https://raw.githubusercontent.com/Katsute/jCore/main/before.png">
    </a>
</div>

### ✔ with jCore

```java
Assertions.assertTrue(false, Workflow.errorSupplier("expected expression to be true"));

Assumptions.assumeTrue(false, Workflow.warningSupplier("expected expression to be true"));
```

 - annotations show warnings and errors
 - full stack traces in annotations
 - direct links to line error

<div align="center">
    <a href="https://github.com/Katsute/jCore/blob/main/after.png">
        <img src="https://raw.githubusercontent.com/Katsute/jCore/main/after.png">
    </a>
    <a href="https://github.com/Katsute/jCore/blob/main/after.link.png">
        <img src="https://raw.githubusercontent.com/Katsute/jCore/main/after.link.png">
    </a>
</div>

## Contributing

 - Found a bug? Post it in [issues](https://github.com/Katsute/jCore/issues).
 - Want to further expand our project? [Fork](https://github.com/Katsute/jCore/fork) this repository and submit a [pull request](https://github.com/Katsute/jCore/pulls).
