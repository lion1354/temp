package com.tibco.ma.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AWSS3Util {

	private static ConfigInfo configInfo = (ConfigInfo) SpringBeanUtil
			.getBean("configInfo");
	private static Logger log = LoggerFactory.getLogger(AWSS3Util.class);

	private static AmazonS3 amazonS3;

	private static AmazonS3 getInstance() throws Exception {
		try {
			if (amazonS3 == null) {
				BasicAWSCredentials awsCreds = new BasicAWSCredentials(
						configInfo.getS3AccessKeyId(),
						configInfo.getS3SecretAccessKey());
				AmazonS3 s3 = new AmazonS3Client(awsCreds);
				return s3;
			} else {
				return amazonS3;
			}

		} catch (Exception e) {
			throw new Exception(
					"AWS S3 Create AmazonS3Client failed, the error message: "
							+ e.getMessage());
		}
	}

	public static void UploadFile(String key, File file, Boolean isPublic)
			throws Exception {
		log.info("Uploading a new object to S3 from a file");
		try {
			AmazonS3 s3 = AWSS3Util.getInstance();
			PutObjectRequest objectRequest = new PutObjectRequest(
					configInfo.getS3Bucket(), key, file);
			if (isPublic) {
				objectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			}
			s3.putObject(objectRequest);
		} catch (Exception e) {
			throw new Exception(
					"AWS S3 Upload new object failed, the error message: "
							+ e.getMessage());
		}
	}

	public static void deleteByKey(String key) throws Exception {
		try {
			log.info("delete s3 object by key");
			AmazonS3 s3 = AWSS3Util.getInstance();
			s3.deleteObject(configInfo.getS3Bucket(), key);
		} catch (Exception e) {
			throw new Exception(
					"AWS S3 delete object failed, the error message: "
							+ e.getMessage());
		}
	}

	public static ObjectListing getAll() throws Exception {
		try {
			log.info("get all objects from bucket");
			AmazonS3 s3 = AWSS3Util.getInstance();
			ObjectListing objectListing = s3
					.listObjects(new ListObjectsRequest()
							.withBucketName(configInfo.getS3Bucket()));
			return objectListing;
		} catch (Exception e) {
			throw new Exception(
					"AWS S3 get all objects failed, the error message: "
							+ e.getMessage());
		}
	}

}
