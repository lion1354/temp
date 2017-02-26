/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

/**
 * 邮件发送类
 *
 * @author	Bruce
 * @version	2012-3-19 10:10:04
 * @deprecated 替换为Spring的mail template
 */
public class Mail
{
//	private final static Logger logger = Logger.getLogger(Mail.class);
	/**
	 * 用户名称
	 */
	private String userName = null;
	/**
	 * 密码
	 */
	private String password = null;
	/**
	 * smtp服务器地址
	 */
	private String smtpHost = null;
	/**
	 * 发送方邮箱地址
	 */
	private String fromMailAddr = null;

	/**
	 * 构造方法
	 * 
	 * @param smtpHost     	发送方邮箱smtp服务器地址
	 * @param fromMailAddr	发送方邮箱
	 * @param userName     	发送方账号
	 * @param password     	发送方密码	
	 */
	public Mail(String smtpHost, String fromMailAddr, String userName, String password)
	{
		this.userName = userName;
		this.password = password;
		this.smtpHost = smtpHost;
		this.fromMailAddr =fromMailAddr;
	}

	/**
	 * 发送邮件
	 * 
	 * @param to				收件人地址(多个地址之间使用“,”进行分割)
	 * @param copyTo			抄送人地址(多个地址之间使用“,”进行分割)
	 * @param subject			标题
	 * @param body			邮件内容
	 * @param filePath			附件文件绝对路径列表
	 * 
	 * @throws Exception
	 */
	public void sendMail(String to, String copyTo, String subject, String body, String[] filePath) throws Exception
	{
//		Properties props = System.getProperties();
//		// 设置SMTP服务器地址
//		props.put("mail.smtp.host", smtpHost);
//		// 设置邮件的字符集为GBK
//		props.put("mail.mime.charset", "GBK");
//		// 设置认证模式
//		props.put("mail.smtp.auth", "true");
//		// 获取会话信息
//		SessionInterceptor session = SessionInterceptor.getDefaultInstance(props, null);
//
//		// 构造邮件消息对象
//		MimeMessage message = new MimeMessage(session);
//		// 发件人
//		message.setFrom(new InternetAddress(fromMailAddr));
//
//		// 多个发送地址
//		message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//		// 多个抄送地址
//		if (StringUtils.hasText(copyTo))
//			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copyTo));
//
//		// 邮件主题
//		message.setSubject(subject);
//
//		// 邮件
//		Multipart mailBody = new MimeMultipart("mixed");
//		// 正文
//		MimeBodyPart bodyPart = new MimeBodyPart();  
//		bodyPart.setContent(body, "text/html;charset=gbk");  
//		mailBody.addBodyPart(bodyPart);  
//
//		// 处理附件
//		if (null != filePath)
//		{
//			for (int i = 0; i < filePath.length; i++)
//			{
//				bodyPart = new MimeBodyPart();
//				DataSource source = new FileDataSource(filePath[i]);
//				bodyPart.setDataHandler(new DataHandler(source));
//				bodyPart.setFileName(new String(filePath[i].substring(filePath[i].lastIndexOf("\\") + 1).getBytes(), 
//						"iso-8859-1"));
//				// logger.debug("附件名称:" + bodyPart.getFileName());
//				// 添加附件内容
//				mailBody.addBodyPart(bodyPart);
//			}
//		}
//
//		// 使用多个body体填充邮件内容。
//		message.setContent(mailBody);
//
////		免认证模式
////		Transport.send(message, message.getAllRecipients());
//		
//		// 使用认证模式发送邮件。
//		Transport transport = session.getTransport("smtp");
//		transport.connect(smtpHost, userName, password);		
//		transport.sendMessage(message, message.getAllRecipients());
//		transport.close();
//
//		// logger.info("邮件发送成功");
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			Mail mail = new Mail("smtp.qq.com","kefu@hy-istudy.com","kefu@hy-istudy.com", "huaruan2014");
			String url = "http://www.hy-istudy.com/member/securityManager!activateAccount.do?p1=dc153b7fb3c3df7f6acdc20cf0793f7e&p2=2b84e0659c11e3744537c1fa6ac279f4";
			String htmlBody = "亲爱的会员，您好：<br><br>恭喜您注册成功，请点击以下链接激活账号：<a target='_blank' href='" + url + "'>" + url + "</a>" +
										"<br><br>也可以复制下面的链到浏览器地址栏中完成注册：" + url +
										"<br><br>本邮件是系统自动发送邮件，请勿回复【Platform代码发送】。<br><br>【华育爱学网】";
			
			mail.sendMail( "AnBaoFeng@hy-istudy.com", null,"会员激活邮件通知", htmlBody, null);			
			System.out.println("邮件发送成功！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}