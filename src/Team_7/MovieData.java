package Team_7;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class MovieModel {
   String mvName;
   String year;
   String dirName;
   String type;
   String age;
   String grade;

   MovieModel(String mvName, String year, String dirName, String type, String age, String grade) {
      this.mvName = mvName;
      this.year = year;
      this.dirName = dirName;
      this.type = type;
      this.age = age;
      this.grade = grade;
   }
}

class MovieView extends JFrame {
   Container c=getContentPane();
   JPanel[] panel=new JPanel[5];
   //0번 panel
   JLabel[] label=new JLabel[6];
   JTextField[] tf=new JTextField[6];
   //1번 panel
   JButton[] button=new JButton[8];
   //2번 panel
   String[] lName= {"● 영화 제목", "● 개봉 연도", "● 감독명", "● 장르", "● 관람가능나이", "● 평 점"};
   String[] lName2= {"영화 제목 ▼", "개봉 연도 ▼", "감독명 ▼", "장르 ▼", "관람가능나이 ▼", "평 점 ▼"};
   JTable table;
   DefaultTableModel model;
   //3번 panel
   JLabel label3;
   JButton btn3;
   //4번 panel
   JTextArea ta;

   public MovieView() {
      setTitle("Movie Data");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      newComponents();
      setComponents();
      addComponents();

      setSize(900, 620);
      setVisible(true);
   }

   void newComponents() {
      model=new DefaultTableModel(lName2, 0);
      table=new JTable(model);

      String[] bName={"DB보기(새로고침)", "사용방법", "검 색", "수 정",  "등록", "삭제", "입력취소", "초기화"};
      for(int i=0;i<button.length;i++) {
         button[i]=new JButton(bName[i]);
         if(i<6) {
            label[i]=new JLabel(lName[i]);
            tf[i]=new JTextField();
         }            
         if(i<5)
            panel[i]=new JPanel();
      }      
      label3=new JLabel("검색/수정 결과창");
      btn3=new JButton("Clear");
      ta = new JTextArea(5, 100);
   }

   void setComponents() {
      panel[0].setLayout(new GridLayout(6, 2));
      panel[2].setLayout(new BorderLayout());
      panel[4].setLayout(new BorderLayout());

      // 라벨 글자 수정 / 테이블 글자 수정
      Font f=new Font("굴림체", Font.BOLD, 15);
      for(int i=0;i<label.length;i++) {
         label[i].setPreferredSize(new Dimension(25,25));
         label[i].setFont(f);
         tf[i].setFont(f);
      }
      table.setFont(f);

      ta.setFont(new Font("굴림체", Font.PLAIN, 13));
      ta.setBackground(Color.LIGHT_GRAY);
      ta.setDisabledTextColor(Color.BLACK);
      ta.setEnabled(false);
   }

   void addComponents() {
      for(int i=0;i<button.length;i++) {
         panel[1].add(button[i]);
         if(i<6) {
            panel[0].add(label[i]);
            panel[0].add(tf[i]);
         }
      }
      panel[2].add(panel[1], BorderLayout.NORTH);
      panel[2].add(new JScrollPane(table));

      panel[3].add(label3);
      panel[3].add(btn3);
      panel[4].add(panel[3], BorderLayout.NORTH);
      ta.setAutoscrolls(true);
      panel[4].add(new JScrollPane(ta));

      c.add(panel[0], BorderLayout.NORTH);
      c.add(panel[2], BorderLayout.CENTER);
      c.add(panel[4], BorderLayout.SOUTH);
   }
}
public class MovieData {
   MovieModel m;
   MovieView v;
   MovieActionHandler handler = new MovieActionHandler();
   MouseHandler handler2=new MouseHandler();

   Connection con = null;
   Statement stmt = null;
   ResultSet rs = null;

   MovieData() {
      v = new MovieView();
      for (int i = 0; i < v.button.length; i++) {
         v.button[i].addActionListener(handler);
         v.button[i].addMouseListener(handler2);
         if (i < 6)
            v.tf[i].addActionListener(handler);
      }
      v.btn3.addActionListener(handler);
      v.btn3.addMouseListener(handler2);

      resetTF();
   }
   class MouseHandler extends MouseAdapter{
      @Override
      public void mouseEntered(MouseEvent e) {
         for(int i=0;i<v.button.length;i++) {
            if(e.getSource()==v.button[i]) {
               if(i==5 || i==3)
                  v.button[i].setBackground(new Color(230,100,100));
               else
                  v.button[i].setBackground(Color.LIGHT_GRAY);               
            }
         }
         if(e.getSource()==v.btn3)
            v.btn3.setBackground(Color.LIGHT_GRAY);
      }

      @Override
      public void mouseExited(MouseEvent e) {
         for(int i=0;i<v.button.length;i++)
            if(e.getSource()==v.button[i])
               v.button[i].setBackground(new ColorUIResource(238,238,238));
         if(e.getSource()==v.btn3)
            v.btn3.setBackground(new ColorUIResource(238,238,238));
      }
   }

   class MovieActionHandler implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == v.button[0])
            getMovie();
         else if (e.getSource() == v.button[2])
            searchMovie();
         else if (e.getSource() == v.button[3])
            updateMovie();
         else if (e.getSource() == v.button[4])
            addMovie();
         else if (e.getSource() == v.button[5])
            deleteMovie();
         else if (e.getSource() == v.button[6])
            resetTF();
         else if (e.getSource() == v.button[7])
            cancel();
         else if (e.getSource() == v.btn3)
            clearT();
         for(int i=0;i<v.tf.length;i++) {
            if(e.getSource()==v.tf[i])
               searchMovie();
         }
      }
   }

   public void clearT() {   // 텍스트 에리어 초기화
      v.ta.setText("");
   }


   public void resetTF() {   // 텍스트 필드 없애기
      for (int i = 0; i < 6; i++)
         v.tf[i].setText("");
   }

   public void cancel() { // 초기화 버튼
      resetTF();
      v.model.setNumRows(0);
      for (int i = 0; i < v.tf.length; i++) {
         v.tf[i].setEnabled(true);
         v.tf[i].setBackground(Color.WHITE);
         v.tf[i].setDisabledTextColor(Color.BLACK);
         v.button[i].setEnabled(true);
      }
      v.button[6].setEnabled(true);
      v.btn3.setEnabled(true);

      v.ta.setText("");
      v.ta.setBackground(Color.LIGHT_GRAY);
      v.ta.setDisabledTextColor(Color.BLACK);
   }

   public Connection makeConnection() { // jdbc mysql 연결하기
      String url = "jdbc:mysql://localhost:3306/movie_db?serverTimezone=Asia/Seoul";
      String id = "root";
      String password = "1234";

      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         // System.out.println("DB연결완료1");
         con = DriverManager.getConnection(url, id, password);
         stmt = con.createStatement();
         // System.out.println("DB연결완료2");
      } catch (ClassNotFoundException e) {
         System.out.println("JDBC 드라이버 로드 에러");
         e.getStackTrace();
      } catch (SQLException e) {
         System.out.println("JDBC 연결 오류");
         e.getStackTrace();
      }
      
      return con;
   }

   public void disConnection() { // mysql 연결 끊기
      try {
         rs.close();
         stmt.close();
         con.close();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void getMovie() { // 조회 (DB갱신)
      v.model.setNumRows(0);
      makeConnection();
      String sql = "";
      sql = "SELECT * FROM movie";

      try {
         rs = stmt.executeQuery(sql);   // select문장
         
         while (rs.next()) {            // movie테이블 불러오기
            m = new MovieModel(rs.getString("mvName"), rs.getString("year"), rs.getString("dirName"),
                  rs.getString("type"), rs.getString("age"), rs.getString("grade"));
            Object[] data= {m.mvName, m.year, m.dirName, m.type, m.age, m.grade};
            v.model.addRow(data);
         }
      } catch (SQLException sqle) {
         System.out.println("getData: SQL Error");
      }
      disConnection();
   }

   void addTextArea() { // 텍스트 에리어에 붙히기
      String s;
      if (m.mvName.length() < 4)
         m.mvName += "\t";
      if (m.mvName.length() < 12)
         m.mvName += "\t\t";
      if (m.dirName.length() <= 6)
         m.dirName += "\t";
      if (m.type.length() < 4)
         m.type += "\t";
      if (m.type.length() > 5)
         m.type += "\t";
      if (m.age.length() < 4)
         m.age += "\t";

      s = String.format("%-10s\t%10s\t%10s\t%5s\t%10s\t%10s\n", m.mvName, m.year, m.dirName, m.type, m.age, m.grade);

      v.ta.append(s);
   }

   public void deleteMovie() { // DB삭제
      viewToModel();
      int isDelete = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?");
      System.out.println("DELETE: " + isDelete);
      if (isDelete == 0) {
         makeConnection();
         String sql = "";
         sql = "DELETE FROM movie where ";
         sql += checkTF();
         System.out.println(sql);
         try {
            stmt.executeUpdate(sql);
         } catch (SQLException sqle) {
            System.out.println("isExist: DELETE SQL Error");
         }
         getMovie();
         resetTF();
         disConnection();
      }
   }

   public void updateMovie() { // DB수정
      viewToModel();
      makeConnection();

      try {
         String s = "";
         s = "UPDATE movie SET mvName='" + m.mvName + "',year='" + m.year + "',dirName='" + m.dirName + "',type='"
               + m.type + "',age='" + m.age + "',grade='" + m.grade + "' WHERE mvName='" + m.mvName + "'";
         System.out.println(s);
         stmt.executeUpdate(s);

         modelToView();
         addTextArea();
         getMovie();

      } catch (SQLException sqle) {
         System.out.println("isExist: SQL Error");
      }
      disConnection();
   }

   public void addMovie() { // DB등록
      makeConnection();
      viewToModel();
      try {
         String s = "";
         s = "INSERT INTO movie (mvName, year, dirName, type, age, grade) values ";
         s += "('" + m.mvName + "','" + m.year + "','" + m.dirName + "','" + m.type + "','" + m.age + "','" + m.grade
               + "')";
         System.out.println(s);
         stmt.executeUpdate(s);

      } catch (SQLException sqle) {
         System.out.println(sqle.getMessage());
      }
      disConnection();
      getMovie();
      resetTF();
   }

   void viewToModel() {
      m = new MovieModel(v.tf[0].getText(), v.tf[1].getText(), v.tf[2].getText(), v.tf[3].getText(),
            v.tf[4].getText(), v.tf[5].getText());
   }

   void modelToView() {
      v.tf[0].setText(m.mvName);
      v.tf[1].setText(m.year);
      v.tf[2].setText(m.dirName);
      v.tf[3].setText(m.type);
      v.tf[4].setText(m.age);
      v.tf[5].setText(m.grade);
   }

   public void searchMovie(){   // DB검색
      makeConnection();
      String sql="";
      String s= "";
      sql="SELECT * FROM movie WHERE ";
      sql+= checkTF();

      System.out.println(sql);
      try{
         rs=stmt.executeQuery(sql);

         while(rs.next()){
            m=new MovieModel(rs.getString("mvName"),rs.getString("year"),rs.getString("dirName"),
                  rs.getString("type"),rs.getString("age"),rs.getString("grade"));

            v.ta.append(s);
            modelToView();
            addTextArea();
         }
      }catch(SQLException sqle){System.out.println("isExist: SQL Error");}
      disConnection();
   }

   public String checkTF() {   // DB 조건문 검색 메서드
      int count = 0;
      String help = "";
      for (int i = 0; i < 6; i++) {
         if (v.tf[i].getText().length() > 1) {
            count++;
            if (count > 1)
               help += " AND ";

            switch (i) {
            case 0:
               help += "mvName";
               break;
            case 1:
               help += "year";
               break;
            case 2:
               help += "dirName";
               break;
            case 3:
               help += "type";
               break;
            case 4:
               help += "age";
               break;
            case 5:
               help += "grade";
               break;
            default:
               System.out.println("checkTF error");
               break;
            }
            help += " like '%" + v.tf[i].getText() + "%'";
         }
      }
      return help;
   }

   public static void main(String[] args) {
      new MovieData();
   }
}