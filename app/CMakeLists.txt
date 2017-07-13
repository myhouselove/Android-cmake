# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.6)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

set(CMAKE_VERBOSE_MAKEFILE on)
set(libs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")
include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/include)

#--------------------------------------------------- import ---------------------------------------------------#
add_library(libopencv_java3 SHARED IMPORTED )
set_target_properties(libopencv_java3 PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libopencv_java3.so")

#add_library(libopencv_java SHARED IMPORTED )
#set_target_properties(libopencv_java PROPERTIES
#    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libopencv_java.so")

#add openni library
add_library(libOpenNI.jni SHARED IMPORTED )
set_target_properties(libOpenNI.jni PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libOpenNI.jni.so")

add_library(libOpenNI SHARED IMPORTED )
set_target_properties(libOpenNI PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libOpenNI.so")

add_library(liborbbecusb SHARED IMPORTED )
set_target_properties(liborbbecusb PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/liborbbecusb.so")

add_library(libOrbbecUtils SHARED IMPORTED )
set_target_properties(libOrbbecUtils PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libOrbbecUtils.so")

add_library(libOrbbecUtils_jni SHARED IMPORTED )
set_target_properties(libOrbbecUtils_jni PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libOrbbecUtils_jni.so")

add_library(libXnCore SHARED IMPORTED )
set_target_properties(libXnCore PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libXnCore.so")

add_library(libXnDDK SHARED IMPORTED )
set_target_properties(libXnDDK PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libXnDDK.so")

add_library(libXnDeviceFile SHARED IMPORTED )
set_target_properties(libXnDeviceFile PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libXnDeviceFile.so")

add_library(libXnDeviceSensorV2 SHARED IMPORTED )
set_target_properties(libXnDeviceSensorV2 PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libXnDeviceSensorV2.so")

add_library(libXnFormats SHARED IMPORTED )
set_target_properties(libXnFormats PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libXnFormats.so")

add_library(libBodyTracking SHARED IMPORTED )
set_target_properties(libBodyTracking PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libBodyTracking.so")

add_library(libHandTracking SHARED IMPORTED )
set_target_properties(libHandTracking PROPERTIES
    IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libHandTracking.so")

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")



add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/AndroidAPI.cpp)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log)

target_link_libraries(native-lib android log -ljnigraphics
    libopencv_java3 libOpenNI.jni libOpenNI liborbbecusb libOrbbecUtils libOrbbecUtils_jni libXnCore libXnDDK libXnDeviceFile libXnDeviceSensorV2 libXnFormats libBodyTracking libHandTracking
    ${log-lib}
    )