/*
 * filelist.c
 *
 *  Created on: 2016-9-5
 *      Author: asus
 */
#include "com_example_sdfilelistndk_MainActivity.h"
#include <jni.h>
#include <dirent.h>
#include <sys/stat.h>
#include <stdio.h>
#include <android/log.h>

#define LOG_TAG "filelist"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
/**
 * 获取文件或目录的大小
 * @param filename
 */
long long get_file_size(const char *filename){
	struct stat s;
	stat(filename,&s);
	if(S_ISDIR(s.st_mode)){
		//若为目录则计算目录总大小
		return (calc_dir_size(filename));
	}else
		return (s.st_size);
}

long long calc_dir_size(const char *filename){
	long long size = 0L;
	DIR *dir = opendir(filename);
	//LOGD("start calc_dir_size:%s",filename);
	struct dirent * dp;
	while(dp=readdir(dir)){
		if(strcmp(dp->d_name,".")!=0&&strcmp(dp->d_name,"..")!=0){
			//LOGD("dp:%d",dp);
			char *sub_path = (char *)malloc(1024);
			sprintf(sub_path,"%s/%s",filename,dp->d_name);
			//LOGD ("%s",sub_path);
			if(dp->d_type==DT_DIR){
				size += calc_dir_size(sub_path);
			}else{
				size += get_file_size(sub_path);
			}
			free(sub_path);
		}
	}
	closedir(dir);
	return (size);
}
JNIEXPORT jlong JNICALL Java_com_example_sdfilelistndk_MainActivity_clacSize
  (JNIEnv *env, jobject obj, jstring file){
	//LOGD("after");
	printf("before\n");
	//const char* filename = (char *)malloc(1024);
	jboolean iscopy = JNI_FALSE;
	const char* filename = (*env)->GetStringUTFChars(env,file,&iscopy);
	LOGD("after:%s\n",filename);
	long long size = get_file_size(filename);
	(*env)->ReleaseStringUTFChars(env,file,filename);
	return (size);
	//return 33;
}
