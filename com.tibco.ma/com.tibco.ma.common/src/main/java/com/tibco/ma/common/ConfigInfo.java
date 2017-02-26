package com.tibco.ma.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author aidan
 * 
 *         2015/5/29
 * 
 */
@Component("configInfo")
public class ConfigInfo {
	@Value("${mongo.host}")
	private String mongoHost;
	@Value("${mongo.port}")
	private String mongoPort;
	@Value("${mongo.connections-per-host}")
	private String mongoPoolSize;
	@Value("${mongo.threads-allowed-to-block-for-connection-multiplier}")
	private String mongoBlockSize;
	@Value("${mongo.dbname}")
	private String mongoDbname;
	@Value("${mongo.username}")
	private String mongoUsername;
	@Value("${mongo.password}")
	private String mongoPassword;

	@Value("${email.server.host}")
	private String emailServerHost;
	@Value("${email.server.transport}")
	private String emailServerTransport;
	@Value("${email.server.port}")
	private String emailServerPort;
	@Value("${email.server.from}")
	private String emailServerFrom;
	@Value("${email.server.pwd}")
	private String emailServerPWD;
	@Value("${email.server.timeout}")
	private String emailServerTimeout;
	@Value("${email.activate.uri}")
	private String emailActivateUri;
	@Value("${email.reset.pwd.uri}")
	private String emailResetPWDUri;
	@Value("${email.context.path}")
	private String emailContextPath;
	@Value("${email.server.path}")
	private String emailServerPath;

	@Value("${nodejs.functions.restApi}")
	private String nodejsFunctionsUrl;
	@Value("${nodejs.schedule.api}")
	private String nodejsScheduleUrl;

	@Value("${s3.access.keyId}")
	private String s3AccessKeyId;
	@Value("${s3.secret.access.key}")
	private String s3SecretAccessKey;
	@Value("${s3.bucket}")
	private String s3Bucket;
	@Value("${s3.bucket.url}")
	private String s3BucketUrl;

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public String getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(String mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoPoolSize() {
		return mongoPoolSize;
	}

	public void setMongoPoolSize(String mongoPoolSize) {
		this.mongoPoolSize = mongoPoolSize;
	}

	public String getMongoBlockSize() {
		return mongoBlockSize;
	}

	public void setMongoBlockSize(String mongoBlockSize) {
		this.mongoBlockSize = mongoBlockSize;
	}

	public String getMongoDbname() {
		return mongoDbname;
	}

	public void setMongoDbname(String mongoDbname) {
		this.mongoDbname = mongoDbname;
	}

	public String getMongoUsername() {
		return mongoUsername;
	}

	public void setMongoUsername(String mongoUsername) {
		this.mongoUsername = mongoUsername;
	}

	public String getMongoPassword() {
		return mongoPassword;
	}

	public void setMongoPassword(String mongoPassword) {
		this.mongoPassword = mongoPassword;
	}

	public String getEmailServerHost() {
		return emailServerHost;
	}

	public void setEmailServerHost(String emailServerHost) {
		this.emailServerHost = emailServerHost;
	}

	public String getEmailServerTransport() {
		return emailServerTransport;
	}

	public void setEmailServerTransport(String emailServerTransport) {
		this.emailServerTransport = emailServerTransport;
	}

	public String getEmailServerPort() {
		return emailServerPort;
	}

	public void setEmailServerPort(String emailServerPort) {
		this.emailServerPort = emailServerPort;
	}

	public String getEmailServerFrom() {
		return emailServerFrom;
	}

	public void setEmailServerFrom(String emailServerFrom) {
		this.emailServerFrom = emailServerFrom;
	}

	public String getEmailServerPWD() {
		return emailServerPWD;
	}

	public void setEmailServerPWD(String emailServerPWD) {
		this.emailServerPWD = emailServerPWD;
	}

	public String getEmailServerTimeout() {
		return emailServerTimeout;
	}

	public String getS3AccessKeyId() {
		return s3AccessKeyId;
	}

	public void setS3AccessKeyId(String s3AccessKeyId) {
		this.s3AccessKeyId = s3AccessKeyId;
	}

	public String getS3SecretAccessKey() {
		return s3SecretAccessKey;
	}

	public void setS3SecretAccessKey(String s3SecretAccessKey) {
		this.s3SecretAccessKey = s3SecretAccessKey;
	}

	public String getS3Bucket() {
		return s3Bucket;
	}

	public void setS3Bucket(String s3Bucket) {
		this.s3Bucket = s3Bucket;
	}

	public String getS3BucketUrl() {
		return s3BucketUrl;
	}

	public void setS3BucketUrl(String s3BucketUrl) {
		this.s3BucketUrl = s3BucketUrl;
	}

	public void setEmailServerTimeout(String emailServerTimeout) {
		this.emailServerTimeout = emailServerTimeout;
	}

	public String getEmailActivateUri() {
		return emailActivateUri;
	}

	public void setEmailActivateUri(String emailActivateUri) {
		this.emailActivateUri = emailActivateUri;
	}

	public String getEmailResetPWDUri() {
		return emailResetPWDUri;
	}

	public void setEmailResetPWDUri(String emailResetPWDUri) {
		this.emailResetPWDUri = emailResetPWDUri;
	}

	public String getEmailContextPath() {
		return emailContextPath;
	}

	public void setEmailContextPath(String emailContextPath) {
		this.emailContextPath = emailContextPath;
	}

	public String getEmailServerPath() {
		return emailServerPath;
	}

	public void setEmailServerPath(String emailServerPath) {
		this.emailServerPath = emailServerPath;
	}

	public String getNodejsFunctionsUrl() {
		return nodejsFunctionsUrl;
	}

	public void setNodejsFunctionsUrl(String nodejsFunctionsUrl) {
		this.nodejsFunctionsUrl = nodejsFunctionsUrl;
	}

	public String getNodejsScheduleUrl() {
		return nodejsScheduleUrl;
	}

	public void setNodejsScheduleUrl(String nodejsScheduleUrl) {
		this.nodejsScheduleUrl = nodejsScheduleUrl;
	}
}
