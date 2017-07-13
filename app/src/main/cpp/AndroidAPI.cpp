//
// Created by win7 on 2017/7/12 0012.
//

#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include "OpenNINative.h"




#define JNIREG_CLASS "wmy/jni/com/colorrecognize"
# define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  , "AndroidAPI", __VA_ARGS__)

extern "C"
 jstring JNICALL
native_stringFromWMY(
        JNIEnv *env,
        jobject /* this */) {


    std::string hello = "Hello from WMY";
    return env->NewStringUTF(hello.c_str());
}



void JNICALL native_engine(JNIEnv *env,
                           jobject){



}
//OPENNI START

XnStatus InitContext()
{
    XnStatus rc = XN_STATUS_OK;
    rc = g_Context.Init();
    CHECK_RC(rc, "1 ---- InitContext()");
    return rc;
}

XnStatus InitDepthGenerator(int depthWidth, int depthHeight)
{
    XnStatus rc = XN_STATUS_OK;
    CHECK_RC(rc, "2 -1--- InitDepthGenerator()");
    rc = g_DepthGenerator.Create( g_Context );
    CHECK_RC(rc, "2 --2-- InitDepthGenerator()");


    g_DepthGenerator.GetMirrorCap().SetMirror(true);


    XnMapOutputMode depthImageMode;
    depthImageMode.nXRes = depthWidth;
    depthImageMode.nYRes = depthHeight;
    depthImageMode.nFPS  = 30;
    g_DepthGenerator.SetMapOutputMode(depthImageMode);

    g_depthInitialized = true;

    return rc;
}

XnStatus InitImageGenerator(int imageWidth, int imageHeight)
{
    XnStatus rc = XN_STATUS_OK;
    rc = g_ImageGenerator.Create( g_Context );
    CHECK_RC(rc, "3 ---- InitImageGenerator()");
    g_ImageGenerator.GetMirrorCap().SetMirror(true);

    LOGI( "3 ---- Image.w=%d,h=%d",imageWidth,imageHeight);
    //Set RGB Mode
    XnMapOutputMode rgbImageMode;
    rgbImageMode.nXRes = imageWidth;
    rgbImageMode.nYRes = imageHeight;
    rgbImageMode.nFPS  = 30;

    g_ImageGenerator.SetMapOutputMode(rgbImageMode);

    //g_ImageGenerator.SetPixelFormat(XN_PIXEL_FORMAT_RGB24);
    g_imageInitialized = true;

    return rc;
}


XnStatus Init(JNIEnv* env, jobject object, jboolean isUVC, jint depthWidth, jint depthHeight, jint imageWidth, jint imageHeight)
{
    g_UseUVC = isUVC;


    if( XN_STATUS_OK != InitContext()){
        LOGE("InitContext failed");
        return XN_STATUS_ERROR;
    }

    g_DepthWidth  = depthWidth;
    g_DepthHeight = depthHeight;
    if(XN_STATUS_OK != InitDepthGenerator(g_DepthWidth, g_DepthHeight)){
        g_depthInitialized = false;
        g_imageInitialized = false;
        LOGE("InitDepthGenerator failed");
        return XN_STATUS_ERROR;
    }

    g_ImageWidth  = imageWidth;
    g_ImageHeight = imageHeight;
    if(XN_STATUS_OK != InitImageGenerator(g_ImageWidth, g_ImageHeight)){
        g_depthInitialized = false;
        g_imageInitialized = false;
        LOGE("InitImageGenerator failed");
        return XN_STATUS_ERROR;
    }


    nRetVal = g_Context.StartGeneratingAll();
    CHECK_RC(nRetVal, "5 ---- StartGenerating");

    // RGB-D registration
    g_DepthGenerator.GetAlternativeViewPointCap().SetViewPoint(g_ImageGenerator);
    CHECK_RC(nRetVal, "6 ---- GetAlternativeViewPointCap");
    return XN_STATUS_OK;
}

jint GetRGBBitmap(JNIEnv* env, jobject object, jobject bitmap)
{
    if(g_UseUVC)
    {
        return -1;
    }
    AndroidBitmapInfo bitmapInfo;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return ret;
    }

    int bitmapWidth  = bitmapInfo.width;
    int bitmapHeight = bitmapInfo.height;

    if(bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        LOGE("Bitmap format is not RGBA_8888 !");
        return -1;
    }

    if(bitmapWidth != g_ImageWidth || bitmapHeight != g_ImageHeight)
    {
        LOGE("Size of ImageBitmap is not %d * %d", g_ImageWidth, g_ImageHeight);
    }

    int* pixels = NULL;

    if((ret = AndroidBitmap_lockPixels(env, bitmap, (void**)&pixels)) < 0)
    {
        LOGE("AndroidBitmap_lockPixels() failed !  error = %d", ret);
        return ret;
    }

    if(g_imageInitialized == false)
    {
        LOGE("g_ImageGenerator is not inited yet");
        return -1;
    }
    const XnRGB24Pixel* rgb24Pixel = g_ImageMD.RGB24Data();
    int width  = g_ImageMD.FullXRes();
    int height = g_ImageMD.FullYRes();
    int size = g_ImageGenerator.GetDataSize();

    if(size <= 0){
        LOGE("g_ImageGenerator width=%d height=%d, size: %d",width,height, size);
        rgb24Pixel=NULL;
        pixels = NULL;
        return -1;
    }
    for(int y = 0; y<g_ImageHeight; y++)
    {
        // LOGE("g_ImageGenerator y=%d",y);
        for(int x = 0; x<g_ImageWidth; x++)
        {
            int r = (*(rgb24Pixel + y * g_ImageWidth + (g_ImageWidth - x))).nRed;
            int g = (*(rgb24Pixel + y * g_ImageWidth + (g_ImageWidth - x))).nGreen;
            int b = (*(rgb24Pixel + y * g_ImageWidth + (g_ImageWidth - x))).nBlue;

            *(pixels + y * g_ImageWidth + x) = 0xff000000
                                               | b << 16
                                               | g << 8
                                               | r;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    rgb24Pixel=NULL;
    pixels = NULL;

    return 1;
}

jint GetDepthBitmap(JNIEnv* env, jobject object, jobject bitmap)
{
    AndroidBitmapInfo bitmapInfo;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return ret;
    }

    int bitmapWidth  = bitmapInfo.width;
    int bitmapHeight = bitmapInfo.height;

    if(bitmapWidth != g_DepthWidth || bitmapHeight != g_DepthHeight)
    {
        LOGE("Size of DepthBitmap is not %d * %d", g_DepthWidth, g_DepthHeight);
        return -1;
    }

    if(bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        LOGE("Bitmap format is not RGBA_8888 !");
        return -1;
    }

    int* pixels = NULL;

    if((ret = AndroidBitmap_lockPixels(env, bitmap, (void**)&pixels)) < 0)
    {
        LOGE("AndroidBitmap_lockPixels() failed !  error = %d", ret);
        return ret;
    }

    if(g_depthInitialized == false)
    {
        LOGE("g_depthInitialized is not inited yet");
        return -1;
    }
    for(int y = 0; y<g_DepthHeight; y++)
    {
        for(int x = 0; x<g_DepthWidth; x++)
        {
            int value = (int)((float)(*((unsigned short*)g_depthMD.Data() + y * g_DepthWidth + x)) / 8000 * 255);
            *(pixels + y * g_DepthWidth + g_DepthWidth - x) = 0x88000000 | value << 16 | value << 8 | value;
        }
    }


    AndroidBitmap_unlockPixels(env, bitmap);
    pixels = NULL;

    return 1;
}


jint Update(JNIEnv* env, jobject obj)
{
    gettimeofday (&tvpre , &tz);
    if(g_depthInitialized)
    {
        // g_Context.WaitAnyUpdateAll();
        g_Context.WaitOneUpdateAll(g_DepthGenerator);
        g_DepthGenerator.GetMetaData(g_depthMD);


        if(!g_UseUVC)
        {
            g_ImageGenerator.GetMetaData(g_ImageMD);
        }

    }
    gettimeofday (&tvafter , &tz);

    // LOGI("passtime: %d\n",(tvafter.tv_sec-tvpre.tv_sec)*1000+(tvafter.tv_usec-tvpre.tv_usec)/1000);
    return 0;
}



void ReleaseSensor(JNIEnv* env, jobject obj)
{
    g_Context.StopGeneratingAll();
    LOGI("Release UserGenerator");
    g_DepthGenerator.Release();
    LOGI("Release DepthGenerator");

    if(!g_UseUVC)
    {
        g_ImageGenerator.Release();
        LOGI("Release ImageGenerator");
    }

    g_Context.Release();
    LOGI("Release Context");
    g_imageInitialized = false;
    exit(0);
}

jint EnableLog(JNIEnv* env, jobject obj, jboolean enable)
{
    g_EnableLog = enable;

    return 0;
}





//END




// Java和JNI函数的绑定表
static JNINativeMethod method_table[] = {
        { "Init",                            "(ZIIII)I",                                                                                           (void*)&Init},
        { "GetDepthBitmap",                  "(Landroid/graphics/Bitmap;)I",                                                                       (void*)&GetDepthBitmap},
        { "GetRGBBitmap",                    "(Landroid/graphics/Bitmap;)I",                                                                       (void*)&GetRGBBitmap},
        { "ReleaseSensor",                   "()V",                                                                                                (void*)&ReleaseSensor},
        { "Update",                          "()I",                                                                                                (void*)&Update},
        {"stringFromWMY","()",(void*)native_stringFromWMY},


};

// 注册native方法到java中
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((env)->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int register_ndk_load(JNIEnv *env)
{
    // 调用注册方法
    return registerNativeMethods(env, JNIREG_CLASS,
                                 method_table, NELEM(method_table));
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;


    if ((vm)->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    register_ndk_load(env);

    // 返回jni的版本
    return JNI_VERSION_1_4;
}