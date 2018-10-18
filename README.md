TypoHunter
==========

This is a test to find typos in documents which are not texts in any natural language like program source codes.
The massive use of abbreviations makes it awkward to use a traditional spell checker for those files.

Working with black lists, this test can not assure correct spelling, but
- produces almost no false positives
- can work even in documents / file trees with mixed language
- has still found lots of typos in each project I ever ran it on

Currently, it finds typical English and German typos.

Usage
-----

Until the jar is available on any public repository, checkout this project and call `gradle publishToMavenLocal`.

**As application**

To find misspelled German or English words, call

```
java -jar TypoHunter-0.1.jar [Path]
```
where optional Path parameter denotes the base directory of the project to check.

**As JUnit test**

To include the test permanently into your target project, add the following dependency:

```
de.tautenhahn.spelling:TypoHunter:0.1
```
For Gradle projects, make sure `mavenLoval` repository is enabled. Add the test class `de.tautenhahn.spelling.checkSpelling`
to your tests.

Remember, having read so far, you do no longer have an excuse to have any of the most usual typos in your code!

** Coming ~~soon~~ when requested:**

- pom, Repository
- more potential typos
- configurable file filter
- configurable white list
- support for other languages?
