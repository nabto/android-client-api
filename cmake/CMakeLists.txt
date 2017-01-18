cmake_minimum_required(VERSION 2.6)

option(BUILD_STUB "Build the stubbed Nabto Client API used for testing the wrapper." ON)
option(BUILD_JARS "Build the JARs containing the Nabto Client API Java wrapper and its unit tests." OFF)

set(CLIENT_API_ROOT ${CMAKE_CURRENT_SOURCE_DIR}/..)

if(BUILD_STUB)
    # Build the stubbed Nabto Client API used for testing the wrapper.
    project(nabto_client_api_jni)

    find_package(JNI) # requires JAVA_HOME to be set
    
    execute_process(
        COMMAND ${CMAKE_CURRENT_SOURCE_DIR}/javah.sh 
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
