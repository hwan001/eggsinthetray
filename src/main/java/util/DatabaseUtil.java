package util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    
    private static DataSource dataSource;
    
    static {
        initializeDataSource();
    }
    
    private static void initializeDataSource() {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/oracle");
            System.out.println("dataSource: " + dataSource);
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
}
