package jalikejdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    @Before
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

    @Test
    public void testForeach() {
        db.readOnly(dbs ->
                dbs.foreach("select * from tag",null,rs -> {
                    System.out.println(new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time")).toString());
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
