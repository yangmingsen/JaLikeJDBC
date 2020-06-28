package jalikejdbc;

import java.sql.Date;
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

    public double getFloat(String columnLabel) {
        try {
            return underlying.getFloat(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public double getFloat(int columnIndex) {
        try {
            return underlying.getFloat(columnIndex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public Date getDate(String columnLabel) {
        try {
            return underlying.getDate(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate(int columnIndex) {
        try {
            return underlying.getDate(columnIndex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Short getShort(String columnLabel) {
        try {
            return underlying.getShort(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Short getShort(int columnIndex) {
        try {
            return underlying.getShort(columnIndex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBoolean(String columnLabel) {
        try {
            return underlying.getBoolean(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getBoolean(int columnIndex) {
        try {
            return underlying.getBoolean(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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


    public byte[] getBytes(String columnLabel) {
        try {
            return underlying.getBytes(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getBytes(int columnIndex) {
        try {
            return underlying.getBytes(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public java.sql.Time getTime(String columnLabel) {
        try {
            return underlying.getTime(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.sql.Time getTime(int columnIndex) {
        try {
            return underlying.getTime(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public java.sql.Timestamp getTimestamp(String columnLabel) {
        try {
            return underlying.getTimestamp(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.sql.Timestamp getTimestamp(int columnIndex) {
        try {
            return underlying.getTimestamp(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(String columnLabel) {
        try {
            return underlying.getObject(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(int columnIndex) {
        try {
            return underlying.getObject(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public java.io.InputStream getAsciiStream(String columnLabel) {
        try {
            return underlying.getAsciiStream(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.io.InputStream getAsciiStream(int columnIndex) {
        try {
            return underlying.getAsciiStream(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public java.io.InputStream getUnicodeStream(String columnLabel) {
        try {
            return underlying.getUnicodeStream(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.io.InputStream getUnicodeStream(int columnIndex) {
        try {
            return underlying.getUnicodeStream(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public java.io.InputStream getBinaryStream(String columnLabel) {
        try {
            return underlying.getBinaryStream(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.io.InputStream getBinaryStream(int columnIndex) {
        try {
            return underlying.getBinaryStream(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
