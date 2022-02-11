<div align="center">
    <h3 align="center">JCore - Java library for GitHub workflow commands</h3>
    <p align="center">
        <a href="https://docs.katsute.dev/jcore">Documentation</a>
        •
        <a href="https://github.com/KatsuteDev/JCore/issues">Issues</a>
    </p>
</div>

<div align="center">
    <a href="https://github.com/KatsuteDev/JCore/actions/workflows/java_ci.yml"><img alt="Java CI" src="https://github.com/KatsuteDev/JCore/actions/workflows/java_ci.yml/badge.svg"></a>
    <a href="https://mvnrepository.com/artifact/dev.katsute/jcore"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/dev.katsute/jcore"></a>
    <a href="https://github.com/KatsuteDev/JCore/releases"><img alt="version" src="https://img.shields.io/github/v/release/KatsuteDev/JCore"></a>
    <a href="https://github.com/KatsuteDev/JCore/blob/main/LICENSE"><img alt="license" src="https://img.shields.io/github/license/KatsuteDev/JCore"></a>
</div>

## Overview

JCore supports most GitHub workflow commands.

 - Inputs
   ```java
   Workflow.getInput("input")
   ```
 - Logging
   ```java
   Workflow.info("info");
   Workflow.debug("debug");
   Workflow.notice("notice");
   Workflow.warning("warning");
   Workflow.error("error");
   ```
 - AnnotationProperties
   ```java
   new AnnotationProperties.Builder()
      .title("A title")
      .startColumn(1)
      .endColumn(2)
      .startLine(3)
      .endLine(4)
      .build()
   ```
 - Groups
   ```java
   Workflow.startGroup("my-group");
   ```
 - Secret masking
   ```java
   Workflow.addMask("secret value");
   ```

View all features in the [documentation](https://docs.katsute.dev/jcore)

## JUnit Integration

Add jCore to your JUnit test cases in order to see failures as annotations.

### ❌ without JCore

```java
Assertions.assertTrue(false);

Assumptions.assumeTrue(false);
```

 - No warning messages.
 - Requires reading Maven log to find warnings and errors.

<div align="center">
    <a href="https://github.com/KatsuteDev/JCore/actions/runs/1704996506">
        <img src="https://raw.githubusercontent.com/KatsuteDev/JCore/main/before.png">
    </a>
</div>

### ✔ with JCore

```java
Workflows.annotateTest(() -> Assertions.assertTrue(false));

Workflows.annotateTest(() -> Assumptions.assumeTrue(false));
```

 - Annotations show warnings and errors.
 - Full stack traces in annotations.
 - Direct links to line error.

<div align="center">
    <a href="https://github.com/KatsuteDev/JCore/actions/runs/1704996503">
        <img src="https://raw.githubusercontent.com/KatsuteDev/JCore/main/after.png">
    </a>
    <a href="https://github.com/KatsuteDev/JCore/blob/335e5c9d02912e789e04809b33c257193c1938a6/src/test/java/dev/katsute/jcore/SampleTests.java#L40">
        <img src="https://raw.githubusercontent.com/KatsuteDev/JCore/main/after.link.png">
    </a>
</div>

## Contributing

<!-- GitHub Copilot Disclaimer -->
<table>
    <img alt="GitHub Copilot" align="left" src="https://raw.githubusercontent.com/KatsuteDev/.github/main/profile/copilot-dark.png#gh-dark-mode-only" width="50"><img alt="GitHub Copilot" align="left" src="https://raw.githubusercontent.com/KatsuteDev/.github/main/profile/copilot-light.png#gh-light-mode-only" width="50">
    <p>GitHub Copilot is <b>strictly prohibited</b> on this repository.<br>Pulls using this will be rejected.</p>
</table>
<!-- GitHub Copilot Disclaimer -->

 - Found a bug? Post it in [issues](https://github.com/KatsuteDev/JCore/issues).
 - Want to further expand our project? [Fork](https://github.com/KatsuteDev/JCore/fork) this repository and submit a [pull request](https://github.com/KatsuteDev/JCore/pulls).

### License

This library is released under the [GNU General Public License (GPL) v2.0](https://github.com/KatsuteDev/JCore/blob/main/LICENSE).