package com.jiayusoft.shengli.bingan.bingan;

import com.jiayusoft.shengli.bingan.user.UserDao;
import com.jiayusoft.shengli.bingan.utils.BaseResponse;
import com.jiayusoft.shengli.bingan.utils.DebugLog;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ASUS on 2014/12/9.
 */
@RestController
@RequestMapping("/bingan")
public class BinganController {

    private final static Logger log = LoggerFactory.getLogger(BinganController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/doctor/list", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getNormalList(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="binganhao", required=false) String binganhao,
            @RequestParam(value="shenfenzhengid", required=false) String shenfenzhengid,
            @RequestParam(value="xingming", required=false) String xingming,
            @RequestParam(value="chaxunleibie", required=false) String chaxunleibie,
            @RequestParam(value="chuyuanriqibegin", required=false) String chuyuanriqibegin,
            @RequestParam(value="chuyuanriqiend", required=false) String chuyuanriqiend,
            @RequestParam(value="keshibianma", required=false) String keshibianma,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||" + binganhao + "|||" + shenfenzhengid + "|||" +
                xingming + "|||" +chaxunleibie + "|||" + chuyuanriqibegin + "|||" + chuyuanriqiend + "|||" + keshibianma + "|||"
                + requirecount + "|||" + startindex);
        if (new UserDao(jdbcTemplate).isDoctorUserExists(userid, orgcode)){
            List<Bingan> bingans = new BinganDao(jdbcTemplate).queryBingan(userid, orgcode, binganhao, shenfenzhengid,
                    xingming, chaxunleibie,chuyuanriqibegin, chuyuanriqiend, keshibianma, requirecount,startindex);
            return new BaseResponse<List<Bingan>>(0,null,bingans);
        }
        return new BaseResponse<List<Bingan>>(1,null,null);
    }

    @RequestMapping(value = "/doctor/detail/{orgcode}/{binganid}", method = RequestMethod.GET)
    public @ResponseBody BaseResponse getDetail(
            @PathVariable String orgcode,
            @PathVariable String binganid,
            @RequestParam(value="baomijibie",required=false) String baomijibie){
        log.info(DebugLog.info()+orgcode+'\t'+binganid+'\t'+baomijibie);
        List<MutablePair<String,String>> temp = new BinganDao(jdbcTemplate).queryBinganDetail(orgcode,binganid,baomijibie);
        if (temp!=null){
            return new BaseResponse<List<MutablePair<String,String>>>(0,null,temp);
        }else {
            return new BaseResponse<List<MutablePair<String, String>>>(1,"病案信息异常，无法查看", null);
        }
    }

    @RequestMapping(value = "/community/list", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getCommunityList(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="password", required=true) String password,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||"  + password + "|||"  + "|||" + requirecount + "|||" + startindex);
        List<Bingan> bingans = new BinganDao(jdbcTemplate)
                .queryCommunityBingan(userid, password, orgcode, requirecount, startindex);
        if (bingans!=null){
            return new BaseResponse<List<Bingan>>(0,null,bingans);
        }
        return new BaseResponse<List<Bingan>>(1,null,null);
    }

    @RequestMapping(value = "/community/listwithoutorg", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getCommunityListWithoutOrg(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="password", required=true) String password,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||"  + password + "|||"  + "|||" + requirecount + "|||" + startindex);
        List<Bingan> bingans = new BinganDao(jdbcTemplate)
                .queryCommunityBingan(userid, password, requirecount, startindex);
        if (bingans!=null){
            return new BaseResponse<List<Bingan>>(0,null,bingans);
        }
        return new BaseResponse<List<Bingan>>(1,null,null);
    }

    @RequestMapping(value = "/community/detail/{orgcode}/{binganid}", method = RequestMethod.GET)
    public @ResponseBody BaseResponse getDetail(
            @PathVariable String orgcode,
            @PathVariable String binganid){
        log.info(DebugLog.info()+orgcode+'\t'+binganid);
        List<MutablePair<String,String>> temp = new BinganDao(jdbcTemplate).queryCommunityBinganDetail(orgcode, binganid);
        if (temp!=null){
            return new BaseResponse<List<MutablePair<String,String>>>(0,null,temp);
        }else {
            return new BaseResponse<List<MutablePair<String, String>>>(1,"病案信息异常，无法查看", null);
        }
    }

    @RequestMapping(value = "/borrowlist", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getBorrowList(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="binganhao", required=false) String binganhao,
            @RequestParam(value="shenfenzhengid", required=false) String shenfenzhengid,
            @RequestParam(value="xingming", required=false) String xingming,
            @RequestParam(value="chuyuanriqibegin", required=false) String chuyuanriqibegin,
            @RequestParam(value="chuyuanriqiend", required=false) String chuyuanriqiend,
            @RequestParam(value="mubiaoorgcode", required=false) String mubiaoorgcode,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||" + binganhao + "|||" + shenfenzhengid + "|||" +
                xingming + "|||" + chuyuanriqibegin + "|||" + chuyuanriqiend + "|||" + mubiaoorgcode + "|||"
                + requirecount + "|||" + startindex);
        List<Bingan> bingans = new BinganDao(jdbcTemplate).queryBorrowBingan(userid, orgcode, binganhao, shenfenzhengid,
                xingming, chuyuanriqibegin, chuyuanriqiend, mubiaoorgcode, requirecount,startindex);
        return new BaseResponse<List<Bingan>>(0,null,bingans);
    }

    @RequestMapping(value = "/borrow", method = RequestMethod.POST)
    public @ResponseBody BaseResponse borrowBingan(
            @RequestParam(value="zuzhidaima", required=true) String zuzhidaima,
            @RequestParam(value="zuzhiname", required=true) String zuzhiname,
            @RequestParam(value="binganid", required=true) String binganid,
            @RequestParam(value="shenqingren", required=true) String shenqingren,
            @RequestParam(value="shenqingkeshicode", required=false) String shenqingkeshicode,
            @RequestParam(value="shenqingkeshiname", required=false) String shenqingkeshiname,
            @RequestParam(value="shenqingdanweicode", required=true) String shenqingdanweicode,
            @RequestParam(value="shenqingdanweiname", required=true) String shenqingdanweiname){
        log.info(DebugLog.info()+zuzhidaima + "|||" + zuzhiname + "|||" + binganid + "|||" + shenqingren+ "|||"
            +shenqingkeshicode + "|||" + shenqingkeshiname + "|||" + shenqingdanweicode + "|||" + shenqingdanweiname);
        int result = new BinganDao(jdbcTemplate).borrowBingan(
                zuzhidaima, zuzhiname, binganid, shenqingren,shenqingkeshicode, shenqingkeshiname, shenqingdanweicode, shenqingdanweiname);
        String msg = "";
        switch (result){
            case 0:
                return new BaseResponse<String>(1,"error",null);
            case 1:
                return new BaseResponse<String>(0,null,binganid);
            default:
                break;
        }
        return new BaseResponse<String>(result,msg,null);
    }

    @RequestMapping(value = "/borrowstates", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getBorrowStates(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="state", required=false, defaultValue="2") String state,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||" + state + "|||" + requirecount + "|||" + startindex);
        List<Bingan> bingans = new BinganDao(jdbcTemplate).queryBorrowBinganState(userid, orgcode, state,requirecount,startindex);
        return new BaseResponse<List<Bingan>>(0,null,bingans);
    }

    @RequestMapping(value = "/qualitystates", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getBinganQuality(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||"  + "|||" + requirecount + "|||" + startindex);
        BinganDao dao = new BinganDao(jdbcTemplate);
        List<BinganQuality> qualities = dao.queryBinganQuality(userid, orgcode, requirecount, startindex);
        if (qualities!=null){
            for (BinganQuality quality : qualities){
                quality.setPingjiaList(
                        dao.queryBinganQualityDetail(quality.getBingan().getZuzhidaima(),quality.getBingan().getBiaoshima()));
            }
            return new BaseResponse<List<BinganQuality>>(0, null, qualities);
        }else {
            return new BaseResponse<List<BinganQuality>>(1, "error", null);
        }
    }

    @RequestMapping(value = "/qualityneedrepair", method = RequestMethod.POST)
    public @ResponseBody BaseResponse getBinganQualityNeedRepair(
            @RequestParam(value="userid", required=true) String userid,
            @RequestParam(value="orgcode", required=true) String orgcode,
            @RequestParam(value="requirecount", required=false, defaultValue="30") String requirecount,
            @RequestParam(value="startindex", required=false, defaultValue="0") String startindex){
        log.info(DebugLog.info()+userid + "|||" + orgcode + "|||"  + "|||" + requirecount + "|||" + startindex);
        BinganDao dao = new BinganDao(jdbcTemplate);
        List<Bingan> bingans = dao.queryBinganQualityNeedRepair(userid, orgcode, requirecount, startindex);
        return new BaseResponse<List<Bingan>>(0,null,bingans);
    }

}
