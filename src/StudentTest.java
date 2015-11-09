/**
 * Created by Yitar on 2015/11/10.
 */
public class StudentTest {
    public static void main(String[] args) throws Exception {
        Student s = new Student();
        s.setId(1);
        s.setName("s1");
        s.setAge(1);

        Session session = new Session();
        session.save(s);
    }
}
