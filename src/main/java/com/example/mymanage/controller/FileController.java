package com.example.mymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.http.Result;
import com.example.mymanage.tool.ReadExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class FileController {
    private final ReadExcel readExcel;

    @PostMapping("/getExcel")
    Result getExcel() {
        return Result.Ok(Base64.getEncoder().encodeToString(readExcel.saveDB2File()));
    }

//    /**
//     * 获取并发送验证码
//     */
//    public void getPic(HttpServletResponse resp) throws Exception {
//        resp.setContentType("multipart/form-data");
//        byte[] buffer = new byte[1024 * 10];
//        InputStream in = readExcel.saveDB2File();
//        ServletOutputStream out = resp.getOutputStream();
//        int len = 0;
//        while ((len = in.read(buffer)) != -1) {
//            out.write(buffer, 0, len);
//        }
//        out.flush();
//        in.close();
//        out.close();
//    }

    @PostMapping("/upload")
    Result uploadDBFile(@RequestBody JSONObject jsonObject) throws IOException {
        String code = jsonObject.getString("data");
        byte[] bytes = Base64.getDecoder().decode(code);
        File file = new File("f:\\db222.xlsx");
        java.nio.file.Files.copy(
                new ByteArrayInputStream(bytes),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

//        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        //直接读inputStream未知错误，故先保存到文件再读
        readExcel.readXlsToDB(file.getPath());
        return Result.Ok();
    }
}
