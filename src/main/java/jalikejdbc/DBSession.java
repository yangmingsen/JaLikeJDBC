package jalikejdbc;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DBSession {
    private Connection conn;

    public DBSession(Connection conn) {
        this.conn = conn;
        init(conn,Optional.ofNullable(null));
    }

    private void init(Connection conn, Optional<Tx> optionalTx) {
        boolean present = optionalTx.isPresent();

        Tx tx = null;
        if (present == true) {
            tx = optionalTx.get();
        }

        if (present && tx.isActive()) {

        } else if (present == false) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException(ErrorMessage.TRANSACTION_IS_NOT_ACTIVE);
        }
    }

    public DBSession(Connection conn, Optional<Tx> optionalTx) {
        this.conn = conn;
        init(conn,optionalTx);
    }

    public PreparedStatement createPreparedStatement(Connection conn, String template) {
        try {
            return conn.prepareStatement(template, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void bindParams(WrappedPreparedStatement wpstmt, Object[] params) {
        if (params == null || params.length == 0) return;

        for (int i = 0; i < params.length; i++) {
            Object match = params[i];
            int idx = i + 1;

            if (match instanceof Integer) {
                wpstmt.setInt(idx, (int) match);

            } else if (match instanceof String) {
                wpstmt.setString(idx, match.toString());

            } else if (match instanceof Double) {
                wpstmt.setDouble(idx, (double) match);

            } else if (match instanceof Long) {
                wpstmt.setLong(idx, (long) match);

            } else if (match instanceof Byte) {
                wpstmt.setByte(idx,(Byte) match);

            } else if (match instanceof Date) {
                wpstmt.setDate(idx, (Date) match);

            } else if (match instanceof Boolean) {
                wpstmt.setBoolean(idx, (boolean) match);

            } else if (match instanceof java.math.BigDecimal) {
                wpstmt.setBigDecimal(idx, (java.math.BigDecimal)match);

            } else if (match instanceof Array) {
                wpstmt.setArray(idx,(Array) match);

            } else  {
                wpstmt.setObject(idx,match);

            }
        }
    }

    static class ResultSetIterator implements Iterator<ResultSet> {

        private ResultSet rs;

        public ResultSetIterator(ResultSet rs) {
            this.rs = rs;
        }

        @Override
        public boolean hasNext() {
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public ResultSet next() {
            return rs;
        }

        public <R> List<R> map(Function<WrappedResultSet, R> f) {
            List<R> result = new ArrayList<>();
            while (this.hasNext()) {
                WrappedResultSet rs2 = new WrappedResultSet(this.rs);
                result.add(f.apply(rs2));
            }
            return result;
        }


        public void foreach(Consumer<WrappedResultSet> c) {
            while (this.hasNext()) {
                WrappedResultSet rs2 = new WrappedResultSet(this.rs);
                c.accept(rs2);
            }
        }

        public <R> Iterator<R> iterator(Function<WrappedResultSet, R> f) {
            return map(f).iterator();
        }

    }


    public <R> List<R> asList(String template, Object[] params, Function<WrappedResultSet, R> f) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            return new ResultSetIterator(wps.executeQuery()).map(rs -> f.apply(rs));
        });
    }

    public <R> Optional<R> asOne(String template, Object[] params, Function<WrappedResultSet, R> f) {
        return asList(template, params, f).stream().findFirst();
    }


    public <R> R foreach(String template, Object[] params, Consumer<WrappedResultSet> c) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            new ResultSetIterator(wps.executeQuery()).foreach(rs -> c.accept(rs));
            return null;
        });
    }

    public <R> Iterator<R> asIterator(String template, Object[] params, Function<WrappedResultSet, R> f) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);

            return new ResultSetIterator(wps.executeQuery()).iterator(rs -> f.apply(rs));
        });
    }

    public <R> Integer update(String template, Object[] params) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            return wps.executeUpdate();
        });
    }


}
