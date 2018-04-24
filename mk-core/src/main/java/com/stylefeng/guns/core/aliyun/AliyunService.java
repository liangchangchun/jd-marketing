package com.stylefeng.guns.core.aliyun;

import java.io.InputStream;
import java.net.URL;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

/**
 * Author: mwan
 * Version: 1.1
 * Date: 2017/09/22
 * Description: 阿里云服务接口.
 * Copyright (c) 2017 伴飞网络. All rights reserved.
 */
public interface AliyunService {
	
	/**
	 * 给物联网套件发布消息
	 */
	public Object pubMessageIot(String ProductKey,String topicName,String message,int... Qos)
			throws ServerException, ClientException;
	/**
	 * 给消息服务MNS的主题发布消息
	 */
	public Object pubMessageMnsTopic(String topicName,String message,int... Qos);
	/**
	 * 从消息服务MSN的队列订阅消息
	 */
	public Object getMessageMnsQueue(String queueName)  throws Exception ;
	/**
	 * 从消息服务MSN的队列订阅消息，根据主题名称过滤消息
	 */
	public Object getMessageMnsQueue(String queueName, String topicFilter);
	/**
	 * 发送验证码短信
	 * @param targetPhone 目标手机号
	 * @param signName 短信签名
	 * @param templateCode 短信模板名称
	 * @param code 验证码的值
	 */
	public boolean sendSMSForCode(String targetPhone, String signName, String templateCode, String code) throws ClientException;
	/**
	 * 上传文件流到OSS
	 * @param bucketName OSS的bucket名字
	 * @param objectKey 文件名字
	 * @param inputStream 文件流
	 */
	public boolean putFileStreamToOSS(String objectKey, InputStream inputStream);
	public boolean putFileStreamToOSS(String bucketName, String objectKey, InputStream inputStream);
	/**
	 * 从OSS中删除对象
	 * @param bucketName OSS的bucket名字
	 * @param objectKey 文件名字
	 */
	public boolean deleteFileFromOSS(String objectKey);
	public boolean deleteFileFromOSS(String bucketName, String objectKey);
	
	/**
	 * 生成一个签名的URL
	 * @param bucketName OSS的bucket名字
	 * @param objectKey 文件名字
	 * @param expireHour 有效时长，单位小时
	 */
	public URL generatePresignedUrl(String bucketName, String objectKey, int expireHour);
	public URL generatePresignedUrl(String objectKey, int expireHour);
	
}
