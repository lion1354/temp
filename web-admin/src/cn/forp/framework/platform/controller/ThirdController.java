/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.platform.service.SystemService;
import cn.forp.framework.platform.vo.User;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 第三方组件Controller
 *
 * @author  Bruce
 * @version 2016-8-11 17:32
 */
@RestController
public class ThirdController extends BaseController
{
	/**
	 * Log4j logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(ThirdController.class);

	//=================================================================
	//		用户头像 - avatar
	//=================================================================

	/**
	 * 头像上传
	 */
	@RequestMapping("/third/avatar")
	public ModelAndView uploadUserAvatar(@RequestParam("big_avatar") String big_avatar, HttpServletResponse resp) throws Exception
	{
		try
		{
			String key = UUID.randomUUID().toString();
			Redis.cacheByteArray(FORP.CACHE_TEMP_AVATAR + key, 30 * 60, Base64.decodeBase64(big_avatar), null);
//			response.setContentType("text/xml");
			// 设置返回值: 200表示成功，&分隔符，文件标识
			resp.setStatus(200);
			resp.setContentType("text/html; charset=utf-8");
			resp.getWriter().write("200&" + key);
			resp.getWriter().flush();
		}
		catch (Exception e)
		{
			lg.error("用户头像上传失败：", e);
//			resp.setContentType("text/xml");
			resp.getWriter().write("400&" + e.toString());
		}

		return null;
	}

	/**
	 * 查看临时上传的用户头像
	 */
	@RequestMapping("/third/avatar/temp/{id}")
	public ModelAndView viewTempUserAvatar(@PathVariable String id, HttpServletResponse resp) throws Exception
	{
		if (Redis.isCached(FORP.CACHE_TEMP_AVATAR + id, null))
		{
			resp.setContentType("image/png");
			byte[] avatar = Redis.getByteArray(FORP.CACHE_TEMP_AVATAR + id, null);
			resp.getOutputStream().write(avatar);
			resp.getOutputStream().flush();
		}
		else
			lg.warn("无效的临时UserAvatar用户头像：{}", id);

		return null;
	}

	//=================================================================
	//		MongoDB - 附件
	//=================================================================

	/**
	 * 在线打开附件
	 *
	 * @param id    文件编号
	 * @param resp  请求回应对象
	 */
	@RequestMapping("/third/mongodb/{id}")
	public ModelAndView viewMongoDBAttachement(@PathVariable String id, HttpServletResponse resp) throws Exception
	{
		GridFsTemplate gridFs = (GridFsTemplate) FORP.SPRING_CONTEXT.getBean("gridFsTemplate");
		GridFSDBFile file = gridFs.findOne(new Query(Criteria.where("_id").is(id)));
		resp.setContentType(file.getContentType());
		file.writeTo(resp.getOutputStream());

		return null;
	}

	//=================================================================
	//		百度地图
	//=================================================================

	/**
	 * 百度地图页面跳转
	 */
	@RequestMapping("/third/baidu")
	public ModelAndView baidu() throws Exception
	{
		return new ModelAndView("/common/baidu-map.jsp");
	}
}