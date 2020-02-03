# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.0] - 2020-02-03

- support for scala 2.11 and 2.12
- nested collections support
- enhanced map support

## [1.1.0] - 2020-01-13

- added support for `scala.collection.Seq`
- updated velocypack version 2.1.0
- changed velocypack dependency scope to `provided`

## [1.0.3] - 2018-08-19

- added java-velocypack version 1.4.2

## [1.0.2] - 2018-02-26

### Added

- added support for `scala.math.BigInt` and `scala.math.BigDecimal`

### Changed

- use a parameterized deserializer for `List`

## [1.0.1] - 2017-06-19

### Added

- added support for deserializing `null` values into `None`

[unreleased]: https://github.com/arangodb/spring-data/compare/1.0.2...HEAD
[1.0.2]: https://github.com/arangodb/spring-data/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/arangodb/spring-data/compare/1.0.0...1.0.1
