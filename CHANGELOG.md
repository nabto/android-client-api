# Change Log

All notable changes to this project will be documented in this file. This projects changelog started
with version [1.3.0] 2017-09-25 for change logs prior to this date contact Nabto and/or look in git
history.

The format is based on [Keep a Changelog](http://keepachangelog.com/)

Guide: always keep an unreleased section which keeps track of current
changes. When a release is made the unreleased section is renamed to
the release and a new unreleased section is added.

## 1.7.4 Unreleased

## 1.7.3 2026-06-11

### Changed
 - 2026 Google Play compliance: bumped `compileSdkVersion`/`targetSdkVersion` to 36 (Android 16) and
   `minSdkVersion` to 21.
 - Upgraded toolchain: Android Gradle Plugin 8.13.0, Gradle 8.13.
 - Updated androidx test dependencies (`ext:junit` 1.2.1, `espresso-core` 3.6.1).
 - Updated Nabto Client Api JNI libraries to version v4.9.3

### Removed
 - Stale unused vendored jars (`libs/junit-4.12.jar`, `libs/hamcrest-core-1.3.jar`).

### Added
 - `LICENSE` file (Apache-2.0) and README note on preserving 16 KB native library alignment.

## 1.7.2 2025-09-02

### Changed
 - Updated CI workflow to use `upload-artifact@v4`.

### Added
 - Android 16 KB native library page-size support (16 KB-aligned `.so` libraries).
 - README now mentions the pre-built release `.aar` files and documents the git LFS
   dependency (helps users running into the "bad elf magic" error).

## 1.7.1 2023-10-04

### Changed
 - Removed file-exists check that prevented asset updates from being installed.

### Added
 - Updated README and added a test.

## 1.7.0 2023-09-03

### Changed
 - Updated Nabto Client Api JNI libraries.
 - Upgraded Gradle and made the code build with Java 17.
 - Moved JNI java and native code (and its tests) to reside primarily in the svn repository.
 - Replaced the legacy bintray/downloads.nabto.com distribution with GitHub releases.

### Added
 - GitHub Actions CI.
 - Committed the native `.so` libraries (via git LFS) so the repository is self-contained.

### Removed
 - Unused legacy build scripts.

## 1.6.2 2021-05-19

### Changed
 - Made NabtoAssetManager public to fix kotlin problems (fix ticket #2)

## 1.6.1 2020-03-10

### Changed
 - Bumped targetSdkVersion to comply with Google minimum requirements

## 1.6.0 2020-02-28

### Changed

- Added tunnelWait to NabtoApi: Block caller until tunnel is ready or closed

## 1.5.3 2019-08-22

### Changed

- Add x86_64 support (1.5.2 identical, but deployment trouble)


## 1.5.1 2018-11-22

### Changed

- Wrap Nabto Client SDK 4.4.0 (configurable stream windows), added missing PSK functions from Nabto Client SDK 4.3.0.

## 1.4.0 2018-10-17

### Changed

- Wrap native Nabto Client SDK 4.3.0, see https://downloads.nabto.com/assets/release-notes/4.3/release-notes.txt.

## 1.3.6 2018-02-19

### Changed
- Asset manager now installs bundle resources if available and ignores files not included in bundle

## 1.3.5 2018-02-15

### Changed
- Use SDK embedded resources where possible

## 1.3.4 2017-11-13

### Changed
- Meaningful state descriptions

## 1.3.3
Skipped due to deployment issue.

## 1.3.2 2017-10-10

### Changed
- Nabto Client SDK 4.1.12 with fix for wrong handling of unicode characters in certificate names (AMP-135)

## 1.3.1 2017-09-25

1.3.0 software, release bøvl caused version bump.

## 1.3.0 2017-09-25

### Added
- Added `setStaticResourceDir` to set a custom directory to hold resources (useful for custom config file).
- Changelog.

### Changed

### Breaking
