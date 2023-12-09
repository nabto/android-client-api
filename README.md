> **Warning**
> Deprecation notice: This SDK is for Nabto 4/Micro (uNabto). For new projects, the next generation Nabto 5/Edge should be used instead. Read about the differences [here](https://docs.nabto.com/developer/guides/concepts/overview/edge-vs-micro.html). Nabto 5/Edge also provides an [Android SDK](https://docs.nabto.com/developer/platforms/android/intro.html) (with Kotlin extensions).

# Android Client API

Legacy [Nabto 4/Micro](https://www.nabto.com) client API for Android.

Nabto provides a full communication infrastructure to allow direct, encrypted communication between clients and IoT devices - the Nabto communication platform. The platform supports direct peer-to-peer connectivity through NAT traversal.

## Installation

The latest .aar file is currently only available as [direct download](https://github.com/nabto/android-client-api/releases) so install manually in your project (or setup a local Maven repository).

## Note about version identifiers

The version information returned by `nabto.versionString` is the core Nabto Client SDK version - _not_ the version of the Android wrapper (the component described in this document). See the release notes for the individual Android wrapper version to see the Nabto Client SDK core version wrapped.


### Build

> **Important**
> The project uses git Large File Storage (git lfs) so read this section carefully! Especially if you observe odd errors like `bad ELF magic: 76657273` - then you have not resolved the `lfs` references and are just installing the placeholder files.

First install git lfs by following [github's guide](https://docs.github.com/en/repositories/working-with-files/managing-large-files/installing-git-large-file-storage). It has detailed instructions for macOS, Linux and Windows.

After installation, confirm lfs is installed correctly:

```
$ git lfs install
Git LFS initialized.
```

If this looks ok, do a fresh clone of the github repo:

```
git clone https://github.com/nabto/android-client-api
```

Confirm that the placeholders are indeed replaced with binaries:

```
$ file ./src/main/jniLibs/arm64-v8a/libnabto_client_api_jni.so
./src/main/jniLibs/arm64-v8a/libnabto_client_api_jni.so: ELF 64-bit LSB shared object, ARM aarch64, version 1 (SYSV), dynamically linked, stripped
```

Finally, you can build the libraries:

```
./gradlew build
```

If you do not want to clone the repos again after installing git lfs, you can use `git lfs install`,
`git lfs fetch` and `git lfs checkout` to resolve the dependencies - but this is error prone and not
recommended over just starting with a fresh clone.

#### Run tests on android

```
./gradlew connectedAndroidTest
```

#### Updating jni libraries and associated files.

src/main/jniJava and src/main/jniLibs contains java and libraries which is built
elsewhere. If updates are needed for these files, the updates should be made in
the svn repository where these files reside.

strip .so files to save some space

```
llvm-strip-14 src/main/jniLibs/*/*.so
```

#### 4. Use the AAR library
Use the generated AAR library, found in *android-client-api/build/outputs/aar/*, in your projects.

## Usage Example

The simplest possible example of using the Android client API:
```java
import com.nabto.api.*;
```
```java
NabtoClient client = new NabtoClient(context);

// Start Nabto and login as guest
if(client.init("guest", "12345") == NabtoStatus.OK) {

    // Make a Nabto request to a device
    String url = "nabto://demo.nabto.net/wind_speed.json?";
    UrlResult result = client.fetchUrl(url);
    if(result.getStatus() == NabtoStatus.OK) {

        // Get the response
        String response = new String(result.getResult());
    }
}

// Stop Nabto
client.pause();
```

However, the *NabtoClient* does not support streaming and tunneling. Use the *NabtoApi* class for a full featured API. Same example:
```java
NabtoApi api = new NabtoApi(new NabtoAndroidAssetManager(this));

// Start Nabto
api.startup();

// Login as guest
Session session = api.openSession("guest", "123456");
if(session.getStatus() == NabtoStatus.OK) {

    // Make a Nabto request to a device
    String url = "nabto://demo.nabto.net/wind_speed.json?";
    UrlResult result = api.fetchUrl(url, session);
    if(result.getStatus() == NabtoStatus.OK) {

        // Get the response
        String response = new String(result.getResult());
    }
}

// Stop Nabto
api.closeSession(session);
api.shutdown();
```

## Test

The Android Client API is tested using a stubbed version of the Nabto Client SDK to exercise the JNI layer (used in NabtoCApiWrapperTest.java) and a mocked API at the Java level to exercise the higher level Java wrapper (used in NabtoApiTest.java). The source files of the stub are located in `/home/cs/nabto/android-client-api/src/test/jniLibs/`. A bash script is provided to automate building the stub and executing the tests.

*Important*: Make sure `$JAVA_HOME` is set.

You have two options:
### 1. Test the whole Android Client API (default)
```
# ./test.sh android
```
### 2. Test the Java wrapper only
```
# ./test.sh java
```
