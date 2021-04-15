import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Deposit extends JFrame implements ActionListener{
	JFrame f;
	JButton savings, current, confirm, cancel;
	JTextField amount;
	JLabel amountLabel1, amountLabel2;
	String accountNumber;
	boolean savingFlag, currentFlag;
	Connection con;
	
	Deposit(String accountNumber){
		this.accountNumber = accountNumber;
		savingFlag = false;
		currentFlag = false;
		
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
					
			savings = new JButton("SAVINGS");
			current = new JButton("CURRENT");
			confirm = new JButton("CONFIRM");
			cancel  = new JButton("CANCEL");
			amount  = new JTextField();
			amountLabel1 = new JLabel("Enter amount");
			amountLabel2 = new JLabel("in multiples of 100");
			
			savings.setFont(new Font("Times New Roman",Font.PLAIN,30));
			current.setFont(new Font("Times New Roman",Font.PLAIN,30));
			confirm.setFont(new Font("Times New Roman",Font.PLAIN,30));
			cancel.setFont(new Font("Times New Roman",Font.PLAIN,30));
			amountLabel1.setFont(new Font("Times New Roman", Font.PLAIN,25));
			amountLabel2.setFont(new Font("Times New Roman", Font.PLAIN,25));
			amount.setFont(new Font("Times New Roman",Font.PLAIN,30));
			
			
			savings.setBounds(0,200,200,75);
			current.setBounds(966,200,200,75);
			amountLabel1.setBounds(400, 375, 200, 50);
			amountLabel2.setBounds(400,425,200,50);
			amount.setBounds(650,375,200,75);
			confirm.setBounds(0,650,200,75);
			cancel.setBounds(966,650,200,75);
			
			f.add(savings);
			f.add(current);
			f.add(amount);
			f.add(amountLabel1);
			f.add(amountLabel2);
			f.add(confirm);			
			f.add(cancel);
			
			savings.addActionListener(this);
			current.addActionListener(this);
			confirm.addActionListener(this);
			cancel.addActionListener(this);			
		}		
		
	}//end of Constructor
	
	public void updateBalance(String accNumber){
		try {
			if(savingFlag){
				PreparedStatement st = con.prepareStatement("update Account set savingBalance = savingBalance + ? where accountNumber = ?");
				st.setFloat(1,Float.parseFloat(amount.getText()));
				st.setString(2,accNumber);
				st.executeUpdate();
				
				updateTransaction("Saving");
			}
			else{
				PreparedStatement st = con.prepareStatement("update Account set currentBalance = currentBalance + ? where accountNumber = ?");
				st.setFloat(1,Float.parseFloat(amount.getText()));
				st.setString(2,accNumber);
				st.executeUpdate();
				
				updateTransaction("Current");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//end of updateBalance
	
	public void updateTransaction(String type){
		
		try{
			PreparedStatement st;
			ResultSet rs;
			String transaction_id;
			char[] temp;
			
			st = con.prepareStatement("select max(transactionID) from Transaction");
			rs = st.executeQuery();
			
			//Incrementing transaction id
			rs.next();
			transaction_id = rs.getString(1);
			
			if(rs.wasNull()){
				transaction_id = "T0115011000";
			}
			else{
				temp = transaction_id.toCharArray();
				
				for( int i = temp.length -1; i > 0; i-- ){
					if(temp[i] == '9')
						temp[i] = '0';
					else{
						temp[i]++;
						break;
					}
				}
				transaction_id = new String(temp);
			}
		
			//System date and time
			long time = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
			
			st = con.prepareStatement("insert into Transaction values( ?, ?, ?,'Deposit', ?,'SELF', ?, 'SELF' )");
			st.setString(1, transaction_id);
			st.setString(2, accountNumber);
			st.setTimestamp(3, timestamp);
			st.setString(4, type);
			st.setFloat(5, Float.parseFloat(amount.getText()));
			
			st.executeUpdate();			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}//end of updateTransaction
	
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == savings){
			savingFlag = true;
			currentFlag = false;
		}
		
		else if(a.getSource() == current){
			currentFlag = true;
			savingFlag = false;
		}
		
		if(a.getSource() == confirm){
			if(savingFlag == false && currentFlag == false){
				JOptionPane.showMessageDialog(this,"Please select any one of the account !");
				amount.setText("");				
			}
			else if(amount.getText().equals("")){
				JOptionPane.showMessageDialog(this,"Please enter the amount !");
			}
			else{
				updateBalance(accountNumber);		
				f.setVisible(false);
				new Menu(accountNumber);
			}				
		}
		
		if(a.getSource() == cancel){
			f.setVisible(false);
			new Menu(accountNumber);
		}
				
	}//end of actionPerformed	
}
