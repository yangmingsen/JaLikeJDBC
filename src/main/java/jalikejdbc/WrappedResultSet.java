package jalikejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * 对java.sql.Result进行包装
 */
public class WrappedResultSet {
    private ResultSet underlying;

    public WrappedResultSet(ResultSet underlying) {
        this.underlying = underlying;
    }

    public Long getLong(String columnLabel) {
        try {
            return underlying.getLong(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public Long getLong(int columnIndex) {
        try {
            return underlying.getLong(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public String getString(String columnLabel) {
        try {
            return underlying.getString(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getString(int columnIndex) {
        try {
            return underlying.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Integer getInt(String columnLabel) {
        try {
            return underlying.getInt(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer getInt(int columnIndex) {
        try {
            return underlying.getInt(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public byte getByte(String columnLabel) {
        try {
            return underlying.getByte(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public byte getByte(int columnIndex) {
        try {
            return underlying.getByte(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public double getDouble(String columnLabel) {
        try {
            return underlying.getDouble(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0d;
    }

    public double getDouble(int columnIndex) {
        try {
            return underlying.getDouble(columnIndex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0d;
    }


    public java.math.BigDecimal getBigDecimal(int columnIndex) {
        try {
            return underlying.getBigDecimal(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.math.BigDecimal getBigDecimal(String columnLabel) {
        try {
            return underlying.getBigDecimal(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
