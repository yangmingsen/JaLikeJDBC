package jalikejdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                wpstmt.setByte(idx, (byte) match);
            } else if (match instanceof Short) {
                wpstmt.setShort(idx, (Short)match);
            } else if (match instanceof Float) {
                wpstmt.setFloat(idx, (Float) match);
            }  else if (match instanceof Date) {
                wpstmt.setDate(idx, (Date) match);
            } else if (match instanceof java.sql.Date) {
                wpstmt.setDate(idx, (java.sql.Date) match);
            } else if (match instanceof Boolean) {
                wpstmt.setBoolean(idx, (boolean) match);
            } else if (match instanceof java.math.BigDecimal) {
                wpstmt.setBigDecimal(idx, (java.math.BigDecimal)match);
            } else if (match instanceof Array) {
                wpstmt.setArray(idx,(Array) match);
            }  if (match == null) {
                wpstmt.setArray(idx,null);
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


    /***
     * <p>描述：标准java属性名转db数据库表设计名</p>
     * <p>例如: myCat => my_cat</p>
     * @param name java标准属性名
     * @return
     * @since 1.1
     */
    private static String standardJavaNameToDbFieldsName(String name) {
        StringBuilder tstr = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                tstr.append("_");
                tstr.append(Character.toLowerCase(name.charAt(i)));
            } else {
                tstr.append(name.charAt(i));
            }
        }
        return tstr.toString();
    }



    /***
     * <p>描述: 用于解析java对象到db表的映射关系</p>
     * <p>注意：数据库的字段名如果由多个名词组成,一定要由下划线分隔</p>
     * @param rs
     * @param clazz
     * @param <R>
     * @since   1.1
     * @return
     */
    private <R> R parseObj(WrappedResultSet rs, Class<R> clazz ) {
        try {
            R obj = clazz.newInstance();
            //获取所有属性
            Field[] fields = clazz.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                //属性名 (驼峰命名)
                String attrName = fields[j].getName();
                //属性的类型
                String typeName = fields[j].getType().getSimpleName();

                String setMethodName = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
                Method method = obj.getClass().getMethod(setMethodName, fields[j].getType());

                if ("Integer".equals(typeName) || "int".equals(typeName)) {
                    method.invoke(obj, rs.getInt(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Long".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getLong(standardJavaNameToDbFieldsName(attrName)));
                }  else if ("Date".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getDate(standardJavaNameToDbFieldsName(attrName)));
                } else if ("String".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getString(standardJavaNameToDbFieldsName(attrName)));
                } else if ("BigDecimal".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getBigDecimal(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Double".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getDouble(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Boolean".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getBoolean(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Short".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getShort(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Byte".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getByte(standardJavaNameToDbFieldsName(attrName)));
                } else if ("Float".equalsIgnoreCase(typeName)) {
                    method.invoke(obj, rs.getFloat(standardJavaNameToDbFieldsName(attrName)));
                } else {
                    method.invoke(obj, rs.getObject(standardJavaNameToDbFieldsName(attrName)));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     * @param template
     * @param params
     * @param f
     * @param <R>
     * @return
     * @since   1.0
     */
    public <R> List<R> asList(String template, Object[] params, Function<WrappedResultSet, R> f) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            return new ResultSetIterator(wps.executeQuery()).map(rs -> f.apply(rs));
        });
    }

    /***
     *
     * @param sql
     * @param params
     * @param clazz
     * @param <R>
     * @return
     * @since   1.1
     */
    public <R> List<R> asList(String sql, Object[] params, Class<R> clazz ) {
        return asList(sql, params, rs -> parseObj(rs, clazz));
    }


    /***
     * @version: 1.0
     * @param template
     * @param params
     * @param f
     * @param <R>
     * @return
     * @since   1.0
     */
    public <R> Optional<R> asOne(String template, Object[] params, Function<WrappedResultSet, R> f) {
        return asList(template, params, f).stream().findFirst();
    }

    /***
     *
     * @param sql
     * @param params
     * @param clazz
     * @param <R>
     * @return
     * @since   1.1
     */
    public <R> Optional<R> asOne(String sql, Object[] params, Class<R> clazz) {
        return asList(sql,params,clazz).stream().findFirst();
    }


    /***
     * @version: 1.0
     * @param template
     * @param params
     * @param c
     * @param <R>
     * @return
     * @since   1.0
     */
    public <R> R foreach(String template, Object[] params, Consumer<WrappedResultSet> c) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            new ResultSetIterator(wps.executeQuery()).foreach(rs -> c.accept(rs));
            return null;
        });
    }

    /***
     * @version: 1.0
     * @param template
     * @param params
     * @param f
     * @param <R>
     * @return
     * @since   1.0
     */
    public <R> Iterator<R> asIterator(String template, Object[] params, Function<WrappedResultSet, R> f) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);

            return new ResultSetIterator(wps.executeQuery()).iterator(rs -> f.apply(rs));
        });
    }

    /***
     *
     * @param sql
     * @param params
     * @param clazz
     * @param <R>
     * @return
     * @since   1.1
     */
    public <R> Iterator<R> asIterator(String sql, Object[] params, Class<R> clazz) {
        return asIterator(sql, params, rs -> parseObj(rs,clazz));
    }


    /***
     *
     * @param template
     * @param params
     * @param <R>
     * @return
     * @since   1.0
     */
    public <R> Integer update(String template, Object[] params) {
        PreparedStatement pstmt = createPreparedStatement(conn, template);
        return LoanPattern.using(pstmt, ps -> {
            WrappedPreparedStatement wps = new WrappedPreparedStatement(ps);
            bindParams(wps, params);
            return wps.executeUpdate();
        });
    }


}
