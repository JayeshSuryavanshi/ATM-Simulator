import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class Transfer extends JFrame implements ActionListener{
	JFrame f;
	JButton savings, current, confirm, cancel;
	JTextField amount, accountNumberText;
	JLabel accountLabel, amountLabel;
	String accountNumber;
	
	boolean savingFlag, currentFlag;
	Connection con;
	
	Transfer(String accountNumber){
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
		
		f = new JFrame();
		f.setLayout(null);
		f.setSize(getMaximumSize());
		f.setVisible(true);			
				
		savings = new JButton("SAVINGS");
		current = new JButton("CURRENT");
		confirm = new JButton("CONFIRM");
		cancel  = new JButton("CANCEL");
		amount  = new JTextField();
		accountNumberText = new JTextField();
		amountLabel = new JLabel("Enter amount");
		accountLabel = new JLabel("Enter Account Number");
		
		savings.setFont(new Font("Times New Roman",Font.PLAIN,30));
		current.setFont(new Font("Times New Roman",Font.PLAIN,30));
		confirm.setFont(new Font("Times New Roman",Font.PLAIN,30));
		cancel.setFont(new Font("Times New Roman",Font.PLAIN,30));
		amount.setFont(new Font("Times New Roman",Font.PLAIN,30));
		accountNumberText.setFont(new Font("Times New Roman",Font.PLAIN,30));
		amountLabel.setFont(new Font("Times New Roman", Font.PLAIN,25));
		accountLabel.setFont(new Font("Times New Roman", Font.PLAIN,25));
		
		savings.setBounds(0,200,200,75);
		current.setBounds(966,200,200,75);
		amountLabel.setBounds(300, 375, 300, 50);
		accountLabel.setBounds(300,450,300,50);
		confirm.setBounds(0,650,200,75);
		cancel.setBounds(966,650,200,75);
		amount.setBounds(650,375,225,50);
		accountNumberText.setBounds(650, 450, 225, 50);
		
		
		f.add(savings);
		f.add(current);
		f.add(amount);
		f.add(amountLabel);
		f.add(accountLabel);
		f.add(confirm);			
		f.add(cancel);
		f.add(accountNumberText);
		
		savings.addActionListener(this);
		current.addActionListener(this);
		confirm.addActionListener(this);
		cancel.addActionListener(this);		
	}

	public void updateWithdraw(String accNumber){
		PreparedStatement st;
		try {
			if(savingFlag){
				if(withdrawConstraint("Saving")){
					st = con.prepareStatement("update Account set savingBalance = savingBalance - ? where accountNumber = ?");
					st.setFloat(1,Float.parseFloat(amount.getText()));
					st.setString(2,accNumber);
					st.executeUpdate();
					updateTransaction("Saving", accNumber, accountNumberText.getText(), "SELF", "Transfer" );
				}
				else{
					JOptionPane.showMessageDialog(this,"Insufficient Balance!");
				}
			}
			
			else{
				if(withdrawConstraint("Current")){
					st = con.prepareStatement("update Account set currentBalance = currentBalance - ? where accountNumber = ?");
					st.setFloat(1,Float.parseFloat(amount.getText()));
					st.setString(2,accNumber);
					st.executeUpdate();				
					updateTransaction("Current", accNumber, accountNumberText.getText(), "SELF", "Transfer" );
				}
				else{
					JOptionPane.showMessageDialog(this,"Insufficient Balance!");
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//end of updateBalance
	
	public void updateDeposit(String accNumber){
		PreparedStatement st;
		try {
			if(savingFlag){
				st = con.prepareStatement("update Account set savingBalance = savingBalance + ? where accountNumber = ?");
				st.setFloat(1,Float.parseFloat(amount.getText()));
				st.setString(2,accNumber);
				st.executeUpdate();
				
				updateTransaction("Saving", accountNumberText.getText(), "SELF", accountNumber,"Transfer");
			}
			else{
				st = con.prepareStatement("update Account set currentBalance = currentBalance + ? where accountNumber = ?");
				st.setFloat(1,Float.parseFloat(amount.getText()));
				st.setString(2,accNumber);
				st.executeUpdate();
				
				updateTransaction("Current", accountNumberText.getText(), "SELF", accountNumber,"Transfer");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//end of updateBalance
	
	public void updateTransaction(String acctype,String accNo, String receiver, String sender, String transactionType){
		
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
			
			st = con.prepareStatement("insert into Transaction values( ?, ?, ?,?, ?,?, ? ,?)");
			st.setString(1, transaction_id);
			st.setString(2, accNo);
			st.setTimestamp(3, timestamp);
			st.setString(4, transactionType);
			st.setString(5, acctype);
			st.setString(6, receiver);
			st.setFloat(7, Float.parseFloat(amount.getText()));
			st.setString(8, sender);
			 
			st.executeUpdate();			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}//end of updateTransaction

	public boolean withdrawConstraint(String type){
		PreparedStatement st;
		ResultSet rs;
		float result;
		try{
			if(type.equals("Saving")){
				st = con.prepareStatement("select savingBalance from Account where accountNumber = ?");
				st.setString(1, accountNumber);
				System.out.println(accountNumber);
				rs = st.executeQuery();
				rs.next();
				result = rs.getFloat(1) - Float.parseFloat(amount.getText());
				if(result < 1000){
					return false;
				}
				else
					return true;
			}			
			
			else if(type.equals("Current")){
				st = con.prepareStatement("select currentBalance from Account where accountNumber = ?");
				st.setString(1, accountNumber);
				rs = st.executeQuery();
				rs.next();
				result = rs.getFloat(1) - Float.parseFloat(amount.getText());
				if(result < 1000){
					return false;
				}
				else
					return true;
			}
		}
		catch(Exception e){}
		return true;
	}
	@Override
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
				updateWithdraw(accountNumber);	
				try {
					PreparedStatement st = con.prepareStatement("select accountNumber from Account where accountNumber = ? ");
					st.setString(1, accountNumberText.getText());
					ResultSet rs = st.executeQuery();
					
					rs.next();
					if(!rs.wasNull()){
						updateDeposit(accountNumberText.getText());
					}
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				
				f.setVisible(false);
				new Menu(accountNumber);
			}				
		}
		
		if(a.getSource() == cancel){
			f.setVisible(false);
			new Menu(accountNumber);
		}
		
	}
}
