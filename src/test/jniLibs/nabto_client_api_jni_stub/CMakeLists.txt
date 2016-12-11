cmake_minimum_required(VERSION 2.6)
project(nabto_client_api_jni)

find_package(JNI) # requires JAVA_HOME to be set

set(NABTO_CLIENT_API_JNI_SRC_DIR ../../../main/jniLibs)

include_directories(
  ${CMAKE_CURRENT_SOURCE_DIR}
  ${JNI_INCLUDE_DIRS}
  ${NABTO_CLIENT_API_JNI_SRC_DIR}
)

set(src
  ${NABTO_CLIENT_API_JNI_SRC_DIR}/nabto_client_api_jni.cpp
  nabto_client_api_stub.cpp
  nabto_client_api_stub_controller_jni.cpp
)

add_library(${PROJECT_NAME} SHARED ${src})
target_compile_features(${PROJECT_NAME} PRIVATE cxx_range_for)
