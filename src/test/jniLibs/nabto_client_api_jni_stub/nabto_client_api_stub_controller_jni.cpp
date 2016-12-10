#include "nabto_client_api_stub_controller.hpp"
#include "com_nabto_api_NabtoCApiWrapperStubController.h"

#include <jni.h>
#include <regex>
#include <iostream>
#include <sstream>

std::map<std::string, std::string> returnValues = {};
std::map<std::string, std::string> parameterValues = {};

void JNICALL Java_com_nabto_api_NabtoCApiWrapperStubController_setReturnValues(JNIEnv* env, 
                                                                               jclass thiz, 
                                                                               jstring jstr) 
{
    const char* str = (env)->GetStringUTFChars(jstr, NULL);
    std::string s(str);
    std::regex e("[^,]+\\=[^,]+");
    std::regex_iterator<std::string::iterator> rit ( s.begin(), s.end(), e );
    std::regex_iterator<std::string::iterator> rend;
    returnValues.clear();
    while (rit!=rend) {
        std::string pair = rit->str();
        std::string key = pair.substr(0, pair.find("="));
        std::string value = pair.substr(pair.find("=") + 1, pair.length());
        returnValues[key] = value;
        ++rit;
    }
    (env)->ReleaseStringUTFChars(jstr, str);
}

jstring JNICALL Java_com_nabto_api_NabtoCApiWrapperStubController_getParameterValues(JNIEnv* env, 
                                                                                     jclass thiz) 
{
    std::stringstream ss;
    std::string delim = "";
    for (auto it = parameterValues.begin(); it!=parameterValues.end(); ++it) {
        ss << delim << it->first << "=" << it->second;
        delim = ",";
    }    
    std::string s = ss.str();
    const char* str = s.c_str();
    return (env)->NewStringUTF(str);     
}
