package com.example.mymanage.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.iface.IReadAndWriteDB;
import com.example.mymanage.tool.MyException;
import com.example.mymanage.tool.StaticConfigData;
import com.example.mymanage.tool.TimedTask;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpUtil implements IReadAndWriteDB {
    private static String AccessToken = null;
    private static final Object object = new Object();

    private String getAccessToken() {
        if (AccessToken == null) {
            getAccessTokenFromHttp();
        }
        return AccessToken;
    }

//    public static void setAccessToken(){
//        AccessToken=null;
//    }
//
////    public static String getOpenID(@NonNull String code) throws IOException {
////        StringBuilder sb = new StringBuilder(StateData.getOpenID());
////        sb.append(StateData.getWXAppId());//自己的appid
////        sb.append("&secret=");
////        sb.append(StateData.getWXAppSecret());//自己的appSecret
////        sb.append("&js_code=");
////        sb.append(code);
////        sb.append("&grant_type=authorization_code");
////        sb.append("&connect_redirect=1");
////        return get(sb.toString()).getString("openid");
////    }

    private JSONObject get(String url) {
        return new HttpUtil().http(new HttpGet(url));
    }

    private JSONObject post(HttpRequestBase request) {
        return new HttpUtil().http(request);
    }

    /**
     * 将集合全部写入云数据库
     *
     * @param tList
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean writeToDB(@NonNull List<T> tList) {
        if (tList.size() == 0) return true;
        String tableName = tList.get(0).getClass().getSimpleName();
        synchronized (object) {
            controlDB(StaticConfigData.DBDeleteTableUrl, "collection_name", tableName);
            if (!controlDB(StaticConfigData.DBAddTableUrl, "collection_name", tableName))
                return false;

            HttpPost post = new HttpPost(StaticConfigData.DBAddDBUrl + this.getAccessToken());
            JSONObject json = new JSONObject();
            json.put("env", StaticConfigData.EnvID);
            StringBuilder query = new StringBuilder("db.collection(\\\"").append(tableName).append("\\\").add({data: ");
            query.append(JSONArray.toJSONString(tList));
            query.append("})");
            try {
                json.put("query", new String(query.toString().getBytes(), "UTF-8"));
                post.setEntity(new StringEntity(json.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("HttpUtil转换参数出错!");
                throw new MyException(HttpResultEnum.ParameterChangeError);
            }
            JSONObject req = this.post(post);
            boolean errcode = req.getInteger("errcode") == 0;
            if (errcode) {
                log.info("写数据库:" + tableName + "成功");
            } else {
                log.error("写数据库失败,code:" + req.getInteger("errcode") + ",MSG:" + req.getString("errmsg"));
            }
            return errcode;
        }
    }

//    /**
//     * 上传文件到服务器，返回file_id
//     */
//    public static String upLoadFile(@NonNull InputStream inputStream)  {
//        SimpleDateFormat format = new SimpleDateFormat(StaticConfigData.UpLoadFileNameFormatString);
//        String tmpFilePath = "tmp/" + format.format(new Date()) + ".xls";
//        synchronized (object) {
//            HttpPost post = new HttpPost(StaticConfigData.UPLoadFileUrl + getAccessToken());
//            JSONObject json = new JSONObject();
//            json.put("env", StaticConfigData.EnvID);
//            json.put("path", tmpFilePath);
//            post.setEntity(new StringEntity(json.toString(), "UTF-8"));
//            JSONObject req = HttpUtil.post(post);
//            if (req.getInteger("errcode") == 0) {
//                if (upLoadFile(req, inputStream, tmpFilePath)) {
//                    log.info("上传文件到服务器成功");
//                    return req.getString("file_id");
//                }
//            }
//            throw new MyException(10000, "上传文件失败,获取Token失败，errCode:" + req.getInteger("errcode") + ",MSG:" + req.getString("errmsg"));
//        }
//    }
//
//    private static boolean upLoadFile(JSONObject req, InputStream inputStream, String fileName) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        val map = new LinkedMultiValueMap<String, Object>();
//        map.add("x-cos-security-token", req.getString("token"));
//        map.add("Signature", req.getString("authorization"));
//        map.add("x-cos-meta-fileid", req.getString("cos_file_id"));
//        map.add("key", fileName);
//
//        map.add("file", file2Bytes(inputStream));
//        val httpEntity = new org.springframework.http.HttpEntity<>(map, headers);
//        ResponseEntity<Object> response = restTemplate.postForEntity(req.getString("url"), httpEntity, Object.class);
//        if (response.getHeaders().containsKey("Location")) {
//            return true;
//        } else {
//            throw new MyException(HttpResultEnum.UploadFileError);
//        }
//    }
//
//
//    private static byte[] file2Bytes(InputStream inputStream) {
//        byte[] buffer = null;
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024 * 10];
//            int n;
//            while ((n = inputStream.read(b)) != -1) {
//                bos.write(b, 0, n);
//            }
//            inputStream.close();
//            bos.close();
//            buffer = bos.toByteArray();
//        } catch (IOException e) {
//            log.error(e.getLocalizedMessage());
//            throw new MyException(HttpResultEnum.UploadFileError);
//        }
//        return buffer;
//    }

    /**
     * 删除、新建表格
     *
     * @param url
     * @param key         删除和新建均为collection_name
     * @param queryString 需执行的SQL语句
     * @return
     */
    private boolean controlDB(String url, String key, String queryString) {
        HttpPost post = new HttpPost(url + this.getAccessToken());
        JSONObject json = new JSONObject();
        json.put("env", StaticConfigData.EnvID);
        json.put(key, queryString);
        try {
            post.setEntity(new StringEntity(json.toString()));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getLocalizedMessage());
            throw new MyException(HttpResultEnum.ParameterChangeError);
        }
        JSONObject req = this.post(post);
        return req.getInteger("errcode") == 0;
    }


    private JSONObject http(HttpRequestBase request) {
        JSONObject res = new JSONObject();
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)                    // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)             // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)                    // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
        request.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try (CloseableHttpResponse response = httpClient.execute(request)) { // 从响应模型中获取响应实体
                val responseEntity = response.getEntity();
                log.info("响应状态为:" + response.getStatusLine());
                if (responseEntity != null) {
                    res = JSON.parseObject(EntityUtils.toString(responseEntity));
                    //如果AccessToken过期，重新申请
                    if (res.containsKey("errcode")) {
                        int code = res.getInteger("errcode");
                        if (code != 0)
                            log.error("http错误码{}", code);
                        if (code == 42001 || code == 40014) {//42001:AccessToken过期,40014:AccessToken 不合法
                            getAccessTokenFromHttp();
                            throw new MyException(HttpResultEnum.AccessTokenPass);
//                            http(resetUrl(request));
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new MyException(HttpResultEnum.POSTError);
        }
        return res;
    }

    /**
     * 重新注入数据库的AccessToken
     *
     * @param request
     */
    @SneakyThrows
    private HttpRequestBase resetUrl(HttpRequestBase request) {
        String url = request.getURI().toString();
        request.setURI(new URI(url.substring(0, url.indexOf("=") + 1) + this.getAccessToken()));
        return request;
    }

    /**
     * 获取数据库的操作accessToken
     */
    private void getAccessTokenFromHttp() {
        String url = StaticConfigData.AccessTokenURL;
        url += StaticConfigData.AppID;
        url += "&secret=";
        url += StaticConfigData.AppSecret;
        JSONObject jsonObject = get(url);
        if (jsonObject.containsKey("access_token")) {
            AccessToken = jsonObject.getString("access_token");
            log.info("读取数据库AccessToken");
        } else {
            throw new MyException(HttpResultEnum.GetAccessTokenError);
        }
    }

    @Override
    public <T> List<T> getListFromDB(@NonNull Class<T> tClass) {
        List<T> result = new ArrayList<>();
        synchronized (object) {
            getListFromDB(tClass, tClass.getSimpleName(), result, 0);
            return result;
        }
    }

    /**
     * 执行从数据库获取数据的操作
     *
     * @param tClass    需转换成的类
     * @param tableName
     * @param <T>
     * @return
     */
    private <T> void getListFromDB(@NonNull Class<T> tClass, @NonNull String tableName, @NonNull List<T> result,
                                   int OffsetRide) {
        int limitNum = 1000;
        StringBuilder sb = new StringBuilder(StaticConfigData.DBQueryUrl);
        sb.append(this.getAccessToken());
        HttpPost post = new HttpPost(sb.toString());
        JSONObject json = new JSONObject();
        json.put("env", StaticConfigData.EnvID);
        String queryString = String.format("db.collection(\\\"%s\\\").limit(%d).skip(%d).get()", tableName, limitNum,
                OffsetRide * limitNum);
        json.put("query", queryString);
        try {
            post.setEntity(new StringEntity(json.toString()));
        } catch (UnsupportedEncodingException e) {
            log.error(tClass.getSimpleName() + "转换参数出错!");
            throw new MyException(HttpResultEnum.ParameterChangeError);
        }
        JSONObject req = this.post(post);
        if (req.getInteger("errcode") != 0) {
            log.error(req.getString("errmsg"));
        }
        String data = req.getString("data");
        if (null == data) {
            log.error("数据库{}不存在！", tableName);
            data = "";
        }
        //将读取的数据转换成对象列表
        data = data.replace("[\"{", "[{").replace("}\"]", "}]");
        data = data.replace("\\", "").replace("}\",\"{", "},{");
        List<T> collection = JSON.parseArray(data, tClass);
        if (collection == null) {
            return;
        }
        log.info("读表" + tableName + "数据从" + OffsetRide * limitNum + "到" + (OffsetRide * limitNum + collection.size() +
                "项"));
        result.addAll(collection);
        //判断是否读完毕
        JSONObject pager = req.getJSONObject("pager");
        if (pager.getInteger("Total") <= pager.getInteger("Offset") * pager.getInteger("Limit"))
            getListFromDB(tClass, tableName, result, ++OffsetRide);

    }

}
