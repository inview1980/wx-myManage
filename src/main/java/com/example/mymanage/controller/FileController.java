package com.example.mymanage.controller;

import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.http.Result;
import com.example.mymanage.tool.ReadExcel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class FileController {
    private final ReadExcel readExcel;

    @PostMapping("/getExcel")
    Result getExcel() throws IOException, CloneNotSupportedException {
        return Result.Ok(HttpUtil.upLoadFile(readExcel.saveDB2File()));
    }

    /**
     * 获取并发送验证码
     */
    public void getPic(HttpServletResponse resp) throws Exception {
        resp.setContentType("multipart/form-data");
        byte[] buffer = new byte[1024 * 10];
        InputStream in = readExcel.saveDB2File();
        ServletOutputStream out = resp.getOutputStream();
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        in.close();
        out.close();
    }

    @PostMapping("/upload")
    Result uploadDBFile(HttpServletRequest request) throws  IOException {
        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> files = mureq.getFileMap();
        if (files.size() > 0) {
            Map.Entry<String, MultipartFile> f = files.entrySet().iterator().next();
            MultipartFile file = f.getValue();
            InputStream is =file.getInputStream();
            final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                swapStream.write(ch);
            }
        }
        return Result.Ok();
    }
}
