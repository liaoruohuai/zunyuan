package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.util.SmsKit;
import com.learning.order.entity.Coupon;
import com.learning.order.service.CouponService;
import com.learning.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletResponse response){
        Map<String,Object>  result = couponService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("coupon",pageModel);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public String sendCoupons(@RequestParam Map<String,String> parm, HttpServletResponse response) throws UnsupportedEncodingException {
        couponService.PreCoupons();
        List<Coupon> coupons = couponService.findSendCoupon();
        for(int i = 0; i < coupons.size();i++){
            String smscontent = "恭喜您!成功领取“车享家”"+coupons.get(i).getCouponDesp()+"一张，您的券码是"+coupons.get(i).getCouponInfo()+"，有效期截止" +
                    coupons.get(i).getCouponValidDate()+"，戳 ??? 立即下单。源自上汽，一站到家！";
            SmsKit.send(coupons.get(i).getGrantMember(),smscontent);
            coupons.get(i).setCouponStatus("2");
            couponService.save(coupons.get(i));
        }
        Map<String,Object>  result = couponService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("coupon",pageModel);
    }

}
