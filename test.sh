#!/bin/bash

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

API_DIR=$DIR/src/main/jniLibs
STUB_DIR=$DIR/src/test/jniLibs/nabto_client_api_jni_stub

function help {
    echo "options:"
    echo "  all   -> build + test (default)"
    echo "  build -> build stubbed native library"
    echo "  test  -> run unit tests"
}

function build {
    # create JNI header files from java classes
    javah -jni -classpath $DIR/src/main/java/ -d $API_DIR com.nabto.api.NabtoCApiWrapper || exit 1
    javah -jni -classpath $DIR/src/test/java/ -d $STUB_DIR com.nabto.api.NabtoCApiWrapperStubController || exit 1

    # build stubbed native library
    pushd $STUB_DIR
    rm -r build; mkdir build; cd build
    cmake .. || exit 1
    make || exit 1
    popd
}

function test {
    export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$STUB_DIR/build
    
    ## Need gradle installed!
    gradle clean --info || exit 1
    gradle test --info || exit 1
}

function all {
    build
    test
}

case $1 in
    "")
        all
        ;;
    "all")
        all
        ;;
    "build")
        build
        ;;
    "test")
        test
        ;;
    *)
        help
        ;;
esac
