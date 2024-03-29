![ArangoDB-Logo](https://www.arangodb.com/docs/assets/arangodb_logo_2016_inverted.png)

# [DEPRECATED]: `velocypack-module-scala` has been deprecated.

Please consider using [jackson-dataformat-velocypack](https://github.com/arangodb/jackson-dataformat-velocypack#scala)
instead. For usage in the ArangoDB Java driver, refer to the
official [serialization documentation](https://www.arangodb.com/docs/stable/drivers/java-reference-serialization.html).

---

# ArangoDB VelocyPack Java Module Scala

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.arangodb/velocypack-module-scala/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.arangodb/velocypack-module-scala)

Scala module for [Java VelocyPack](https://github.com/arangodb/java-velocypack).

Added support for:

- scala.Option
- scala.collection.immutable.List
- scala.collection.immutable.Map
- scala.math.BigInt
- scala.math.BigDecimal

## Maven

To add the dependency to your project with maven, add the following code to your pom.xml:

```XML
<!-- Scala 2.11 -->
<dependency>
    <groupId>com.arangodb</groupId>
    <artifactId>velocypack-module-scala_2.11</artifactId>
    <version>1.2.0</version>
</dependency>

        <!-- Scala 2.12 -->
<dependency>
<groupId>com.arangodb</groupId>
<artifactId>velocypack-module-scala_2.12</artifactId>
<version>1.2.0</version>
</dependency>
```

## Compile

```
mvn clean install
```

## Usage / registering module

```Scala
val vpack: VPack = new VPack.Builder().registerModule(new VPackScalaModule).build
```

## Learn more

- [ArangoDB](https://www.arangodb.com/)
- [Changelog](ChangeLog.md)
