package com.jiayusoft.shengli.bingan.selfupload;

import com.jiayusoft.shengli.bingan.Application;
import com.jiayusoft.shengli.bingan.utils.BaseResponse;
import com.jiayusoft.shengli.bingan.utils.DebugLog;
import com.jiayusoft.shengli.bingan.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 15-3-17.
 */
@RestController
@RequestMapping("/iosself")
public class IOSSelfUploadController {
    private final static Logger log = LoggerFactory.getLogger(IOSSelfUploadController.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String uploadSelfDescribe(
            @RequestParam(value="idcard", required=true) String idcard,
            @RequestParam(value="orgcode", required=true) String orgCode,
            @RequestParam(value="selfdescribe", required=true) String selfDescribe,
            @RequestParam(value="uploadtype", required=true) Integer uploadtype){
        log.info(DebugLog.info()+"selfdescribe:" + selfDescribe+"|--" + Utils.getBase64Data(selfDescribe) + "--|");
        String id = new SelfUploadDao(jdbcTemplate).insertDescribe(idcard,orgCode, Utils.getBase64Data(selfDescribe), uploadtype);
        if (StringUtils.isNotEmpty(id)){
            return id;
        }else {
            return "failed";
        }
    }

    @RequestMapping(value="/upload/file", method=RequestMethod.POST)
    public @ResponseBody String uploadSelfFile(
            @RequestParam(value="idcard", required=true) String idcard,
            @RequestParam(value="serialnum", required=true) String serialnum,
            @RequestParam(value="file", required=true) MultipartFile file,
            @RequestParam(value="uploadtype", required=true) Integer uploadtype){
        log.info(DebugLog.info()+"idcard:" + idcard);
        String baseFolder = getBaseFolder(uploadtype);
        if (StringUtils.isEmpty(baseFolder)){
            return "failed";
        }
        String uploadDate = DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
        String relativePath = getRelativePath(idcard,uploadDate);
        File folder = new File(baseFolder+relativePath);
        if (!folder.exists()){
            log.info("mkdirs:"+folder.getAbsolutePath());
            folder.mkdirs();
        }
        try {
            final String fileName = UUID.randomUUID().toString();
            int pos = file.getOriginalFilename().lastIndexOf(".");
            String extName = StringUtils.substring(file.getOriginalFilename(), pos);
            File targetFile = new File(folder.getPath()+File.separator
                    +fileName+extName
            );
            file.transferTo(targetFile);
            if (new SelfUploadDao(jdbcTemplate).insertFile(serialnum, uploadDate, fileName+extName) !=null)
                return "success";
        } catch (Exception e) {
            return "failed";
        }
        return "failed";
    }

    @RequestMapping(value = "/list/{orgcode}/{idcard}/{uploadtype}/{startindex}", method = RequestMethod.GET)
    public @ResponseBody BaseResponse getHealthCheckList(
            @PathVariable String orgcode,
            @PathVariable String idcard,
            @PathVariable Integer uploadtype,
            @PathVariable String startindex){
        log.info(DebugLog.info()+orgcode+'\t'+idcard+'\t'+startindex);
        List<SelfUpload> temp = new SelfUploadDao(jdbcTemplate).querySelfList(orgcode, idcard, uploadtype, startindex);
        if (temp!=null){
            return new BaseResponse<List<SelfUpload>>(0,null,temp);
        }else {
            return new BaseResponse<List<SelfUpload>>(1,"信息异常，无法查看", null);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody BaseResponse deleteSelf(
            @RequestParam(value="idcard", required=true) String idcard,
            @RequestParam(value="serialnum", required=true) String serialnum,
            @RequestParam(value="uploaddate", required=true) String uploaddate,
            @RequestParam(value="uploadtype", required=true) Integer uploadtype){
        log.info(DebugLog.info()+serialnum);
//        String temp = new HealthCheckDao(jdbcTemplate).deleteEmr(serialnum);
//        return new BaseResponse<String>(0,null,temp);
        List<String> temp = new SelfUploadDao(jdbcTemplate).querySelfDetail(serialnum);

        if (new SelfUploadDao(jdbcTemplate).deleteSelf(serialnum)){
            if (temp!=null){
                for(String filename:temp){
                    deleteFile(uploadtype,idcard,uploaddate,filename);
                }
            }
            return new BaseResponse<String>(0,null,"删除成功");
        }else {
            return new BaseResponse<String>(1,"删除失败", null);
        }
    }

    @RequestMapping(value = "/delete/detail", method = RequestMethod.POST)
    public @ResponseBody BaseResponse deleteSelfDetail(
            @RequestParam(value="idcard", required=true) String idcard,
            @RequestParam(value="serialnum", required=true) String serialnum,
            @RequestParam(value="uploaddate", required=true) String uploaddate,
            @RequestParam(value="filename", required=true) String filename,
            @RequestParam(value="uploadtype", required=true) Integer uploadtype){
        log.info(DebugLog.info()+filename);
//        String temp = new HealthCheckDao(jdbcTemplate).deleteEmrDetail(serialnum,filename);
//        return new BaseResponse<String>(0,null,temp);
        if (new SelfUploadDao(jdbcTemplate).deleteSelfDetail(serialnum,filename)){
            deleteFile(uploadtype,idcard,uploaddate,filename);
            return new BaseResponse<String>(0,null,"删除成功");
        }else {
            return new BaseResponse<String>(1,"删除失败", null);
        }
    }


    String getBaseFolder(Integer uploadtype){
        String baseFolder = null;
        if (uploadtype!=null) {
            switch (uploadtype) {
                case 1:
                    baseFolder = Application.describeFolder;
                    break;
                case 2:
                    baseFolder = Application.healthcheckFolder;
                    break;
                default:
                    break;
            }
        }
        return baseFolder;
    }

    String getRelativePath(String idcard,String uploadDate){
        return StringUtils.substring(idcard,0,6)+ File.separator
                + idcard+File.separator
                + uploadDate+File.separator;
    }

    boolean deleteFile(Integer uploadtype,String idcard,String uploadDate,String filename){
        boolean result = false;
        String baseFolder = getBaseFolder(uploadtype);
        if (StringUtils.isNotEmpty(baseFolder)) {
            String relativePath = getRelativePath(idcard,uploadDate);
            File targetFile = new File(baseFolder + relativePath + filename);
            if (targetFile.exists() && targetFile.isFile()) {
                log.info(DebugLog.info() + "delete" + filename);
                result = targetFile.delete();
            }
        }
        return result;
    }

}
