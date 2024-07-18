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
import java.awt.Window;
import java.awt.Insets;
import java.awt.CardLayout;
public class LoginForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    int xx, xy;
    private String focus = "";
    private JTextField textField;
    private JPasswordField passwordField;
    private String userType = "";
    private String table = "";
    private String idType = "";
    
    private JPanel first_panel;
    private JLabel login_label;
  
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginForm frame = new LoginForm();
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
    
     
    public LoginForm() {
    	
    	// Establish connection    	    	
    			
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(250, 250, 668, 393);
        contentPane = new JPanel();

        // Event handlers to move the frame
        contentPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                LoginForm.this.setLocation(x - xx, y - xy);
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
        contentPane.setLayout(new CardLayout(0, 0));
        
        JPanel login_panel = new JPanel();
        login_panel.setVisible(false);
        contentPane.add(login_panel, "name_614214030784800");
        
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setPreferredSize(new Dimension(310, 10));
        
        JButton btn_7 = new JButton("7");
        btn_7.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_7.setFocusPainted(false);
        btn_7.setBackground(Color.WHITE);
        btn_7.setBounds(14, 35, 88, 56);
        panel_1.add(btn_7);
        
        JButton btn_8 = new JButton("8");
        btn_8.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_8.setFocusPainted(false);
        btn_8.setBackground(Color.WHITE);
        btn_8.setBounds(108, 35, 88, 56);
        panel_1.add(btn_8);
        
        JButton btn_9 = new JButton("9");
        btn_9.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_9.setFocusPainted(false);
        btn_9.setBackground(Color.WHITE);
        btn_9.setBounds(202, 35, 88, 56);
        panel_1.add(btn_9);
        
        JButton btn_4 = new JButton("4");
        btn_4.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_4.setFocusPainted(false);
        btn_4.setBackground(Color.WHITE);
        btn_4.setBounds(14, 97, 88, 56);
        panel_1.add(btn_4);
        
        JButton btn_5 = new JButton("5");
        btn_5.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_5.setFocusPainted(false);
        btn_5.setBackground(Color.WHITE);
        btn_5.setBounds(108, 97, 88, 56);
        panel_1.add(btn_5);
        
        JButton btn_6 = new JButton("6");
        btn_6.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_6.setFocusPainted(false);
        btn_6.setBackground(Color.WHITE);
        btn_6.setBounds(202, 97, 88, 56);
        panel_1.add(btn_6);
        
        JButton btn_1 = new JButton("1");
        btn_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_1.setFocusPainted(false);
        btn_1.setBackground(Color.WHITE);
        btn_1.setBounds(14, 159, 88, 56);
        panel_1.add(btn_1);
        
        JButton btn_2 = new JButton("2");
        btn_2.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_2.setFocusPainted(false);
        btn_2.setBackground(Color.WHITE);
        btn_2.setBounds(108, 159, 88, 56);
        panel_1.add(btn_2);
        
        JButton btn_3 = new JButton("3");
        btn_3.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_3.setFocusPainted(false);
        btn_3.setBackground(Color.WHITE);
        btn_3.setBounds(202, 159, 88, 56);
        panel_1.add(btn_3);
        
        JButton login_btn = new JButton("<html><div style='text-align: center;'>Login<br></div></html>");
        login_btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        login_btn.setFocusPainted(false);
        login_btn.setBackground(Color.WHITE);
        login_btn.setBounds(14, 221, 88, 56);
        panel_1.add(login_btn);
        
        JButton btn_0 = new JButton("0");
        btn_0.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn_0.setFocusPainted(false);
        btn_0.setBackground(Color.WHITE);
        btn_0.setBounds(108, 222, 88, 56);
        panel_1.add(btn_0);
        
        JButton clear_btn = new JButton("Clear");
        clear_btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        clear_btn.setFocusPainted(false);
        clear_btn.setBackground(Color.WHITE);
        clear_btn.setBounds(202, 222, 88, 56);
        panel_1.add(clear_btn);
        
        JPanel panel_1_1 = new JPanel();
        panel_1_1.setLayout(null);
        panel_1_1.setPreferredSize(new Dimension(310, 10));
        
        textField = new JTextField();
        textField.setMargin(new Insets(4, 4, 4, 4));
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setColumns(10);
        textField.setBounds(20, 131, 280, 38);
        panel_1_1.add(textField);
        // Check if this field id selected
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
        
        login_label = new JLabel("Cashier Login");
        login_label.setHorizontalAlignment(SwingConstants.CENTER);
        login_label.setFont(new Font("Arial", Font.BOLD, 25));
        login_label.setBounds(71, 34, 175, 38);
        panel_1_1.add(login_label);
        
        JLabel lblNewLabel_1 = new JLabel("User ID");
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel_1.setBounds(20, 115, 102, 14);
        panel_1_1.add(lblNewLabel_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel_1_1.setBounds(20, 180, 102, 14);
        panel_1_1.add(lblNewLabel_1_1);
        
        passwordField = new JPasswordField();
        passwordField.setMargin(new Insets(4, 4, 4, 4));
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        passwordField.setBounds(20, 195, 280, 38);
        panel_1_1.add(passwordField);      
        // Check if this field id selected
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
        
        JLabel lblNewLabel_1_2 = new JLabel("Please enter your user id and password");
        lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1_2.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNewLabel_1_2.setBounds(23, 73, 272, 14);
        panel_1_1.add(lblNewLabel_1_2);
        GroupLayout gl_login_panel = new GroupLayout(login_panel);
        gl_login_panel.setHorizontalGroup(
        	gl_login_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_login_panel.createSequentialGroup()
        			.addComponent(panel_1_1, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(33, Short.MAX_VALUE))
        );
        gl_login_panel.setVerticalGroup(
        	gl_login_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_login_panel.createSequentialGroup()
        			.addGroup(gl_login_panel.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(panel_1_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
        			.addContainerGap(29, Short.MAX_VALUE))
        );
        login_panel.setLayout(gl_login_panel);
        
        first_panel = new JPanel();
        contentPane.add(first_panel, "name_614462070939000");
        
        JLabel lblNewLabel_1_2_1 = new JLabel("Welcome to Lomi Haus POS System");
        lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1_2_1.setFont(new Font("Arial", Font.BOLD, 25));
        
        JButton cashier_btn = new JButton("<html><div style='text-align: center;'>Cashier<br></div></html>");
        cashier_btn.setFont(new Font("Arial", Font.BOLD, 14));
        cashier_btn.setFocusPainted(false);
        cashier_btn.setBackground(Color.WHITE);
        
        JButton admin_btn = new JButton("<html><div style='text-align: center;'>Admin<br></div></html>");
        admin_btn.setFont(new Font("Arial", Font.BOLD, 14));
        admin_btn.setFocusPainted(false);
        admin_btn.setBackground(Color.WHITE);
        
        JLabel lblNewLabel_1_2_2 = new JLabel("Please select user type");
        lblNewLabel_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1_2_2.setFont(new Font("Arial", Font.PLAIN, 14));
        GroupLayout gl_first_panel = new GroupLayout(first_panel);
        gl_first_panel.setHorizontalGroup(
        	gl_first_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_first_panel.createSequentialGroup()
        			.addGap(102)
        			.addComponent(lblNewLabel_1_2_1, GroupLayout.PREFERRED_SIZE, 442, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(123, Short.MAX_VALUE))
        		.addGroup(gl_first_panel.createSequentialGroup()
        			.addGap(206)
        			.addGroup(gl_first_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_first_panel.createSequentialGroup()
        					.addComponent(lblNewLabel_1_2_2, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(gl_first_panel.createSequentialGroup()
        					.addGroup(gl_first_panel.createParallelGroup(Alignment.TRAILING)
        						.addComponent(cashier_btn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        						.addComponent(admin_btn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        					.addGap(205))))
        );
        gl_first_panel.setVerticalGroup(
        	gl_first_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_first_panel.createSequentialGroup()
        			.addGap(55)
        			.addComponent(lblNewLabel_1_2_1)
        			.addGap(18)
        			.addComponent(lblNewLabel_1_2_2)
        			.addGap(49)
        			.addComponent(cashier_btn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(admin_btn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(121, Short.MAX_VALUE))
        );
        first_panel.setLayout(gl_first_panel);
        
        first_panel.setVisible(true);
        
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
       
        cashier_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		userType = "cashier";
        		table = "cashier_tb";
        		idType = "cashier_id";
        		login_panel.setVisible(true);
        		first_panel.setVisible(false);
        	}
        });
        admin_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		userType = "admin";
        		table = "admin_tb";
        		idType = "admin_id";
        		login_panel.setVisible(true);
        		first_panel.setVisible(false);
        		login_label.setText("Admin Login");       		
        	}
        });
        
        clear_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (focus.equals("textField")) {
                    String currentText = textField.getText();
                    if (currentText.length() > 0) {
                        String modifiedText = currentText.substring(0, currentText.length() - 1);
                        textField.setText(modifiedText);
                    }
                } else if (focus.equals("passwordField")) {
                    String currentText = new String(passwordField.getPassword());
                    if (currentText.length() > 0) {
                        String modifiedText = currentText.substring(0, currentText.length() - 1);
                        passwordField.setText(modifiedText);
                    }
                }
            }
        });
        
        login_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(textField.getText().trim().isEmpty() || passwordField.getPassword().length == 0){
		    		JOptionPane.showMessageDialog(LoginForm.this, "Please enter user id and password.", "Enter User ID and Password", JOptionPane.WARNING_MESSAGE);
		    	}
            	else {
            		System.out.println(table);
            		System.out.println(idType);
                    String userId = textField.getText();
                    int userIdInt = Integer.parseInt(userId);                             
                    String pass = getMD5Hash(new String( passwordField.getPassword()));        
                    ResultSet rs = null;
                    try {
                        // Database connection     
                    	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
                    	
                    	if(userType == "cashier") {
                    		String cashierTbQuery = "SELECT * FROM `users_tb` AS utb " +
                                    "INNER JOIN `cashier_tb` AS ctb ON utb.user_id = ctb.user_id " +
                                    "WHERE utb.user_id = ? AND utb.password = ?";
                    		
                    		 PreparedStatement ps = con.prepareStatement(cashierTbQuery);
                    		  ps.setInt(1, userIdInt);
                              ps.setString(2, pass);
                         
                              rs = ps.executeQuery();
                    	}
                    	if(userType == "admin") {
                    		String cashierTbQuery = "SELECT * FROM `users_tb` AS utb " +
                                    "INNER JOIN `admin_tb` AS atb ON utb.user_id = atb.user_id " +
                                    "WHERE utb.user_id = ? AND utb.password = ?";
                    		
                    		 PreparedStatement ps = con.prepareStatement(cashierTbQuery);
                    		  ps.setInt(1, userIdInt);
                              ps.setString(2, pass);
                              rs = ps.executeQuery();
                    	}                	               	                                            
                        if (rs.next()) {    
                        	Window[] windows = Window.getWindows();
                            for (Window window : windows) {
                                if (window instanceof POS_system) {
                                    window.dispose();
                                }
                            }
                            
                            LoginForm.this.dispose();
                            POS_system pos = new POS_system(rs.getString("firstName"), rs.getInt(idType), userType, idType);                       
                            pos.setExtendedState(JFrame.MAXIMIZED_BOTH);                        
                            pos.setVisible(true);                     

                        } else {
                        	JOptionPane.showMessageDialog(LoginForm.this, "Incorrect user id or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);                       
                        }

                        con.close();

                    } catch (Exception ee) {
                        JOptionPane.showMessageDialog(LoginForm.this, "Failed to connect to database", "Failed database connection", JOptionPane.ERROR_MESSAGE);
                    }
            	}
            	
            }
        });
        
            
       
    }
    
    
    // Getters
    public JPanel getFirstPanel() {
        return first_panel;
    }
    public void setLabel() {
    	login_label.setText("Admin Login");       	
    }
    
    public void setUserType(String type) {
    	this.userType = type;       	
    }
    
    public void setIdType(String type) {
        this.idType = type;
    }
    
    public void setTable(String table) {
        this.table = table;
    }    
    
    public String getUserType() {
        return userType;
    }
}
