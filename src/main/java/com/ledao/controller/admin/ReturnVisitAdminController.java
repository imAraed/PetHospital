package com.ledao.controller.admin;

import com.ledao.entity.ReturnVisit;
import com.ledao.entity.Log;
import com.ledao.entity.PageBean;
import com.ledao.service.ReturnVisitService;
import com.ledao.service.LogService;
import com.ledao.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理客户回访记录Controller层
 *
 * @author LeDao
 * @company
 * @create 2020-03-19 12:42
 */
@RestController
@RequestMapping("/admin/returnVisit")
public class ReturnVisitAdminController {

    @Resource
    private ReturnVisitService returnVisitService;

    @Resource
    private LogService logService;

    /**
     * 分页分条件查询客户回访记录
     *
     * @param returnVisit
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = "客户回访记录管理")
    public Map<String, Object> list(ReturnVisit returnVisit, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("customerName", StringUtil.formatLike(returnVisit.getCustomerName()));
        map.put("petName", StringUtil.formatLike(returnVisit.getPetName()));
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        resultMap.put("rows", returnVisitService.list(map));
        resultMap.put("total", returnVisitService.getCount(map));
        logService.add(new Log(Log.SEARCH_ACTION, "查询客户回访记录"));
        return resultMap;
    }

    /**
     * 添加或修改客户回访记录
     *
     * @param returnVisit
     * @return
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = "客户回访记录管理")
    public Map<String, Object> save(ReturnVisit returnVisit) {
        Map<String, Object> resultMap = new HashMap<>(16);
        int key;
        if (returnVisit.getId() == null) {
            logService.add(new Log(Log.ADD_ACTION, "添加客户回访记录" + returnVisit));
            key = returnVisitService.add(returnVisit);
        } else {
            logService.add(new Log(Log.UPDATE_ACTION, "修改客户回访记录" + returnVisit));
            key = returnVisitService.update(returnVisit);
        }
        if (key > 0) {
            resultMap.put("success", true);
        } else {
            resultMap.put("success", false);
        }
        return resultMap;
    }

    /**
     * 删除病例单(可批量删除)
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = "客户回访记录管理")
    public Map<String, Object> delete(String ids) {
        Map<String, Object> resultMap = new HashMap<>(16);
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            logService.add(new Log(Log.DELETE_ACTION, "删除文章类型" + returnVisitService.findById(Integer.parseInt(idsStr[i]))));
            returnVisitService.delete(Integer.parseInt(idsStr[i]));
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
