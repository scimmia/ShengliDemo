package com.jiayusoft.shengli.bingan.bingan;

import com.jiayusoft.shengli.bingan.utils.DataBaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by ASUS on 2014/12/9.
 */
public class BinganDao {
    public static final String serverUrl = "http://11.0.0.55";
    JdbcTemplate jdbcTemplate;

    public BinganDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Bingan> queryBingan(String userid, String orgcode, String binganhao, String shenfenzhengid, String
            xingming,String chaxunleibie, String chuyuanriqibegin, String chuyuanriqiend,String keshibianma, String requirecount, String startindex) {
        List<Bingan> bingans = null;
        if (StringUtils.isNotEmpty(orgcode)){
            StringBuilder sql = new StringBuilder(
                    "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname\n" +
                    "from t_ba_base \n" +
                    "left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ where zzdm='");
//            StringBuilder sql = new StringBuilder("SELECT * from t_ba_base where zzdm='");
            sql.append(orgcode).append('\'');
            if (StringUtils.isNotEmpty(binganhao)){
                sql.append(" and banum='").append(binganhao).append('\'');
            }
            if (StringUtils.isNotEmpty(shenfenzhengid)){
                sql.append(" and idcard='").append(shenfenzhengid).append('\'');
            }
            if (StringUtils.isNotEmpty(xingming)){
                sql.append(" and name='").append(xingming).append('\'');
            }
            if (StringUtils.isNotEmpty(chuyuanriqibegin)){
                sql.append(" and cytime>=to_date('").append(chuyuanriqibegin).append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')");
            }
            if (StringUtils.isNotEmpty(chuyuanriqiend)){
                sql.append(" and cytime<=to_date('").append(chuyuanriqiend).append(" 23:59:59','YYYY-MM-DD hh24:mi:ss')");
            }
            sql.append(" and (bnbiaoshi1 is null or bnbiaoshi1='00')");
            if (StringUtils.isNotEmpty(keshibianma)){
                sql.append(" and cyksbm in ('");
                sql.append(StringUtils.replace(keshibianma,",","','"));
                sql.append("')");
            }else if(!StringUtils.equals(chaxunleibie,"2")){
                sql.append(" and t_ba_base.id in (select zblsh from T_Bnzrr where zrridcard ='");
                sql.append(userid);
                sql.append("' group by zblsh)");
            }
            sql.append(" order by cytime desc,cyksbm");
            String finanSql = DataBaseUtil.buildPage(sql.toString(), startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<Bingan>() {
                        @Override
                        public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                            return buildBingan(rs);
                        }
                    });
        }
        return bingans;
    }
    Bingan buildBingan(ResultSet rs) throws SQLException {
        Bingan bingan = new Bingan(rs.getString("BAIDENTITY"));
        bingan.setBinganhao(rs.getString("BANUM"));
        bingan.setZuzhidaima(rs.getString("zzdm"));
        bingan.setZuzhiname(rs.getString("ZZNAME"));
        bingan.setFenzhijigoubiaosi(rs.getString("FZJGBSF"));
        bingan.setBinganhao(rs.getString("BANUM"));
        bingan.setXingming(rs.getString("NAME"));
        Timestamp timestamp = rs.getTimestamp("CYTIME");
        if (timestamp != null){
            bingan.setChuyuanshijian(DateFormatUtils.ISO_DATE_FORMAT.format(timestamp));
        }
//        bingan.setChuyuanshijian(rs.getTimestamp("CYTIME"));
        bingan.setChuyuankeshi(rs.getString("cyksname"));
//        bingan.setLiyuanfangshi(rs.getString("NAME"));
//        bingan.setZhusu(rs.getString("ZHUSU"));
//        bingan.setXingming(rs.getString("NAME"));
        return bingan;
    }

    public List<Bingan> queryBorrowBingan(String userid, String orgcode, String binganhao, String shenfenzhengid, String
            xingming, String chuyuanriqibegin, String chuyuanriqiend,String mubiaoorgcode, String requirecount, String startindex) {
        List<Bingan> bingans = null;
        if (StringUtils.isNotEmpty(orgcode)){
            StringBuilder sql = new StringBuilder(
                    "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname,BNZT from t_ba_base " +
                    " left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ ");
//            StringBuilder sql = new StringBuilder("SELECT * from t_ba_base where zzdm='");
            sql.append("left join")
                    .append(" (select zzdm as tempzzdm,JYIDENTITY,bnzt from t_bnjy where sqr='").append(userid)
                    .append("' and SQDANWEI ='").append(orgcode)
                    .append("' and zzdm ='").append(mubiaoorgcode)
                    .append("' and ((BNZT ='2'")
                    .append(" and jzrq>=to_date('").append(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())).append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')")
                    .append(") or bnzt = '1')) on ZZDM=tempzzdm and BAIDENTITY=JYIDENTITY")
                    .append(" where zzdm='").append(mubiaoorgcode).append('\'');
            if (StringUtils.isNotEmpty(binganhao)){
                sql.append(" and banum='").append(binganhao).append('\'');
            }
            if (StringUtils.isNotEmpty(shenfenzhengid)){
                sql.append(" and idcard='").append(shenfenzhengid).append('\'');
            }
            if (StringUtils.isNotEmpty(xingming)){
                sql.append(" and name='").append(xingming).append('\'');
            }
            if (StringUtils.isNotEmpty(chuyuanriqibegin)){
                sql.append(" and cytime>=to_date('").append(chuyuanriqibegin).append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')");
            }
            if (StringUtils.isNotEmpty(chuyuanriqiend)){
                sql.append(" and cytime<=to_date('").append(chuyuanriqiend).append(" 23:59:59','YYYY-MM-DD hh24:mi:ss')");
            }
            sql.append(" and (bnbiaoshi1 is null or bnbiaoshi1='00')");
//            if (StringUtils.isNotEmpty(keshibianma)){
//                sql.append(" and cyksbm in ('");
//                sql.append(StringUtils.replace(keshibianma,",","','"));
//                sql.append("')");
//            }
//            {
//                sql.append(" and BAIDENTITY not in (select JYIDENTITY from t_bnjy where sqr ='")
//                        .append(userid)
//                        .append("' and (bnzt='1' or jzrq>=to_date('")
//                        .append(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()))
//                        .append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')))");
//            }
            sql.append(" order by cytime desc,cyksbm");
            String finanSql = DataBaseUtil.buildPage(sql.toString(), startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<Bingan>() {
                        @Override
                        public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                            Bingan bingan = buildBingan(rs);
                            if (StringUtils.isNotEmpty(rs.getString("bnzt"))) {
                                bingan.setJieyue(true);
                            }
                            return bingan;
                        }
                    });
        }
        return bingans;
    }

    public List<Bingan> queryBorrowBinganState(String userid, String orgcode, String state,String requirecount, String startindex) {
        List<Bingan> bingans = null;
        if (StringUtils.isNotEmpty(orgcode) && StringUtils.isNotEmpty(userid)){
            StringBuilder sql = new StringBuilder("SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname,bnjb from t_ba_base " +
                    "left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ " +
                    "inner join ");
//            StringBuilder sql = new StringBuilder("SELECT * from t_ba_base where zzdm='");
            sql.append("(select zzdm as tempzzdm,JYIDENTITY,bnjb from t_bnjy where sqr='")
                    .append(userid).append("' and SQDANWEI ='")
                    .append(orgcode).append("' and BNZT ='")
                    .append(state).append("'");
            if(StringUtils.equals("2",state)){
                sql.append(" and jzrq>=to_date('")
                        .append(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()))
                        .append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')");
            }else{
                sql.append(" and sqrq>=to_date('")
                        .append(DateFormatUtils.ISO_DATE_FORMAT.format(DateUtils.addMonths(new Date(),-1)))
                        .append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')");
            }
            sql.append(") on ZZDM=tempzzdm and BAIDENTITY=JYIDENTITY");

            String finanSql = DataBaseUtil.buildPage(sql.toString(), startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<Bingan>() {
                        @Override
                        public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                            Bingan bingan = buildBingan(rs);
                            bingan.setBaomijibie(rs.getString("bnjb"));
                            return bingan;
                        }
                    });
        }
        return bingans;
    }

    public List<BinganQuality> queryBinganQuality(String userid, String orgcode,String requirecount, String startindex) {
        List<BinganQuality> bingans = null;
        if (StringUtils.isNotEmpty(orgcode) && StringUtils.isNotEmpty(userid)){
            String finanSql = DataBaseUtil.buildPage(
                    "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname,ZHIKONGPINGFEN from t_ba_base " +
                            "left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ "
                            + " where zkrq is not null "
                            + " and zhikongpingfen < 100"
                            + " and updatedate is  null"
                            + " and zzdm ='" + orgcode + "'"
                            + " and baidentity in (select distinct zrridentity from t_BNZRR where bazrrtype in ('91','92') and zrridcard like '%" + userid + "%')", startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<BinganQuality>() {
                        @Override
                        public BinganQuality mapRow(ResultSet rs, int i) throws SQLException {
                            Bingan temp = buildBingan(rs);
                            int score = rs.getInt("ZHIKONGPINGFEN");
                            return new BinganQuality(temp,score);
                        }
                    });
        }
        return bingans;
    }

    public List<Bingan> queryBinganQualityNeedRepair(String userid, String orgcode,String requirecount, String startindex) {
        List<Bingan> bingans = null;
        if (StringUtils.isNotEmpty(orgcode) && StringUtils.isNotEmpty(userid)){
            String finanSql = DataBaseUtil.buildPage(
                    "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname,ZHIKONGPINGFEN from t_ba_base " +
                            " left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ "
                            + " where zkrq is not null "
                            + " and zhikongpingfen < 100"
                            + " and updatedate is  null"
                            + " and zzdm ='" + orgcode + "'"
                            + " and baidentity in (select distinct zrridentity from t_BNZRR where bazrrtype in ('91','92') and zrridcard like '%" + userid + "%')", startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<Bingan>() {
                        @Override
                        public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                            return buildBingan(rs);
                        }
                    });
        }
        return bingans;
    }
    public List<ImmutableTriple<String,String,Integer>> queryBinganQualityDetail(String orgcode, String identity) {
        List<ImmutableTriple<String,String,Integer>> bingans = null;
        if (StringUtils.isNotEmpty(orgcode) && StringUtils.isNotEmpty(identity)){
            String finanSql =
                    "SELECT pjxm,jcnr,kf from t_bnzlkz "
                            + " where zlkzidentity='"+ identity+"'"
                            + " and zzjgdm ='" + orgcode + "'";
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<ImmutableTriple<String,String,Integer>>() {
                        @Override
                        public ImmutableTriple<String,String,Integer> mapRow(ResultSet rs, int i) throws SQLException {
                            String pingjiaxiangmu = rs.getString("pjxm");
                            Clob jcnr = rs.getClob("jcnr");
                            String neirong = jcnr!=null? jcnr.getSubString(1,(int)jcnr.length()):"";
                            int koufen = rs.getInt("kf");
                            return new ImmutableTriple<String,String,Integer>(pingjiaxiangmu,neirong,koufen);
                        }
                    });
        }
        return bingans;
    }


    public List<MutablePair<String,String>> queryBinganDetail(final String orgcode, String biaoshima,String baomijibie) {
        List<MutablePair<String,String>> binganDetails = null;
        if (StringUtils.isNotEmpty(orgcode)&& StringUtils.isNotEmpty(biaoshima)){
            String finanSql = "SELECT syspath,guidangpath,filename,name_ FROM t_saomiao a  " +
                    "inner join stdmrpagetype_ b " +
                    "on a.pagename=b.code_ " +
                    "where a.zzdm='"+orgcode+"' and a.biaoshima='"+biaoshima+"' ";
            if (baomijibie!=null){
                finanSql +=" and a.biaomilv <="+baomijibie;
            }
            finanSql += " order by pagename,jlh";
            System.out.println(finanSql);
            binganDetails = jdbcTemplate.query(finanSql,
                    new RowMapper<MutablePair<String,String>>() {
                        @Override
                        public MutablePair<String,String> mapRow(ResultSet rs, int i) throws SQLException {
                            MutablePair<String,String> pair = new MutablePair<String,String>();
//http://11.0.0.55/MrDoc/49557184-0/thumbnail_/41/80/1-10104180/1389573512753.png
                            String filename = StringUtils.replace(rs.getString("filename"),"tif","png") ;

                            StringBuilder s = new StringBuilder();
                            s.append(orgcode)
                            .append(StringUtils.contains(rs.getString("filename"),"tif")?"/thumbnail_":"/source")
                            .append(rs.getString("guidangpath"))
                            .append('/')
                            .append(filename);
//                            String url = String.format(
//                                    "http://11.0.0.55%s%s/thumbnail_%s/1389573512753.png",
//                                    rs.getString("syspath"),orgcode,rs.getString("guidangpath"));
                            pair.setLeft(s.toString());
                            pair.setRight(rs.getString("name_"));
                            return pair;
                        }
                    });
        }
        return binganDetails;
    }

    public List<MutablePair<String, String>> queryCommunityBinganDetail(final String orgcode, String binganid) {
        List<MutablePair<String,String>> binganDetails = null;
        List<String> pageCodes = jdbcTemplate.query(
                "select zhcotent from t_bndyzh where zzdm='"+orgcode+"' and zhname='门规'",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString("zhcotent");
                    }
                }
        );
        String pageCondition = null;
        if (pageCodes!=null && pageCodes.size()>0){
            pageCondition = " where code_ in ('"+StringUtils.replace(pageCodes.get(0),",","','")+"')";
        }

        String finanSql = "SELECT syspath,guidangpath,filename,name_ FROM t_saomiao a  " +
                "inner join (select code_,name_ from stdmrpagetype_ "+pageCondition+") " +
                "on a.pagename=code_ " +
                "where a.zzdm='"+orgcode+"' and a.biaoshima='"+binganid+"'  order by pagename,jlh";
        System.out.println(finanSql);
        binganDetails = jdbcTemplate.query(finanSql,
                new RowMapper<MutablePair<String,String>>() {
                    @Override
                    public MutablePair<String,String> mapRow(ResultSet rs, int i) throws SQLException {
                        MutablePair<String,String> pair = new MutablePair<String,String>();
//http://11.0.0.55/MrDoc/49557184-0/thumbnail_/41/80/1-10104180/1389573512753.png
                        String filename = StringUtils.replace(rs.getString("filename"),"tif","png") ;

                        StringBuilder s = new StringBuilder();
                        s.append(orgcode)
                                .append(StringUtils.contains(rs.getString("filename"), "tif") ? "/thumbnail_" : "/source")
                                .append(rs.getString("guidangpath"))
                                .append('/')
                                .append(filename);
//                            String url = String.format(
//                                    "http://11.0.0.55%s%s/thumbnail_%s/1389573512753.png",
//                                    rs.getString("syspath"),orgcode,rs.getString("guidangpath"));
                        pair.setLeft(s.toString());
                        pair.setRight(rs.getString("name_"));
                        return pair;
                    }
                });
        return binganDetails;
    }

    public int borrowBingan(final String zuzhidaima,
                            final String zuzhiname,
                            final String binganid,
                            final String shenqingren,
                            final String shenqingkeshicode,
                            final String shenqingkeshiname,
                            final String shenqingdanweicode,
                            final String shenqingdanweiname) {
        int result = 0;
        StringBuilder s = new StringBuilder();
        s.append("select id from t_bnjy where sqr ='")
                .append(shenqingren)
                .append("' and jyidentity ='")
                .append(binganid)
                .append("' and (bnzt='1' or jzrq>=to_date('")
                .append(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()))
                .append(" 00:00:00','YYYY-MM-DD hh24:mi:ss'))");
        System.out.println(s.toString());
        List<Integer> ids = jdbcTemplate.query(s.toString(),
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getInt("id");
                    }
                });
        if (ids!=null && ids.size()>0){
            return 1;
        }
//        Integer count = jdbcTemplate.queryForObject(s.toString(),Integer.class);
//        if (count>0){
//            return 2;
//        }
        final Integer maxID = jdbcTemplate.queryForObject("select MAX(id) from t_bnjy",Integer.class);

        result = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "insert into t_bnjy (id,ZZDM,ZZNAME,SQR,SQRQ,BNZT,JYIDENTITY,SQKS,SQKSNAME,SQDANWEI,SQDANWEINAME) values(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, maxID + 1);
                ps.setString(2, zuzhidaima);
                ps.setString(3, zuzhiname);
                ps.setString(4, shenqingren);
                ps.setDate(5, new java.sql.Date(new Date().getTime()));
                ps.setString(6, "1");
                ps.setString(7, binganid);
                ps.setString(8,shenqingkeshicode);
                ps.setString(9, shenqingkeshiname);
                ps.setString(10, shenqingdanweicode);
                ps.setString(11, shenqingdanweiname);
                return ps;
            }
        });
        return result;
    }

    public List<Bingan> queryCommunityBingan(String userid, String password,String orgcode, String requirecount, String startindex) {
        List<Bingan> bingans = null;
        if (StringUtils.isNotEmpty(orgcode)){
            String sql = "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname\n" +
                    "from t_ba_base \n" +
                    "left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ " +
                    "where zzdm='"+orgcode+"' and BAIDENTITY in("+
                        "select biaoshima from medreccopyings_ " +
                        "where username = '"+userid+"' and userpassword='"+password+"' and yljgbm='"+orgcode+"' " +
                        "and regtime_=( " +
                            "select max(regtime_) from medreccopyings_ " +
                            "where username = '"+userid+"' and userpassword='"+password+"' and yljgbm='"+orgcode+"')"
                    +")"
//                    +" and (bnbiaoshi1 is null or bnbiaoshi1='00')"
                    +" order by cytime desc,cyksbm"
                    ;
            String finanSql = DataBaseUtil.buildPage(sql, startindex, requirecount);
            System.out.println(finanSql);
            bingans = jdbcTemplate.query(finanSql,
                    new RowMapper<Bingan>() {
                        @Override
                        public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                            return buildBingan(rs);
                        }
                    });
        }
        return bingans;
    }

    public List<Bingan> queryCommunityBingan(String userid, String password,String requirecount, String startindex) {
        List<Bingan> bingans = null;
        String sql = "SELECT BAIDENTITY,ZZDM,ZZNAME,FZJGBSF,BANUM,NAME,CYTIME,stdhospitaloffice_.name_ as cyksname\n" +
                "from t_ba_base \n" +
                "left join stdhospitaloffice_ on t_ba_base.zzdm = stdhospitaloffice_.orgcode_ and t_ba_base.cyksbm = stdhospitaloffice_.code_ " +
                "where BAIDENTITY in("+
                "select biaoshima from medreccopyings_ " +
                "where username = '"+userid+"' and userpassword='"+password+"' and  regtime_=( " +
                "select max(regtime_) from medreccopyings_ " +
                "where username = '"+userid+"' and userpassword='"+password+"' )"
                +")"
//                    +" and (bnbiaoshi1 is null or bnbiaoshi1='00')"
                +" order by cytime desc,cyksbm"
                ;
        String finanSql = DataBaseUtil.buildPage(sql, startindex, requirecount);
        System.out.println(finanSql);
        bingans = jdbcTemplate.query(finanSql,
                new RowMapper<Bingan>() {
                    @Override
                    public Bingan mapRow(ResultSet rs, int i) throws SQLException {
                        return buildBingan(rs);
                    }
                });
        return bingans;
    }
}
