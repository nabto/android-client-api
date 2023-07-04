cmake_minimum_required(VERSION 3.0)
project(nabto_client_api_jni)

option(BUILD_STUB "Build the stubbed Nabto Client API used for testing the wrapper." ON)
option(BUILD_JARS "Build the JARs containing the Nabto Client API Java wrapper and its unit tests." OFF)

set(CLIENT_API_ROOT ${CMAKE_CURRENT_SOURCE_DIR}/..)

find_package(Java COMPONENTS Development)
find_package(JNI)

if (BUILD_STUB AND NOT Java_Development_FOUND)
  message(Does not build test stub since java development tools was not found)
endif()


if(BUILD_STUB AND Java_Development_FOUND)
    # Build the stubbed Nabto Client API used for testing the wrapper.

    set(API_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../src/main/jniLibs)
    set(STUB_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../src/test/jniLibs)

    execute_process(
      COMMAND ${Java_JAVAC_EXECUTABLE} -h ${API_DIR} -classpath ${CMAKE_CURRENT_SOURCE_DIR}/../src/main/java ${CMAKE_CURRENT_SOURCE_DIR}/../src/main/java/com/nabto/api/NabtoCApiWrapper.java
      WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
    )

    execute_process(
      COMMAND ${Java_JAVAC_EXECUTABLE} -h ${STUB_DIR} -classpath ${CMAKE_CURRENT_SOURCE_DIR}/../src/test/java ${CMAKE_CURRENT_SOURCE_DIR}/../src/test/java/com/nabto/api/NabtoCApiWrapperStubController.java
      WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
    )

    set(NABTO_CLIENT_API_JNI_SRC_DIR    ${CLIENT_API_ROOT}/src/main/jniLibs)
    set(NABTO_CLIENT_API_STUB_SRC_DIR   ${CLIENT_API_ROOT}/src/test/jniLibs)

    include_directories(
      ${JNI_INCLUDE_DIRS}
      ${NABTO_CLIENT_API_JNI_SRC_DIR}
      ${NABTO_CLIENT_API_STUB_SRC_DIR}
    )

    set(SRC
      ${NABTO_CLIENT_API_JNI_SRC_DIR}/nabto_client_api_jni.cpp
      ${NABTO_CLIENT_API_STUB_SRC_DIR}/nabto_client_api_stub.cpp
      ${NABTO_CLIENT_API_STUB_SRC_DIR}/nabto_client_api_stub_controller_jni.cpp
    )

    add_library(${PROJECT_NAME} SHARED ${SRC})
    target_compile_features(${PROJECT_NAME} PRIVATE cxx_range_for)

endif()

if(BUILD_JARS)
    add_subdirectory(java-wrapper)
endif()
