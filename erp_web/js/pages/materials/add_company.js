$.get("../../pages/template/company.html?789", function(tem) {
    if(tem) {
        var template = Handlebars.compile(tem);
        /**
         * 加载供应商、客户模块
         */
        $("#supplier").html(template({
            supplierSelectCompany: true
        }));
        $('#supplierDlgCompany').dialog({
            closed: true,
            modal: true,
            collapsible: false,
            closable: true
        });
        $("#supplierDlgCompany #supplier").validatebox({
            required: true,
            validType: 'length[2,30]'
        });
        $("#supplierDlgCompany #email").validatebox({
            validType: 'email'
        });
        $("#BeginNeedGet,#BeginNeedPay,#AllNeedGet,#AllNeedPay").numberbox({
            min:0,
            precision:2
        });
        $("#saveSupplier").linkbutton({
            iconCls: 'icon-ok'
        });
        $("#cancelSupplier").linkbutton({
            iconCls: 'icon-cancel'
        });

        /**
         * 加载仓库
         */
        $("#depot").html(template({
            depotSelect: true
        }));
        $('#depotDlg').dialog({
            closed: true,
            modal: true,
            collapsible: false,
            closable: true
        });
        $("#depotDlg #name,#depotDlg #address").validatebox({
            required: true,
            validType: 'length[2,30]'
        });
        $("#depotDlg #warehousing,#depotDlg #truckage").numberbox({
            min:0,
            precision:2
        });
        $("#saveDepot").linkbutton({
            iconCls: 'icon-ok'
        });
        $("#cancelDepot").linkbutton({
            iconCls: 'icon-cancel'
        });
    }
});