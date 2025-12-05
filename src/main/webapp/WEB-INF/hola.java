import java.sql.*;

public class hola {
    private static final String PASSWORD = "admin123"; // Hardcoded secret

    public boolean login(String user, String pass) {
        return pass.equals(PASSWORD);
    }

    public void getUser(String username) throws Exception 
    {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "root");
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM users WHERE username = '" + username + "'"); // Injection
    }

    
}
public class MathService {
    public int dividir(int a, int b) {
        return a / b; // Zero division risk
    }
}

public class UserRepository {
    public void getUser(String username) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "root");
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM users WHERE username = '" + username + "'"); // Injection
    }
}
