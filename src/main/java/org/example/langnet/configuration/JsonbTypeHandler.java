//package org.example.langnet.configuration;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class JsonbTypeHandler<T> extends BaseTypeHandler<T> {
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//    private Class<T> type;
//
//    public JsonbTypeHandler(Class<T> type) {
//        this.type = type;
//    }
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
//        try {
//            ps.setString(i, mapper.writeValueAsString(parameter));
//        } catch (JsonProcessingException e) {
//            throw new SQLException("Error converting parameter to JSON", e);
//        }
//    }
//
//    @Override
//    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        String json = rs.getString(columnName);
//        return parseJson(json);
//    }
//
//    @Override
//    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        String json = rs.getString(columnIndex);
//        return parseJson(json);
//    }
//
//    @Override
//    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        String json = cs.getString(columnIndex);
//        return parseJson(json);
//    }
//
//    private T parseJson(String json) throws SQLException {
//        try {
//            return json == null ? null : mapper.readValue(json, type);
//        } catch (Exception e) {
//            throw new SQLException("Error parsing JSON", e);
//        }
//    }
//}
