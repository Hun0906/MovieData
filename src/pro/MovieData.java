package pro;

import java.awt.*;
import javax.swing.*;
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
	Container c = getContentPane();
	JPanel[] panel = new JPanel[3];
	// 0�� JPanel
	JLabel mvName;
	JLabel year;
	JLabel dirName;
	JLabel type;
	JLabel age;
	JLabel grade;
	JTextField[] tf = new JTextField[6];
//	JComboBox<String>ageC;
//	JComboBox<String>typeC;
//	JComboBox<Double>gradeC;
	// 1�� JPanel
	JTextArea ta;
	// 2�� JPanel
	JButton[] button = new JButton[7];

	public MovieView() {
		setTitle("Movie Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		newComponents();
		setComponents();
		addComponents();

		setSize(900, 500);
		setVisible(true);
	}

	void newComponents() {
		mvName = new JLabel("��ȭ ����", JLabel.CENTER);
		age = new JLabel("�������ɳ���", JLabel.CENTER);
		dirName = new JLabel("������", JLabel.CENTER);
		type = new JLabel("��    ��", JLabel.CENTER);
		year = new JLabel("���� ����", JLabel.CENTER);
		grade = new JLabel("��    ��", JLabel.CENTER);
		String[] bName = { "��ü����(���ΰ�ħ)", "��� ���", "�˻�", "���", "����", "����", "���" };
		for (int i = 0; i < 7; i++) {
			button[i] = new JButton(bName[i]);
			if (i < 3)
				panel[i] = new JPanel();
			if (i < 6)
				tf[i] = new JTextField();

		}
//		String[] aC= {"ALL", "12��", "15��", "19��", "29��"};
//		String[] tC= {"������", "�׼�", "����", "���", "�ڹ̵�", "���͸�", "���� �����", "����", "�ִϸ��̼�", "����", "���"};
//		Double[] gC= {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0}; 
//		ageC=new JComboBox<String>(aC);
//		typeC=new JComboBox<String>(tC);
//		gradeC=new JComboBox<Double>(gC);
		ta = new JTextArea(25, 125);
	}

	void setComponents() {
		c.setLayout(new BorderLayout());
		panel[0].setLayout(new GridLayout(2, 6));

		ta.setFont(new Font("����ü", Font.BOLD, 13));
		ta.setBackground(Color.LIGHT_GRAY);
		ta.setDisabledTextColor(Color.BLACK);
		ta.setEnabled(false);

//		ageC.setSelectedIndex(4);
//		typeC.setSelectedIndex(5);
//		gradeC.setSelectedIndex(7);
	}

	void addComponents() {
		panel[0].add(mvName);
		panel[0].add(tf[0]);
		panel[0].add(year);
		panel[0].add(tf[1]);
		panel[0].add(dirName);
		panel[0].add(tf[2]);
		panel[0].add(type);
		panel[0].add(tf[3]);
		panel[0].add(age);
		panel[0].add(tf[4]);
		panel[0].add(grade);
		panel[0].add(tf[5]);

		ta.setAutoscrolls(true);
		panel[1].add(new JScrollPane(ta));

		for (int i = 0; i < 7; i++)
			panel[2].add(button[i]);

		c.add(panel[0], BorderLayout.NORTH);
		c.add(panel[1]);
		c.add(panel[2], BorderLayout.SOUTH);
	}
}

public class MovieData {
	MovieModel m;
	MovieView v;
	//SearchView s;
	MovieActionHandler handler = new MovieActionHandler();

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	MovieData() {
		v = new MovieView();
		for (int i = 0; i < v.button.length; i++) {
			v.button[i].addActionListener(handler);
			if (i < 6)
				v.tf[i].addActionListener(handler);
		}

		v.addWindowListener(new WindowHandler());
		resetTF();
	}

	public void resetTF() {
		for (int i = 0; i < 6; i++)
			v.tf[i].setText("");
	}

	class MovieActionHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == v.button[0])
				getMovie();
			else if (e.getSource() == v.button[1])
				manual();
			else if (e.getSource() == v.button[2])
				searchMovie();
			else if (e.getSource() == v.button[3])
				addMovie();
			else if (e.getSource() == v.button[4])
				updateMovie();
			else if (e.getSource() == v.button[5])
				deleteMovie();
			else if (e.getSource() == v.button[6])
				cancel();
			else if (e.getSource() == v.tf[0])
				searchMovie();
			else if (e.getSource() == v.tf[1])
				searchMovie();
			else if (e.getSource() == v.tf[2])
				searchMovie();
			else if (e.getSource() == v.tf[3])
				searchMovie();
			else if (e.getSource() == v.tf[4])
				searchMovie();
			else if (e.getSource() == v.tf[5])
				searchMovie();
		}
	}

	class WindowHandler extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			cancel();
		}
	}

	public void manual() {
		for (int i = 0; i < v.tf.length; i++) {
			v.tf[i].setEnabled(false);
			v.tf[i].setBackground(Color.LIGHT_GRAY);
			v.tf[i].setDisabledTextColor(Color.RED);
			v.button[i].setEnabled(false);
		}
		v.tf[0].setText("��� �� �� �Է��ϼ���");
		v.tf[1].setText("YYYY.MM.DD");
		v.tf[2].setText("��� �� �� �Է��ϼ���");
		v.tf[3].setText("��� �� �� �Է��ϼ���");
		v.tf[4].setText("*�� �̻� ������ (�Ǵ� ALL)");
		v.tf[5].setText("8.57 (�Ҽ� �� ���ڸ�����)");

		v.ta.setBackground(Color.BLACK);
		v.ta.setDisabledTextColor(Color.WHITE);
		v.ta.setFont(new Font("����ü", Font.PLAIN, 20));

		v.ta.setText("");
		v.ta.append("���� ����\n");
		v.ta.append("��� �ÿ��� ��� ���� ���Ŀ� �°� ��� �Է��ϼž� �մϴ�.\n\n");
		v.ta.append("�˻� �ÿ��� �˻��ϰ� ���� �׸� ���Ŀ� �°� �Է��ϼž� �մϴ�.\n");
		v.ta.append("���� �׸��� ���ÿ� �����ϰ� ������ ���� �� �Է� �� �˻��� �����ø� �˴ϴ�.\n");
		v.ta.append("�˻��Ͻ� �׸��� �Է� �� Enter�� ������ �˻��� �����մϴ�.\n\n");
		v.ta.append("���� �ÿ��� �˻��� ���� �մϴ�.\n");
		v.ta.append("���� �׸��� �Է��Ͻø� ���ÿ� �����ϴ� ������ �׸� ���� �մϴ�.\n\n");
		v.ta.append("���� �ÿ� �˻��� �Ͻ� �� �����Ͻð� ���� ������ ���� �� ���� ��ư�� �����ø� �˴ϴ�.\n\n");
		v.ta.append("��� �ÿ� �ʱ� ���·� ���ư��ϴ�.\n");
	}

	public void cancel() { // ��ҹ�ư
		resetTF();
		for (int i = 0; i < v.tf.length; i++) {
			v.tf[i].setEnabled(true);
			v.tf[i].setBackground(Color.WHITE);
			v.tf[i].setDisabledTextColor(Color.BLACK);
			v.button[i].setEnabled(true);
		}
		v.ta.setText("");
		v.ta.setBackground(Color.LIGHT_GRAY);
		v.ta.setDisabledTextColor(Color.BLACK);
		v.ta.setFont(new Font("����ü", Font.PLAIN, 13));
	}

	public Connection makeConnection() { // jdbc mysql �����ϱ�
		String url = "jdbc:mysql://localhost:3306/movie_db?serverTimezone=Asia/Seoul";
		String id = "root";
		String password = "1234";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// System.out.println("DB����Ϸ�1");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			// System.out.println("DB����Ϸ�2");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC ����̹� �ε� ����");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("JDBC ���� ����");
			e.getStackTrace();
		}

		return con;
	}

	public void disConnection() { // mysql ���� ����
		try {
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void getMovie() { // ��ȸ
		makeConnection();
		String sql = "";
		sql = "SELECT * FROM movie";
		v.ta.setText("");
		v.ta.setText("��ȭ ����\t\t        ���� �⵵\t     �� �� �� \t       ��      �� \t ���� ���� ���� \t   ��      ��\n");
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				m = new MovieModel(rs.getString("mvName"), rs.getString("year"), rs.getString("dirName"),
						rs.getString("type"), rs.getString("age"), rs.getString("grade"));
				addTextArea();
			}
		} catch (SQLException sqle) {
			System.out.println("getData: SQL Error");
		}
		disConnection();
	}

	void addTextArea() { // �ؽ�Ʈ ����� ���� ������
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

		s = String.format("%-10s\t%10s\t%10s\t%5s\t%10s\t%10s\n", m.mvName, m.year, m.dirName, m.type, m.age,
				"��" + m.grade);
		v.ta.append(s);
	}

	public void deleteMovie() { // ����
		viewToModel();
		int isDelete = JOptionPane.showConfirmDialog(null, "���� �Ͻðڽ��ϱ�?");
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
			disConnection();
		}
	}

	public void updateMovie() { // ����
		viewToModel();
		makeConnection();
		try {
			String s = "";
			s = "UPDATE movie SET mvName='" + m.mvName + "',year='" + m.year + "',dirName='" + m.dirName + "',type='"
					+ m.type + "',age='" + m.age + "',grade='" + m.grade + "' WHERE mvName='" + m.mvName + "'";
			System.out.println(s);
			stmt.executeUpdate(s);

		} catch (SQLException sqle) {
			System.out.println("isExist: SQL Error");
		}
		disConnection();
	}

	public void addMovie() { // �Է�
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

	public void searchMovie(){	   //�˻�
		makeConnection();
		String sql="";
		String s= "";
		sql="SELECT * FROM movie WHERE ";
		sql+= checkTF();
		//Font font = new Font("Britannic ����",Font.BOLD,15);
		v.ta.setText("");
		//v.ta.setFont(font);
		//v.ta.setForeground(Color.yellow);
		v.ta.setBackground(Color.white);
		v.ta.setText("��ȭ ����\t\t        ���� �⵵\t     �� �� �� \t       ��      �� \t ���� ���� ���� \t   ��      ��\n");
		//System.out.println(sql+"'%"+v.tf[0].getText()+"%'");
		try{
			rs=stmt.executeQuery(sql);
	
			while(rs.next()){
				m=new MovieModel(rs.getString("mvName"),rs.getString("year"),rs.getString("dirName"),
						rs.getString("type"),rs.getString("age"),rs.getString("grade"));
				
				addTextArea();
				v.ta.append(s);
				modelToView();
			}
//			else{
//				resetTF();
//			}
		}catch(SQLException sqle){System.out.println("isExist: SQL Error");}
		disConnection();
	}

	public String checkTF() {
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