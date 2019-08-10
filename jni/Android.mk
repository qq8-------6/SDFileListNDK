LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := filelist
LOCAL_SRC_FILES := filelist.c
LOCAL_C_INCLUDES:= $(JNI_H_INCLUDE)
LOCAL_LDLIBS	:= -llog
LOCAL_PRELINK_MODULE:= false
include $(BUILD_SHARED_LIBRARY)
