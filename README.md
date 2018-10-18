TypoHunter
==========

This is a test to find typos in documents which are not texts in any natural language like program source codes.
The massive use of abbreviations makes it awkward to use a traditional spell checker for those files.

Working with black lists, this test can not assure correct spelling, but
- produces almost no false positives
- can work even in documents with mixed language
- has still found lots of typos in each project I ever ran it on

Usage
-----

To find misspelled German or English words, call

```
java -jar TypoHunter-0.1.jar [Path]
```
where optional Path parameter denotes the base directory of the project to check.

To use the tool as permanent unit test, add `de.tautenhahn.spelling.TestSpelling` to your test suite.

** Coming ~~soon~~ when requested:**

- pom, Repository
- more potential typos
- configurable file filter
- configurable white list
- support for other languages?