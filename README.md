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

To include the test permanently into your target project, add the following dependency:

```
de.tautenhahn.spelling:TypoHunter:0.1
```
For Gradle projects, make sure `mavenLoval` repository is enabled. Add the test class `de.tautenhahn.spelling.checkSpelling`
to your tests.

To check a project once, just use the command line application. At the moment, it takes as single argument
the path to your project and will report its findings on standard out.

Do not hesitate to use this check and find some of your typos before others do!