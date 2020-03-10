package jalikejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class DB {
    private Connection conn; //数据库连接

    public DB(Connection conn) {
        this.conn = conn;
    }

    /***
     *instance metho
     */


    public boolean isTxNotActive() {
        boolean ok = false; //默认
        try {
            ok = ( conn == null || conn.isClosed() || conn.isReadOnly());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public boolean isTxNotYetStarted() {
        boolean ok = false; //默认
        try {
            ok = ( conn != null && conn.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public boolean isTxAlreadyStarted() {
        boolean ok = false; //默认
        try {
            ok = ( (conn != null) && (!conn.getAutoCommit()) );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public Tx newTx(Connection conn) {
        if (isTxNotActive() || isTxAlreadyStarted()) {
            throw new IllegalStateException(ErrorMessage.CANNOT_START_A_NEW_TRANSACTION);
        }

        return new Tx(conn);
    }

    public Tx newTx() {
        return newTx(this.conn);
    }

    public Tx currentTx() {
        if (isTxNotActive() || isTxNotYetStarted()) {
            throw new IllegalStateException(ErrorMessage.TRANSACTION_IS_NOT_ACTIVE);
        }
        return new Tx(this.conn);
    }

    public Tx tx() {
        return currentTx();
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void begin() {
        newTx().begin();
    }

    public void commit() {
        tx().commit();
    }

    public void rollback() {
        tx().rollback();
    }

    public DBSession withinTxSession(Tx tx) {
        if (tx == null) {
            tx = currentTx();
        }

        if (!tx.isActive()) {
            throw new IllegalStateException(ErrorMessage.TRANSACTION_IS_NOT_ACTIVE);
        }

        return new DBSession(this.conn, Optional.ofNullable(tx));
    }

    public <R> R withinTx(Function<DBSession, R> f) {
        return f.apply(withinTxSession(currentTx()));
    }

    public <R> R localTx(Function<DBSession, R> f) {
        Tx tx = newTx();
        tx.begin();
        try {
            if (!tx.isActive()) {
                throw new IllegalStateException(ErrorMessage.TRANSACTION_IS_NOT_ACTIVE);
            }

            DBSession dbSession = new DBSession(conn, Optional.ofNullable(tx));
            R res = f.apply(dbSession);
            tx.commit();
            return res;
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            return null;
        }
    }


    public DBSession readOnlySession() {
        try {
            conn.setReadOnly(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DBSession(conn);
    }

    public <R> R readOnly(Function<DBSession,R> f) {
       return f.apply(readOnlySession());
    }

    public DBSession autoCommitSession() {
        try {
            conn.setReadOnly(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DBSession(conn);
    }

    public <R> R autoCommit(Function<DBSession,R> f) {
        return f.apply(autoCommitSession());
    }

}
