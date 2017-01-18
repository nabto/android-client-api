# Build the JARs containing the Nabto Client API Java wrapper and its unit tests.

find_package(Java 1.5)

if (JAVA_FOUND)
  include(UseJava)

  set(JAVA_API_DIR      ${CLIENT_API_ROOT}/src/main/java/com/nabto/api)
  set(JAVA_API_TEST_DIR ${CLIENT_API_ROOT}/src/test/java/com/nabto/api)
  set(LIBS_DIR          ${CLIENT_API_ROOT}/libs)
  
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
        
  install_jar(NabtoClientApiWrapper jar)
  install_jar(NabtoClientApiWrapperTest jar)
  
  install(FILES ${LIBS_DIR}/hamcrest-core-1.3.jar ${LIBS_DIR}/junit-4.12.jar DESTINATION jar)
endif()
