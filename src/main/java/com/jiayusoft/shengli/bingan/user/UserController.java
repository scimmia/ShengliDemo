package com.jiayusoft.shengli.bingan.user;

import com.jiayusoft.shengli.bingan.utils.BaseResponse;
import com.jiayusoft.shengli.bingan.utils.DebugLog;
import com.jiayusoft.shengli.bingan.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ASUS on 2014/11/19.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/login/doctor", method = RequestMethod.POST)
    public @ResponseBody BaseResponse loginDoctor(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="password", required=false) String password) {
        log.info(DebugLog.info()+userid + "\t---\t" + password + "\t---\t" + orgcode);
        BaseResponse<UserDoctor> response;
        UserDao userDao = new UserDao(jdbcTemplate);
        UserDoctor temp = userDao.checkDoctorUser(userid, password, orgcode);
        if (temp != null){
            MutablePair<String,String> logoAndColor = userDao.getLogoAndColor(orgcode);
            if (logoAndColor!=null){
                temp.setLogoName(Utils.getSignalImageUrl(logoAndColor.getLeft()));
                temp.setLogoColor(logoAndColor.getRight());
            }
            response = new BaseResponse<UserDoctor>(0,null,temp);
        }else {
            response = new BaseResponse<UserDoctor>(1,"用户名密码错误",null);
        }
        return response;
    }

    @RequestMapping(value = "/login/doctorwithimages", method = RequestMethod.POST)
    public @ResponseBody BaseResponse loginDoctorWithImages(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="password", required=false) String password) {
        log.info(DebugLog.info()+userid + "\t---\t" + password + "\t---\t" + orgcode);
        BaseResponse<UserDoctor> response;
        UserDao userDao = new UserDao(jdbcTemplate);
        UserDoctor temp = userDao.checkDoctorUser(userid, password, orgcode);
        if (temp != null){
            MutablePair<String,String> logoAndColor = userDao.getLogoAndColor(orgcode);
            if (logoAndColor!=null){
                temp.setLogoName(logoAndColor.getLeft());
                temp.setLogoColor(logoAndColor.getRight());
            }
            response = new BaseResponse<UserDoctor>(0,null,temp);
        }else {
            response = new BaseResponse<UserDoctor>(1,"用户名密码错误",null);
        }
        return response;
    }
    @RequestMapping(value = "/login/community", method = RequestMethod.POST)
    public @ResponseBody BaseResponse loginCommunity(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="password", required=false) String password) {
        log.info(DebugLog.info()+userid + "\t---\t" + password + "\t---\t" + orgcode);
        BaseResponse<UserCommunity> response;
        UserDao userDao = new UserDao(jdbcTemplate);
        UserCommunity temp = userDao.checkCommunityUser(userid, password, orgcode);
        if (temp != null){
            if (StringUtils.isEmpty(temp.getIdcard()) || temp.getIdcard().length()<6){
                return new BaseResponse<String>(2,"身份证号不合法，请联系账号分配人员",null);
            }else {
                MutablePair<String,String> logoAndColor = userDao.getLogoAndColor(orgcode);
                if (logoAndColor!=null){
                    temp.setLogoName(Utils.getSignalImageUrl(logoAndColor.getLeft()));
                    temp.setLogoColor(logoAndColor.getRight());
                }
                response = new BaseResponse<UserCommunity>(0, null, temp);
            }
        }else {
            response = new BaseResponse<UserCommunity>(1,"用户名密码错误",null);
        }
        return response;
    }
    @RequestMapping(value = "/login/communitywithimages", method = RequestMethod.POST)
    public @ResponseBody BaseResponse loginCommunityWithimages(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="password", required=false) String password) {
        log.info(DebugLog.info()+userid + "\t---\t" + password + "\t---\t" + orgcode);
        BaseResponse<UserCommunity> response;
        UserDao userDao = new UserDao(jdbcTemplate);
        UserCommunity temp = userDao.checkCommunityUser(userid, password, orgcode);
        if (temp != null){
            if (StringUtils.isEmpty(temp.getIdcard()) || temp.getIdcard().length()<6){
                return new BaseResponse<String>(2,"身份证号不合法，请联系账号分配人员",null);
            }else {
                MutablePair<String,String> logoAndColor = userDao.getLogoAndColor(orgcode);
                if (logoAndColor!=null){
                    temp.setLogoName(logoAndColor.getLeft());
                    temp.setLogoColor(logoAndColor.getRight());
                }
                response = new BaseResponse<UserCommunity>(0, null, temp);
            }
        }else {
            response = new BaseResponse<UserCommunity>(1,"用户名密码错误",null);
        }
        return response;
    }

    @RequestMapping(value = "/login/communitywithoutorg", method = RequestMethod.POST)
    public @ResponseBody BaseResponse loginCommunityWithoutorg(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="password", required=false) String password) {
        log.info(DebugLog.info()+userid + "\t---\t" + password);
        BaseResponse<UserCommunity> response;
        UserDao userDao = new UserDao(jdbcTemplate);
        UserCommunity temp = userDao.checkCommunityUserWithoutOrg(userid, password);
        if (temp != null){
            MutablePair<String,String> logoAndColor = userDao.getEhrLogoAndColor();
            if (logoAndColor!=null){
                temp.setLogoName(logoAndColor.getLeft());
                temp.setLogoColor(logoAndColor.getRight());
            }
            response = new BaseResponse<UserCommunity>(0, null, temp);
        }else {
            response = new BaseResponse<UserCommunity>(1,"用户名密码错误",null);
        }
        return response;
    }
    @RequestMapping(value = "/checkUpdate", method = RequestMethod.POST)
    public @ResponseBody BaseResponse checkUpdate(
            @RequestParam(value="versioncode", required=true) String versioncode,
            @RequestParam(value="softname", required=true) String softname){
        log.info(DebugLog.info()+versioncode + "|||" + softname);
        UpdateInfo updateInfo = new UserDao(jdbcTemplate).getMaxVersionInfo(softname);
        if (updateInfo!=null && updateInfo.getVersionCode()> NumberUtils.toInt(versioncode)){
            return new BaseResponse<UpdateInfo>(0,null,updateInfo);
        }else {
            return new BaseResponse<UpdateInfo>(1,null,null);
        }
    }

    @RequestMapping(value = "/orginfo", method = RequestMethod.GET)
    public @ResponseBody BaseResponse getOrgInfo(){
        log.info(DebugLog.info());
        List<MutablePair<String,String>> orgInfo = new UserDao(jdbcTemplate).getOrgInfo();
        if (orgInfo!=null){
            return new BaseResponse<List<MutablePair<String,String>>>(0,null,orgInfo);
        }else {
            return new BaseResponse<List<MutablePair<String,String>>>(1,"获取机构信息失败",null);
        }
    }
}
