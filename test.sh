#!/bin/bash

set -e

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

function clean {
    rm -rf cmake/build
    gradle clean --info || exit 1
}

# build stubbed native library
function make-stuff {
    pushd $DIR/cmake 
    if [ ! -d build ]; then
        mkdir build
        cd build
        cmake $1 .. || exit 1
        cd ..
    fi
    cd build
    make -j 8
    popd
}

function test-android {
    make-stuff

    ## Run tests
    echo "Using java.library.path=$LIB_PATH"
    pushd $DIR
    gradle -Djava.library.path=$LIB_PATH test --info  || exit 1
    popd
}

function test-java {
     make-stuff -DBUILD_JARS=ON

     JAR_DIR=$DIR/libs
     mv $DIR/cmake/build/java-wrapper/*.jar $JAR_DIR/ || exit 1
    
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

    "clean")
        clean
        ;;
    *)
        help
        ;;
esac
