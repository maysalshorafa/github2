package com.sunmi.aidl;

/** 
 * TODO<请描述这个类是干什么的> 
 * @author 郭晗 
 * @versionCode 1 <每次修改提交前+1>
 */
 
import com.sunmi.aidl.callback;
interface MSCardService {
	int getMSReaderStatus();  //阻塞
	void readRawMSCard(in int timeOut,in callback call);  //阻塞
	boolean setTempFormat(in String [] decorate,in int [] order);  //阻塞
}