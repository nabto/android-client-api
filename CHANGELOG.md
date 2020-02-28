# Change Log

All notable changes to this project will be documented in this file. This projects changelog started
with version [1.3.0] 2017-09-25 for change logs prior to this date contact Nabto and/or look in git
history.

The format is based on [Keep a Changelog](http://keepachangelog.com/)

Guide: always keep an unreleased section which keeps track of current
changes. When a release is made the unreleased section is renamed to
the release and a new unreleased section is added.

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
