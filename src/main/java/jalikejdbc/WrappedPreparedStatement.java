package jalikejdbc;

import java.sql.*;

public class WrappedPreparedStatement {
    private PreparedStatement underlying;

    public WrappedPreparedStatement(PreparedStatement underlying) {
        this.underlying = underlying;
    }

    public int executeUpdate() {
        int rs = -1;
        try {
            rs = underlying.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet executeQuery() {
        ResultSet rs = null;
        try {
            rs = underlying.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void setInt(int parameterIndex, int x) {
        try {
            underlying.setInt(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLong(int parameterIndex, long x) {
        try {
            underlying.setLong(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDouble(int parameterIndex, double x) {
        try {
            underlying.setDouble(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setByte(int parameterIndex, Byte x) {
        try {
            underlying.setByte(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setString(int parameterIndex, String x) {
        try {
            underlying.setString(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDate(int parameterIndex, Date x) {
        try {
            underlying.setDate(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBoolean(int parameterIndex, Boolean x) {
        try {
            underlying.setBoolean(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) {
        try {
            underlying.setBigDecimal(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setArray(int parameterIndex, Array x) {
        try {
            underlying.setArray(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setObject(int parameterIndex, Object x) {
        try {
            underlying.setObject(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setFloat(int parameterIndex, Float x) {
        try {
            underlying.setFloat(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setShort(int parameterIndex, Short x) {
        try {
            underlying.setShort(parameterIndex, x);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
