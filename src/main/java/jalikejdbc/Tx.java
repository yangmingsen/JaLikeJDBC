package jalikejdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DB Transaction
 */
public class Tx {
    private Connection conn;

    public Tx(Connection conn) {
        this.conn = conn;
    }

    public boolean isActive() {
        boolean ok = false; //默认
        try {
            ok = (!conn.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    //回滚
    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //提交
    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //开始
    public void begin() {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
