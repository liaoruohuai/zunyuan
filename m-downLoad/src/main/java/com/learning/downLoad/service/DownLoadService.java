package com.learning.downLoad.service;

import com.learning.login.entity.Saler;
import com.learning.login.repository.SalerRepository;
import com.learning.order.entity.Apply;
import com.learning.order.entity.Orders;
import com.learning.order.repository.ApplyRepository;
import com.learning.order.repository.OrderRepository;
import com.learning.order.service.ApplyService;
import com.learning.util.basic.ObjectUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:下载的service
 */
@Service
public class DownLoadService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SalerRepository salerRepository;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private ApplyService applyService;

    public void salerDownLoad(HttpServletResponse response){
        try {
            List<Saler> salers = salerRepository.findAll();
            HSSFWorkbook workbook=new HSSFWorkbook();
            HSSFCellStyle cellStyle = getCellStyle(workbook);
            HSSFSheet sheet = workbook.createSheet();
            setSalerSheetContent(salers,sheet,cellStyle);
            String filePath="m-downLoad\\src\\main\\java\\com\\hzbuvi\\downLoad\\file\\saler.xlsx";
            FileOutputStream out=new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            download(filePath,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void orderDownLoad(HttpServletResponse response){
        try {
            List<Orders> orders = orderRepository.findAll();
            HSSFWorkbook workbook=new HSSFWorkbook();
            HSSFCellStyle cellStyle = getCellStyle(workbook);
            HSSFSheet sheet = workbook.createSheet();
            setOrderSheetContent(orders,sheet,cellStyle);
//            String filePath="C:\\excel\\orders.xlsx";
            String filePath="m-downLoad\\src\\main\\java\\com\\hzbuvi\\downLoad\\file\\orders.xlsx";
            FileOutputStream out=new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            download(filePath,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HSSFWorkbook applyDownLoad(Map<String, String> param){
        try {
            List<Apply> applys = applyRepository.findAll(applyService.getWhereClause(param.get("mobile"),param.get("applyDate"),param.get("applyType"),param.get("salesId")));
            HSSFWorkbook workbook=new HSSFWorkbook();
            HSSFCellStyle cellStyle = getCellStyle(workbook);
            HSSFSheet sheet = workbook.createSheet();
            setApplySheetContent(applys,sheet,cellStyle);
//            String filePath="C:\\excel\\orders.xlsx";
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HSSFCellStyle getCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        return cellStyle;
    }

    private void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private HSSFSheet setSalerSheetContent(List<Saler> salers,HSSFSheet sheet,HSSFCellStyle cellStyle){
        List<String> names= Arrays.asList("销售人员id","手机号码","姓名","网点编号");
        setRow(0,names,sheet,cellStyle);
        for(int i=1;i<=salers.size();i++){
            List<String> list=Arrays.asList(salers.get(i-1).getSalerId(),salers.get(i-1).getSalerPhone(),
                    salers.get(i-1).getSalerName(),salers.get(i-1).getNetNumber());
            setRow(i,list,sheet,cellStyle);
        }
        return sheet;
    }

    private HSSFSheet setOrderSheetContent(List<Orders> orders,HSSFSheet sheet,HSSFCellStyle cellStyle){
        List<String> names= Arrays.asList("订单号","订单类型","交易时间","交易状态","销售码","原订单号","网店号","所属机构号","商品编号","是否删除");
        setRow(0,names,sheet,cellStyle);
        for(int i=1;i<=orders.size();i++){
            String sellCode=setDefultIfNUll(orders.get(i-1).getSaleCode());
            String shopId=setDefultIfNUll(orders.get(i-1).getShopId());
            String orgNumber=setDefultIfNUll(orders.get(i-1).getOrgNumber());
            List<String> list=Arrays.asList(orders.get(i-1).getOrderId(),orders.get(i-1).getOrderType(),orders.get(i-1).getTradeTime()
                    ,orders.get(i-1).getTradeStatus(),sellCode,orders.get(i-1).getOrigOrderId()
                    ,shopId,orgNumber,orders.get(i-1).getProductId().toString(),orders.get(i-1).getIsDelete());
            setRow(i,list,sheet,cellStyle);
        }
        return sheet;
    }

    private HSSFSheet setApplySheetContent(List<Apply> applys,HSSFSheet sheet,HSSFCellStyle cellStyle){
        List<String> names= Arrays.asList("姓名","手机","地址");
        setRow(0,names,sheet,cellStyle);
        for(int i=1;i<=applys.size();i++){
            List<String> list=Arrays.asList(applys.get(i-1).getName(),applys.get(i-1).getMobile(),applys.get(i-1).getAddress());
            setRow(i,list,sheet,cellStyle);
        }
        return sheet;
    }

    private String setDefultIfNUll(String str){
        if (ObjectUtil.isEmpty(str)){
            return "";
        }
        return str;
    }

    private HSSFRow setRow(Integer n, List<? extends Object> list, HSSFSheet sheet,HSSFCellStyle cellStyle){
        HSSFRow row = sheet.createRow(n);
        for(int i=0;i<list.size();i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(String.valueOf(list.get(i)));
        }
        return row;
    }

   /* protected void downloadExcel(HSSFWorkbook workbook, HttpServletResponse response, String filename) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/msexcel;charset=UTF-8");
        workbook.write(out);
        out.close();
    }*/
}
