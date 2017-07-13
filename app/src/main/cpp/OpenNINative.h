//
// Created by win7 on 2017/7/13 0013.
//

#ifndef COLORRECOGNIZE_OPENNINATIVE_H
#define COLORRECOGNIZE_OPENNINATIVE_H


#include <jni.h>
#include <string>
#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include <android/log.h>
#include <pthread.h>
#include <math.h>
#include <android/bitmap.h>
//#include <XnOpenNI.h>
#include <XnCppWrapper.h>
#include <string.h>
#include <assert.h>
#include <iostream>
#include <opencv2/opencv.hpp>

using namespace xn;
using namespace std;
using namespace cv;

typedef int (*onTouchDownCallback_t)(vector<Point3f>);
typedef int (*onTouchReleaseCallback_t)();
typedef int (*onStartCalibrationCallback_t)();

onTouchDownCallback_t callbackTouchdown;
onTouchReleaseCallback_t callbackTouchrelease;
onStartCalibrationCallback_t callbackCalibration;



#define  JNIREG_CLASS "com/orbbec/core/NativeMethod"
#define  JNIREG_CLASS_TOUCH  "com/orbbec/anytouch/TouchEngine"
#define  LOG_TAG      "OpenNINative"
#define  LOGINFO(...)  if(g_EnableLog) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#ifdef DEBUG_MODE
#define  LOGI(...)   __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#else
#define LOGI(...)
#define LOGE(...)
#endif


#define CHECK_RC(nRetVal, what)                           \
  if( nRetVal != XN_STATUS_OK)                            \
  {                                                       \
     LOGINFO("%s: %s", what, xnGetStatusString(nRetVal)); \
     return nRetVal;                                      \
  }                                                   \
  else                                                    \
  {                                                       \
     LOGINFO("%s: %s", what, xnGetStatusString(nRetVal)); \
  }                                                       \

//Global varialbes
Context          g_Context;
DepthMetaData    g_depthMD;
ImageMetaData    g_ImageMD;

SceneMetaData    g_sceneMD;


DepthGenerator   g_DepthGenerator   = 0;
ImageGenerator   g_ImageGenerator   = 0;

XnChar   g_strPose[20] = "";

long     g_PrevTs;

int      g_ImageWidth  = 640;
int      g_ImageHeight = 480;
int      g_DepthWidth = 640;
int      g_DepthHeight = 480;

bool     g_EnableLog = true;
bool     g_UseUVC    = false;
bool     g_UserExist = false;
bool     g_uerRecognized = false;
bool     g_depthInitialized = false;
bool     g_imageInitialized = false;

XnStatus nRetVal = XN_STATUS_OK;

JNIEnv *gEnv;
int firstFlag = 0;
struct timeval tvafter,tvpre;
struct timezone tz;



/*Functions Declare*/

XnStatus InitContext();
XnStatus InitDepthGenerator(int depthWidth, int depthHeight);
XnStatus InitImageGenerator(int imageWidth, int imageHeight);
XnStatus Init(JNIEnv* env, jobject object, jboolean isUVC, jint depthWidth, jint depthHeight, jint imageWidth, jint imageHeight);
jint GetRGBBitmap(JNIEnv* env, jobject object, jobject bitmap);
jint GetDepthBitmap(JNIEnv* env, jobject object, jobject bitmap);
jint Update(JNIEnv* env, jobject obj);
void ReleaseSensor(JNIEnv* env, jobject obj);
jint EnableLog(JNIEnv* env, jobject obj, jboolean enable);



#endif //COLORRECOGNIZE_OPENNINATIVE_H
