package com.popular.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.popular.common.AppConfig;
import com.popular.rest.api.CityController;
import com.popular.service.MessageService;

@Component
public class MessageTask {
	private static Logger log = LoggerFactory.getLogger(CityController.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private MessageService messageService;

	@Scheduled(cron = "0 0 3 * * ?") // 每天的 3:00 AM执行一次
	public void run() {
		try {
			int keepDay = Integer.parseInt(appConfig.getMessage_keep_day());
			Date date = getDateBefore(keepDay);
			log.error(date.toString());
			messageService.deleteByDate(date);
		} catch (Exception e) {
			log.error("删除聊天消息错误： {}", e.getMessage());
		}
	}

	private Date getDateBefore(int day) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
}
