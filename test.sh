#!/bin/bash

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

API_DIR=$DIR/src/main/jniLibs
STUB_DIR=$DIR/src/test/jniLibs/nabto_client_api_jni_stub
LIB_PATH=$DIR/cmake/build

function help {
    echo "Test the Android Client API wrapper against a stubbed native API."
    echo "options:"
    echo "  android -> test the Android Client API (default)"
    echo "  java    -> test the Java Client API only"
}

function test-android {
    # build stubbed native library
    pushd $DIR/cmake
    rm -r build; mkdir build; cd build
    cmake .. || exit 1
    make || exit 1
    popd

    ## Run tests
    echo "Using java.library.path=$LIB_PATH"
    pushd $DIR
    gradle clean --info || exit 1
    gradle -Djava.library.path=$LIB_PATH test --info  || exit 1
    popd
}

function test-java {
    JAR_DIR=$DIR/libs

    # build stubbed native library and JARs
    pushd $DIR/cmake
    rm -rf build; mkdir build; cd build
    cmake -DBUILD_JARS=ON .. || exit 1
    make || exit 1
    mv java-wrapper/*.jar $JAR_DIR/ || exit 1
    popd
    
    ## Run tests
    JNI_DIR=$DIR/cmake/build
    JAR_DIR=$DIR/libs
    java -Djava.library.path=$JNI_DIR \
         -cp $JAR_DIR/junit-4.12.jar:$JAR_DIR/hamcrest-core-1.3.jar:$JAR_DIR/NabtoClientApiWrapper.jar:$JAR_DIR/NabtoClientApiWrapperTest.jar \
         org.junit.runner.JUnitCore com.nabto.api.NabtoCApiWrapperTest || exit 1
}

case $1 in
    "")
        test-android
        ;;
    "android")
        test-android
        ;;
    "java")
        test-java
        ;;
    *)
        help
        ;;
esac
