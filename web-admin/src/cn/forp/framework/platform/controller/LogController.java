/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.LogService;
import cn.forp.framework.platform.vo.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志Controller
 *
 * @author	Bruce
 * @version	2016年7月28日 下午3:19:31
 */
@RestController
public class LogController extends BaseController
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(LogController.class);
  /**
   * Service
   */
  @Autowired
  protected LogService service = null;

  /**
   * 分页查询
   *
   * @param req	Request请求参数
   */
  @RequestMapping("/platform/log")
  public Page<Log> index(HttpServletRequest req) throws Exception
  {
    IForm form = getFormWrapper(req);
    lg.debug("日志查询：{} ~ {}", form.get("fromDate"), form.get("toDate"));
    return service.search(getSessionUser(req), form.get("fromDate"), form.get("toDate"), form.get("content"), getPageSort(req));
  }
}