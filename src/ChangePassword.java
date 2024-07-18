import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Insets;

public class ChangePassword extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    int xx, xy;
    private JTextField textField;
    private JPasswordField passwordField;
    private String focus = "";
    private JPasswordField newPasswordField;
    private String userType;
  
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	String userType = " ";
                	ChangePassword frame = new ChangePassword(userType);
//                  frame.setUndecorated(true);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void numButton(JButton numButton, String btnNumber) {
        numButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println(focus);
                if (focus.equals("textField")) {
                    String userIdText = textField.getText();
                    userIdText += btnNumber;
                    textField.setText(userIdText);
                } else if (focus.equals("passwordField")) {
                    char[] passwordText = passwordField.getPassword();
                    String newPassword = new String(passwordText) + btnNumber;
                    passwordField.setText(newPassword);
                }
                else if (focus.equals("newPasswordField")) {
                    char[] passwordText = newPasswordField.getPassword();
                    String newPassword = new String(passwordText) + btnNumber;
                    newPasswordField.setText(newPassword);
                }
            }
        });
    
    }
    
    public String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            
            // Convert message digest into hex value
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ChangePassword(String userType) {
    	
    	this.userType = userType;
    			
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(250, 250, 646, 369);
        contentPane = new JPanel();

        // Event handlers to move the frame
        contentPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                ChangePassword.this.setLocation(x - xx, y - xy);
            }
        });
        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xx = e.getX();
                xy = e.getY();
            }
        });
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(310, 10));
        contentPane.add(panel, BorderLayout.EAST);
        panel.setLayout(null);
        
        JButton btn_7 = new JButton("7");
        btn_7.setFocusPainted(false);
        btn_7.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_7.setBackground(new Color(255, 255, 255));
        btn_7.setBounds(10, 48, 88, 56);
        panel.add(btn_7);
        
        JButton btn_8 = new JButton("8");
        btn_8.setFocusPainted(false);
        btn_8.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_8.setBackground(new Color(255, 255, 255));
        btn_8.setBounds(104, 48, 88, 56);
        panel.add(btn_8);
        
        JButton btn_9 = new JButton("9");
        btn_9.setFocusPainted(false);
        btn_9.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_9.setBackground(new Color(255, 255, 255));
        btn_9.setBounds(198, 48, 88, 56);
        panel.add(btn_9);
        
        JButton btn_4 = new JButton("4");
        btn_4.setFocusPainted(false);
        btn_4.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_4.setBackground(new Color(255, 255, 255));
        btn_4.setBounds(10, 110, 88, 56);
        panel.add(btn_4);
        
        JButton btn_5 = new JButton("5");
        btn_5.setFocusPainted(false);
        btn_5.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_5.setBackground(new Color(255, 255, 255));
        btn_5.setBounds(104, 110, 88, 56);
        panel.add(btn_5);
        
        JButton btn_6 = new JButton("6");
        btn_6.setFocusPainted(false);
        btn_6.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_6.setBackground(new Color(255, 255, 255));
        btn_6.setBounds(198, 110, 88, 56);
        panel.add(btn_6);
        
        JButton btn_1 = new JButton("1");
        btn_1.setFocusPainted(false);
        btn_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_1.setBackground(new Color(255, 255, 255));
        btn_1.setBounds(10, 172, 88, 56);
        panel.add(btn_1);
        
        JButton btn_2 = new JButton("2");
        btn_2.setFocusPainted(false);
        btn_2.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_2.setBackground(new Color(255, 255, 255));
        btn_2.setBounds(104, 172, 88, 56);
        panel.add(btn_2);
        
        JButton btn_3 = new JButton("3");
        btn_3.setFocusPainted(false);
        btn_3.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_3.setBackground(new Color(255, 255, 255));
        btn_3.setBounds(198, 172, 88, 56);
        panel.add(btn_3);
        
        JButton changePass_btn = new JButton("<html><div style='text-align: center;'>Change<br>Password</div></html>\r\n");
        changePass_btn.setFocusPainted(false);
        changePass_btn.setFont(new Font("Tahoma", Font.BOLD, 11));
        changePass_btn.setBackground(new Color(255, 255, 255));
        changePass_btn.setBounds(10, 234, 88, 56);
        panel.add(changePass_btn);
        
        JButton btn_0 = new JButton("0");
        btn_0.setFocusPainted(false);
        btn_0.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_0.setBackground(new Color(255, 255, 255));
        btn_0.setBounds(104, 235, 88, 56);
        panel.add(btn_0);
        
        JButton clear_btn = new JButton("Clear");
        clear_btn.setFocusPainted(false);
        clear_btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        clear_btn.setBackground(new Color(255, 255, 255));
        clear_btn.setBounds(198, 235, 88, 56);
        panel.add(clear_btn);
        
        JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(310, 10));
        contentPane.add(panel_1, BorderLayout.WEST);
        panel_1.setLayout(null);
        
        textField = new JTextField();
        textField.setMargin(new Insets(4, 4, 4, 4));
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {          	
            	focus = "textField";
                System.out.println(focus);
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("textField lost focus.");
               
            }
        });



        textField.setBounds(10, 124, 280, 38);
        panel_1.add(textField);
        textField.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("Change Password");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(61, 24, 187, 38);
        panel_1.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("User ID");
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel_1.setBounds(10, 110, 102, 14);
        panel_1.add(lblNewLabel_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel_1_1.setBounds(10, 174, 102, 14);
        panel_1.add(lblNewLabel_1_1);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        passwordField.setMargin(new Insets(4, 4, 4, 4));
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            	focus = "passwordField";
            	 System.out.println(focus);
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("textField lost focus.");
               
            }
        });
        passwordField.setBounds(10, 187, 280, 38);
        panel_1.add(passwordField);
        
        JLabel lblNewLabel_1_2 = new JLabel("<html><div style='text-align: center;'>Please enter your user id,<br>password, and your new password</div></html>");
        lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1_2.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNewLabel_1_2.setBounds(10, 57, 290, 31);
        panel_1.add(lblNewLabel_1_2);
        
        newPasswordField = new JPasswordField();
        newPasswordField.setMargin(new Insets(4, 4, 4, 4));
        newPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        newPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {          	
            	focus = "newPasswordField";
                System.out.println(focus);
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("newPasswordField lost focus.");
               
            }
        });
        newPasswordField.setBounds(10, 250, 280, 38);
        panel_1.add(newPasswordField);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("New Password");
        lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel_1_1_1.setBounds(10, 236, 102, 14);
        panel_1.add(lblNewLabel_1_1_1);
        
        numButton(btn_0, "0");   
        numButton(btn_1, "1");   
        numButton(btn_2, "2");     
        numButton(btn_3, "3");     
        numButton(btn_4, "4");     
        numButton(btn_5, "5");     
        numButton(btn_6, "6"); 
        numButton(btn_7, "7");     
        numButton(btn_8, "8");     
        numButton(btn_9, "9");     
        
        clear_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (focus.equals("textField")) {
                    String currentText = textField.getText();
                    if (currentText.length() > 0) {
                        String modifiedText = currentText.substring(0, currentText.length() - 1);
                        textField.setText(modifiedText);
                    }
                } 
                else if (focus.equals("passwordField")) {
                    String currentText = new String(passwordField.getPassword());
                    if (currentText.length() > 0) {
                        String modifiedText = currentText.substring(0, currentText.length() - 1);
                        passwordField.setText(modifiedText);
                    }                 
                }
                else if (focus.equals("newPasswordField")) {
                    String currentText = new String(newPasswordField.getPassword());
                    if (currentText.length() > 0) {
                        String modifiedText = currentText.substring(0, currentText.length() - 1);
                        newPasswordField.setText(modifiedText);
                    }
                }
           }
        });
        
        changePass_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(textField.getText().trim().isEmpty() || passwordField.getPassword().length == 0 || newPasswordField.getPassword().length == 0){
		    		JOptionPane.showMessageDialog(ChangePassword.this, "Please fill out all fields.", "Fill Out All Fields", JOptionPane.WARNING_MESSAGE);
		    	}
		    	else if(newPasswordField.getPassword().length < 8) {
		    		JOptionPane.showMessageDialog(ChangePassword.this, "The new password must be at least 8 characters long.", "Invalid Password Length", JOptionPane.WARNING_MESSAGE);
		    	}
            	
		    	else {
		    		String userId = textField.getText();
	                int userIdInt = Integer.parseInt(userId);               
	                String currentPass = getMD5Hash(new String(passwordField.getPassword()));
	                String newPass = getMD5Hash(new String(newPasswordField.getPassword()));
	                
	                ResultSet rs = null;
	                try { 
	                	
	                	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
	                	
	                	// Check user credentials
	                	if(userType == "cashier") {
	                		String cashierTbQuery = "SELECT * FROM `users_tb` AS utb " +
	                                "INNER JOIN `cashier_tb` AS ctb ON utb.user_id = ctb.user_id " +
	                                "WHERE utb.user_id = ? AND utb.password = ?";
	                		
	                		 PreparedStatement ps = con.prepareStatement(cashierTbQuery);
	                		  ps.setInt(1, userIdInt);
	                          ps.setString(2, currentPass);
	                     
	                          rs = ps.executeQuery();
	                	}
	                	if(userType == "admin") {
	                		String cashierTbQuery = "SELECT * FROM `users_tb` AS utb " +
	                                "INNER JOIN `admin_tb` AS atb ON utb.user_id = atb.user_id " +
	                                "WHERE utb.user_id = ? AND utb.password = ?";
	                		
	                		 PreparedStatement ps = con.prepareStatement(cashierTbQuery);
	                		  ps.setInt(1, userIdInt);
	                          ps.setString(2, currentPass);
	                          rs = ps.executeQuery();
	                	}   

	                    if (rs.next()) { 
	                        // Update password if current password matches
	                    	String newPassIsEmpty = new String(newPasswordField.getPassword());
	                    	if(newPassIsEmpty.isEmpty()) {
	                    		JOptionPane.showMessageDialog(ChangePassword.this, "Please enter new password!", "Password Updated", JOptionPane.WARNING_MESSAGE);                    		                  		
	                    	}
	                    	else {
	                    		int answer = JOptionPane.showConfirmDialog(ChangePassword.this, "Confirm change password?", "Change Password", JOptionPane.OK_CANCEL_OPTION);
	                    		int rowsUpdated;
	            				if(answer == 0) {
	            						 String updatePass = "UPDATE users_tb SET password = ? WHERE user_id = ?";
	                                     PreparedStatement psUpdate = con.prepareStatement(updatePass);
	                                     psUpdate.setString(1, newPass);
	                                     psUpdate.setInt(2, userIdInt);
	                                     rowsUpdated = psUpdate.executeUpdate();
	                                     
	                                     if (rowsUpdated > 0) {
	                                     	JOptionPane.showMessageDialog(ChangePassword.this, "Password updated successfully!", "Password Updated", JOptionPane.INFORMATION_MESSAGE);
	                          				                				
	                      					 ChangePassword.this.dispose();
	                                         LoginForm loginForm = new LoginForm();
	                                         loginForm.setLocationRelativeTo(null);
	                                         loginForm.setVisible(true);
	                                         
	                                         System.out.println("Password Updated successfully for user_id: " + userIdInt);
	                          				                     
	                                      } else {
	                                          System.out.println("Failed to update password for user_id: " + userIdInt);
	                                      }           					           					                                     
	            				}
	                    	}
	                    	       
	                    } else {
	                        JOptionPane.showMessageDialog(ChangePassword.this, "Incorrect user id or password", "Password Update Failed", JOptionPane.ERROR_MESSAGE);
	                    }
	                    
	                    con.close();
	                    
	                } catch (SQLException ee) {
	                    JOptionPane.showMessageDialog(ChangePassword.this, "Failed to connect to database", "Database Error", JOptionPane.ERROR_MESSAGE);
	                    ee.printStackTrace();
	                }
		    	}          	
                
            }
        });

       
       
    }
}
