# Release History

## 2.0.0

This release is a major refactor to support the new features.

New Features:
* `Lexer` and `Formatter` classes.
* Low-level Python API exposed as `Pygments`. (see below)

Breaking Changes:
* Existing `Pygments` renamed to `PygmentsContext`.
* `Pygments.newBuilder` renamed to `PygmentsContext.newContext`.
* New class `ClassNotFoundException` is thrown instead of generic
  `IllegalArgumentException` when lexer or formatter creation fails.

## 1.0.0

Initial release.
