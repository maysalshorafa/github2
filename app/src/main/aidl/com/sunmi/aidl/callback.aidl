package com.sunmi.aidl;

/** 
 * TODO<请描述这个类是干什么的> 
 * @author 郭晗 
 * @versionCode 1 <每次修改提交前+1>
 */
interface callback {
	void MSCardInfo(boolean isSuccess,in byte[] m1,in byte[] m2,in byte[] m3);
}
