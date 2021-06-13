<div align="center">
    <h3 align="center">jCore - Java library for GitHub workflow commands</h3>
    <p align="center">
        <a href="https://jCore.katsute.dev/">Docs</a>
        •
        <a href="https://github.com/Katsute/jCore/issues">Issues</a>
    </p>
</div>

<div align="center">
    <a href="https://github.com/Katsute/jCore/actions/workflows/java_ci.yml"><img alt="Java CI" src="https://github.com/Katsute/jCore/workflows/Java%20CI/badge.svg"></a>
    <a href="https://github.com/Katsute/jCore/actions/workflows/codeql.yml"><img alt="CodeQL" src="https://github.com/Katsute/jCore/actions/workflows/codeql.yml/badge.svg"></a>
    <a href="https://github.com/Katsute/jCore/actions/workflows/release.yml"><img alt="Deploy" src="https://github.com/Katsute/jCore/workflows/Deploy/badge.svg"></a>
    <a href="https://mvnrepository.com/artifact/com.kttdevelopment/jCore"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.kttdevelopment/jCore"></a>
    <a href="https://github.com/Katsute/jCore/releases"><img alt="version" src="https://img.shields.io/github/v/release/Katsute/jCore"></a>
    <a href="https://github.com/Katsute/jCore/blob/main/LICENSE"><img alt="license" src="https://img.shields.io/github/license/Katsute/jCore"></a>
</div>

## Overview

jCore supports most GitHub workflow commands.
 - inputs
 - logging
 - groups
 - secret masking
 - matcher

## JUnit Integration

Add jCore to your JUnit test cases in order to see failures as annotations.

```java
Assertions.assertTrue(false, Workflow.errorSupplier("expected expression to be true"));

Assumptions.assumeTrue(false, Workflow.warningSupplier("expected expression to be true"));
```

|Before|After|
|:-:|:-:|
|![before](https://raw.githubusercontent.com/Katsute/jCore/main/before.png)|![after](https://raw.githubusercontent.com/Katsute/jCore/main/after.png)|

<hr>

- Found a bug? Post it in [issues](https://github.com/Katsute/jCore/issues).
- Want to further expand our project? [Fork](https://github.com/Katsute/jCore/fork) this repository and submit a [pull request](https://github.com/Katsute/jCore/pulls).
