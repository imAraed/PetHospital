package com.ledao.controller.admin;

import com.ledao.entity.*;
import com.ledao.service.CustomerService;
import com.ledao.service.PetService;
import com.ledao.service.ReservationService;
import com.ledao.util.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理预约单Controller
 *
 * @author LeDao
 * @company
 * @create 2020-02-05 18:34
 */
@RestController
@RequestMapping("/admin/reservation")
public class ReservationAdminController {

    @Resource
    private ReservationService reservationService;

    @Resource
    private CustomerService customerService;

    @Resource
    private PetService petService;

    /**
     * 查看未处理的医生预约单
     *
     * @param reservation
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listNotHandleDoctor")
    @RequiresPermissions(value = "医生预约单")
    public Map<String, Object> listNotHandleDoctor(Reservation reservation, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("status", 0);
        map.put("type", "预约医生");
        if (reservation.getCustomer() != null) {
            if (!StringUtil.isEmpty(reservation.getCustomer().getContact())) {
                Customer customer = customerService.findByContact(reservation.getCustomer().getContact());
                if (customer != null) {
                    int customerId = customer.getId();
                    map.put("customerId", customerId);
                } else {
                    map.put("customerId", -1);
                }
            }
        }
        if (reservation.getPet() != null) {
            if (!StringUtil.isEmpty(reservation.getPet().getName())) {
                Pet pet = petService.findByName(reservation.getPet().getName());
                if (pet != null) {
                    int petId = pet.getId();
                    map.put("petId", petId);
                } else {
                    map.put("petId", -1);
                }
            }
        }
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Reservation> reservationList = reservationService.list(map);
        resultMap.put("rows", reservationList);
        resultMap.put("total", reservationService.getCount(map));
        return resultMap;
    }

    /**
     * 查看未处理的医生预约单
     *
     * @param reservation
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listNotHandleBeautician")
    @RequiresPermissions(value = "美容师预约单")
    public Map<String, Object> listNotHandleBeautician(Reservation reservation, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("status", 0);
        map.put("type", "预约美容师");
        if (reservation.getCustomer() != null) {
            if (!StringUtil.isEmpty(reservation.getCustomer().getContact())) {
                Customer customer = customerService.findByContact(reservation.getCustomer().getContact());
                if (customer != null) {
                    int customerId = customer.getId();
                    map.put("customerId", customerId);
                } else {
                    map.put("customerId", -1);
                }
            }
        }
        if (reservation.getPet() != null) {
            if (!StringUtil.isEmpty(reservation.getPet().getName())) {
                Pet pet = petService.findByName(reservation.getPet().getName());
                if (pet != null) {
                    int petId = pet.getId();
                    map.put("petId", petId);
                } else {
                    map.put("petId", -1);
                }
            }
        }
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Reservation> reservationList = reservationService.list(map);
        resultMap.put("rows", reservationList);
        resultMap.put("total", reservationService.getCount(map));
        return resultMap;
    }

    /**
     * 处理客户预约单
     *
     * @param status
     * @param reservationId
     * @return
     */
    @RequestMapping("/dealReservation")
    @RequiresPermissions(value = {"医生预约单", "美容师预约单", "我的预约单"}, logical = Logical.OR)
    public Map<String, Object> dealReservation(Integer status, Integer reservationId, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>(16);
        Reservation reservation = reservationService.findById(reservationId);
        reservation.setStatus(status);
        if (status == 0) {
            reservation.setIsCancel(1);
        } else if (status==1){
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                resultMap.put("errorInfo", "登录状态已过期,请重新登录!");
                return resultMap;
            }
            reservation.setUser(user);
        }
        reservationService.update(reservation);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 后台获取我的预约单
     *
     * @param reservation
     * @param page
     * @param rows
     * @param session
     * @return
     */
    @RequestMapping("/listMyReservation")
    public Map<String, Object> listMyReservation(Reservation reservation, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows, HttpSession session) {
        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", session.getAttribute("currentUser"));
        if (reservation.getCustomer() != null) {
            if (!StringUtil.isEmpty(reservation.getCustomer().getContact())) {
                Customer customer = customerService.findByContact(reservation.getCustomer().getContact());
                if (customer != null) {
                    int customerId = customer.getId();
                    map.put("customerId", customerId);
                } else {
                    map.put("customerId", -1);
                }
            }
        }
        if (reservation.getPet() != null) {
            if (!StringUtil.isEmpty(reservation.getPet().getName())) {
                Pet pet = petService.findByName(reservation.getPet().getName());
                if (pet != null) {
                    int petId = pet.getId();
                    map.put("petId", petId);
                } else {
                    map.put("petId", -1);
                }
            }
        }
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Reservation> reservationList = reservationService.list(map);
        resultMap.put("rows", reservationList);
        resultMap.put("total", reservationService.getCount(map));
        return resultMap;
    }
}