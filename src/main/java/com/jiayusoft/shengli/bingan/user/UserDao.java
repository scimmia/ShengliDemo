package com.jiayusoft.shengli.bingan.user;

import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ASUS on 2014/11/19.
 */
public class UserDao {

//    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDoctor checkDoctorUser(String userid, String password,String orgcode) {
        List<UserDoctor> users = jdbcTemplate.query("SELECT * from t_bnzlys where (idcard=? or yonghuming = ?) and password=? and yyidentiry=?",
                new String[]{userid,userid,password,orgcode},
                new RowMapper<UserDoctor>() {
                    @Override
                    public UserDoctor mapRow(ResultSet rs, int i) throws SQLException {
                        UserDoctor tBnzlys = new UserDoctor();
                        tBnzlys.setName(rs.getString("name"));
                        tBnzlys.setIdcard(rs.getString("IDCARD"));
                        tBnzlys.setGlksbm(rs.getString("glksbm"));
                        tBnzlys.setCxlb(rs.getString("cxlb"));
                        tBnzlys.setYyidentiry(rs.getString("YYIDENTIRY"));
                        tBnzlys.setOfficecode(rs.getString("officecode"));
                        tBnzlys.setBnjyyxq(rs.getLong("bnjyyxq"));
                        return tBnzlys;
//                        return rs.getString("glshenheks");
                    }
                });
        if (users != null && users.size() != 0) {
            return users.get(0);
        }else {
            return null;
        }
//            JdbcTemplate jdbcTemplate = DataBaseUtil.getJdbcTemplate();
//        List<String> users = jdbcTemplate.bingan("SELECT glshenheks FROM user WHERE cardid=? and password=? and org",
//                new String[]{userid,password},
//                new RowMapper<String>() {
//                    @Override
//                    public String mapRow(ResultSet rs, int i) throws SQLException {
//
//                        return rs.getString("glshenheks");
//                    }
//                });
//        List uNames = jdbcTemplate.queryForList("SELECT uesrtype FROM user WHERE username=? and password=?",
//                Integer.class, userid,password);
////        if (users.size()==1)
////            return users.get(0);
//        return null;
    }

    public UserCommunity checkCommunityUser(String userid, String password,String orgcode) {
        List<UserCommunity> users = jdbcTemplate.query(
                "select PATIENTIDCARD_,PATIENTNAME_ from medreccopyings_ where username = ? and userpassword=? and yljgbm=? and regtime_=(" +
                        "select max(regtime_) from medreccopyings_ " +
                        "where username = ? and userpassword=? and yljgbm=?)",
                new String[]{userid, password, orgcode, userid, password, orgcode},
                new RowMapper<UserCommunity>() {
                    @Override
                    public UserCommunity mapRow(ResultSet rs, int i) throws SQLException {
                        UserCommunity userTemp = new UserCommunity();
                        userTemp.setName(rs.getString("PATIENTNAME_"));
                        userTemp.setIdcard(rs.getString("PATIENTIDCARD_"));
                        return userTemp;
                    }
                });
        if (users != null && users.size() > 0) {
            return users.get(0);
        }else {
            return null;
        }
//        return users != null && users > 0;
    }

    public UserCommunity checkCommunityUserWithoutOrg(String userid, String password) {
        List<UserCommunity> users = jdbcTemplate.query(
                "select name_ from cardids_ where cardno_ = ? and password=?",
                new String[]{userid, password },
                new RowMapper<UserCommunity>() {
                    @Override
                    public UserCommunity mapRow(ResultSet rs, int i) throws SQLException {
                        UserCommunity userTemp = new UserCommunity();
                        userTemp.setName(rs.getString("name_"));
                        return userTemp;
                    }
                });
        if (users != null && users.size() > 0) {
            return users.get(0);
        }else {
            return null;
        }
//        return users != null && users > 0;
    }
    public MutablePair<String,String> getLogoAndColor(String orgcode){
        List<MutablePair<String,String>> logoAndColor =
                jdbcTemplate.query(
                        "SELECT PHONEIMG,PHONEZHUTI FROM t_yybs WHERE code ='"+orgcode+"'",
                        new RowMapper<MutablePair<String, String>>() {
                            @Override
                            public MutablePair<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
                                MutablePair<String,String> pair = new MutablePair<String,String>();
                                pair.setLeft(resultSet.getString("PHONEIMG"));
                                pair.setRight(resultSet.getString("PHONEZHUTI"));
                                return pair;
                            }
                        });
        if (logoAndColor!=null && logoAndColor.size()>0){
            return logoAndColor.get(0);
        }else {
            return null;
        }
    }

    public MutablePair<String,String> getEhrLogoAndColor(){
        List<MutablePair<String,String>> logoAndColor =
                jdbcTemplate.query(
                        "SELECT PHONEIMG,PHONEZHUTI FROM t_ptbs WHERE modelname ='EHR'",
                        new RowMapper<MutablePair<String, String>>() {
                            @Override
                            public MutablePair<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
                                MutablePair<String,String> pair = new MutablePair<String,String>();
                                pair.setLeft(resultSet.getString("PHONEIMG"));
                                pair.setRight(resultSet.getString("PHONEZHUTI"));
                                return pair;
                            }
                        });
        if (logoAndColor!=null && logoAndColor.size()>0){
            return logoAndColor.get(0);
        }else {
            return null;
        }
    }

    public boolean isDoctorUserExists(String userid,String orgcode) {
        List<String> users = jdbcTemplate.query("SELECT * from t_bnzlys where (idcard=? or yonghuming = ?) and yyidentiry=?",
                new String[]{userid,userid,orgcode},
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getString("name");
                    }
                });
        return users != null && users.size() > 0;
    }

    public UpdateInfo getMaxVersionInfo(String softName){
        List<UpdateInfo> updateInfos = jdbcTemplate.query(
                "SELECT * FROM t_ba_mobile_version where soft_name=? and version_code=" +
                        "(SELECT MAX(version_code) FROM t_ba_mobile_version where soft_name=?)",
                new String[]{softName,softName},
                new RowMapper<UpdateInfo>() {
                    @Override
                    public UpdateInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UpdateInfo updateInfo = new UpdateInfo();
                        updateInfo.setVersionCode(rs.getInt("VERSION_CODE"));
                        updateInfo.setVersionName(rs.getString("VERSION_NAME"));
                        updateInfo.setSoftName(rs.getString("SOFT_NAME"));
                        updateInfo.setSoftUrl(rs.getString("SOFT_URL"));
                        updateInfo.setUpdateLog(rs.getString("UPDATE_INFO"));
                        return updateInfo;
                    }
                });
        if (updateInfos!=null && updateInfos.size()>0){
            return updateInfos.get(0);
        }else {
            return null;
        }
    }

    public List<MutablePair<String,String>> getOrgInfo(){
        List<MutablePair<String,String>> orgs = jdbcTemplate.query(
                "select code,name from t_yybs order by id",
                new RowMapper<MutablePair<String,String>>() {
                    @Override
                    public MutablePair<String,String> mapRow(ResultSet rs, int i) throws SQLException {
                        MutablePair<String,String> pair = new MutablePair<String,String>();
                        pair.setLeft(rs.getString("code"));
                        pair.setRight(rs.getString("name"));
                        return pair;
                    }
                });
        return orgs;
    }
}
