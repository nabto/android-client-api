
cmake_minimum_required(VERSION 2.6)

option(BUILD_STUB "Build the stubbed Nabto Client API used for testing the wrapper." ON)
option(BUILD_JARS "Build the JARs containing the Nabto Client API Java wrapper and its unit tests." OFF)

set(ROOT_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)

if(BUILD_STUB)
    # Build the stubbed Nabto Client API used for testing the wrapper.
    project(nabto_client_api_jni)

    find_package(JNI) # requires JAVA_HOME to be set
    
    execute_process(
        COMMAND ${CMAKE_CURRENT_SOURCE_DIR}/javah.sh 
        WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
    )    

    set(NABTO_CLIENT_API_JNI_SRC_DIR    ${ROOT_DIR}/src/main/jniLibs)
    set(NABTO_CLIENT_API_STUB_SRC_DIR   ${ROOT_DIR}/src/test/jniLibs)

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
    # Build the JARs containing the Nabto Client API Java wrapper and its unit tests.
    find_package(Java)
    include(UseJava)

    set(JAVA_API_DIR        ${ROOT_DIR}/src/main/java/com/nabto/api)
    set(JAVA_API_TEST_DIR   ${ROOT_DIR}/src/test/java/com/nabto/api)
    set(LIBS_DIR            ${ROOT_DIR}/libs)
      
    set(JAR_SRC
        ${JAVA_API_DIR}/NabtoCApiWrapper.java
        ${JAVA_API_DIR}/NabtoConnectionType.java
        ${JAVA_API_DIR}/NabtoStatus.java
        ${JAVA_API_DIR}/NabtoStreamOption.java
        ${JAVA_API_DIR}/NabtoTunnelState.java
        ${JAVA_API_DIR}/RpcResult.java
        ${JAVA_API_DIR}/Session.java
        ${JAVA_API_DIR}/Stream.java
        ${JAVA_API_DIR}/StreamReadResult.java
        ${JAVA_API_DIR}/TunnelInfoResult.java
        ${JAVA_API_DIR}/Tunnel.java
        ${JAVA_API_DIR}/UrlResult.java
    )

    set(JAR_TEST_SRC
        ${JAVA_API_TEST_DIR}/NabtoCApiWrapperStubController.java
        ${JAVA_API_TEST_DIR}/NabtoCApiWrapperStubControllerTest.java
        ${JAVA_API_TEST_DIR}/NabtoCApiWrapperTest.java
    )

    add_jar(NabtoClientApiWrapper "${JAR_SRC}")

    add_jar(NabtoClientApiWrapperTest "${JAR_TEST_SRC}"
        INCLUDE_JARS 
            NabtoClientApiWrapper 
            ${LIBS_DIR}/hamcrest-core-1.3.jar 
            ${LIBS_DIR}/junit-4.12.jar
    )
endif()
