#ifndef _JNI_STRING_HPP
#define _JNI_STRING_HPP

#include <jni.h>

class jni_string {
 public:
    jni_string(JNIEnv *env, jstring javaString)
        : env_(env), javaString_(javaString)
    { 
        if(javaString_ == NULL) {
            nativeString_ = NULL;
        } else {
            nativeString_ = env_->GetStringUTFChars(javaString_, NULL);
        }
    }

    ~jni_string() 
    { 
        if (javaString_ != NULL) {
            env_->ReleaseStringUTFChars(javaString_, nativeString_);
        }
    }

    operator const char *() const { return nativeString_; }
    
 private:
    jni_string(const jni_string &x);
    jni_string &operator=(const jni_string &x);

    JNIEnv *env_;
    jstring javaString_;
    const char *nativeString_;
};

#endif // _JNI_STRING_HPP
