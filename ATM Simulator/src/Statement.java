import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class Statement extends JPanel implements ActionListener {
	JFrame f;
	Container cp;
	JButton back;
	JTable table ;
	JScrollPane jsp;
	String colHeads[] = {"Account Number", "Sender","Receiver", "Amount", "Transaction type","Account Type","Date"};
	String data[][];
	String accountNumber;
	Connection con;
	
	
	@SuppressWarnings("deprecation")
	Statement(String accountNumber){
		this.accountNumber = accountNumber;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "manager");			
		}
		catch(Exception e){
			System.out.println(e);
		}	
		
		
		f = new JFrame();
		cp = f.getContentPane();
		cp.setLayout(new BorderLayout());
		f.setSize(getMaximumSize());
		f.setVisible(true);
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(10,10));
	
		
		back = new JButton("BACK");
		//back.setBounds(0,625,350,75);
		back.setFont(new Font("Times New Roman",Font.PLAIN, 25));
		f.add(back, BorderLayout.SOUTH);
		back.addActionListener(this);
		
		PreparedStatement st;
		ResultSet rs;
		ResultSetMetaData rsmd;
		try {
			long time = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
			timestamp.setMonth(timestamp.getMonth()-1);
			
			st = con.prepareStatement("select count(*) from Transaction where accountNumber = ? and transactionDateTime > ?");
			st.setString(1, accountNumber);
			st.setTimestamp(2 , timestamp);
			rs = st.executeQuery();
			rs.next();
			data = new String[rs.getInt(1)][7];
			
			st = con.prepareStatement("select accountNumber, sender,receiver, transactionAmount, transactionType, accountType, transactionDateTime from Transaction where accountNumber = ? and transactionDateTime > ?");
			st.setString(1, accountNumber);
			st.setTimestamp(2 , timestamp);
			rs = st.executeQuery();
			rsmd = rs.getMetaData();
			System.out.println(rsmd.getColumnCount());
			
			
			int i=0;
			while(rs.next()){
				
				data[i][0] = rs.getString(1);
				data[i][1] = rs.getString(2);
				data[i][2] = rs.getString(3);
				data[i][3] = Float.toString(rs.getFloat(4));
				data[i][4] = rs.getString(5);
				data[i][5] = rs.getString(6);
				data[i][6] = rs.getString(7);
				
				i++;
			}
			
			table = new JTable(data, colHeads);
			jsp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//table.setBounds(100, 100, 1000, 600);
			//jsp.setBounds(table.getBounds());
			cp.add(jsp,BorderLayout.CENTER);
			//f.add(table);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource()==back){
			f.setVisible(false);
			new Menu(accountNumber);
		}
	}
}
