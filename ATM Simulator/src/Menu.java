import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Menu extends JFrame implements ActionListener{
	JButton checkBalance, transferFund, withdraw, deposit, changePin, orderSlip, miniStatement, exit;
	JFrame f;
	String accountNumber;
	
	
	Menu(String accountNumber){
		
		this.accountNumber  = accountNumber;
		
		if(true){
			f = new JFrame();
			f.setLayout(null);
			f.setSize(getMaximumSize());
			f.setVisible(true);
			
			checkBalance = new JButton("CHECK BALANCE");
			transferFund = new JButton("TRANSFER MONEY");
			withdraw = new JButton("WITHDRAW");
			deposit = new JButton("DEPOSIT");
			changePin = new JButton("CHANGE PIN");
			orderSlip = new JButton("ORDER CHEQUE BOOK");
			miniStatement = new JButton("MINI STATEMENT");
			exit = new JButton("EXIT");
		
			checkBalance.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			transferFund.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			withdraw.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			deposit.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			changePin.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			orderSlip.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			miniStatement.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			exit.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			
			
			checkBalance.setBounds(0,100,350,75);
			withdraw.setBounds(0,275,350,75);
			changePin.setBounds(0,450,350,75);
			miniStatement.setBounds(0,625,350,75);
			transferFund.setBounds(966,100,350,75);
			deposit.setBounds(966,275,350,75);
			orderSlip.setBounds(966,450,350,75);
			exit.setBounds(966,625,350,75);			
					
			f.add(checkBalance);
			f.add(withdraw);
			f.add(changePin);
			f.add(miniStatement);
			f.add(transferFund);
			f.add(deposit);
			f.add(orderSlip);
			f.add(exit);
			
			checkBalance.addActionListener(this);
			withdraw.addActionListener(this);
			changePin.addActionListener(this);
			miniStatement.addActionListener(this);
			transferFund.addActionListener(this);
			deposit.addActionListener(this);
			orderSlip.addActionListener(this);
			exit.addActionListener(this);		
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == checkBalance){
			f.setVisible(false);
			new CheckBalance(accountNumber);
		}
		
		if(a.getSource() == transferFund){
			f.setVisible(false);
			new Transfer(accountNumber);
		}
		
		if(a.getSource() == withdraw){
			f.setVisible(false);
			new Withdraw(accountNumber);
		}
		
		if(a.getSource() == deposit){
			f.setVisible(false);
			new Deposit(accountNumber);
		}
		
		if(a.getSource() == changePin){
			f.setVisible(false);
			new ChangePin(accountNumber);
		}
		
		if(a.getSource() == orderSlip){
			f.setVisible(false);
			new OrderSlip(accountNumber);
		}
		
		if(a.getSource() == miniStatement){			
			f.setVisible(false);
			new Statement(accountNumber);
		}
		
		if(a.getSource() == exit){
			f.setVisible(false);
			new login();
			
		}
	}
}
