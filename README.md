> **Warning**
> Deprecation notice: This SDK is for Nabto 4/Micro (uNabto). For new projects, the next generation Nabto 5/Edge should be used instead. Read about the differences [here](https://docs.nabto.com/developer/guides/concepts/overview/edge-vs-micro.html). Nabto 5/Edge also provides an [Android SDK](https://docs.nabto.com/developer/platforms/android/intro.html) (with Kotlin extensions).

# Android Client API

Legacy [Nabto 4/Micro](https://www.nabto.com) client API for Android.

Nabto provides a full communication infrastructure to allow direct, encrypted communication between clients and IoT devices - the Nabto communication platform. The platform supports direct peer-to-peer connectivity through NAT traversal.

## Installation

The latest .aar file is currently only available as [direct download](https://downloads.nabto.com/assets/nabto-android-client/1.6.2/nabto-api-release.aar) so install manually in your project (or setup a local Maven repository).

## Note about version identifiers

The version information returned by `nabto.versionString` is the core Nabto Client SDK version - _not_ the version of the Android wrapper (the component described in this document). See the release notes for the individual Android wrapper version to see the Nabto Client SDK core version wrapped.


### Build

In case you want to build the Android Client API library yourself, follow these steps:

#### 1. Download native libraries and resources
Obtain the Nabto Client SDK resources and native libraries by contact Nabto Support (legacy product not available for public download). Copy the libraries to *android-client-api/src/main/* in order to get the following structure:

```
src
└── main
    ├── assets
    │   └── share
    │       └── nabto
    │           ├── configuration
    │           ├── roots
    │           ├── schemas
    │           ├── skins
    │           └── users
    ├── java
    ├── jniLibs
    │   ├── armeabi
    │   │   └── libnabto_client_api_jni.so
    │   ├── armeabi-v7a
    │   │   └── libnabto_client_api_jni.so
    │   └── x86
    │       └── libnabto_client_api_jni.so
    └── res

```

#### 2. Run tests (optional)
```
# ./test.sh
```
*For more details see section "Test".*
#### 3. Build
```
# gradle build -x test
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
