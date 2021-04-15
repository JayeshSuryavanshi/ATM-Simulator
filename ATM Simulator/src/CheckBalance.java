import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class CheckBalance extends JFrame implements ActionListener{
	JFrame f;
	JLabel currentLable, currentAmount, savingLable, savingAmount;
	JButton back;
	String accountNumber;
	Connection con;
	
	CheckBalance(String accountNumber){
		this.accountNumber = accountNumber;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "manager");			
		}
		catch(Exception e){
			System.out.println(e);
		}	
		
		
		f = new JFrame();
		f.setLayout(null);
		f.setSize(getMaximumSize());
		f.setVisible(true);
		
		currentLable = new JLabel("CURRENT BALANCE:");
		savingLable = new JLabel("SAVINGS BALANCE:");
		currentAmount = new JLabel();
		savingAmount = new JLabel();
		back = new JButton("BACK");
		
		currentLable.setFont(new Font("Times New Roman",Font.PLAIN,30));
		savingLable.setFont(new Font("Times New Roman",Font.PLAIN,30));
		currentAmount.setFont(new Font("Times New Roman",Font.PLAIN,30));
		savingAmount.setFont(new Font("Times New Roman",Font.PLAIN,30));
		back.setFont(new Font("Times New Roman",Font.PLAIN,30));
		
		currentLable.setBounds(300,250,310,75);
		currentAmount.setBounds(650,250,300,75);
		savingLable.setBounds(300,350,300,75);
		savingAmount.setBounds(650,350,200,75);
		back.setBounds(0,550,150,50);
		
		f.add(currentLable);
		f.add(currentAmount);
		f.add(savingLable);
		f.add(savingAmount);
		f.add(back);
		
		back.addActionListener(this);
		
		showBalance(accountNumber);	
		
	}
	
	public void showBalance(String accNumber){
		ResultSet rs;
		try {
			PreparedStatement st = con.prepareStatement("select savingBalance, currentBalance from Account where accountNumber = ?");
			st.setString(1, accNumber);
			
			rs = st.executeQuery();
			
			if(rs.next()){
				currentAmount.setText(String.valueOf(rs.getFloat(2)));
				savingAmount.setText(String.valueOf(rs.getFloat(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent a){
		f.setVisible(false);
		new Menu(accountNumber);	
	}
}
