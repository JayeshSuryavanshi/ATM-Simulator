import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class OrderSlip extends JFrame implements ActionListener{
	JFrame f;
	JLabel message;
	JLabel residence, street, city, state;
	JButton confirm, cancel;
	String accountNumber;
	Connection con;
	
	public OrderSlip(String accountNumber){
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
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("select c.residence, c.street, c.city, c.state from Customer c, Account a where a.accountNumber = ? and c.customerId = a.customerId");
			st.setString(1, accountNumber);
			ResultSet rs = st.executeQuery();
			rs.next();
		
			message = new JLabel("New Check Book will be delivered at below address:");
			residence = new JLabel(rs.getString(1));
			street = new JLabel(rs.getString(2));
			city = new JLabel(rs.getString(3));
			state = new JLabel(rs.getString(4));
			confirm = new JButton("CONFIRM");
			cancel = new JButton("CANCEL");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		message.setFont(new Font("Times New Roman",Font.PLAIN,25));
		residence.setFont(new Font("Times New Roman",Font.PLAIN,25));
		street.setFont(new Font("Times New Roman",Font.PLAIN,25));
		city.setFont(new Font("Times New Roman",Font.PLAIN,25));
		state.setFont(new Font("Times New Roman",Font.PLAIN,25));
		confirm.setFont(new Font("Times New Roman",Font.PLAIN,30));
		cancel.setFont(new Font("Times New Roman",Font.PLAIN,30));
		
		message.setBounds(200,150,1000,75);
		residence.setBounds(200,250,500,50);
		street.setBounds(200,300,500,50);
		city.setBounds(200,350,500,50);
		state.setBounds(200,400,500,50);
		confirm.setBounds(0,550,200,50);
		cancel.setBounds(1125,550,200,50);
		
		f.add(message);
		f.add(residence);
		f.add(street);
		f.add(city);
		f.add(state);
		f.add(confirm);
		f.add(cancel);
		
		confirm.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent a){
		if(a.getSource() == cancel){
			f.setVisible(false);
			new Menu(accountNumber);	
		}
		
		else if(a.getSource() == confirm){
			try {
				PreparedStatement st = con.prepareStatement("select accountNumber from OrderChequeBook where accountNumber = ?");
				st.setString(1, accountNumber);
				ResultSet rs = st.executeQuery();
				//rs.next();
				if(!rs.next()){
					long time = System.currentTimeMillis();
					Timestamp timestamp = new Timestamp(time);
					
					st = con.prepareStatement("insert into OrderChequeBook values(?, ?)");
					st.setString(1, accountNumber);
					st.setTimestamp(2, timestamp);
					st.executeUpdate();
					
				}
				else{
					JOptionPane.showMessageDialog(this,"Order Already Exists!");
				}
				
				f.setVisible(false);
				new Menu(accountNumber);	
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
	
