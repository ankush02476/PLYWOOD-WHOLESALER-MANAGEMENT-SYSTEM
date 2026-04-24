import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    public static Connection getConnection() {

        Connection con = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@ANKUSH:1521/orcl",  
                "JMJP",  
                "JMJP"   
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}
