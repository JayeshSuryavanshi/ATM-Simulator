import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

@SuppressWarnings("serial")
public class login extends JFrame implements ActionListener{
	JFrame f;
	JButton submitButton, resetButton;
	JLabel accountNumber, pin;
	JTextField accountText;
	JPasswordField pinText;
	String accNumber;
	Connection con;
		
	login(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "manager");			
		}
		catch(Exception e){
			System.out.println(e);
		}	
		
		f = new JFrame();
		
		try {
            		f.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("ATMImage.jpg")))));
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	f.pack();
		
		f.setLayout(null);
		f.setSize(getMaximumSize());
		f.setVisible(true);		
		
		submitButton = new JButton("SUBMIT");
		resetButton = new JButton("RESET");
		accountNumber = new JLabel("ACC NO");
		pin = new JLabel("PIN CODE");
		accountText = new JTextField();
		pinText = new JPasswordField();
			
		accountNumber.setBounds(400,250,200,50);
		accountText.setBounds(650,250,300,50);
		
		pin.setBounds(400,350,200,50);
		pinText.setBounds(650,350,300,50);
		
		submitButton.setBounds(450,450,150,50);
		resetButton.setBounds(650,450,150,50);	
		
		accountNumber.setFont(new Font("Times New Roman",Font.PLAIN,30));
		pin.setFont(new Font("Times New Roman",Font.PLAIN,30));
		accountText.setFont(new Font("Times New Roman",Font.PLAIN,30));
		pinText.setFont(new Font("Times New Roman",Font.PLAIN,30));
		submitButton.setFont(new Font("Times New Roman",Font.PLAIN,25));
		resetButton.setFont(new Font("Times New Roman",Font.PLAIN,25));
		
		f.add(accountNumber);
		f.add(accountText);
		f.add(pin);
		f.add(pinText);
		f.add(submitButton);
		f.add(resetButton);
		
		
		resetButton.addActionListener(this);
		submitButton.addActionListener(this);
	}
	
	public int getAuthentication(String accountNumber){
		ResultSet rs;
		
		try {
			PreparedStatement st = con.prepareStatement("select pin from Account where accountNumber = ?");
			st.setString(1,accountNumber);
			rs = st.executeQuery();
			
			if(rs.next()){
				return rs.getInt(1);				
			}
						
		} 		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}


	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == resetButton){
			accountText.setText("");
			pinText.setText("");
		}
		
		if(a.getSource() == submitButton){
			int tempPin = getAuthentication(accountText.getText());
			if(tempPin == Integer.parseInt(pinText.getText()) && tempPin!=-1){
				new Menu(accountText.getText());
				f.setVisible(false);			
			}
			else{
				JOptionPane.showMessageDialog(this,"Please enter correct Pin!");				
			}
		}		
	}
}
