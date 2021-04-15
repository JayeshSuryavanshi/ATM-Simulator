import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.*;

@SuppressWarnings("serial")
public class ChangePin extends JFrame implements ActionListener{
	JFrame f;
	JButton confirm, reset, cancel;
	JLabel oldPassword, newPassword, confirmPassword;
	JPasswordField oldPass, newPass, confirmPass;
	String accountNumber;
	Connection con;
	
	ChangePin(String accountNumber){
		this.accountNumber = accountNumber;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "manager");			
		}
		catch(Exception e){
			System.out.println(e);
		}	
		
		if(true){
			f = new JFrame();
			f.setLayout(null);
			f.setSize(getMaximumSize());
			f.setVisible(true);		
			
			confirm = new JButton("CONFIRM");
			reset = new JButton("RESET");
			cancel =  new JButton("CANCEL");
			oldPassword = new JLabel("OLD PASSWORD");
			newPassword = new JLabel("NEW PASSWORD");
			confirmPassword = new JLabel("CONFIRM PASSWORD");
			oldPass = new JPasswordField();
			newPass = new JPasswordField();
			confirmPass = new JPasswordField();
			
			confirm.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			reset.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			cancel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			oldPassword.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			newPassword.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			confirmPassword.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			oldPass.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			newPass.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			confirmPass.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			
			oldPassword.setBounds(400,250,200,50);
			oldPass.setBounds(700,250,300,50);
			newPassword.setBounds(400,350,200,50);
			newPass.setBounds(700,350,300,50);
			confirmPassword.setBounds(400,450,300,50);
			confirmPass.setBounds(700,450,300,50);
			confirm.setBounds(300,550,200,50);
			reset.setBounds(550,550,200,50);
			cancel.setBounds(800,550,200,50);
			f.add(oldPassword);
			f.add(oldPass);
			f.add(confirmPassword);
			f.add(confirmPass);
			f.add(newPass);
			f.add(newPassword);
			f.add(confirm);
			f.add(reset);
			f.add(cancel);
			
			reset.addActionListener(this);
			cancel.addActionListener(this);
			confirm.addActionListener(this);
			
		}		
	}

	@SuppressWarnings("deprecation")
	public void updatePin(String accNumber){
		ResultSet rs;
		PreparedStatement st;
		try {
			st = con.prepareStatement("select pin from Account where accountNumber = ?");
			st.setString(1,accountNumber);
		
			rs = st.executeQuery();
			if(rs.next()){
				if(Integer.parseInt(oldPass.getText()) == rs.getInt(1)){
					st = con.prepareStatement("update Account set pin = ? where accountNumber = ?");
					st.setInt(1, Integer.parseInt(newPass.getText()));
					st.setString(2,accountNumber);
					
					st.executeUpdate();
				}
			}
			
			else{
				JOptionPane.showMessageDialog(this,"Please enter correct Pin!");
				oldPass.setText("");
				newPass.setText("");
				confirmPass.setText("");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == reset){
			oldPass.setText("");
			newPass.setText("");
			confirmPass.setText("");
		}
		
		if(a.getSource() == cancel){
			f.setVisible(false);
			new Menu(accountNumber);
		}
		
		if(a.getSource() == confirm){
			if(newPass.getText().equals(confirmPass.getText()) && !newPass.getText().isEmpty() && !oldPass.getText().isEmpty()){
				updatePin(accountNumber);
				JOptionPane.showMessageDialog(this,"You have successfully changed your Pin!");
				f.setVisible(false);
				new Menu(accountNumber);
			}
			
			else{
				JOptionPane.showMessageDialog(this,"Please enter correct Pin!");
				oldPass.setText("");
				newPass.setText("");
				confirmPass.setText("");
			}
		}
		
	}	
}
