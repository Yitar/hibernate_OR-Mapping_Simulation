import java.lang.reflect.Method;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yitar on 2015/11/10.
 */
public class Session {

    String tableName = "_Student";
    Map<String, String> cfs = new HashMap<String, String>();

    String[] methodNames;

    public Session() {
        cfs.put("_id", "id");
        cfs.put("_name", "name");
        cfs.put("_age", "age");
        methodNames = new String[cfs.size()];
    }

    public void save(Student s) throws Exception {
        String sql = createSQL();

        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/student", "root", "root");
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < methodNames.length; i++) {
            Method m = s.getClass().getMethod(methodNames[i]);
            Class r = m.getReturnType();

            if (r.getName().equals("java.lang.String")) {
                String returnValue = (String) m.invoke(s);
                ps.setString(i + 1, returnValue);
            }
            if (r.getName().equals("int")) {
                Integer returnValue = (Integer) m.invoke(s);
                ps.setInt(i + 1, returnValue);
            }
        }
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    private String createSQL() {
        String Str1 = "";
        int index = 0;
        for (String s : cfs.keySet()) {
            String v = cfs.get(s);
            v = Character.toUpperCase(v.charAt(0)) + v.substring(1);
            methodNames[index] = "get" + v;
            Str1 += s + ",";
            index++;
        }
        Str1 = Str1.substring(0, Str1.length() - 1);

        String Str2 = "";
        for (int i = 0; i < cfs.size(); i++) {
            Str2 += "?,";
        }
        Str2 = Str2.substring(0, Str2.length() - 1);

        String sql = "insert into " + tableName + "(" + Str1 + ")" + " values (" + Str2 + ")";
        return sql;
    }
}
