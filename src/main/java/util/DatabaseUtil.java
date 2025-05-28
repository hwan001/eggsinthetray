package util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    
    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
    
    @FunctionalInterface
    public interface PreparedStatementSetter {
        void setParameters(PreparedStatement pstmt) throws SQLException;
    }
    
    private static DataSource dataSource;
    
    static {
        initializeDataSource();
    }
    
    private static void initializeDataSource() {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/oracle");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("DataSource 초기화 실패", e);
        }
    }
    
    public static DataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }
    
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
    
    // 업데이트
    public static void executeUpdate(String sql, SQLFunction<PreparedStatement, Void> operation) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            operation.apply(pstmt);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 조회용 - PreparedStatement 설정과 ResultSet 처리를 한번에
    public static <T> T executeQuery(String sql, PreparedStatementSetter setter, SQLFunction<ResultSet, T> processor) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            setter.setParameters(pstmt);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return processor.apply(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 파라미터가 없는 조회용
    public static <T> T executeQuery(String sql, SQLFunction<ResultSet, T> processor) {
        return executeQuery(sql, pstmt -> {}, processor);
    }
} 
