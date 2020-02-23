package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.DepotHeadMapperEx;
import com.jsh.erp.datasource.mappers.MaterialMapper;
import com.jsh.erp.datasource.mappers.MsgMapperEx;
import com.jsh.erp.datasource.mappers.SupplierMapper;
import com.jsh.erp.datasource.vo.*;
import com.jsh.erp.exception.BusinessParamCheckingException;
import com.jsh.erp.service.depotHead.DepotHeadService;
import com.jsh.erp.service.msg.MsgService;
import com.jsh.erp.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;
import static com.jsh.erp.utils.Tools.getNow3;

/**
 * @author ji-sheng-hua 752*718*920
 */
@RestController
@RequestMapping(value = "/depotHead")
public class DepotHeadController {
    private Logger logger = LoggerFactory.getLogger(DepotHeadController.class);

    @Resource
    private DepotHeadService depotHeadService;
    @Resource
    private DepotHeadMapperEx depotHeadMapperEx;
    @Resource
    private MsgService msgService;

    /**
     * 批量设置状态-审核或者反审核
     * @param status
     * @param depotHeadIDs
     * @param request
     * @return
     */
    @PostMapping(value = "/batchSetStatus")
    public String batchSetStatus(@RequestParam("status") String status,
                                 @RequestParam("depotHeadIDs") String depotHeadIDs,
                                 HttpServletRequest request) throws Exception{
        Map<String, Object> objectMap = new HashMap<String, Object>();
//        if (status.equals("3")){
//            return returnJson(objectMap, ErpInfo.ERROR.name, 300);
//        }
        if (depotHeadIDs.equals("")){
            depotHeadIDs = status;
        }
        int res = depotHeadService.batchSetStatus(status, depotHeadIDs);
        if(res > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    /**
     * 单据编号生成接口
     * @param request
     * @return
     */
    @GetMapping(value = "/buildNumber")
    public BaseResponseInfo buildNumber(HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String number = depotHeadService.buildOnlyNumber();
            map.put("DefaultNumber", number);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 获取最大的id
     * @param request
     * @return
     */
    @GetMapping(value = "/getMaxId")
    public BaseResponseInfo getMaxId(HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long maxId = depotHeadService.getMaxId();
            map.put("maxId", maxId);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 查找单据_根据月份(报表)
     * @param monthTime
     * @param request
     * @return
     */
    @GetMapping(value = "/findByMonth")
    public BaseResponseInfo findByMonth(@RequestParam("monthTime") String monthTime,
                                        HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<DepotHead> dataList = depotHeadService.findByMonth(monthTime);
            String headId = "";
            if (null != dataList) {
                for (DepotHead depotHead : dataList) {
                    headId = headId + depotHead.getId() + ",";
                }
            }
            if (headId != "") {
                headId = headId.substring(0, headId.lastIndexOf(","));
            }
            map.put("HeadIds", headId);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 入库出库明细接口
     * @param currentPage
     * @param pageSize
     * @param oId
     * @param pid
     * @param dids
     * @param beginTime
     * @param endTime
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "/findInDetail")
    public BaseResponseInfo findInDetail(@RequestParam("currentPage") Integer currentPage,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam("organId") Integer oId,
                                        @RequestParam("projectId") Integer pid,
                                        @RequestParam("depotIds") String dids,
                                        @RequestParam("beginTime") String beginTime,
                                        @RequestParam("endTime") String endTime,
                                        @RequestParam("type") String type,
                                        HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<DepotHeadVo4InDetail> resList = new ArrayList<DepotHeadVo4InDetail>();
            List<DepotHeadVo4InDetail> list = depotHeadService.findByAll(beginTime, endTime, type, pid, dids, oId, (currentPage-1)*pageSize, pageSize);
            int total = depotHeadService.findByAllCount(beginTime, endTime, type, pid, dids, oId);
            map.put("total", total);
            //存放数据json数组
            if (null != list) {
                for (DepotHeadVo4InDetail dhd : list) {
                    resList.add(dhd);
                }
            }
            map.put("rows", resList);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 入库出库统计接口
     * @param currentPage
     * @param pageSize
     * @param oId
     * @param pid
     * @param dids
     * @param beginTime
     * @param endTime
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "/findInOutMaterialCount")
    public BaseResponseInfo findInOutMaterialCount(@RequestParam("currentPage") Integer currentPage,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("organId") Integer oId,
                                         @RequestParam("projectId") Integer pid,
                                         @RequestParam("depotIds") String dids,
                                         @RequestParam("beginTime") String beginTime,
                                         @RequestParam("endTime") String endTime,
                                         @RequestParam("type") String type,
                                         HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<DepotHeadVo4InOutMCount> resList = new ArrayList<DepotHeadVo4InOutMCount>();
            List<DepotHeadVo4InOutMCount> list = depotHeadService.findInOutMaterialCount(beginTime, endTime, type, pid, dids, oId, (currentPage-1)*pageSize, pageSize);
            int total = depotHeadService.findInOutMaterialCountTotal(beginTime, endTime, type, pid, dids, oId);
            map.put("total", total);
            //存放数据json数组
            if (null != list) {
                for (DepotHeadVo4InOutMCount dhc : list) {
                    resList.add(dhc);
                }
            }
            map.put("rows", resList);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 对账单接口
     * @param currentPage
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @param organId
     * @param supType
     * @param request
     * @return
     */
    @GetMapping(value = "/findStatementAccount")
    public BaseResponseInfo findStatementAccount(@RequestParam("currentPage") Integer currentPage,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   @RequestParam("beginTime") String beginTime,
                                                   @RequestParam("endTime") String endTime,
                                                   @RequestParam("organId") Integer organId,
                                                   @RequestParam("supType") String supType,
                                                   HttpServletRequest request) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int j = 1;
            if (supType.equals("客户")) { //客户
                j = 1;
            } else if (supType.equals("供应商")) { //供应商
                j = -1;
            }
            List<DepotHeadVo4StatementAccount> resList = new ArrayList<DepotHeadVo4StatementAccount>();
            List<DepotHeadVo4StatementAccount> list = depotHeadService.findStatementAccount(beginTime, endTime, organId, supType, (currentPage-1)*pageSize, pageSize);
            int total = depotHeadService.findStatementAccountCount(beginTime, endTime, organId, supType);
            map.put("total", total);
            //存放数据json数组
            if (null != list) {
                for (DepotHeadVo4StatementAccount dha : list) {
                    dha.setNumber(dha.getNumber()); //单据编号
                    dha.setType(dha.getType()); //类型
                    String type = dha.getType();
                    BigDecimal p1 = BigDecimal.ZERO ;
                    BigDecimal p2 = BigDecimal.ZERO;
                    if (dha.getDiscountLastMoney() != null) {
                        p1 = dha.getDiscountLastMoney();
                    }
                    if (dha.getChangeAmount() != null) {
                        p2 = dha.getChangeAmount();
                    }
                    BigDecimal allPrice = BigDecimal.ZERO;
                    if ((p1.compareTo(BigDecimal.ZERO))==-1) {
                        p1 = p1.abs();
                    }
                    if ((p2 .compareTo(BigDecimal.ZERO))==-1) {
                        p2 = p2.abs();
                    }
                    if (type.equals("采购入库")) {
                        allPrice = p2 .subtract(p1);
                    } else if (type.equals("销售退货入库")) {
                        allPrice = p2 .subtract(p1);
                    } else if (type.equals("销售出库")) {
                        allPrice = p1 .subtract(p2);
                    } else if (type.equals("采购退货出库")) {
                        allPrice = p1 .subtract(p2);
                    } else if (type.equals("付款")) {
                        allPrice = p1.add(p2);
                    } else if (type.equals("收款")) {
                        allPrice = BigDecimal.ZERO.subtract(p1.add(p2));
                    } else if (type.equals("收入")) {
                        allPrice =  p1 .subtract(p2);
                    } else if (type.equals("支出")) {
                        allPrice = p2 .subtract(p1);
                    }
                    dha.setDiscountLastMoney(p1); //金额
                    dha.setChangeAmount(p2); //金额
                    DecimalFormat df = new DecimalFormat(".##");
                    dha.setAllPrice(new BigDecimal(df.format(allPrice .multiply(new BigDecimal(j))))); //计算后的金额
                    dha.setSupplierName(dha.getSupplierName()); //供应商
                    dha.setoTime(dha.getoTime()); //入库出库日期
                    resList.add(dha);
                }
            }
            map.put("rows", resList);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 查询单位的累计应收和累计应付，零售不能计入
     * @param supplierId
     * @param endTime
     * @param supType
     * @param request
     * @return
     */
    @GetMapping(value = "/findTotalPay")
    public BaseResponseInfo findTotalPay(@RequestParam("supplierId") Integer supplierId,
                                                 @RequestParam("endTime") String endTime,
                                                 @RequestParam("supType") String supType,
                                                 HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject outer = new JSONObject();
            BigDecimal sum = depotHeadService.findTotalPay(supplierId, endTime, supType);
            outer.put("getAllMoney", sum);
            map.put("rows", outer);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据编号查询单据信息
     * @param number
     * @param request
     * @return
     */
    @GetMapping(value = "/getDetailByNumber")
    public BaseResponseInfo getDetailByNumber(@RequestParam("number") String number,
                                         HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        DepotHeadVo4List dhl = new DepotHeadVo4List();
        try {
            List<DepotHeadVo4List> list = depotHeadService.getDetailByNumber(number);
            if(list.size() == 1) {
                dhl = list.get(0);
            }
            res.code = 200;
            res.data = dhl;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * create by: cjl
     * description:
     *  新增单据主表及单据子表信息
     * create time: 2019/1/25 14:36
     * @Param: beanJson
     * @Param: inserted
     * @Param: deleted
     * @Param: updated
     * @return java.lang.String
     */
    @RequestMapping(value = "/addDepotHeadAndDetail")
    public Object addDepotHeadAndDetail(@RequestParam("info") String beanJson,@RequestParam("inserted") String inserted,
                          @RequestParam("deleted") String deleted,
                          @RequestParam("updated") String updated, HttpServletRequest request) throws  Exception{
        JSONObject result = ExceptionConstants.standardSuccess();
        Long billsNumLimit = Long.parseLong(request.getSession().getAttribute("billsNumLimit").toString());
        Long tenantId = Long.parseLong(request.getSession().getAttribute("tenantId").toString());
        Long count = depotHeadService.countDepotHead(null,null,null,null,null,null,null);
        if(count>= billsNumLimit) {
            throw new BusinessParamCheckingException(ExceptionConstants.DEPOT_HEAD_OVER_LIMIT_FAILED_CODE,
                    ExceptionConstants.DEPOT_HEAD_OVER_LIMIT_FAILED_MSG);
        } else {
            depotHeadService.addDepotHeadAndDetail(beanJson,inserted,deleted,updated,tenantId);
        }
        return result;
    }
    /**
     * create by: cjl
     * description:
     * 更新单据主表及单据子表信息
     * create time: 2019/1/28 14:47
     * @Param: id
     * @Param: beanJson
     * @Param: inserted
     * @Param: deleted
     * @Param: updated
     * @Param: preTotalPrice
     * @return java.lang.Object
     */
    @RequestMapping(value = "/updateDepotHeadAndDetail")
    public Object updateDepotHeadAndDetail(@RequestParam("id") Long id,
                                           @RequestParam("info") String beanJson,
                                           @RequestParam("inserted") String inserted,
                                           @RequestParam("deleted") String deleted,
                                           @RequestParam("updated") String updated,
                                           @RequestParam("preTotalPrice") BigDecimal preTotalPrice,
                                           HttpServletRequest request) throws  Exception{
        Long tenantId = Long.parseLong(request.getSession().getAttribute("tenantId").toString());
        JSONObject result = ExceptionConstants.standardSuccess();
        depotHeadService.updateDepotHeadAndDetail(id,beanJson,inserted,deleted,updated,preTotalPrice,tenantId);
        return result;
    }
    /**
     * create by: cjl
     * description:
     *  删除单据主表及子表信息
     * create time: 2019/1/28 17:29
     * @Param: id
     * @return java.lang.Object
     */
    @RequestMapping(value = "/deleteDepotHeadAndDetail")
    public Object deleteDepotHeadAndDetail(@RequestParam("id") Long id) throws  Exception{

        JSONObject result = ExceptionConstants.standardSuccess();
        depotHeadService.deleteDepotHeadAndDetail(id);
        return result;
    }
    /**
     * create by: cjl
     * description:
     *  删除单据主表及子表信息
     * create time: 2019/1/28 17:29
     * @Param: id
     * @return java.lang.Object
     */
    @RequestMapping(value = "/batchDeleteDepotHeadAndDetail")
    public Object batchDeleteDepotHeadAndDetail(@RequestParam("ids") String ids) throws  Exception{

        JSONObject result = ExceptionConstants.standardSuccess();
        depotHeadService.batchDeleteDepotHeadAndDetail(ids);
        return result;
    }

    /**
     * 统计今日销售额、本月销售额、本月进货额
     * @param request
     * @return
     */
    @GetMapping(value = "/getBuyAndSaleStatistics")
    public BaseResponseInfo getBuyAndSaleStatistics(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String today = Tools.getNow() + " 00:00:00";
            String firstDay = Tools.getCurrentMonth() + "-01 00:00:00";
            BigDecimal todaySale = depotHeadService.getBuyAndSaleStatistics("出库", "销售",
                    1, today, getNow3()); //今日销售出库
            BigDecimal todaySalebak = depotHeadService.getBuyAndSaleStatistics("入库", "销售退货",
                    1, today, getNow3()); //今日销售出库

            BigDecimal todayRetailSale = depotHeadService.getBuyAndSaleStatistics("出库", "零售",
                    0, today, getNow3()); //今日零售出库
            BigDecimal todayRetailSalebak = depotHeadService.getBuyAndSaleStatistics("入库", "零售退货",
                    0, today, getNow3()); //今日零售出库

            BigDecimal monthSale = depotHeadService.getBuyAndSaleStatistics("出库", "销售",
                    1,firstDay, getNow3()); //本月销售出库
            BigDecimal monthSalebak = depotHeadService.getBuyAndSaleStatistics("入库", "销售退货",
                    1,firstDay, getNow3()); //本月销售出库
            BigDecimal monthRetailSale = depotHeadService.getBuyAndSaleStatistics("出库", "零售",
                    0,firstDay, getNow3()); //本月零售出库
            BigDecimal monthRetailSalebak = depotHeadService.getBuyAndSaleStatistics("入库", "零售退货",
                    0,firstDay, getNow3()); //本月零售出库
            BigDecimal monthBuy = depotHeadService.getBuyAndSaleStatistics("入库", "采购",
                    1, firstDay, getNow3()); //本月采购入库
            map.put("todaySale", todaySale.add(todayRetailSale).subtract(todaySalebak).add(todayRetailSalebak));
            map.put("thisMonthSale", monthSale.add(monthRetailSale).add(monthRetailSalebak).subtract(monthSalebak));
            map.put("thisMonthBuy", monthBuy);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }


    @GetMapping(value = "/getSaleByYear")
    public BaseResponseInfo getSaleByYear(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String today = Tools.getNow() + " 00:00:00";
            String firstDay = Tools.getCurrentYear()+ "-01-01 00:00:00";


            BigDecimal Sale = depotHeadService.getBuyAndSaleStatistics("出库", "销售",
                    1,firstDay, getNow3()); //本年销售出库
            BigDecimal Salebak = depotHeadService.getBuyAndSaleStatistics("入库", "销售退货",
                    1,firstDay, getNow3()); //本年销售退货
            BigDecimal RetailSale = depotHeadService.getBuyAndSaleStatistics("出库", "零售",
                    0,firstDay, getNow3()); //本年零售出库
            BigDecimal RetailSalebak = depotHeadService.getBuyAndSaleStatistics("入库", "零售退货",
                    0,firstDay, getNow3()); //本年零售退货
//            BigDecimal monthBuy = depotHeadService.getBuyAndSaleStatistics("入库", "采购",
//                    1, firstDay, getNow3()); //本月采购入库
            map.put("sale", Sale.subtract(Salebak));
            map.put("retailSale",RetailSale.add(RetailSalebak));
//            map.put("thisMonthBuy", monthBuy);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }



    @GetMapping(value = "/getOrderByYear")
    public BaseResponseInfo getOrderByYear(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        Long tenantId = Long.parseLong(request.getSession().getAttribute("tenantId").toString());
        try {
            String today = Tools.getNow() + " 00:00:00";
            String firstDay = Tools.getCurrentYear()+ "-01-01 00:00:00";
            DepotHeadOrder order=depotHeadService.getOrderByYear("其它","销售订单",tenantId,firstDay,today);
            res.code = 200;
            res.data = order;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    @GetMapping(value = "/getSaleByQuarter")
    public BaseResponseInfo getSaleByQuarter(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        Long tenantId = Long.parseLong(request.getSession().getAttribute("tenantId").toString());
        try {
            String year=Tools.getCurrentYear();

            Integer years=Integer.parseInt(year);

            List<DepotHeadQuarter> depoList= new ArrayList<>();
            List<DepotHeadQuarter> list=depotHeadService.getSaleByYear("出库","销售",tenantId, year);
            for (int i = 0; i < list.size(); i++) {
                DepotHeadQuarter q= list.get(i);
                if(Integer.parseInt(q.getQuarter())>Tools.getQuarter()) continue;
                q.setQuarter(year+" Q"+q.getQuarter());
                depoList.add(q);
            }

            List<DepotHeadQuarter> list2=depotHeadService.getSaleByYear("出库","销售",tenantId, (years-1)+"");
            for (int i = 0; i < list2.size(); i++) {
                DepotHeadQuarter q= list2.get(i);
                q.setQuarter((years-1)+" Q"+q.getQuarter());
                depoList.add(q);
            }

            List<DepotHeadQuarter> list3=depotHeadService.getSaleByYear("出库","销售",tenantId, (years-2)+"");
            for (int i = 0; i < list3.size(); i++) {
                DepotHeadQuarter q= list3.get(i);
                if(Integer.parseInt(q.getQuarter())<Tools.getQuarter()) continue;
                q.setQuarter((years-2)+" Q"+q.getQuarter());
                depoList.add(q);
            }

            res.code = 200;
            res.data = depoList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }


    /**
     * 上传图片
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/uploadContract")
    public void uploadFile_project_img_infos(@RequestParam(value = "file", required = false) MultipartFile file,
                                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        //文件上传路径
        String path = request.getServletContext().getRealPath("/img/");
        logger.error("假钞1151"+path);
        //上传文件名
        String filename = file.getOriginalFilename();
        File filepath = new File(path, filename);
        Msg msg = new Msg();
        msg.setMsgContent(path);

        String names[] = filename.split("\\.");
        if (names.length >= 1) {
            String uuid = UUID.randomUUID().toString();
            filename = uuid + "." + names[names.length - 1];
            msg.setMsgTitle(filename);
        }
        //判断路径是否存在
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            file.transferTo(new File(path + File.separator + filename));
            //获取图片高度宽度
            File picFile = new File(path + File.separator + filename);
            BufferedImage bi = ImageIO.read(picFile);
            int hg = bi.getHeight();
            int wd = bi.getWidth();
            JSONObject jo = new JSONObject();
            //用&
            response.getWriter().write(filename + "&" + wd + "&" + hg);
            msgService.insertSelectiveMsg(msg);
        } catch (IOException e) {
            response.getWriter().write("error");
        }
    }
     /*
     * 查找订单表的单据编号下拉框
     * @param request
     * @return
     */
    @PostMapping(value = "/findDefaultNumber")
    public JSONArray getPersonByNumType(HttpServletRequest request)throws Exception {
        JSONArray dataArray = new JSONArray();
        try {
            List<DepotHead> depotHeadsList = depotHeadService.findDefaultNumber();
            if (null != depotHeadsList) {
                for (DepotHead depotHead : depotHeadsList) {
                    JSONObject item = new JSONObject();
                    item.put("id", depotHead.getId());
                    item.put("defaultnumber", depotHead.getDefaultnumber());
                    dataArray.add(item);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return dataArray;
    }
    @Resource
    private SupplierMapper supplierMapper;
    @GetMapping(value = "/exportDepotItemMSExcel")
    public int exportDepotItemMSExcel(String ids, HttpServletRequest request, HttpServletResponse response)throws Exception {
        int res = 0;
        try {
            String [] headIds=ids.split(",");
            //存放数据json数组
            String[] names = {"合同编号", "客户名称", "供应商","工程名称", "采购型号", "数量","单价", "总价", "合同总额",
                    "收款日期", "已收款金额", "未收款金额"};
            String title = "项目对账表";
            List<String[]> objects = new ArrayList<String[]>();
            for(int i=0;i<headIds.length;i++){
                List<DepotHeadVo4List> dataList = depotHeadMapperEx.selectByConditionDepotHead("其它","销售订单",null,null,null,null,null,0,100,Integer.parseInt(headIds[i]));
                if (null != dataList) {
                    for (DepotHeadVo4List diEx : dataList) {
                        String[] objs = new String[12];
                        objs[0] = diEx.getConyract_number() == null ? " " : diEx.getConyract_number().toString();
                        String companyName = supplierMapper.seleSupplier(Long.valueOf(diEx.getSupplier_id()));
                        objs[1] = companyName;//公司名称
                        objs[2] = "一虎电子";
                        objs[3] = diEx.getSupplier() == null ? "0" : diEx.getSupplier().toString();
                        String name = depotHeadMapperEx.findMaterialsListByHeaderId(diEx.getHeaderId());
                        objs[4] = name;//采购型号
                        objs[5] = diEx.getOperNumber() == null ? "0" : diEx.getOperNumber().toString();//数量
                        objs[6] = diEx.getUnitPrice() == null ? " " : diEx.getUnitPrice().toString();//单价
                        objs[7] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();
                        objs[8] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();
                        objs[9] = diEx.getInstaller_time() == null ? " " : diEx.getInstaller_time().toString();
                        if (diEx.getPayment().equals("是")) {
                            objs[10] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();
                            objs[11] = "0.00";
                        } else {
                            objs[10] = "0.00";
                            objs[11] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();
                        }
                        objects.add(objs);
                    }
                    res = 1;
                }
            }
            File file = ExcelUtils.exportObjectsWithoutTitle(title, names, title, objects);
            ExportExecUtil.showExec(file, file.getName(), response);
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    @Resource
    private MaterialMapper materialMapper;
    @GetMapping(value = "/exportDepotItemMSExcelBaoDin")
    public int exportDepotItemMSExcelBaoDin(String ids,HttpServletRequest request, HttpServletResponse response)throws Exception {
        int res = 0;
        try {
            String [] headIds=ids.split(",");
            //存放数据json数组
            String[] names = {"申请日期", "开票单位全称", "发票抬头","保定清研订单号（合同编号）", "发票类型", "订单总金额","收税分类编码+货物名称", "规格型号", "单位",
                    "数量", "含税价格", "含税开票金额","公司名称","纳税号","公司地址","公司电话","开户行","账号","备注"};
            String title = "清华研究院对账表";
            List<String[]> objects = new ArrayList<String[]>();
            for(int i=0;i<headIds.length;i++) {
                List<DepotHeadVo4List> dataList = depotHeadMapperEx.selectByConditionDepotHead("其它", "销售订单", null, null, null, null, null, 0, 100, Integer.parseInt(headIds[i]));
                if (null != dataList) {
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = formatter.format(currentTime);
                    for (DepotHeadVo4List diEx : dataList) {
                        Supplier companyName = supplierMapper.selectByPrimaryKey(Long.valueOf(diEx.getSupplier_id()));//公司名称
                        String name = depotHeadMapperEx.findMaterialsListByHeaderId(diEx.getHeaderId());//采购型号
                        String[] objs = new String[19];
                        objs[0] = dateString;//申请日期
                        objs[1] = "保定清研物联网科技有限公司";//开票单位全称
                        objs[2] = companyName.getSupplier();//公司名称
                        objs[3] = diEx.getConyract_number() == null ? " " : diEx.getConyract_number().toString();//保定清研订单号（合同编号）
                        objs[4] = diEx.getConyract_money() == null ? " " : diEx.getConyract_money().toString();//发票类型
                        objs[5] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();
                        ;//订单总金额
//                    for (int i = 1;i<=2;i++){
//                        Material material = new Material();
//                        if (i == 1){
//                            if (diEx.getMachine_type() != null || diEx.getMachine_type() != ""){
////                                List<Material> materialList = materialMapper.machineSeleAll(material);
////                                material.setName();
//                            }
//                        }
//                    }

                        Material material = materialMapper.selectByPrimaryKey(diEx.getMaterialId());
                        objs[6] = diEx.getConyract_money() == null ? " " : "计算机网络设备，"+material.getName();//收税分类编码+货物名称
                        objs[7] = "";//规格型号
                        objs[8] = diEx.getMunit() == null ? " " : "" + diEx.getMunit().toString();//单位
                        objs[9] = diEx.getOperNumber() == null ? "0" : diEx.getOperNumber().toString();//数量
                        objs[10] = diEx.getUnitPrice() == null ? " " : diEx.getUnitPrice().toString();//含税单价
                        objs[11] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();//含税总金额
                        objs[12] = companyName.getSupplier();//公司名称
                        objs[13] = companyName.getTaxnum();//纳税号
                        objs[14] = companyName.getAddress();//地址
                        objs[15] = companyName.getTelephone();//电话
                        objs[16] = companyName.getBankname();//开户会
                        objs[17] = companyName.getAccountnumber();//账号
                        objs[18] = diEx.getDescription() == null ? " " : diEx.getDescription().toString();//备注
                        objects.add(objs);
                    }
                    res = 1;

                }
            }
            File file = ExcelUtils.exportObjectsWithoutTitle(title, names, title, objects);
            ExportExecUtil.showExec(file, file.getName(), response);
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    @GetMapping(value = "/exportDepotItemFinanceExcel")
    public int exportDepotItemFinanceExcel(String ids,HttpServletRequest request, HttpServletResponse response)throws Exception {
        int res = 0;
        try {
            String [] headIds=ids.split(",");
            //存放数据json数组
            String[] names = {"开票日期", "发票类型","商品名称","单位", "数量", "含税价格", "含税开票金额","公司名称","纳税号","公司地址","公司电话","开户行","账号","项目地址","备注"};
            String title = "财务与项目对账表";
            List<String[]> objects = new ArrayList<String[]>();
            for(int i=0;i<headIds.length;i++) {
                List<DepotHeadVo4List> dataList = depotHeadMapperEx.selectByConditionDepotHead("其它", "销售订单", null, null, null, null, null, 0, 100, Integer.parseInt(headIds[i]));
                if (null != dataList) {
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = formatter.format(currentTime);
                    for (DepotHeadVo4List diEx : dataList) {
                        Supplier companyName = supplierMapper.selectByPrimaryKey(Long.valueOf(diEx.getSupplier_id()));//公司名称
                        String name = depotHeadMapperEx.findMaterialsListByHeaderId(diEx.getHeaderId());//采购型号
                        String[] objs = new String[15];
                        objs[0] = diEx.getInvoiceData();//申请日期
                        objs[1] =diEx.getConyract_money();//开票类型
                        Material material = materialMapper.selectByPrimaryKey(diEx.getMaterialId());
                        objs[2] = material.getName();//公司名称
                        objs[3] = diEx.getMunit() == null ? " " : "" + diEx.getMunit().toString();//单位
                        objs[4] = diEx.getOperNumber() == null ? "0" : diEx.getOperNumber().toString();//数量
                        objs[5] = diEx.getUnitPrice() == null ? " " : diEx.getUnitPrice().toString();//含税单价
                        objs[6] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();//含税总金额
                        objs[7] = companyName.getSupplier();//公司名称
                        objs[8] = companyName.getTaxnum();//纳税号
                        objs[9] = companyName.getAddress();//地址
                        objs[10] = companyName.getTelephone();//电话
                        objs[11] = companyName.getBankname();//开户会
                        objs[12] = companyName.getAccountnumber();//账号
                        objs[13] = diEx.getAddress();//项目地址
                        objs[14] = diEx.getDescription() == null ? " " : diEx.getDescription().toString();//备注
                        objects.add(objs);
                    }
                    res = 1;

                }
            }
            File file = ExcelUtils.exportObjectsWithoutTitle(title, names, title, objects);
            ExportExecUtil.showExec(file, file.getName(), response);
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }


    @GetMapping(value = "/exportDepotItemSupplierExcel")
    public int exportDepotItemSupplierExcel(String ids,HttpServletRequest request, HttpServletResponse response)throws Exception {
        int res = 0;
        try {
            String [] headIds=ids.split(",");
            //存放数据json数组
            String[] names = {"单据日期", "品名","数量","规格", "单价", "供应商","总价","备注"};
            String title = "与供应商对账表";
            List<String[]> objects = new ArrayList<String[]>();
            for(int i=0;i<headIds.length;i++) {
                List<DepotHeadVo4List> dataList = depotHeadMapperEx.selectByConditionDepotHead("其它", "采购订单", null, null, null, null, null, 0, 100, Integer.parseInt(headIds[i]));
                if (null != dataList) {
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = formatter.format(currentTime);
                    for (DepotHeadVo4List diEx : dataList) {
                        Supplier companyName = supplierMapper.selectByPrimaryKey(Long.valueOf(diEx.getSupplier_id()));//公司名称
                        String name = depotHeadMapperEx.findMaterialsListByHeaderId(diEx.getHeaderId());//采购型号
                        Material material = materialMapper.selectByPrimaryKey(diEx.getMaterialId());
                        String[] objs = new String[8];
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date = simpleDateFormat.format(diEx.getCreatetime());
                        objs[0] = date;//单据日期
                        objs[1] =material.getName();//名称
                        objs[2] = diEx.getOperNumber() == null ? "0" : diEx.getOperNumber().toString();//数量
                        objs[3] = diEx.getMunit() == null ? " " : "" + diEx.getMunit().toString();//单位
                        objs[4] = diEx.getUnitPrice() == null ? " " : diEx.getUnitPrice().toString();//含税单价
                        objs[5] = supplierMapper.seleSupplier(diEx.getOrganid());//供应商
                        objs[6] = diEx.getTotalprice() == null ? " " : diEx.getTotalprice().setScale(0, BigDecimal.ROUND_DOWN).toString();//含税总金额
                        objs[7] = diEx.getDescription() == null ? " " : diEx.getDescription().toString();//备注
                        objects.add(objs);
                    }
                    res = 1;

                }
            }
            File file = ExcelUtils.exportObjectsWithoutTitle(title, names, title, objects);
            ExportExecUtil.showExec(file, file.getName(), response);
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

}
