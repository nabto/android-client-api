#ifndef _JNI_BYTE_ARRAY_HPP
#define _JNI_BYTE_ARRAY_HPP

#include <jni.h>

class jni_byte_array {
 public:
    jni_byte_array(JNIEnv *env, jbyteArray javaByteArray)
        : env_(env), javaByteArray_(javaByteArray)
    {
        if(javaByteArray == NULL) {
            nativeByteArray_ = NULL;
            length_ = 0;
        } else {
            nativeByteArray_ = env_->GetByteArrayElements(javaByteArray_, NULL);
            length_ = env->GetArrayLength(javaByteArray);
        }
    }

    ~jni_byte_array() 
    { 
        if (javaByteArray_ != NULL) {
            env_->ReleaseByteArrayElements(javaByteArray_, nativeByteArray_, 0);
        }
    }

    operator const char *() const { return reinterpret_cast<const char*>(nativeByteArray_); }
    char * data() const { return reinterpret_cast<char*>(nativeByteArray_); }
    const size_t length() const { return length_; }
    
 private:
    jni_byte_array(const jni_byte_array &x);
    jni_byte_array &operator=(const jni_byte_array &x);

    JNIEnv *env_;
    jbyteArray javaByteArray_;
    jbyte *nativeByteArray_;
    size_t length_;
};

#endif // _JNI_BYTE_ARRAY_HPP
