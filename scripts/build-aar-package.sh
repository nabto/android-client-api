#!/bin/bash
#
# Build android-client-api AAR package and optionally deploy to bintray
#
# Note: Pollutes local repo with artifacts downloaded into plugin dirs, so clone repo just for
# building and throw away afterwards.
#
# $0 <artifact download url prefix> <target dir> [<deploy {yes|no|deploy-only}>]"
#
# If $3 is set and not set to no, BINTRAY_USER and BINTRAY_API_KEY env variables must have been
# set with relevant bintray info.
#

set -e

if [ -z "$ANDROID_HOME" ]; then
    export ANDROID_HOME=~/Android/Sdk
fi

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

DOWNLOAD_URL_PREFIX=$1
TARGET_DIR=$2
if [ ! -z "$3" ] && [ "$3" != "no" ]; then
    DEPLOY_TO_BINTRAY=1
    if [ "$3" == "deploy-only" ]; then
        DEPLOY_ONLY=1
    fi
fi

BUILD_DIR=$DIR/..
LIB_DIR=$BUILD_DIR/src/main/jniLibs

function die() {
    echo $1
    exit 1
}

function usage() {
    die "$0 <artifact download url prefix> <target dir> [<deploy {true|false}>]"
}

function prep_dirs() {
    rm -rf $TARGET_DIR
    mkdir -p $TARGET_DIR
    mkdir -p $LIB_DIR/arm64-v8a
    mkdir -p $LIB_DIR/armeabi-v7a
    mkdir -p $LIB_DIR/x86
    mkdir -p $LIB_DIR/x86_64
}

function check_deploy_ready() {
    if [ -z "$DEPLOY_TO_BINTRAY" ]; then
        return
    fi
    if [ -z "$BINTRAY_USER" ]; then
        die "ERROR: BIiNTRAY_USER env variable must be set for bintray deployment"
    fi
    if [ -z "$BINTRAY_API_KEY" ]; then
        die "ERROR: BINTRAY_API_KEY env variable must be set for bintray deployment"
    fi
}

function download() {
    local platform=$1
    local output_path=$2

    local url="$DOWNLOAD_URL_PREFIX/nabto/${platform}/lib/libnabto_client_api_jni.so"
    local output_file="$LIB_DIR/$output_path/libnabto_client_api_jni.so"

    echo "Downloading release artifact from $url"
    curl -fs $url > $output_file || die "Download from '$url' to '$output_path' failed"
}

function test() {
    if [ ! -z "$DEPLOY_ONLY" ]; then
        return
    fi
    $BUILD_DIR/scripts/test.sh java
    $BUILD_DIR/scripts/test.sh android
}

function package() {
    if [ ! -z "$DEPLOY_ONLY" ]; then
        return
    fi
    download android-arm64 arm64-v8a
    download android-armv7 armeabi-v7a
    download android-x86 x86
        download android-x86_64 x86_64

    if [ -z "$DEPLOY_TO_BINTRAY" ]; then
        cd $BUILD_DIR
        gradle clean assembleRelease sourcesJar javadocJar --info --no-daemon
    else
        echo "Package is assembled as part of bintray upload"
    fi
}

function deploy() {
    if [ -z "$DEPLOY_TO_BINTRAY" ]; then
        return
    fi
    cd $BUILD_DIR
    gradle clean bintrayUpload
}

function archive() {
    mkdir -p $TARGET_DIR
    cp $BUILD_DIR/build/outputs/aar/nabto-api-release.aar $TARGET_DIR
    cp -r $BUILD_DIR/build/libs/nabto-api-*.jar $TARGET_DIR
}

if [ $# -lt 2 ]; then
    usage
fi

prep_dirs
check_deploy_ready
#test
package
deploy
archive

exit $?
