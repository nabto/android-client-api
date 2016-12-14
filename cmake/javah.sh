#!/bin/bash

# create JNI header files from java classes

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

API_DIR=$DIR/../src/main/jniLibs
STUB_DIR=$DIR/../src/test/jniLibs

javah -jni -classpath $DIR/../src/main/java/ -d $API_DIR com.nabto.api.NabtoCApiWrapper || exit 1
javah -jni -classpath $DIR/../src/test/java/ -d $STUB_DIR com.nabto.api.NabtoCApiWrapperStubController || exit 1
