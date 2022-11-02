<a href="http://artipie.com"><img src="https://www.artipie.com/logo.svg" width="64px" height="64px"/></a>

[![Join our Telegramm group](https://img.shields.io/badge/Join%20us-Telegram-blue?&logo=telegram&?link=http://right&link=http://t.me/artipie)](http://t.me/artipie)

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/artipie/cocoa-adapter)](http://www.rultor.com/p/artipie/cocoa-adapter)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Javadoc](http://www.javadoc.io/badge/com.artipie/cocoa-adapter.svg)](http://www.javadoc.io/doc/com.artipie/cocoa-adapter)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/com.artipie/cocoa-adapter/blob/master/LICENSE.txt)
[![codecov](https://codecov.io/gh/artipie/cocoa-adapter/branch/master/graph/badge.svg)](https://codecov.io/gh/artipie/cocoa-adapter)
[![Hits-of-Code](https://hitsofcode.com/github/artipie/cocoa-adapter)](https://hitsofcode.com/view/github/artipie/cocoa-adapter)
[![Maven Central](https://img.shields.io/maven-central/v/com.artipie/cocoa-adapter.svg)](https://maven-badges.herokuapp.com/maven-central/com.artipie/cocoa-adapter)
[![PDD status](http://www.0pdd.com/svg?name=artipie/cocoa-adapter)](http://www.0pdd.com/p?name=artipie/cocoa-adapter)

This Java library turns your binary [ASTO](https://github.com/artipie/asto)
storage (binary, Amazon S3 objects) into a CocoaPods repository. It provides [Cocoa PodSpec](https://cocoapods.org/) 
repository support for [Artipie](https://github.com/artipie) distribution and allows you to use 
`pod` client commands (such as `pod repo push` and `pod repo get`) to work with NuGet packages. 
Besides, cocoa-adapter can be used as a library to parse `.podspec` package specification and 
obtain package metadata.

Some valuable references:
- [Private Cocoa Pods Repository](https://guides.cocoapods.org/making/private-cocoapods.html)
- [Package specification Podspec or Spec](https://guides.cocoapods.org/making/specs-and-specs-repo.html)
- [Spec file full description](https://guides.cocoapods.org/syntax/podspec.html#specification) 

## Cocoa-adapter SDK API

Add dependency to `pom.xml`:

```xml
<dependency>
  <groupId>com.artipie</groupId>
  <artifactId>cocoa-adapter</artifactId>
  <version>[...]</version>
</dependency>
```

Then, you can it to obtain metadata from `.spec` files. Note, that `pospec` can bee represented 
either in Ruby or in json formats, we understand both:

```java
// create `Podspec` instance from `.spec` file input stream
final Podspec spec = Podspec.initiate(Files.newInputStream("path/to/spec"));

// call various methods of `Podspec` to obtain package metadata
final String name = spec.name();
```

If you have any question or suggestions, do not hesitate to [create an issue](https://github.com/artipie/cocoa-adapter/issues/new) or contact us in
[Telegram](https://t.me/artipie).  
Artipie [roadmap](https://github.com/orgs/artipie/projects/3).

## How to contribute

Please read [contributing rules](https://github.com/artipie/artipie/blob/master/CONTRIBUTING.md).

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice
```

To avoid build errors use Maven 3.2+.