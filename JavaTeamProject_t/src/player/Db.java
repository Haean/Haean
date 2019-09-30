package player;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Db {
   static int length = 0;
   static int pre; //
   static String name;
   static String driver = "com.mysql.cj.jdbc.Driver";
   static String url = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=UTC";
   static String user = "root";
   static String pw = "1111";
   static Connection con = null;
   static PreparedStatement pstmt = null;
   static ResultSet result = null;
   static String path = null;
   static Statement stmt = null;
   static ArrayList<String> list = new ArrayList<String>();

   //���ϰ�θ� �Ѱܹ޾� �����ͺ��̽��� �߰�
   public static void insert(String path) {
      try {
         Class.forName(driver);
         con = DriverManager.getConnection(url, user, pw);
         System.out.println("DB����");
         stmt = con.createStatement();
         path = path.replace("\\" , ",");
         System.out.println(path);
         String sql2 = "INSERT INTO test.test VALUES(NULL,'" + path + "')";
          try{
                stmt.executeUpdate(sql2);
               
          }catch(Exception e) {}
      } catch (Exception e) {
         System.out.println("����");
         e.printStackTrace();
      } finally {
         try {
            if (con != null)
               con.close();
            if (stmt != null)
               stmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("����");
         }
      } 
      while (path == null);
      
      System.out.println("����");
      return;
   }
   //�ε������� �̿��� �����ͺ��̽��� �ִ� ��� �ҷ�����
   public static String load(int add) {
      try {
         Class.forName(driver);
         con = DriverManager.getConnection(url, user, pw);
         System.out.println("DB����");
         String sql = "SELECT * FROM test.test";

         pstmt = con.prepareStatement(sql);
         result = pstmt.executeQuery();

         while (result.next()) {
            path = result.getString("path");
            path = path.replace(",", "\\");
            list.add(path);
         }
         length = list.size();
         if (add >= length) {
            add = 0;
         } else if (add < 0) {
            add = length - 1;
         }
         pre = add;
         name = list.get(add);
         System.out.println(list);
         return list.get(add);
      } catch (Exception e) {
         System.out.println("����");
         e.printStackTrace();
      } finally {
         try {
            if (con != null)
               con.close();
            if (result != null)
               result.close();
            if (pstmt!=null)
            	pstmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("����");
         }
      }
      while (path != null);
      System.out.println("����");
      return null;
   }
   //JFrame
   public static void list(ArrayList<String> music) {
	   try {
		music.clear();
		Class.forName(driver);
		con = DriverManager.getConnection(url, user, pw);
		int a = 0;
		
		String sql = "SELECT * FROM test.test";
		
		pstmt = con.prepareStatement(sql);
        result = pstmt.executeQuery();

        while (result.next()) {
           path = result.getString("path");
           path = path.replace(",", "\\");
           music.add(a, path);
           a++;
        }
		
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
         try {
            if (con != null)
               con.close();
            if (result != null)
               result.close();
            if (pstmt!=null)
            	pstmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("����");
         }
      }

   }

//   public int Random() {
//      Random random = new Random();
//      int[] check = new int[length];
//      int adder = 0;
//
//      for (int r = 0; r < length; r++) {
//         check[r] = random.nextInt(length);
//
//         for (int i = 0; i < r; i++) {
//            if (check[i] == check[r]) {
//               check[r] = random.nextInt(length);
//            }
//         }
//         adder = check[length - 1];
//      }
//      return adder;
//   }
   //���� index ���� ��θ� ��ȯ�Լ�
   public int Pre() {
      return pre;
   }

   public String Name() {
      return name;
   }
}

