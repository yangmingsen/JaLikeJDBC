package jalikejdbc;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class TestOne {

    Connection conn = null;
    DB db = null;

    static class Tag{
        private long id;
        private String tagName;
        private String createdTime;

        public Tag(long id, String tagName, String createdTime) {
            this.id = id;
            this.tagName = tagName;
            this.createdTime = createdTime;
        }

        @Override
        public String toString() {
            return "Tag{" +
                    "id=" + id +
                    ", tagName='" + tagName + '\'' +
                    ", createdTime='" + createdTime + '\'' +
                    '}';
        }
    }


    public void testBefore() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_blog?characterEncoding=utf8&useSSL=false", "yms", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db = new DB(conn);
    }

    public void testBefore2() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hello?characterEncoding=utf8&useSSL=false", "yms", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db = new DB(conn);
    }


    @Before
    public void testBefore3() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jbxt?characterEncoding=utf8&useSSL=false", "root", "ymsyms");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db = new DB(conn);
    }

    static class TUser {
        private Integer id;
        private String username;
        private String password;

        @Override
        public String toString() {
            return "TUser{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    static class Bid {
        /*唯一编码*/
        private String code;
        /*竞品id*/
        private Integer goodsId;
        /*用户code*/
        private String userCode;
        /*竞标活动code*/
        private String activityCode;
        /*用户出价(保留2位小数)*/
        private BigDecimal bid;
        /*创建时间*/
        private Date createdTime;
        /*更新时间如果没有默认创建时间，修改数据必须更新*/
        private Date updatedTime;
        /*更新人*/
        private String updator;
        /*创建人*/
        private String creator;
        /*公司唯一编码*/
        private String companyCode;
        /*系统唯一编码*/
        private String sysCode;

        @Override
        public String toString() {
            return "Bid{" +
                    "code='" + code + '\'' +
                    ", goodsId=" + goodsId +
                    ", userCode='" + userCode + '\'' +
                    ", activityCode='" + activityCode + '\'' +
                    ", bid=" + bid +
                    ", createdTime=" + createdTime +
                    ", updatedTime=" + updatedTime +
                    ", updator='" + updator + '\'' +
                    ", creator='" + creator + '\'' +
                    ", companyCode='" + companyCode + '\'' +
                    ", sysCode='" + sysCode + '\'' +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public BigDecimal getBid() {
            return bid;
        }

        public void setBid(BigDecimal bid) {
            this.bid = bid;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public Date getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(Date updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String getUpdator() {
            return updator;
        }

        public void setUpdator(String updator) {
            this.updator = updator;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }
    }


    @Test
    public void testGetObj2() {
        db.readOnly(session -> session.asList("select * from t_jbxt_bidding",null,Bid.class)).forEach(x -> System.out.println(x.toString()));
    }

    @Test
    public void testGetObj() {

        List<TUser> tUsers = db.readOnly(session ->
                session.asList("select * from t_user", null, TUser.class));
        tUsers.forEach(x -> System.out.println(x.toString()));

    }



    @Test
    public void testForeach() {
        db.readOnly(dbs ->
                dbs.foreach("select * from tag",null,rs -> {
                    System.out.println(new Tag(rs.getLong("id"),rs.getString("tag_name"), rs.getString("created_time")).toString());
        }));
    }


    @Test
    public void testFindOne() {
        Object [] params = {new Long("1561914017524099")};
        Optional<Tag> tag1 = db.readOnly(session -> session.asOne("select * from tag where id=?", params, rs -> new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));
        if (tag1.isPresent()) {
            System.out.println(tag1.get().toString()); //输出tag实体信息
        }

    }

    @Test
    public void testFindAll() {
        //查找所有
        List<Tag> tags = db.readOnly(session ->
                session.asList("select * from tag",null, rs ->
                    new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));
        tags.stream().forEach(x -> System.out.println(x.toString()));//s输出
    }

    @Test
    public void testIterator() {
        Iterator<Tag> iter = db.readOnly(session ->
                session.asIterator("select * from tag", null, rs ->
                        new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));

        while (iter.hasNext()) { //hashNext
            System.out.println(iter.next().toString());
        }

    }

    //test Tx
    @Test
    public void testWithTx() {
        db.begin();
        Object[] params = {new Long("1561910658515066")};
        Object[] params2 = {"Scala",1561910658515066L};
        db.withinTx(session -> {
            Optional<Tag> tag1 = session.asOne("select * from tag where id=?", params, rs ->
                    new Tag(rs.getLong(1), rs.getString(2), rs.getString(3)));

            if (tag1.isPresent()) {
                System.out.println(tag1.get().toString());
            }


            session.update("update tag set tag_name=? where id=?",params2);

            Optional<Tag> tag2 = session.asOne("select * from tag where id=?", params, rs ->
                    new Tag(rs.getLong(1), rs.getString(2), rs.getString(3)));

            if (tag1.isPresent()) {
                System.out.println(tag2.get().toString());
            }

            return null;
            }
        );

        db.commit();

    }

    @Test
    public void testLocalTx() {

        Object[] params = {new Long("1561910658515066")};
        Object[] params2 = {"Scala",1561910658515066L};
        db.localTx(session -> {
            Optional<Tag> tag1 = session.asOne("select * from tag where id=?", params, rs ->
                    new Tag(rs.getLong(1), rs.getString(2), rs.getString(3)));

            if (tag1.isPresent()) {
                System.out.println(tag1.get().toString());
            }

            session.update("update tag set tag_name=? where id=?",params2);

            Optional<Tag> tag2 = session.asOne("select * from tag where id=?", params, rs ->
                    new Tag(rs.getLong(1), rs.getString(2), rs.getString(3)));

            if (tag1.isPresent()) {
                System.out.println(tag2.get().toString());
            }

            return null;
        });

    }

}
