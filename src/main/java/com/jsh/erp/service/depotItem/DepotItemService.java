package com.jsh.erp.service.depotItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.*;
import com.jsh.erp.datasource.vo.DepotItemStockWarningCount;
import com.jsh.erp.datasource.vo.DepotItemVo4Stock;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.service.log.LogService;
import com.jsh.erp.service.material.MaterialService;
import com.jsh.erp.service.msg.MsgService;
import com.jsh.erp.service.serialNumber.SerialNumberService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DepotItemService {
    private Logger logger = LoggerFactory.getLogger(DepotItemService.class);

    private final static String TYPE = "入库";
    private final static String SUM_TYPE = "number";
    private final static String IN = "in";
    private final static String OUT = "out";

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private MsgService msgService;
    @Resource
    private DepotItemMapper depotItemMapper;
    @Resource
    private DepotItemMapperEx depotItemMapperEx;
    @Resource
    private MaterialService materialService;
    @Resource
    SerialNumberMapperEx serialNumberMapperEx;
    @Resource
    private DepotHeadMapper depotHeadMapper;
    @Resource
    SerialNumberService serialNumberService;
    @Resource
    private UserService userService;
    @Resource
    private LogService logService;

    public DepotItem getDepotItem(long id)throws Exception {
        DepotItem result=null;
        try{
            result=depotItemMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<DepotItem> getDepotItem()throws Exception {
        DepotItemExample example = new DepotItemExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<DepotItem> list=null;
        try{
            list=depotItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<DepotItem> select(String name, Integer type, String remark, int offset, int rows)throws Exception {
        List<DepotItem> list=null;
        try{
            list=depotItemMapperEx.selectByConditionDepotItem(name, type, remark, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countDepotItem(String name, Integer type, String remark) throws Exception{
        Long result =null;
        try{
            result=depotItemMapperEx.countsByDepotItem(name, type, remark);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertDepotItem(String beanJson, HttpServletRequest request)throws Exception {
        DepotItem depotItem = JSONObject.parseObject(beanJson, DepotItem.class);
        int result =0;
        try{
            result=depotItemMapper.insertSelective(depotItem);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateDepotItem(String beanJson, Long id)throws Exception {
        DepotItem depotItem = JSONObject.parseObject(beanJson, DepotItem.class);
        depotItem.setId(id);
        int result =0;
        try{
            result=depotItemMapper.updateByPrimaryKeySelective(depotItem);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteDepotItem(Long id)throws Exception {
        int result =0;
        try{
            result=depotItemMapper.deleteByPrimaryKey(id);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteDepotItem(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        DepotItemExample example = new DepotItemExample();
        example.createCriteria().andIdIn(idList);
        int result =0;
        try{
            result=depotItemMapper.deleteByExample(example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        DepotItemExample example = new DepotItemExample();
        example.createCriteria().andIdNotEqualTo(id).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<DepotItem> list =null;
        try{
            list=depotItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public List<DepotItemVo4DetailByTypeAndMId> findDetailByTypeAndMaterialIdList(Map<String, String> map)throws Exception {
        String mIdStr = map.get("mId");
        Long mId = null;
        if(!StringUtil.isEmpty(mIdStr)) {
            mId = Long.parseLong(mIdStr);
        }
        List<DepotItemVo4DetailByTypeAndMId> list =null;
        try{
            list = depotItemMapperEx.findDetailByTypeAndMaterialIdList(mId, QueryUtils.offset(map), QueryUtils.rows(map));
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long findDetailByTypeAndMaterialIdCounts(Map<String, String> map)throws Exception {
        String mIdStr = map.get("mId");
        Long mId = null;
        if(!StringUtil.isEmpty(mIdStr)) {
            mId = Long.parseLong(mIdStr);
        }
        Long result =null;
        try{
            result = depotItemMapperEx.findDetailByTypeAndMaterialIdCounts(mId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertDepotItemWithObj(DepotItem depotItem)throws Exception {
        int result =0;
        try{
            result = depotItemMapper.insertSelective(depotItem);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateDepotItemWithObj(DepotItem depotItem)throws Exception {
        int result =0;
        try{
            result = depotItemMapper.updateByPrimaryKeySelective(depotItem);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateNumber2(DepotItem depotItem)throws Exception {
        int result =0;
        try{
            result = depotItemMapper.updateNumber2(depotItem);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public List<DepotItemVo4WithInfoEx> getDetailList(Long headerId)throws Exception {
        List<DepotItemVo4WithInfoEx> list =null;
        try{
            list = depotItemMapperEx.getDetailList(headerId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<DepotItemVo4WithInfoEx> getDetailNumberList(String number)throws Exception {
        List<DepotItemVo4WithInfoEx> list =null;
        try{
            list = depotItemMapperEx.getDetailNumberList(number);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }


    public DepotItemVo4WithInfoEx findMachineTypeId(Long tId,Long mId)throws Exception {
        DepotItemVo4WithInfoEx depotItemVo4WithInfoEx =null;
        try{
            depotItemVo4WithInfoEx = depotItemMapperEx.findMachineTypeId(tId,mId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return depotItemVo4WithInfoEx;
    }


    public DepotItemVo4WithInfoEx findGateTypeId(Long tId,Long mId)throws Exception {
        DepotItemVo4WithInfoEx depotItemVo4WithInfoEx =null;
        try{
            depotItemVo4WithInfoEx = depotItemMapperEx.findGateTypeId(tId,mId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return depotItemVo4WithInfoEx;
    }

    ;

    public List<DepotItemVo4WithInfoEx> findByAll(String headIds, String materialIds, Integer offset, Integer rows)throws Exception {
        List<DepotItemVo4WithInfoEx> list =null;
        try{
            list = depotItemMapperEx.findByAll(headIds, materialIds, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public int findByAllCount(String headIds, String materialIds)throws Exception {
        int result=0;
        try{
            result = depotItemMapperEx.findByAllCount(headIds, materialIds);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public BigDecimal buyOrSale(String type, String subType, Long MId, String MonthTime, String sumType) throws Exception{
        BigDecimal result= BigDecimal.ZERO;
        try{
            if (SUM_TYPE.equals(sumType)) {
                result= depotItemMapperEx.buyOrSaleNumber(type, subType, MId, MonthTime, sumType);
            } else {
                result= depotItemMapperEx.buyOrSalePrice(type, subType, MId, MonthTime, sumType);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if (result == null){
            result = BigDecimal.ZERO;
        }
        return result;

    }

    /**
     * 2019-02-02修改
     * 我之前对操作数量的理解有偏差
     * 这里重点重申一下：BasicNumber=OperNumber*ratio
     * */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public String saveDetials(String inserted, String deleted, String updated, Long headerId, Long tenantId) throws Exception{
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_DEPOT_ITEM,
                BusinessConstants.LOG_OPERATION_TYPE_ADD,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        //查询单据主表信息
        DepotHead depotHead=null;
        try{
            depotHead =depotHeadMapper.selectByPrimaryKey(headerId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        //获得当前操作人
        User userInfo=userService.getCurrentUser();
        //转为json
            JSONArray insertedJson = JSONArray.parseArray(inserted);
            JSONArray deletedJson = JSONArray.parseArray(deleted);
            JSONArray updatedJson = JSONArray.parseArray(updated);
            /**
             * 2019-01-28优先处理删除的
             * 删除的可以继续卖，删除的需要将使用的序列号回收
             * 插入的需要判断当前货源是否充足
             * 更新的需要判断货源是否充足
             * */
            if (null != deletedJson) {
                StringBuffer bf=new StringBuffer();
                for (int i = 0; i < deletedJson.size(); i++) {
                    //首先回收序列号，如果是调拨，不用处理序列号
                    JSONObject tempDeletedJson = JSONObject.parseObject(deletedJson.getString(i));
                    if(BusinessConstants.DEPOTHEAD_TYPE_OUT.equals(depotHead.getType())
                            &&!BusinessConstants.SUB_TYPE_TRANSFER.equals(depotHead.getSubtype())){
                        DepotItem depotItem = getDepotItem(tempDeletedJson.getLong("Id"));
                        if(depotItem==null){
                            continue;
                        }
                        /**
                         * 判断商品是否开启序列号，开启的收回序列号，未开启的跳过
                         * */
                        Material material= materialService.getMaterial(depotItem.getMaterialid());
                        if(material==null){
                            continue;
                        }
                        if(BusinessConstants.ENABLE_SERIAL_NUMBER_ENABLED.equals(material.getEnableserialnumber())){
                            serialNumberService.cancelSerialNumber(depotItem.getMaterialid(),depotItem.getHeaderid(),(depotItem.getBasicnumber()==null?0:depotItem.getBasicnumber()).intValue(),
                                    userInfo);
                        }
                    }
                    this.deleteDepotItem(tempDeletedJson.getLong("Id"));
                    bf.append(tempDeletedJson.getLong("Id"));
                    if(i<(deletedJson.size()-1)){
                        bf.append(",");
                    }
                }
                this.batchDeleteDepotItemByIds(bf.toString());
            }
            if (null != insertedJson) {
                for (int i = 0; i < insertedJson.size(); i++) {
                    DepotItem depotItem = new DepotItem();
                    JSONObject tempInsertedJson = JSONObject.parseObject(insertedJson.getString(i));
                    depotItem.setHeaderid(headerId);
                    depotItem.setMaterialid(tempInsertedJson.getLong("MaterialId"));
                    depotItem.setMunit(tempInsertedJson.getString("Unit"));
                    depotItem.setGate("否");
                    depotItem.setInstall("否");
                    depotItem.setInvoice("否");
                    depotItem.setPayment("否");
                    depotItem.setContract("否");
                    if (tempInsertedJson.getString("machine_type_model") != null && !tempInsertedJson.getString("machine_type_model").equals("") && !tempInsertedJson.getString("machine_type_model").equals("无")) {
                        depotItem.setMachinetype_id(Long.valueOf(tempInsertedJson.getString("machine_type_model")));
                    }
                    if (tempInsertedJson.getString("gate_type_model") != null && !tempInsertedJson.getString("gate_type_model").equals("") && !tempInsertedJson.getString("gate_type_model").equals("无")) {
                        depotItem.setGatetype_id(Long.valueOf(tempInsertedJson.getString("gate_type_model")));
                    }
                    if (tempInsertedJson.getInteger("machine_number") != null) {
                        Integer ii = tempInsertedJson.getInteger("OperNumber");//数量
                        Integer kk = tempInsertedJson.getInteger("machine_number");//人脸机数量
                        depotItem.setMachine_number2(ii * kk);
                    }
                    if (tempInsertedJson.getInteger("gate_number") != null) {
                        Integer ii = tempInsertedJson.getInteger("OperNumber");//数量
                        Integer ee = tempInsertedJson.getInteger("gate_number");//闸机数量
                        depotItem.setGate_number2(ii * ee);
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("OperNumber").toString())) {
                        depotItem.setOpernumber(tempInsertedJson.getBigDecimal("OperNumber"));
                        try {
                            String Unit = tempInsertedJson.get("Unit").toString();
                            BigDecimal oNumber = tempInsertedJson.getBigDecimal("OperNumber");
                            Long mId = Long.parseLong(tempInsertedJson.get("MaterialId").toString());
                            /***
                             * 为什么调用的方法要先把基础单位去掉，去掉之后后续还能获取到？
                             * */
                            //以下进行单位换算
//                            String UnitName = findUnitName(mId); //查询计量单位名称
                            String unitName = materialService.findUnitName(mId);
                            if (!StringUtil.isEmpty(unitName)) {
                                String unitList = unitName.substring(0, unitName.indexOf("("));
                                String ratioList = unitName.substring(unitName.indexOf("("));
                                String basicUnit = unitList.substring(0, unitList.indexOf(",")); //基本单位
                                String otherUnit = unitList.substring(unitList.indexOf(",") + 1); //副单位
                                Integer ratio = Integer.parseInt(ratioList.substring(ratioList.indexOf(":") + 1).replace(")", "")); //比例
                                if (Unit.equals(basicUnit)) { //如果等于基础单位
                                    depotItem.setBasicnumber(oNumber); //数量一致
                                } else if (Unit.equals(otherUnit)) { //如果等于副单位
                                    depotItem.setBasicnumber(oNumber.multiply(new BigDecimal(ratio))); //数量乘以比例
                                }
                            } else {
                                depotItem.setBasicnumber(oNumber); //其他情况
                            }
                        } catch (Exception e) {
                            logger.error(">>>>>>>>>>>>>>>>>>>设置基础数量异常", e);
                        }
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("UnitPrice").toString())) {
                        depotItem.setUnitprice(tempInsertedJson.getBigDecimal("UnitPrice"));
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("TaxUnitPrice").toString())) {
                        depotItem.setTaxunitprice(tempInsertedJson.getBigDecimal("TaxUnitPrice"));
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("AllPrice").toString())) {
                        depotItem.setAllprice(tempInsertedJson.getBigDecimal("AllPrice"));
                    }
                    depotItem.setRemark(tempInsertedJson.getString("Remark"));
                    if (tempInsertedJson.get("DepotId") != null && !StringUtil.isEmpty(tempInsertedJson.get("DepotId").toString())) {
                        depotItem.setDepotid(tempInsertedJson.getLong("DepotId"));
                    }
                    if (tempInsertedJson.get("AnotherDepotId") != null && !StringUtil.isEmpty(tempInsertedJson.get("AnotherDepotId").toString())) {
                        depotItem.setAnotherdepotid(tempInsertedJson.getLong("AnotherDepotId"));
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("TaxRate").toString())) {
                        depotItem.setTaxrate(tempInsertedJson.getBigDecimal("TaxRate"));
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("TaxMoney").toString())) {
                        depotItem.setTaxmoney(tempInsertedJson.getBigDecimal("TaxMoney"));
                    }
                    if (!StringUtil.isEmpty(tempInsertedJson.get("TaxLastMoney").toString())) {
                        depotItem.setTaxlastmoney(tempInsertedJson.getBigDecimal("TaxLastMoney"));
                    }
                    if (tempInsertedJson.get("OtherField1") != null) {
                        depotItem.setOtherfield1(tempInsertedJson.getString("OtherField1"));
                    }
                    if (tempInsertedJson.get("OtherField2") != null) {
                        depotItem.setOtherfield2(tempInsertedJson.getString("OtherField2"));
                    }
                    if (tempInsertedJson.get("OtherField3") != null) {
                        depotItem.setOtherfield3(tempInsertedJson.getString("OtherField3"));
                    }
                    if (tempInsertedJson.get("OtherField4") != null) {
                        depotItem.setOtherfield4(tempInsertedJson.getString("OtherField4"));
                    }
                    if (tempInsertedJson.get("OtherField5") != null) {
                        depotItem.setOtherfield5(tempInsertedJson.getString("OtherField5"));
                    }
                    if (tempInsertedJson.get("MType") != null) {
                        depotItem.setMtype(tempInsertedJson.getString("MType"));
                    }
                    if (tempInsertedJson.get("contacts_name") != null) {
                        depotItem.setContacts_name(tempInsertedJson.getString("contacts_name"));
                    }
                    if (tempInsertedJson.get("contacts_phone") != null) {
                        depotItem.setContacts_phone(tempInsertedJson.getString("contacts_phone"));
                    }
                    if (tempInsertedJson.get("company") != null) {
                        depotItem.setCompany(tempInsertedJson.getString("company"));
                    }
                    if (tempInsertedJson.get("we_chat") != null) {
                        depotItem.setWe_chat(tempInsertedJson.getString("we_chat"));
                    }
                    if (tempInsertedJson.get("project_address") != null) {
                        depotItem.setProject_address(tempInsertedJson.getString("project_address"));
                    }
                    if (tempInsertedJson.get("project_name") != null) {
                        depotItem.setProject_name(tempInsertedJson.getString("project_name"));
                    }
                    if (tempInsertedJson.get("card_ognizer") != null) {
                        depotItem.setCard_ognizer(tempInsertedJson.getString("card_ognizer"));
                    }
                    if (tempInsertedJson.get("ognizer_number") != null) {
                        depotItem.setOgnizer_number(tempInsertedJson.getString("ognizer_number"));
                    }
                    if (tempInsertedJson.get("contract") != null) {
                        depotItem.setContract(tempInsertedJson.getString("contract"));
                    }
                    if (tempInsertedJson.get("conyract_number") != null) {
                        depotItem.setConyract_number(tempInsertedJson.getString("conyract_number"));
                    }
                    if (tempInsertedJson.get("conyract_money") != null) {
                        depotItem.setConyract_money(tempInsertedJson.getString("conyract_money"));
                    }
                    if (tempInsertedJson.get("payment") != null) {
                        depotItem.setPayment(tempInsertedJson.getString("payment"));
                    }
                    if (tempInsertedJson.get("invoice") != null) {
                        depotItem.setInvoice(tempInsertedJson.getString("invoice"));
                    }
                    if (tempInsertedJson.get("install") != null) {
                        depotItem.setInstall(tempInsertedJson.getString("install"));
                    }
                    if (tempInsertedJson.get("installer") != null) {
                        depotItem.setInstaller(tempInsertedJson.getString("installer"));
                    }
                    if (tempInsertedJson.get("installer_time") != null) {
                        depotItem.setInstaller_time(tempInsertedJson.getString("installer_time"));
                    }
                    if (tempInsertedJson.get("machine") != null) {
                        depotItem.setMachine(tempInsertedJson.getString("machine"));
                    }
                    if (tempInsertedJson.get("machine_type") != null) {
                        Material material = materialMapper.selectByPrimaryKey(Long.valueOf(tempInsertedJson.getString("machine_type_model")));
                        depotItem.setMachine_type(material.getName()+material.getModel());
                    }
                    if (tempInsertedJson.get("machine_number") != null) {
                        depotItem.setMachine_number(tempInsertedJson.getString("machine_number"));
                    }
                    if (tempInsertedJson.get("gate") != null) {
                        depotItem.setGate(tempInsertedJson.getString("gate"));
                    }
                    if (tempInsertedJson.get("gate_type") != null) {
                        Material material = materialMapper.selectByPrimaryKey(Long.valueOf(tempInsertedJson.getString("gate_type_model")));
                        depotItem.setGate_type(material.getName()+material.getModel());
                    }
                    if (tempInsertedJson.get("gate_number") != null) {
                        depotItem.setGate_number(tempInsertedJson.getString("gate_number"));
                    }
                    if (tempInsertedJson.get("order_type") != null) {
                        depotItem.setOrder_type(tempInsertedJson.getString("order_type"));
                    }
                    /**
                     * 出库时判断库存是否充足
                     * */
                    if (BusinessConstants.DEPOTHEAD_TYPE_OUT.equals(depotHead.getType())) {
                        if (depotItem == null) {
                            continue;
                        }
                        Material material = materialService.getMaterial(depotItem.getMaterialid());
                        if (material == null) {
                            continue;
                        }
                        BigDecimal stock = getStockByParam(depotItem.getDepotid(), depotItem.getMaterialid(), null, null, tenantId);
                        BigDecimal thisBasicNumber = depotItem.getBasicnumber() == null ? BigDecimal.ZERO : depotItem.getBasicnumber();
                        if (stock.compareTo(thisBasicNumber) < 0) {
                            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_CODE,
                                    String.format(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_MSG, material == null ? "" : material.getName()));
                        }

                        /**出库时处理序列号*/
                        if (!BusinessConstants.SUB_TYPE_TRANSFER.equals(depotHead.getSubtype())) {
                            /**
                             * 判断商品是否开启序列号，开启的收回序列号，未开启的跳过
                             * */
                            if (BusinessConstants.ENABLE_SERIAL_NUMBER_ENABLED.equals(material.getEnableserialnumber())) {
                                //查询单据子表中开启序列号的数据列表
                                serialNumberService.checkAndUpdateSerialNumber(depotItem, userInfo);
                            }
                        }
                    }
                    this.insertDepotItemWithObj(depotItem);
                }
                Msg msg = new Msg();
                msg.setMsgTitle(String.valueOf(headerId));
                msg.setId(headerId);
                msgService.updateMsgContract(msg);
            }
            if (null != updatedJson) {
                for (int i = 0; i < updatedJson.size(); i++) {
                    JSONObject tempUpdatedJson = JSONObject.parseObject(updatedJson.getString(i));
                    DepotItem depotItem = this.getDepotItem(tempUpdatedJson.getLong("Id"));
                    if(depotItem==null){
                        continue;
                    }
                    Material material= materialService.getMaterial(depotItem.getMaterialid());
                    if(material==null){
                        continue;
                    }
                    //首先回收序列号
                    if(BusinessConstants.DEPOTHEAD_TYPE_OUT.equals(depotHead.getType())
                            &&!BusinessConstants.SUB_TYPE_TRANSFER.equals(depotHead.getSubtype())) {
                        /**
                         * 判断商品是否开启序列号，开启的收回序列号，未开启的跳过
                         * */
                        if(BusinessConstants.ENABLE_SERIAL_NUMBER_ENABLED.equals(material.getEnableserialnumber())) {
                            serialNumberService.cancelSerialNumber(depotItem.getMaterialid(), depotItem.getHeaderid(), (depotItem.getBasicnumber()==null?0:depotItem.getBasicnumber()).intValue(),
                                    userInfo);
                        }
                        /**收回序列号的时候释放库存*/
                        depotItem.setOpernumber(BigDecimal.ZERO);
                        depotItem.setBasicnumber(BigDecimal.ZERO);
                        this.updateDepotItemWithObj(depotItem);
                    }
                    depotItem.setId(tempUpdatedJson.getLong("Id"));
                    depotItem.setMaterialid(tempUpdatedJson.getLong("MaterialId"));
                    depotItem.setMunit(tempUpdatedJson.getString("Unit"));
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("OperNumber").toString())) {
                        depotItem.setOpernumber(tempUpdatedJson.getBigDecimal("OperNumber"));
                        try {
                            String Unit = tempUpdatedJson.get("Unit").toString();
                            BigDecimal oNumber = tempUpdatedJson.getBigDecimal("OperNumber");
                            Long mId = Long.parseLong(tempUpdatedJson.get("MaterialId").toString());
                            //以下进行单位换算
//                            String UnitName = findUnitName(mId); //查询计量单位名称
                            String unitName = materialService.findUnitName(mId);
                            if (!StringUtil.isEmpty(unitName)) {
                                String unitList = unitName.substring(0, unitName.indexOf("("));
                                String ratioList = unitName.substring(unitName.indexOf("("));
                                String basicUnit = unitList.substring(0, unitList.indexOf(",")); //基本单位
                                String otherUnit = unitList.substring(unitList.indexOf(",") + 1); //副单位
                                Integer ratio = Integer.parseInt(ratioList.substring(ratioList.indexOf(":") + 1).replace(")", "")); //比例
                                if (Unit.equals(basicUnit)) { //如果等于基础单位
                                    depotItem.setBasicnumber(oNumber); //数量一致
                                } else if (Unit.equals(otherUnit)) { //如果等于副单位
                                    depotItem.setBasicnumber(oNumber.multiply(new BigDecimal(ratio))); //数量乘以比例
                                }
                            } else {
                                depotItem.setBasicnumber(oNumber); //其他情况
                            }
                        } catch (Exception e) {
                            logger.error(">>>>>>>>>>>>>>>>>>>设置基础数量异常", e);
                        }
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("UnitPrice").toString())) {
                        depotItem.setUnitprice(tempUpdatedJson.getBigDecimal("UnitPrice"));
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("TaxUnitPrice").toString())) {
                        depotItem.setTaxunitprice(tempUpdatedJson.getBigDecimal("TaxUnitPrice"));
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("AllPrice").toString())) {
                        depotItem.setAllprice(tempUpdatedJson.getBigDecimal("AllPrice"));
                    }
                    depotItem.setRemark(tempUpdatedJson.getString("Remark"));
                    if (tempUpdatedJson.get("DepotId") != null && !StringUtil.isEmpty(tempUpdatedJson.get("DepotId").toString())) {
                        depotItem.setDepotid(tempUpdatedJson.getLong("DepotId"));
                    }
                    if (tempUpdatedJson.get("AnotherDepotId") != null && !StringUtil.isEmpty(tempUpdatedJson.get("AnotherDepotId").toString())) {
                        depotItem.setAnotherdepotid(tempUpdatedJson.getLong("AnotherDepotId"));
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("TaxRate").toString())) {
                        depotItem.setTaxrate(tempUpdatedJson.getBigDecimal("TaxRate"));
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("TaxMoney").toString())) {
                        depotItem.setTaxmoney(tempUpdatedJson.getBigDecimal("TaxMoney"));
                    }
                    if (!StringUtil.isEmpty(tempUpdatedJson.get("TaxLastMoney").toString())) {
                        depotItem.setTaxlastmoney(tempUpdatedJson.getBigDecimal("TaxLastMoney"));
                    }
                    depotItem.setOtherfield1(tempUpdatedJson.getString("OtherField1"));
                    depotItem.setOtherfield2(tempUpdatedJson.getString("OtherField2"));
                    depotItem.setOtherfield3(tempUpdatedJson.getString("OtherField3"));
                    depotItem.setOtherfield4(tempUpdatedJson.getString("OtherField4"));
                    depotItem.setOtherfield5(tempUpdatedJson.getString("OtherField5"));
                    depotItem.setMtype(tempUpdatedJson.getString("MType"));

                    /**
                     * create by: qiankunpingtai
                     * create time: 2019/3/25 15:18
                     * website：https://qiankunpingtai.cn
                     * description:
                     * 修改了商品类型时，库中的商品和页面传递的不同
                     * 这里需要重新获取页面传递的商品信息
                     */
                    if(!material.getId().equals(depotItem.getMaterialid())){
                        material= materialService.getMaterial(depotItem.getMaterialid());
                        if(material==null){
                            continue;
                        }
                    }
                    /**出库时处理序列号*/
                    if(BusinessConstants.DEPOTHEAD_TYPE_OUT.equals(depotHead.getType())){
                        BigDecimal stock = getStockByParam(depotItem.getDepotid(),depotItem.getMaterialid(),null,null,tenantId);
                        BigDecimal thisBasicNumber = depotItem.getBasicnumber()==null?BigDecimal.ZERO:depotItem.getBasicnumber();
                        if(stock.compareTo(thisBasicNumber)<0){
                            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_CODE,
                                    String.format(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_MSG,material==null?"":material.getName()));
                        }
                        if(!BusinessConstants.SUB_TYPE_TRANSFER.equals(depotHead.getSubtype())) {
                            /**
                             * 判断商品是否开启序列号，开启的收回序列号，未开启的跳过
                             * */
                            if(BusinessConstants.ENABLE_SERIAL_NUMBER_ENABLED.equals(material.getEnableserialnumber())) {
                                //查询单据子表中开启序列号的数据列表
                                serialNumberService.checkAndUpdateSerialNumber(depotItem, userInfo);
                            }
                        }
                    }
                    this.updateDepotItemWithObj(depotItem);
                }
            }
        return null;
    }
    /**
     * 查询计量单位信息
     *
     * @return
     */
    public String findUnitName(Long mId) throws Exception{
        String unitName = "";
        try {
            unitName = materialService.findUnitName(mId);
            if (unitName != null) {
                unitName = unitName.substring(1, unitName.length() - 1);
                if (unitName.equals("null")) {
                    unitName = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unitName;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteDepotItemByIds(String ids)throws Exception {
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_DEPOT_ITEM,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo=userService.getCurrentUser();
        String [] idArray=ids.split(",");
        int  result =0;
        try{
            result =depotItemMapperEx.batchDeleteDepotItemByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public List<DepotItemStockWarningCount> findStockWarningCount(int offset, Integer rows, Integer pid) {

        List<DepotItemStockWarningCount> list = null;
        try{
            list =depotItemMapperEx.findStockWarningCount( offset, rows, pid);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int findStockWarningCountTotal(Integer pid) {
        int result = 0;
        try{
            result =depotItemMapperEx.findStockWarningCountTotal(pid);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    /**
     * 统计该商品已分批出库的总数量-用于订单
     * @param mid
     * @param headerId
     * @return
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BigDecimal getFinishNumber(Long mid, Long headerId) {
        DepotHead depotHead =depotHeadMapper.selectByPrimaryKey(headerId);
        String linkNumber = depotHead.getLinknumber(); //关联单号
        BigDecimal count = depotItemMapperEx.getFinishNumber(mid, linkNumber);
        return count;
    }

    /**
     * 库存统计
     * @param depotId
     * @param mId
     * @param beginTime
     * @param endTime
     * @return
     */
    public BigDecimal getStockByParam(Long depotId, Long mId, String beginTime, String endTime, Long tenantId){
        DepotItemVo4Stock stockObj = depotItemMapperEx.getStockByParam(depotId, mId, beginTime, endTime, tenantId);
        BigDecimal intNum = stockObj.getInNum();
        BigDecimal outNum = stockObj.getOutNum();
        return intNum.subtract(outNum);
    }

    /**
     * 入库统计
     * @param depotId
     * @param mId
     * @param beginTime
     * @param endTime
     * @return
     */
    public BigDecimal getInNumByParam(Long depotId, Long mId, String beginTime, String endTime, Long tenantId){
        DepotItemVo4Stock stockObj = depotItemMapperEx.getStockByParam(depotId, mId, beginTime, endTime, tenantId);
        return stockObj.getInNum();
    }

    /**
     * 出库统计
     * @param depotId
     * @param mId
     * @param beginTime
     * @param endTime
     * @return
     */
    public BigDecimal getOutNumByParam(Long depotId, Long mId, String beginTime, String endTime, Long tenantId){
        DepotItemVo4Stock stockObj = depotItemMapperEx.getStockByParam(depotId, mId, beginTime, endTime, tenantId);
        return stockObj.getOutNum();
    }


    public List<DepotItem> selectDepotitem() throws Exception { ;
        List<DepotItem> list=null;
        try{
            list=depotItemMapper.selectDepotitem();
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

}
