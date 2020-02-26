package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.DepotItem;
import com.jsh.erp.datasource.entities.Msg;
import com.jsh.erp.service.msg.MsgService;
import com.jsh.erp.utils.BaseResponseInfo;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.ExportExecUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author ji sheng hua 港信ERP
 */
@RestController
@RequestMapping(value = "/msg")
public class MsgController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(MsgController.class);

    @Resource
    private MsgService msgService;

    @GetMapping("/getMsgByStatus")
    public BaseResponseInfo getMsgByStatus(@RequestParam("status") String status,
                                           HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<Msg> list = msgService.getMsgByStatus(status);
            res.code = 200;
            res.data = list;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    @PostMapping("/batchUpdateStatus")
    public BaseResponseInfo batchUpdateStatus(@RequestParam("ids") String ids,
                                              @RequestParam("status") String status,
                                              HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            msgService.batchUpdateStatus(ids, status);
            res.code = 200;
            res.data = "更新成功";
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 下载文件
     * @return 成功下载文件，失败返回0
     * @throws java.io.IOException
     */
    @RequestMapping("/downloadMsgkk")
    public void download(HttpServletRequest req,HttpServletResponse resp) {
        int res = 0;
        try {
            List<Msg> list = msgService.selectMsgContract();
            for (Msg msg :list) {
                String fileName = msg.getMsgTitle();
                fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
                //上传文件都是保存在/web-inf/loadjsp目录下的子目录中
                String fileSaveRootPath = req.getServletContext().getRealPath("/img/");
                //通过文件名找到文件所在的目录
                String path = fileSaveRootPath;
                //得到要下载的文件
                File file = new File(path + "\\" + fileName);
                if (!file.exists()) {
                    req.setAttribute( "message", "您要下载的资源不存在");
//                    req.getRequestDispatcher("/message.jsp").forward(req, resp);
                    return;
                }
                //处理文件名称
                String realName = fileName.substring(fileName.lastIndexOf("_") + 1);
                //控制浏览器下载该文件
                resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                //读取需要下载的文件 保存到文件输入流
                FileInputStream fileInputStream = new FileInputStream(file);
                //创建输出流
                OutputStream fileOutputStream = resp.getOutputStream();
                //创建缓冲区
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileInputStream.close();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
   }

    /**
     * 下载文件
     * @return 成功下载文件，失败返回0
     * @throws java.io.IOException
     */
    @PostMapping("/downloadMsg")
    public JSONObject downdan(Integer id) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            Msg msga = new Msg();
            msga.setMsgTitle(String.valueOf(id));
            List<Msg> list = msgService.selectMsgContractId(msga);
            for (Msg msg :list) {
                JSONObject object = new JSONObject();
                object.put("name",msg.getType());
                object.put("url",msg.getMsgContent()+msg.getMsgTitle());
                array.add(object);
            }
            jsonObject.put("data",array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 下载文件
     * @return 成功下载文件，失败返回0
     * @throws java.io.IOException
     */
    @RequestMapping("/downdanMsg2")
    public void downdanMsg2(Integer id,HttpServletRequest req,HttpServletResponse resp) {
        int res = 0;
        try {
            Msg msga = new Msg();
            msga.setMsgTitle(String.valueOf(id));
            List<Msg> list = msgService.selectMsgContractId(msga);
            for (Msg msg:list) {
                URL url = new URL(msg.getMsgContent() + msg.getMsgTitle());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置超时间为3秒
                conn.setConnectTimeout(10 * 1000);
                //防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                //得到输入流
                InputStream inputStream = conn.getInputStream();
                String fileName = msg.getMsgTitle();
                //开始写出流
                //处理文件名称
                String realName = fileName.substring(fileName.lastIndexOf("_") + 1);
                //控制浏览器下载该文件
                resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));

                //创建输出流
                OutputStream fileOutputStream = resp.getOutputStream();
                //创建缓冲区
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
                fileOutputStream.close();

                System.out.println("info:" + url + " download success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
    }
}
