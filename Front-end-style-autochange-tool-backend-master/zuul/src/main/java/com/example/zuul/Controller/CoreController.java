package com.example.zuul.Controller;

import com.example.zuul.VO.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 14:52
 */
@RestController
@RequestMapping("/core")
public class CoreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreController.class);
    @Autowired
    RestTemplate restTemplate;

    final String STORAGE_HEADER = "http://objectstorage/minio";
    final String USER_HEADER = "http://user/user";
    final String CORE_HEADER = "http://core/replace";

    @RequestMapping(value = "/adjust", method = RequestMethod.POST)
    public ResponseVO adjustHtml(@RequestParam("fileId") String fileId,@RequestParam("id") String id,@RequestParam("attribute") String attribute,@RequestParam("time")String time){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(CORE_HEADER+"/adjust?fileId={1}&id={3}&attribute={3}&time={4}",null,String.class,fileId,id,attribute,time));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public ResponseVO sourceUpload(@RequestParam("userId")int userId, @RequestParam("file")MultipartFile file,@RequestParam("name")String name,@RequestParam("type") String type){
        try {
            String fileId = fileSave(file, type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadTime = sdf.format(new Date())+"";
            restTemplate.postForObject(USER_HEADER+"/sourceadd?fileId={1}&userId={2}&sourceName={3}&uploadTime={4}&type={5}",null,Boolean.class,fileId,userId,name,uploadTime,type);
            return ResponseVO.buildSuccess(fileId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/attribute", method = RequestMethod.GET)
    public ResponseVO getAttribute(@RequestParam("fileId")String fileId,@RequestParam("tagId")String tagId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(CORE_HEADER+"/attribute?fileId={1}&tagId={2}",String.class,fileId,tagId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseVO.buildFailure("error");
        }
    }

    @RequestMapping(value = "/target", method = RequestMethod.POST)
    public ResponseVO targetUpload(@RequestParam("userId")int userId, @RequestParam("file")MultipartFile file,@RequestParam("type") String type){
        try {
            String fileId = fileSave(file,type);
            return ResponseVO.buildSuccess(fileId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    public String fileSave(MultipartFile file,String type){
        try {
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            MediaType paramType = MediaType.parseMediaType("multipart/form-data");
            headers.setContentType(paramType);

            //转换文件
            ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            //设置请求体，注意是LinkedMultiValueMap
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", byteArrayResource);
            form.add("type",type);

            //用HttpEntity封装整个请求报文
            HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

            String fileId = restTemplate.postForObject(STORAGE_HEADER+"/upload", files, String.class);
            return fileId;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return "error";
    }

    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public ResponseVO getFile(@RequestParam("fileId") String fileId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(STORAGE_HEADER+"/url?htmlKey={1}",String.class,fileId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }
}
