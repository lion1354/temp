/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import cn.forp.framework.core.FORP;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


/**
 * 七牛云存储平台工具类
 *
 * @author  Bruce
 * @version 2017-2-13 21:20
 */
public class QiNiu
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(QiNiu.class);

	/**
	 * ak
	 */
	private static String AK = null;
	/**
	 * sk
	 */
	private static String SK = null;
	/**
	 * 空间
	 */
	private static String BUCKET_NAME = null;
	/**
	 * 域名
	 */
	private static String DOMAIN_NAME = null;
	/**
	 * 认证
	 */
	private static Auth auth = null;
	/**
	 * Uploader Manager
	 */
	private static UploadManager uploadManager = null;

	/**
	 * 七牛云存储初始化
	 *
	 * @param ak          Access Key
	 * @param sk          Security Key
	 * @param bucketName  命名空间
	 * @param domainName  域名
	 */
	public static void init(String ak, String sk, String bucketName, String domainName)
	{
		AK = ak;
		SK = sk;
		BUCKET_NAME = bucketName;
		DOMAIN_NAME = domainName;

		auth = Auth.create(AK, SK);
		// 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
		uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
	}

	/**
	 * 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	 */
	private static String getUploadToken()
	{
		return auth.uploadToken(BUCKET_NAME);
	}

	/**
	 * 上传附件
	 *
	 * @param mf      SpringMVC文件
	 * @param fileId  文件ID
	 */
	public static void upload(MultipartFile mf, String fileId) throws Exception
	{
		File tempFile = new File(FORP.WEB_APP_PATH + "/disk-file/temp/" + UUID.randomUUID().toString());
		mf.transferTo(tempFile);

		Response resp = uploadManager.put(tempFile, fileId, getUploadToken());
		lg.debug("七牛存储返回结果：{}", resp.bodyString());

		// 删除临时文件
		FileUtils.deleteQuietly(tempFile);
	}

	/**
	 * 获取资源访问URL路径
	 *
	 * @param fileId  文件编号
	 */
	public static String getResourceURL(String fileId)
	{
		return "http://" + DOMAIN_NAME + "/" + fileId;
	}

	/**
	 * 测试方法
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
//		QiNiu.init("R9ZoImx2pWyPDuhGwcxeSrxGoxADhLOtapNQAemt", "UQ845DYf17Jb3empGGniT-7gStdMukJHw9LS16EK",
//				"image-dev", "oky0so4m0.bkt.clouddn.com");
//		QiNiu.upload("A:/QQ图片20170117092740.jpg", "123-abc-000");
//		System.out.printf(QiNiu.getResourceURL("123-abc-000"));
	}
}