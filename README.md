# Android Client API 

[Nabto ApS](http://nabto.com) client API for Android.

Nabto provides a full communication infrastructure to allow direct, encrypted communication between clients and IoT devices - the Nabto communication platform. The platform supports direct peer-to-peer connectivity through NAT traversal.

## Installation

The simplest way to use the Nabto client API for Android is by including the Apache Maven package from JCenter by adding the following to your project's `build.gradle`:

```java
repositories {
    jcenter()
}

dependencies {
    compile 'com.nabto.android:nabto-api:0.1'
}
```

In case you want to build the API yourself, follow these steps:

1. Download Nabto libraries and assets to *android-client-api/src/main/* (See "Source File Structure" section).
2. Open the project in Android Studio.
3. Build the project. 
4. Import the library from *android-client-api/build/outputs/aar/* into your project.

## Example

The simplest possible example of using the Android client API:
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
NabtoApi api = new NabtoApi(context);

// Start Nabto
api.setStaticResourceDir();
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

## Source File Structure

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

## Test

The native interface is tested using a stubbed Nabto library. The source files of the stub are located in `/home/cs/nabto/android-client-api/src/test/jniLibs/nabto_client_api_jni_stub`. A bash script is provided to automate building the stub and executing the tests.

```
./test.sh
```
