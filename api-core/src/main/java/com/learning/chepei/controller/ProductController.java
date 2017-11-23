package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.SessionData;
import com.learning.product.entity.Product;
import com.learning.product.service.ProductService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 加载商品接口
     * @param request
     * @return
     */
    @RequestMapping("/load")
    public String loadProduct(HttpServletRequest request,HttpServletResponse response){
        try {
            response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
            return ValueUtil.toJson("products",productService.loadProduct());
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 根据商品编号查询价格(App)
     * @param request
     * @param productNumber
     * @return
     */
    @RequestMapping("/find_price")
    public String findProductPrice(HttpServletRequest request, HttpServletResponse response, String productNumber){
        try {
            SessionData.verify(request,response);
            return ValueUtil.toJson("productPrice",productService.findProductPrice(productNumber));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String create(Product product,HttpServletRequest request, HttpServletResponse response){
        try {
            SessionData.verifyBack(request,response);
            return  ValueUtil.toJson("salesNetwork",productService.insert(product));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public String destroy(String productNumber,HttpServletRequest request, HttpServletResponse response) {
        try {
            SessionData.verifyBack(request,response);
            return ValueUtil.toJson("salesNetwork", productService.delete(productNumber));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }

    }
    @RequestMapping(value = "/productUpdate",method = RequestMethod.POST)
    public String update(Product product,HttpServletRequest request, HttpServletResponse response) {
        try {
            SessionData.verifyBack(request,response);
            return ValueUtil.toJson("salesNetwork", productService.update(product));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }

    }
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletRequest request, HttpServletResponse response){
        try {
            SessionData.verifyBack(request,response);
            Map<String,Object>  result = productService.show(parm);
            PageModel pageModel = new PageModel((Page) result.get("page"));
            pageModel.setCondition(result.get("condition"));
            return  ValueUtil.toJson("organizationUser",pageModel);
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(@RequestParam Integer id,HttpServletRequest request, HttpServletResponse response){
        try {
            SessionData.verifyBack(request,response);
            return ValueUtil.toJson("Product",productService.index(id));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }
}
