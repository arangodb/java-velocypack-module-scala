
![ArangoDB-Logo](https://docs.arangodb.com/assets/arangodb_logo_2016_inverted.png)

# ArangoDB VelocyPack Java Module Scala

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.arangodb/velocypack-module-scala/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.arangodb/velocypack-module-scala)


Scala module for [Java VelocyPack](https://github.com/arangodb/java-velocypack).

Added support for:
* scala.Option
* scala.collection.immutable.List
* scala.collection.immutable.Map
* scala.math.BigInt
* scala.math.BigDecimal


## Maven

To add the dependency to your project with maven, add the following code to your pom.xml:

```XML
<dependencies>
  <dependency>
    <groupId>com.arangodb</groupId>
    <artifactId>velocypack-module-scala</artifactId>
    <version>1.0.2</version>
  </dependency>
</dependencies>
```

If you want to test with a snapshot version (e.g. 1.0.0-SNAPSHOT), add the staging repository of oss.sonatype.org to your pom.xml:

```XML
<repositories>
  <repository>
    <id>arangodb-snapshots</id>
    <url>https://oss.sonatype.org/content/groups/staging</url>
  </repository>
</repositories>
```

## Compile

```
mvn clean install
```

## Usage / registering module

``` Scala
val vpack: VPack = new VPack.Builder().registerModule(new VPackScalaModule).build
``` 