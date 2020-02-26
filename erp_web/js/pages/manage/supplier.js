var supplierID = null;
var kid = sessionStorage.getItem("userId");
var userBusinessList=null;
var userdepot=null;
//初始化界面
$(function() {
    var listTitle = ""; //单据标题
    var listType = ""; //类型
    var listTypeEn = ""; //英文类型
    var supplierID = null;
    initSystemData_UB();
    getType();
    initTableData();
    ininPager();
    bindEvent();
    initSupplier();//初始化供应商
    // initMaterialId();//初始化产品和套餐
    initSales_Type();//初始化产品类型
    initSales_Types(0)
    initType_Types(0);
});

//根据名称获取类型
function getType(){
    listTitle = $("#tablePanel").prev().text();
    if(listTitle === "供应商信息列表"){
        listType = "供应商";
        listTypeEn = "Vendor";
    }
    else if(listTitle === "客户信息列表"){
        listType = "客户";
        listTypeEn = "Customer";
    }
    else if(listTitle === "会员信息列表"){
        listType = "会员";
        listTypeEn = "Member";
    }
}

//初始化系统基础信息
function initSystemData_UB(){
    $.ajax({
        type:"get",
        url: "/userBusiness/getBasicData",
        data: ({
            KeyId:kid,
            Type:"UserDepot"
        }),
        //设置为同步
        async:false,
        dataType: "json",
        success: function (res) {
            if (res && res.code === 200) {
                userBusinessList = res.data.userBusinessList;
                if(userBusinessList !=null) {
                    if(userBusinessList.length>0) {
                        //用户对应的仓库列表 [1][2][3]...
                        userdepot =userBusinessList[0].value;
                    }
                }
            }
            else {
                userBusinessList = null;
            }
        }
    });
}

//初始化表格数据
function initTableData() {
    //改变宽度和高度
    $("#searchPanel").panel({width:webW-2});
    $("#tablePanel").panel({width:webW-2});
    if (userdepot == "[10]" || userdepot == "[20]" ) {
        $('#tableData').datagrid({
            //title:'单位列表',
            //iconCls:'icon-save',
            //width:700,
            height:heightInfo,
            nowrap: false,
            rownumbers: false,
            //动画效果
            animate:false,
            //选中单行
            singleSelect : true,
            collapsible:false,
            selectOnCheck:false,
            //fitColumns:true,
            //单击行是否选中
            checkOnSelect : false,
            //交替出现背景
            striped : true,
            pagination: true,
            //自动截取数据
            //nowrap : true,
            //loadFilter: pagerFilter,
            pageSize: initPageSize,
            pageList: initPageNum,
            columns:[[
                { field: 'id',width:35,align:"center",checkbox:true},
                { title: '操作',field: 'op',align:"center",width:60,formatter:function(value,rec)
                    {
                        var str = '';
                        var rowInfo = rec.id + 'AaBb' + rec.supplier +'AaBb' + rec.contacts + 'AaBb'+ rec.phonenum + 'AaBb'+ rec.email + 'AaBb'+ rec.beginneedget + 'AaBb'+ rec.beginneedpay + 'AaBb' + rec.isystem + 'AaBb' + rec.description+ 'AaBb' + rec.type
                            + 'AaBb' + rec.fax + 'AaBb' + rec.telephone + 'AaBb' + rec.address + 'AaBb' + rec.taxnum + 'AaBb' + rec.bankname + 'AaBb' + rec.accountnumber + 'AaBb' + rec.taxrate;
                        str += '<img title="编辑" src="/js/easyui-1.3.5/themes/icons/pencil.png" style="cursor: pointer;" onclick="editSupplier(\'' + rowInfo + '\');"/>&nbsp;&nbsp;&nbsp;';
                        str += '<img title="删除" src="/js/easyui-1.3.5/themes/icons/edit_remove.png" style="cursor: pointer;" onclick="deleteSupplier(\'' + rowInfo + '\');"/>';
                        return str;
                    }
                },
                { title: '名称',field: 'supplier',width:150},
                { title: '联系人', field: 'contacts',width:50,align:"center"},
                { title: '手机号码', field: 'telephone',width:100,align:"center"},
                { title: '电子邮箱',field: 'email',width:80,align:"center"},
                { title: '技术人员姓名', field: 'phonenum',width:100,align:"center"},
                { title: '技术人员联系方式', field: 'fax',width:100,align:"center"},
                { title: '预付款',field: 'advancein',width:70,align:"center"},
                { title: '税率(%)', field: 'taxrate',width:50,align:"center"},
                { title: '供货范围', field: 'supply',width:50,align:"center"},
                { title: '付款方式', field:'method',width:50,align:"center"},
                { title: '状态',field: 'enabled',width:70,align:"center",formatter:function(value){
                        return value? "启用":"禁用";
                    }}
            ]],
            toolbar:[
                {
                    id:'addSupplier',
                    text:'增加',
                    iconCls:'icon-add',
                    handler:function() {
                        addSuppler();
                    }
                },'-',
                {
                    id:'addSupplierMaterial',
                    text:'增加供应商产品',
                    iconCls:'icon-add',
                    handler:function() {
                        addSupplerMaterial();
                    }
                },'-',
                {
                    id:'deleteSupplier',
                    text:'删除',
                    iconCls:'icon-remove',
                    handler:function() {
                        batDeleteSupplier();
                    }
                },'-',
                {
                    id:'setEnable',
                    text:'启用',
                    iconCls:'icon-ok',
                    handler:function() {
                        setEnableFun();
                    }
                },'-',
                {
                    id:'setDisEnable',
                    text:'禁用',
                    iconCls:'icon-no',
                    handler:function() {
                        setDisEnableFun();
                    }
                },'-',
                {
                    id:'setInput',
                    text:'导入',
                    iconCls:'icon-excel',
                    handler:function() {
                        setInputFun();
                    }
                },'-',
                {
                    id:'setOutput',
                    text:'导出',
                    iconCls:'icon-excel',
                    handler:function() {
                        setOutputFun();
                    }
                },'-',
                {
                    id: 'setExp',
                    text: '导入模板',
                    iconCls: 'icon-excel-new',
                    handler: function () {
                        setExpFun();
                    }
                }
            ],
            onLoadError:function() {
                $.messager.alert('页面加载提示','页面加载异常，请稍后再试！','error');
                return;
            }
        });
    }else if (userdepot == "[24]"){
        $('#tableData').datagrid({
            //title:'单位列表',
            //iconCls:'icon-save',
            //width:700,
            height:heightInfo,
            nowrap: false,
            rownumbers: false,
            //动画效果
            animate:false,
            //选中单行
            singleSelect : true,
            collapsible:false,
            selectOnCheck:false,
            //fitColumns:true,
            //单击行是否选中
            checkOnSelect : false,
            //交替出现背景
            striped : true,
            pagination: true,
            //自动截取数据
            //nowrap : true,
            //loadFilter: pagerFilter,
            pageSize: initPageSize,
            pageList: initPageNum,
            columns:[[
                { field: 'id',width:35,align:"center",checkbox:true},
                { title: '操作',field: 'op',align:"center",width:60,formatter:function(value,rec)
                    {
                        var str = '';
                        var rowInfo = rec.id + 'AaBb' + rec.supplier +'AaBb' + rec.contacts + 'AaBb'+ rec.phonenum + 'AaBb'+ rec.email + 'AaBb'+ rec.beginneedget + 'AaBb'+ rec.beginneedpay + 'AaBb' + rec.isystem + 'AaBb' + rec.description+ 'AaBb' + rec.type
                            + 'AaBb' + rec.fax + 'AaBb' + rec.telephone + 'AaBb' + rec.address + 'AaBb' + rec.taxnum + 'AaBb' + rec.bankname + 'AaBb' + rec.accountnumber + 'AaBb' + rec.taxrate;
                        return str;
                    }
                },
                { title: '名称',field: 'supplier',width:150},
                { title: '联系人', field: 'contacts',width:50,align:"center"},
                { title: '手机号码', field: 'telephone',width:100,align:"center"},
                { title: '电子邮箱',field: 'email',width:80,align:"center"},
                { title: '技术人员姓名', field: 'phonenum',width:100,align:"center"},
                { title: '技术人员联系方式', field: 'fax',width:100,align:"center"},
                { title: '预付款',field: 'advancein',width:70,align:"center"},
                { title: '税率(%)', field: 'taxrate',width:50,align:"center"},
                { title: '供货范围', field: 'supply',width:50,align:"center"},
                { title: '付款方式', field:'method',width:50,align:"center"},
                { title: '状态',field: 'enabled',width:70,align:"center",formatter:function(value){
                        return value? "启用":"禁用";
                    }}
            ]],
            toolbar:[
                {
                    id:'addSupplier',
                    text:'增加',
                    iconCls:'icon-add',
                    handler:function() {
                        addSuppler();
                    }
                },'-',
                {
                    id:'addSupplierMaterial',
                    text:'增加供应商产品',
                    iconCls:'icon-add',
                    handler:function() {
                        addSupplerMaterial();
                    }
                },'-',
                {
                    id:'deleteSupplier',
                    text:'删除',
                    iconCls:'icon-remove',
                    handler:function() {
                        batDeleteSupplier();
                    }
                },'-',
                {
                    id:'setEnable',
                    text:'启用',
                    iconCls:'icon-ok',
                    handler:function() {
                        setEnableFun();
                    }
                },'-',
                {
                    id:'setDisEnable',
                    text:'禁用',
                    iconCls:'icon-no',
                    handler:function() {
                        setDisEnableFun();
                    }
                },'-',
                {
                    id:'setInput',
                    text:'导入',
                    iconCls:'icon-excel',
                    handler:function() {
                        setInputFun();
                    }
                },'-',
                {
                    id:'setOutput',
                    text:'导出',
                    iconCls:'icon-excel',
                    handler:function() {
                        setOutputFun();
                    }
                },'-',
                {
                    id: 'setExp',
                    text: '导入模板',
                    iconCls: 'icon-excel-new',
                    handler: function () {
                        setExpFun();
                    }
                }
            ],
            onLoadError:function() {
                $.messager.alert('页面加载提示','页面加载异常，请稍后再试！','error');
                return;
            }
        });
    }
    dgResize();
    showSupplierDetails(1,initPageSize);
}

function setExpFun() {
    window.location.href = "/supplier/exportExample?type=" + listType
}
//分页信息处理
function ininPager() {
    try {
        var opts = $("#tableData").datagrid('options');
        var pager = $("#tableData").datagrid('getPager');
        pager.pagination({
            onSelectPage:function(pageNum, pageSize)
            {
                opts.pageNumber = pageNum;
                opts.pageSize = pageSize;
                pager.pagination('refresh', {
                    pageNumber:pageNum,
                    pageSize:pageSize
                });
                showSupplierDetails(pageNum,pageSize);
            }
        });
    }
    catch (e) {
        $.messager.alert('异常处理提示',"分页信息异常 :  " + e.name + ": " + e.message,'error');
    }
}

//删除信息
function deleteSupplier(supplierInfo) {
    $.messager.confirm('删除确认','确定要删除此条信息吗？',function(r) {
        if (r) {
            var supplierTotalInfo = supplierInfo.split("AaBb");
            $.ajax({
                type:"post",
                url: "/supplier/batchDeleteSupplierByIds",
                dataType: "json",
                data: ({
                    ids : supplierTotalInfo[0]
                }),
                success: function (res) {
                    if(res && res.code == 200) {
                        $("#searchBtn").click();
                    } else {
                        if(res && res.code == 601){
                            var jsondata={};
                            jsondata.ids=supplierTotalInfo[0];
                            jsondata.deleteType='2';
                            var type='single';
                            batDeleteSupplierForceConfirm(res,"/supplier/batchDeleteSupplierByIds",jsondata,type);
                        }else if(res && res.code == 600){
                            $.messager.alert('删除提示', res.msg, 'error');
                        }else{
                            $.messager.alert('删除提示', '删除信息失败，请稍后再试！', 'error');
                        }
                    }
                },
                //此处添加错误处理
                error:function() {
                    $.messager.alert('删除提示','删除信息异常，请稍后再试！','error');
                    return;
                }
            });
        }
    });
}

//批量删除单位
function batDeleteSupplier() {
    var row = $('#tableData').datagrid('getChecked');
    if(row.length == 0) {
        $.messager.alert('删除提示','没有记录被选中！','info');
        return;
    }
    if(row.length > 0) {
        $.messager.confirm('删除确认','确定要删除选中的' + row.length + '条信息吗？',function(r) {
            if (r) {
                var ids = "";
                for(var i = 0;i < row.length; i ++) {
                    if(i == row.length-1)
                    {
                        ids += row[i].id;
                        break;
                    }
                    ids += row[i].id + ",";
                }
                $.ajax({
                    type:"post",
                    url: "/supplier/batchDeleteSupplierByIds",
                    dataType: "json",
                    async :  false,
                    data: ({
                        ids : ids
                    }),
                    success: function (res) {
                        if(res && res.code === 200) {
                            $("#searchBtn").click();
                            $(":checkbox").attr("checked", false);
                        } else {
                            if(res && res.code == 601){
                                var jsondata={};
                                jsondata.ids=ids;
                                jsondata.deleteType='2';
                                var type='batch';
                                batDeleteSupplierForceConfirm(res,"/supplier/batchDeleteSupplierByIds",jsondata,type);
                            }else if(res && res.code == 600){
                                $.messager.alert('删除提示', res.msg, 'error');
                            }else{
                                $.messager.alert('删除提示', '删除信息失败，请稍后再试！', 'error');
                            }
                        }
                    },
                    //此处添加错误处理
                    error:function() {
                        $.messager.alert('删除提示','删除信息异常，请稍后再试！','error');
                        return;
                    }
                });
            }
        });
    }
}
/**
 * 确认强制删除
 * */
function batDeleteSupplierForceConfirm(res,url,jsondata) {
    $.messager.confirm('删除确认', res.msg, function (r) {
        if (r) {
            $.ajax({
                type: "post",
                url: url,
                dataType: "json",
                data: (jsondata),
                success: function (res) {
                    if(res && res.code == 200) {
                        $("#searchBtn").click();
                        if(type=='batch'){
                            $(":checkbox").attr("checked", false);
                        }
                    }else if(res && res.code == 600){
                        $.messager.alert('删除提示', res.msg, 'error');
                    }else {
                        $.messager.alert('删除提示', '删除信息失败，请稍后再试！', 'error');
                    }
                },
                //此处添加错误处理
                error: function () {
                    $.messager.alert('删除提示', '删除信息失败，请稍后再试！', 'error');
                    return;
                }
            });
        }
    });
}

//批量启用
function setEnableFun() {
    var row = $('#tableData').datagrid('getChecked');
    if(row.length == 0) {
        $.messager.alert('启用提示','没有记录被选中！','info');
        return;
    }
    if(row.length > 0) {
        $.messager.confirm('启用确认','确定要启用选中的' + row.length + '条信息吗？',function(r) {
            if (r) {
                var ids = "";
                for(var i = 0;i < row.length; i ++) {
                    if(i == row.length-1) {
                        ids += row[i].id;
                        break;
                    }
                    ids += row[i].id + ",";
                }
                $.ajax({
                    type:"post",
                    url: "/supplier/batchSetEnable",
                    dataType: "json",
                    async :  false,
                    data: ({
                        enabled: true,
                        supplierIDs : ids
                    }),
                    success: function (res) {
                        if(res && res.code === 200) {
                            $("#searchBtn").click();
                            $(":checkbox").attr("checked", false);
                        } else {
                            $.messager.alert('启用提示', '启用信息失败，请稍后再试！', 'error');
                        }
                    },
                    //此处添加错误处理
                    error:function() {
                        $.messager.alert('启用提示','启用信息异常，请稍后再试！','error');
                        return;
                    }
                });
            }
        });
    }
}

//批量禁用
function setDisEnableFun() {
    var row = $('#tableData').datagrid('getChecked');
    if(row.length == 0) {
        $.messager.alert('禁用提示','没有记录被选中！','info');
        return;
    }
    if(row.length > 0) {
        $.messager.confirm('禁用确认','确定要禁用选中的' + row.length + '条信息吗？',function(r) {
            if (r) {
                var ids = "";
                for(var i = 0;i < row.length; i ++) {
                    if(i == row.length-1) {
                        ids += row[i].id;
                        break;
                    }
                    ids += row[i].id + ",";
                }
                $.ajax({
                    type:"post",
                    url: "/supplier/batchSetEnable",
                    dataType: "json",
                    async :  false,
                    data: ({
                        enabled: false,
                        supplierIDs : ids
                    }),
                    success: function (res) {
                        if(res && res.code === 200) {
                            $("#searchBtn").click();
                            $(":checkbox").attr("checked", false);
                        } else {
                            $.messager.alert('禁用提示', '禁用信息失败，请稍后再试！', 'error');
                        }
                    },
                    //此处添加错误处理
                    error:function() {
                        $.messager.alert('禁用提示','禁用信息异常，请稍后再试！','error');
                        return;
                    }
                });
            }
        });
    }
}

//导入数据
function setInputFun(){
    //IE下不允许编辑 input=file的值  解决思路：重新克隆input=file，把这个input元素复制一个，然后将原来的删除。
    //在IE下复制元素的时候，其中的值是不会被复制的，所以就达到了清空文件域的目的了。
    //而在Firefox下，其中的值也会被一同复制，清空一下就做到兼容
    var fileUploadInput = $("#supplierFile");
    fileUploadInput.after(fileUploadInput.clone().val(""));
    fileUploadInput.remove();
    $("#isCheck").val(1);
    $('#importExcelDlg').dialog('open').dialog('setTitle','导入' + listType + '信息');
    $(".window-mask").css({ width: webW-20 ,height: webH});
    $("#supplierFile").focus();
}

//导出数据
function setOutputFun(){
    var supplier = $.trim($("#searchSupplier").val());
    var phonenum = $.trim($("#searchPhonenum").val());
    var telephone = $.trim($("#searchTelephone").val());
    var description = $.trim($("#searchDesc").val());
    window.location.href = "/supplier/exportExcel?browserType=" + getOs()
        + "&supplier=" + supplier + "&type=" + listType + "&phonenum=" + phonenum + "&telephone=" + telephone + "&description=" + description;
}

//增加单位
var url;
var supplierID = 0;
//保存编辑前的名称
var oldSupplier = "";

function addSuppler() {
    $('#supplierDlg').dialog('open').dialog('setTitle','<img src="/js/easyui-1.3.5/themes/icons/edit_add.png"/>&nbsp;增加'+listType+"信息");
    $(".window-mask").css({ width: webW ,height: webH});
    $("#supplier").focus();
    $('#supplierFM').form('clear');
    oldSupplier = "";
    supplierID = 0;
    url = '/supplier/add';
}


//新增材料框
function addSupplerMaterial() {
    $('#supplierMaterialDlg').dialog('open').dialog('setTitle','<img src="/js/easyui-1.3.5/themes/icons/edit_add.png"/>&nbsp;增加'+listType+"信息");
    $('#supplierMaterialFM').form('clear');
}


//保存材料信息
//  $("#saveSupplierMaterial").off("click").on("click", function () {
function  AddMaterial() {
    debugger
    if (validateForm("supplierMaterialFM")) {
        return;
    }
    // if (checkSupplierName()) {
    //     return;
    // }
    // var levelStr = "";
    // var level = $('#level').combobox('getValues').toString(); //销售人员
    // if(level) {
    //     var levelArray = level.split(",");
    //     for (var i = 0; i < levelArray.length; i++) {
    //         if (i === levelArray.length - 1) {
    //             levelStr += "<" + levelArray[i] + ">";
    //         }
    //         else {
    //             levelStr += "<" + levelArray[i] + ">,";
    //         }
    //     }
    // }

    var sales_types = "";
    if ($('#sales_types').length){
        var typejs = $('#sales_types').combobox('getValue');
        if(typejs == 1){
            sales_types = "人脸机";
        }else if(typejs == 2){
            sales_types = "闸机";
        }else if(typejs == 3){
            sales_types = "身份证识别器";
        }
        var level ="";
        var type_typejs = $('#type_type').combobox('getValue');
        if(type_typejs == 4){
            level = "623,624,625,626,627,629";
        }else if(type_typejs == 5){
            level = "623,624,625,626,627,629";
        }else if(type_typejs == 6){
            level = "628";
        }else if(type_typejs == 7){
            level = "623,624,625,626,627,631";
        }else if(type_typejs == 8){
            level = "634,635";
        }else if(type_typejs == 9){
            level = "632,633";
        }else if(type_typejs == 10){
            level = "630";
        }
    }

    var levelStr = "";
    if(level) {
        var levelArray = level.split(",");
        for (var i = 0; i < levelArray.length; i++) {
            if (i === levelArray.length - 1) {
                levelStr += "<" + levelArray[i] + ">";
            }
            else {
                levelStr += "<" + levelArray[i] + ">,";
            }
        }
    }

    var infoStr=JSON.stringify({
        organId:$('#organId').combobox('getValue'),
        level:levelStr,
        sales_typeId:sales_types,
        type_typeId:$('#type_type').combobox('getValue'),
        name:$.trim($("#name").val()),
        model:$.trim($("#model").val()),
        retailprice:$.trim($("#retailprice").val()),
        unit:$.trim($("#unit").val()),
        remark:$.trim($("#remark").val()),
        categoryid:$.trim($("#categoryid").val()),
        enabled:$.trim($("#enabled").val()),
        packing:$.trim($("#packing").val()),
        safetystock:$.trim($("#safetystock").val()),
        standard:$.trim($("#standard").val()),
        lowprice:$.trim($("#lowprice").val()),
        color:$.trim($("#color").val()),
        presetpriceone:$.trim($("#presetpriceone").val()),
        presetpricetwo:$.trim($("#presetpricetwo").val()),
        unitid:$.trim($("#unitid").val()),
        firstoutunit:$.trim($("#firstoutunit").val()),
        firstinunit:$.trim($("#firstinunit").val()),
        pricestrategy:$.trim($("#pricestrategy").val()),
        otherfield1:$.trim($("#otherfield1").val()),
        otherfield2:$.trim($("#otherfield2").val()),
        otherfield3:$.trim($("#otherfield3").val()),
        deleteFlag:$.trim($("#deleteFlag").val()),
        barcode:$.trim($("#barcode").val())
    });
    $.ajax({
        url: "/material/addSupplierMaterial",
        type: "post",
        dataType: "json",
        data: {
            info: infoStr
        },
        success: function (res) {
            if (res > 0) {
                $('#supplierMaterialDlg').dialog('close');
                $.messager.alert('提示：', '保存成功！');
            } else {
                $.messager.alert('提示：', '保存失败！');
            }
        },
        error:function(){}
    });
}


//初始化供应商、客户、散户信息
function initSupplier(){
    $('#organId').combobox({
        url: "/supplier/findBySelect_sup",
        valueField:'id',
        textField:'supplier',
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) >-1;
        },
        onSelect: function(rec){
            debugger
            var sId = rec.id;
            initSales_Types(sId);
            initType_Types(0);
        }
    });
}


//添加供应商产品的关闭方法
function supplierMaterialClose(){
    $('#supplierMaterialDlg').dialog('close');
    initSales_Type();
}


//初始化产品类型
function initSales_Type(){
    $('#sales_type').combobox({
        url: "/type/selectType",
        mathod:'post',
        valueField:'Id',
        textField:'tName',
        panelHeight: 'auto',//自适应
        multiple: true,
        editable: false,
        formatter: function (row) { // formatter方法就是实现了在每个下拉选项前面增加checkbox框的方法
            var opts = $(this).combobox('options');
            return '<input type="checkbox" id="'+row.Id+'" class="combobox-checkbox">' + row[opts.textField]
        },
        onLoadSuccess: function () { // 下拉框数据加载成功调用
            // 正常情况下是默认选中“所有”，但我想实现点击所有全选功能，这这样会冲突，暂时默认都不选
            $("#sales_type").combobox('clear'); //清空
        },
        onSelect: function (row) { // 选中一个选项时调用
            debugger
            var opts = $(this).combobox('options');
            //当点击所有时，则勾中所有的选项
            if (row.tName === "全选") {
                var data = $("#sales_type").combobox('getData');
                if ($("#selectAll")[0].checked == true) {
                    var list = [];
                    for (var i = 0; i < data.length; i++) {
                        if ($("#"+data[0].domId).find("input[type=checkbox]")[0].checked == false) {
                            $('#' + data[i].domId + ' input[type="checkbox"]').click();
                        }
                        $('#' + data[i].domId + ' input[type="checkbox"]').prop("checked", true);
                        if ($("#"+data[i].domId).find("input[type=checkbox]").attr("id") != "selectAll") {
                            list.push(data[i].tName);
                        }
                    }
                    $("#sales_type").combobox('setValues', list); // combobox全选
                }else{
                    for (var i = 0; i < data.length; i++) {
                        if ($("#"+data[0].domId).find("input[type=checkbox]")[0].checked == true) {
                            $('#' + data[i].domId + ' input[type="checkbox"]').click();
                        }
                        $('#' + data[i].domId + ' input[type="checkbox"]').prop("checked", false);
                    }
                    $("#sales_type").combobox('clear');//清空
                }
            } else {
                //设置选中选项所对应的复选框为选中状态
                $('#'+row.domId + ' input[type="checkbox"]').prop("checked", true);
            }
        },
        onUnselect: function (row) { // 取消选中一个选项时调用
            debugger
            var opts = $(this).combobox('options');
            // 当取消全选勾中时，则取消所有的勾选
            if (row.tName === "全选") {
                var data = $("#sales_type").combobox('getData');
                if ($("#selectAll")[0].checked == true) {
                    var list = [];
                    for (var i = 0; i < data.length; i++) {
                        if ($("#" + data[0].domId).find("input[type=checkbox]")[0].checked == false) {
                            $('#' + data[i].domId + ' input[type="checkbox"]').click();
                        }
                        $('#' + data[i].domId + ' input[type="checkbox"]').prop("checked", true);
                        if ($("#"+data[i].domId).find("input[type=checkbox]").attr("id") != "selectAll") {
                            list.push(data[i].tName);
                        }
                    }
                    $("#sales_type").combobox('setValues', list); // combobox全选
                } else {
                    for (var i = 0; i < data.length; i++) {
                        if ($("#" + data[0].domId).find("input[type=checkbox]")[0].checked == true) {
                            $('#' + data[i].domId + ' input[type="checkbox"]').click();
                        }
                        $('#' + data[i].domId + ' input[type="checkbox"]').prop("checked", false);
                    }
                    $("#sales_type").combobox('clear');//清空
                }
            }
            else {
                $('#'+row.domId + ' input[type="checkbox"]').prop("checked", false);
            }
        }
    });
}
//关闭增加供应商页面
function closeInitSales_Type() {
    $('#supplierDlg').dialog('close');
    initSales_Type();
}


//初始化1级产品类型
function initSales_Types(sId){
    debugger
    $('#sales_types').combobox({
        url: "/supplier/findSalesType?sId="+sId,
        mathod:'post',
        valueField:'Id',
        textField:'tName',
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) >-1;
        },
        onSelect: function(rec){
            debugger
            var tId = rec.Id;
            initType_Types(tId);

        }
    });
}


//初始化2级类型分类
function initType_Types(tId){
    debugger
    $('#type_type').combobox({
        url: "/type/selectTypeId?tId="+tId,
        mathod:'post',
        valueField:'Id',
        textField:'Name',
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) >-1;
        },
        onSelect: function(rec){

        }
    });
}


//初始化产品和套餐
// function initMaterialId() {
//     debugger
//     $('#level').combobox({
//         url: "/material/findMaterialId",
//         mathod:'post',
//         valueField:'Id',
//         textField:'MaterialName',
//         multiple: true,
//         formatter: function(row) {
//             var opts = $(this).combobox('options');
//             return '<input type="checkbox" class="combobox-checkbox">'+ row[opts.textField];
//         },
//         onLoadSuccess: function () {  //下拉框数据加载成功调用
//             var opts = $(this).combobox('options');
//             var target = this;
//             var values = $(target).combobox('getValues');//获取选中的值的values
//             $.map(values, function (value) {
//                 var el = opts.finder.getEl(target, value);
//                 el.find('input.combobox-checkbox')._propAttr('checked', true);
//             })
//         },
//         onSelect: function (row) { //选中一个选项时调用
//             var opts = $(this).combobox('options');
//             //获取选中的值的values
//             $('#level').val($(this).combobox('getValues'));
//
//             //设置选中值所对应的复选框为选中状态
//             var el = opts.finder.getEl(this, row[opts.valueField]);
//             el.find('input.combobox-checkbox')._propAttr('checked', true);
//         },
//         onUnselect: function (row) {//不选中一个选项时调用
//             var opts = $(this).combobox('options');
//             //获取选中的值的values
//             $('#level').val($(this).combobox('getValues'));
//
//             var el = opts.finder.getEl(this, row[opts.valueField]);
//             el.find('input.combobox-checkbox')._propAttr('checked', false);
//         }
//     });
// }


function bindEvent(){
    //导入excel对话框
    $('#importExcelDlg').dialog({
        width: 400,
        closed: true,
        cache: false,
        modal: true,
        collapsible:false,
        closable: true,
        buttons:'#dlg-buttons5'
    });
    //导入excel表格
    $("#saveimport").unbind().bind({
        click:function() {
            if($("#supplierFile").val().length == 0)
            {
                $.messager.alert('提示','请选择文件！','info');
                return;
            }
            $("#importExcelFM").submit();
            $('#importExcelDlg').dialog('close');

            $.messager.progress({
                title:'请稍候',
                msg:'数据处理ing...'
            });
            setTimeout(function(){
                $.messager.progress('close');
                var opts = $("#tableData").datagrid('options');
                showSupplierDetails(opts.pageNumber,opts.pageSize);
            },3300);
        }
    });
    //保存信息
    $("#saveSupplier").off("click").on("click", function () {
        debugger
        if(validateForm("supplierFM")) {
            return;
        }
        if (checkSupplierName()) {
            return;
        }
        // var reg = /^([0-9])+$/;
        // var phonenum = $.trim($("#phonenum").val());
        // if(phonenum.length>0 && !reg.test(phonenum)) {
        //     $.messager.alert('提示','电话号码只能是数字','info');
        //     $("#phonenum").val("").focus();
        //     return;
        // }
        var beginNeedGet = $.trim($("#BeginNeedGet").val());
        var beginNeedPay = $.trim($("#BeginNeedPay").val());
        if(beginNeedGet && beginNeedPay) {
            $.messager.alert('提示','期初应收和期初应付不能同时输入','info');
            return;
        }
        var sales_typeStr = "";
        var sales_type = $('#sales_type').combobox('getValues').toString();
        if(sales_type) {
            var sales_typeArray = sales_type.split(",");
            for (var i = 0; i < sales_typeArray.length; i++) {
                if (i === sales_typeArray.length - 1) {
                    sales_typeStr += "<" + sales_typeArray[i] + ">";
                }
                else {
                    sales_typeStr += "<" + sales_typeArray[i] + ">,";
                }
            }
        }


        var obj = $("#supplierFM").serializeObject();
        obj.type = listType;
        obj.enabled = 1;
        obj.sales_type = sales_typeStr;
        $.ajax({
            url: url,
            type:"post",
            dataType: "json",
            data:{
                info: JSON.stringify(obj)
            },
            success: function(res) {
                if(res && res.code === 200) {
                    $('#supplierDlg').dialog('close');
                    initSupplier();
                    //加载完以后重新初始化
                    var opts = $("#tableData").datagrid('options');
                    showSupplierDetails(opts.pageNumber, opts.pageSize);
                }
            }
        });
    });

    //初始化键盘enter事件
    $(document).keydown(function(event) {
        debugger
        //兼容 IE和firefox 事件
        var e = window.event || event;
        var k = e.keyCode||e.which||e.charCode;
        //兼容 IE,firefox 兼容
        var obj = e.srcElement ? e.srcElement : e.target;
        //绑定键盘事件为 id是指定的输入框才可以触发键盘事件 13键盘事件
        if(k == "13"&&(obj.id=="supplier" || obj.id=="contacts"|| obj.id=="phonenum"
            || obj.id=="email" || obj.id=="description" ))
        {
            $("#saveSupplier").click();

        }

        //搜索按钮添加快捷键
        if(k == "13"&&(obj.id=="searchSupplier" || obj.id=="searchContacts"|| obj.id=="searchPhonenum"
            || obj.id=="searchEmail" || obj.id=="searchDesc" ))
        {
            $("#searchBtn").click();
        }
    });

    //搜索处理
    $("#searchBtn").unbind().bind({
        click:function() {
            showSupplierDetails(1,initPageSize);
            var opts = $("#tableData").datagrid('options');
            var pager = $("#tableData").datagrid('getPager');
            opts.pageNumber = 1;
            opts.pageSize = initPageSize;
            pager.pagination('refresh', {
                pageNumber:1,
                pageSize:initPageSize
            });
        }
    });

    $("#searchBtn").click();

    //重置按钮
    $("#searchResetBtn").unbind().bind({
        click:function(){
            $("#searchSupplier").val("");
            $("#searchType").val("");
            $("#searchPhonenum").val("");
            $("#searchTelephone").val("");
            $("#searchDesc").val("");

            //加载完以后重新初始化
            $("#searchBtn").click();
        }
    });

}


//编辑信息
function editSupplier(supplierTotalInfo) {
    var sales_typeStr = "";
    var sales_type = $('#sales_type').combobox('getValues').toString();
    if(sales_type) {
        var sales_typeArray = sales_type.split(",");
        for (var i = 0; i < sales_typeArray.length; i++) {
            if (i === sales_typeArray.length - 1) {
                sales_typeStr += "<" + sales_typeArray[i] + ">";
            }
            else {
                sales_typeStr += "<" + sales_typeArray[i] + ">,";
            }
        }
    }
    var supplierInfo = supplierTotalInfo.split("AaBb");
    var beginNeedGet = supplierInfo[5];
    var beginNeedPay = supplierInfo[6];
    var row = {
        supplier : supplierInfo[1],
        contacts : supplierInfo[2].replace("undefined",""),
        phonenum : supplierInfo[3].replace("undefined",""),
        email : supplierInfo[4].replace("undefined",""),
        sales_type:sales_typeStr,
        BeginNeedGet : beginNeedGet == "0"? "":beginNeedGet,
        BeginNeedPay : beginNeedPay == "0"? "":beginNeedPay,
        AllNeedGet: "",
        AllNeedPay: "",
        description : supplierInfo[8].replace("undefined",""),
        type : supplierInfo[9],
        fax : supplierInfo[10].replace("undefined",""),
        telephone : supplierInfo[11].replace("undefined",""),
        address : supplierInfo[12].replace("undefined",""),
        taxNum : supplierInfo[13].replace("undefined",""),
        bankName : supplierInfo[14].replace("undefined",""),
        accountNumber : supplierInfo[15].replace("undefined",""),
        taxRate : supplierInfo[16].replace("undefined","")
    };
    oldSupplier = supplierInfo[1];
    $('#supplierDlg').dialog('open').dialog('setTitle','<img src="/js/easyui-1.3.5/themes/icons/pencil.png"/>&nbsp;编辑'+listType +"信息");
    $(".window-mask").css({ width: webW ,height: webH});
    $('#supplierFM').form('load',row);
    supplierID = supplierInfo[0];
    //焦点在名称输入框==定焦在输入文字后面
    $("#supplier").val("").focus().val(supplierInfo[1]);
    url = '/supplier/update?id=' + supplierInfo[0];

    //显示累计应收和累计应付
    var thisDateTime = getNowFormatDateTime(); //当前时间
    var supType = "customer";
    if(listType === "客户"){
        supType = "customer"
    }
    else if(listType === "供应商"){
        supType = "vendor"
    }
    $.ajax({
        type:"get",
        url: "/depotHead/findTotalPay",
        dataType: "json",
        async:  false,
        data: ({
            supplierId: supplierInfo[0],
            endTime:thisDateTime,
            supType: supType
        }),
        success: function(res){
            if (res && res.code === 200 && res.data && res.data.rows && res.data.rows.getAllMoney !== "") {
                var moneyA = res.data.rows.getAllMoney.toFixed(2)-0;
                $.ajax({
                    type:"get",
                    url: "/accountHead/findTotalPay",
                    dataType: "json",
                    async:  false,
                    data: ({
                        supplierId: supplierInfo[0],
                        endTime:thisDateTime,
                        supType: supType
                    }),
                    success: function(res){
                        if (res && res.code === 200 && res.data && res.data.rows && res.data.rows.getAllMoney !== "") {
                            var moneyB = res.data.rows.getAllMoney.toFixed(2)-0;
                            var money = moneyA+moneyB;
                            var moneyBeginNeedGet = $("#BeginNeedGet").val()-0; //期初应收
                            var moneyBeginNeedPay = $("#BeginNeedPay").val()-0; //期初应付
                            if(listType === "客户"){
                                money = (money + moneyBeginNeedGet - moneyBeginNeedPay).toFixed(2);
                                $("#AllNeedGet").val(money);  //累计应收
                            }
                            else if(listType === "供应商"){
                                money = (money + moneyBeginNeedPay - moneyBeginNeedGet).toFixed(2);
                                $("#AllNeedPay").val(money); //累计应付
                            }
                        }
                    },
                    error: function(){
                        $.messager.alert('提示','网络异常请稍后再试！','error');
                        return;
                    }
                });
            }
        },
        error: function(){
            $.messager.alert('提示','网络异常请稍后再试！','error');
            return;
        }
    });
}


//检查单位名称是否存在 ++ 重名无法提示问题需要跟进
function checkSupplierName() {
    var supplierName = $.trim($("#supplier").val());
    //表示是否存在 true == 存在 false = 不存在
    var flag = false;
    //开始ajax名称检验，不能重名
    if(supplierName.length > 0 &&( oldSupplier.length ==0 || supplierName != oldSupplier)) {
        $.ajax({
            type:"get",
            url: "/supplier/checkIsNameExist",
            dataType: "json",
            async :  false,
            data: ({
                id : supplierID,
                name : supplierName
            }),
            success: function (res) {
                if(res && res.code === 200) {
                    if(res.data && res.data.status) {
                        flag = res.data.status;
                        if (flag) {
                            $.messager.alert('提示', '单位名称已经存在', 'info');
                            return;
                        }
                    }
                }
            },
            //此处添加错误处理
            error:function() {
                $.messager.alert('提示','检查单位名称是否存在异常，请稍后再试！','error');
                return;
            }
        });
    }
    return flag;
}


function showSupplierDetails(pageNo,pageSize) {
    var supplier = $.trim($("#searchSupplier").val());
    var phonenum = $.trim($("#searchPhonenum").val());
    var telephone = $.trim($("#searchTelephone").val());
    var description = $.trim($("#searchDesc").val());
    $.ajax({
        type:"get",
        url: "/supplier/list",
        dataType: "json",
        data: ({
            search: JSON.stringify({
                supplier: supplier,
                type: listType,
                phonenum: phonenum,
                telephone: telephone,
                description: description
            }),
            currentPage: pageNo,
            pageSize: pageSize
        }),
        success: function (res) {
            if(res && res.code === 200){
                if(res.data && res.data.page) {
                    $("#tableData").datagrid('loadData', res.data.page);
                }
            }
        },
        //此处添加错误处理
        error:function() {
            $.messager.alert('查询提示','查询数据后台异常，请稍后再试！','error');
            return;
        }
    });
}

