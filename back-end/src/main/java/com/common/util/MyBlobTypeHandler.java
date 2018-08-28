package com.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * Created by lego-jspx01 on 2016/6/13.
 */
public class MyBlobTypeHandler extends BaseTypeHandler<String> {
    private static final Log LOGGER = LogFactory.getLog(MyBlobTypeHandler.class);
    // 指定字符集
    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    String parameter, JdbcType jdbcType) throws SQLException
    {
        ByteArrayInputStream bis;
        try
        {
            // 把String转化成byte流
            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Blob Encoding Error!");
        }
        ps.setBinaryStream(i, bis, parameter.length());
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException
    {
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        String result = null;
        if (null != blob)
        {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try
        {
            if (null != returnValue)
            {
                // 把byte转化成string
                result = new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Blob Encoding Error!");
        }
        return result;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException
    {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        String result = null;
        if (null != blob)
        {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try
        {
            if (null != returnValue)
            {
                result = new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Blob Encoding Error!");
        }
        return result;
    }

    /**
     * @Description:
     * @throws SQLException
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(ResultSet,
     *      int)
     *
     */

    @Override
    public String getNullableResult(ResultSet rs, int columnName)
            throws SQLException
    {
        LOGGER.debug("enter function");
        String result = null;
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob)
        {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try
        {
            // 把byte转化成string
            if (null != returnValue)
            {
                result = new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Blob Encoding Error!");
        }
        LOGGER.debug("exit function");
        return result;

    }
}
