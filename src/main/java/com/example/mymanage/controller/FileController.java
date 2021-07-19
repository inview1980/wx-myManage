package com.example.mymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.tool.MyException;
import com.example.mymanage.tool.ReadExcel;
import com.example.mymanage.tool.StaticConfigData;
import com.example.mymanage.tool.TimedTask;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class FileController {
    private final ReadExcel readExcel;
    private final ApplicationContext context;

    @Value("${resource.default-db-file}")
    private String defaultDBFilePath;

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
        File path = File.createTempFile("updataFile", ".xlsx");
        java.nio.file.Files.copy(
                new ByteArrayInputStream(bytes),
                path.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        log.info("保存上传文件到临时文件：{}", path.getAbsolutePath());
        //直接读inputStream未知错误，且easyExcel读流时也是先保存到文件再读取内容，故先保存到文件再读
        readExcel.readXlsToDB(path.getAbsolutePath());
        return Result.Ok();
    }

    @PostMapping("/reloadDB")
    Result reloadDB() {
        readExcel.readXlsToDB(defaultDBFilePath);
        return Result.Ok();
    }

    @PostMapping("/reloadDBByNonVerify")
    @SneakyThrows
    Result reloadDBByNonVerify() {
        return reloadDB();
    }

    /**
     * 重新加载远程数据库
     */
    @PostMapping("/reloadRemoteDB")
    Result reloadRemoteDB() {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            ((IWriteToDB) context.getBean(value.getBeanClass())).removeDB();
        }
        TimedTask.SetAllTableNotChanged();//将数据修改全部设置为否
        StaticConfigData.reloadDebugState();//重读DebugState参数
        return Result.Ok();
    }

    @PostMapping("/reReadRemoteDBByNonVerify")
    Result reReadRemoteDBByNonVerify() {
        return reloadRemoteDB();
    }
}
