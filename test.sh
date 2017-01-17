#!/bin/bash

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

API_DIR=$DIR/src/main/jniLibs
STUB_DIR=$DIR/src/test/jniLibs/nabto_client_api_jni_stub

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
    export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$DIR/cmake/build
    pushd $DIR
    gradle clean --info || exit 1
    gradle test --info || exit 1
    popd
}

function test-java {
    # build stubbed native library and JARs
    pushd $DIR/cmake
    rm -r build; mkdir build; cd build
    cmake -DBUILD_JARS=ON .. || exit 1
    make || exit 1
    popd
    
    ## Run tests
    BUILD_DIR=$DIR/cmake/build
    JAR_DIR=$DIR/libs
    java -Djava.library.path=$BUILD_DIR \
         -cp $JAR_DIR/junit-4.12.jar:$JAR_DIR/hamcrest-core-1.3.jar:$BUILD_DIR/NabtoClientApiWrapper.jar:$BUILD_DIR/NabtoClientApiWrapperTest.jar \
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
