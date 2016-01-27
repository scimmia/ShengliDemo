package com.jiayusoft.shengli.bingan.selfupload;

import com.jiayusoft.shengli.bingan.utils.DataBaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 15-3-17.
 */
public class SelfUploadDao {
    JdbcTemplate jdbcTemplate;

    public SelfUploadDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String insertDescribe(final String idcard,final String orgCode,final String describe, final int uploadtype){
        if(StringUtils.isEmpty(idcard)){
            return null;
        }
        final String uuid = UUID.randomUUID().toString();
        int i = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "insert into T_BA_MOBILE_SELF_DESCRIBE " +
                        "(serial_num,idcard,org_code,SELFDESCRIBE,UPLOADTIME,UPLOADTYPE) " +
                        "values(?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, uuid);
                ps.setString(2, idcard);
                ps.setString(3, orgCode);
                ps.setString(4, describe);
                ps.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                ps.setInt(6,uploadtype);
                return ps;
            }
        });
        if (i>0) {
            return uuid;
        }else {
            return null;
        }
    }

    public String insertFile(final String serialNum,final String uploadDate,final String fileName){
        if(StringUtils.isNotEmpty(serialNum)) {
            int i = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "insert into T_BA_MOBILE_SELF_FILE " +
                            "(serial_num,FILEPATH,FILENAME) " +
                            "values(?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, serialNum);
                    ps.setString(2, uploadDate);
                    ps.setString(3, fileName);
                    return ps;
                }
            });
            if (i > 0) {
                return "success";
            }
        }
        return null;
    }

    public List<SelfUpload> querySelfList(String orgcode, String idcard,Integer uploadtype, String startindex) {
        List<SelfUpload> result = null;
        if (StringUtils.isNoneBlank(orgcode, idcard, startindex)){
            String finanSql = DataBaseUtil.buildPage(
                    "select * from t_ba_mobile_self_describe " +
                            "where ORG_CODE='" + orgcode + "' and idcard='" + idcard + "' and UPLOADTYPE=" +uploadtype +
                            " order by UPLOADTIME desc",
                    startindex, null);
            System.out.println(finanSql);
            result = jdbcTemplate.query(finanSql,
                    new RowMapper<SelfUpload>() {
                        @Override
                        public SelfUpload mapRow(ResultSet rs, int rowNum) throws SQLException {
                            SelfUpload selfUpload = new SelfUpload();
                            selfUpload.setSerialNum(rs.getString("SERIAL_NUM"));
                            selfUpload.setIdCard(rs.getString("IDCARD"));
                            selfUpload.setOrgCode(rs.getString("ORG_CODE"));
                            selfUpload.setDescribe(rs.getString("SELFDESCRIBE"));
                            Timestamp t = rs.getTimestamp("UPLOADTIME");
                            selfUpload.setUploadTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(t.getTime()));
                            return selfUpload;
                        }
                    });
            if (result!=null){
                for (SelfUpload selfUpload : result){
                    List<String> temp = querySelfDetail(selfUpload.getSerialNum());
                    if (temp!=null) {
                        selfUpload.getFileNames().addAll(temp);
                    }
                }
            }
        }

        return result;
    }

    public List<String> querySelfDetail(String serialNum) {
        return jdbcTemplate.query(
                "select FILENAME from t_ba_mobile_self_file where SERIAL_NUM ='"+serialNum+"'",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("FILENAME");
                    }
                }
        );
    }

    public boolean deleteSelf(final String serialNum) {
        boolean result = false;
        if(StringUtils.isNotEmpty(serialNum)) {
            int i = jdbcTemplate.update(
                    "delete from t_ba_mobile_self_describe where serial_num = '"+serialNum+"'");
            result = i > 0;
        }
        return result;
    }

    public boolean deleteSelfDetail(String serialNum,String filename) {
        boolean result = false;
        if(!StringUtils.isAnyEmpty(serialNum, filename)) {
            int i = jdbcTemplate.update(
                    "delete from t_ba_mobile_self_FILE " +
                            "where serial_num = '"+serialNum+"' " +
                            "and filename='"+filename+"'");
            result = i > 0;
        }
        return result;
    }
}
