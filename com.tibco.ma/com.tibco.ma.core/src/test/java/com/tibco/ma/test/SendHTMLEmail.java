package com.tibco.ma.test;

import com.tibco.ma.common.mail.MailUtils;
 
public class SendHTMLEmail
{
   public static void main(String [] args)
   {
     
      // 
      String to = "ydu@tibco-support.com";
 
      // 
      String from = "MobilePlatform0mail@gmail.com";
 
      // 
      String host = "smtp.gmail.com";
      
      String userName = "MobilePlatform0mail@gmail.com";
      
      String pwd = "mobileplatform";
      
      String subject = "Your Fan Zone Account Activation";
      
      String text = "Thank you for your loyal support of the Sacramento Kings!<br>"
      		+ "To participate in the Fan Zone conversation and activate your account now, please click below<br>"
      		+ "<a href='www.baidu.com'>Activate Account</a>";
 
      try {
		new MailUtils(host, true).send(userName, pwd, subject, from, to, text);
	} catch (Exception e) {
		e.printStackTrace();
	}
   }
}