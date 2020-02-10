package com.jsh.erp.controller;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    public void word(String unitid,HttpServletResponse response) throws Exception {
        //===============主要下载的代码======================
        /**
         * 下载
         */
        FileInputStream in = null;
        OutputStream out = null;
        try {
            List<Msg> list = msgService.selectMsgContract();
            //获取文件名
            String filename = list.get(0).getMsgTitle();
            filename = new String(filename.getBytes("iso8859-1"),"UTF-8");
            String savePath = list.get(0).getMsgContent()+list.get(0).getMsgTitle();
            String downloadpath = savePath;
            //File file = new File(path1);
            //如果文件不存在
			/*if(!file.exists()){
			    return false;
			}*/
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            //读取要下载的文件，保存到文件输入流
            in= new FileInputStream(downloadpath);
            //创建输出流
            out= response.getOutputStream();
            //缓存区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区中
            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }
        }finally {
            //关闭
            in.close();
            out.close();
        }
        //删除服务器上的临时文件
//        File deleteFile=new File(savePath);
//        deleteFile.delete();
    }
//    public void download(HttpServletRequest req,HttpServletResponse resp) {
//        int res = 0;
//        try {
//            List<Msg> list = msgService.selectMsgContract();
//            for (Msg msg :list) {
//                String fileName = msg.getMsgTitle();
//                fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
//                //上传文件都是保存在/web-inf/loadjsp目录下的子目录中
//                String fileSaveRootPath = req.getServletContext().getRealPath("/img/");
//                //通过文件名找到文件所在的目录
//                String path = fileSaveRootPath;
//                //得到要下载的文件
//                File file = new File(path + "\\" + fileName);
//                if (!file.exists()) {
//                    req.setAttribute( "message", "您要下载的资源不存在");
//                    req.getRequestDispatcher("/message.jsp").forward(req, resp);
//                    return;
//                }
//                //处理文件名称
//                String realName = fileName.substring(fileName.lastIndexOf("_") + 1);
//                //控制浏览器下载该文件
//                resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
//                //读取需要下载的文件 保存到文件输入流
//                FileInputStream fileInputStream = new FileInputStream(path + "//" + fileName);
//                //创建输出流
//                OutputStream fileOutputStream = resp.getOutputStream();
//                //创建缓冲区
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = fileInputStream.read(buffer)) > 0) {
//                    fileOutputStream.write(buffer, 0, len);
//                }
//                fileInputStream.close();
//                fileOutputStream.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            res = 0;
//        }
//   }

    /**
     * 下载文件
     * @return 成功下载文件，失败返回0
     * @throws java.io.IOException
     */
    @RequestMapping("/downloadMsg")
    public void downdan(Integer id,HttpServletRequest req,HttpServletResponse resp) {
        int res = 0;
        try {
            Msg msga = new Msg();
            msga.setMsgTitle(String.valueOf(id));
            List<Msg> list = msgService.selectMsgContractId(msga);
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
                    req.setAttribute("message", "您要下载的资源不存在");
                    req.getRequestDispatcher("/message.jsp").forward(req, resp);
                    return;
                }
                //处理文件名称
                String realName = fileName.substring(fileName.lastIndexOf("_") + 1);
                //控制浏览器下载该文件
                resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                //读取需要下载的文件 保存到文件输入流
                FileInputStream fileInputStream = new FileInputStream(path + "//" + fileName);
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

    public String findFileSavePathByFileName(String filename,String saveRootPath)
    {
        int hashcode = filename.hashCode();
        int dir1 = hashcode&0xf;
        String dir  = saveRootPath+"//"+dir1;
        File file = new File(dir);
        if(!file.exists())
        {
            file.mkdir();
        }
        return dir;
    }
}
