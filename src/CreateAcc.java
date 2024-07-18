import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class CreateAcc extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField firstName_field;
	private JTextField lastName_field;
	private String accType;
	private JLabel login_label;
	private JPasswordField password_field;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateAcc frame = new CreateAcc();
					frame.setVisible(true);
					 frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
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
	

	/**
	 * Create the frame.
	 */
	public CreateAcc() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 444);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setVisible(false);
		contentPane.add(panel, "name_712251013946700");
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Create Account");
		lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_1.setFont(new Font("Arial", Font.BOLD, 25));
		
		JLabel lblNewLabel_1_2_2 = new JLabel("Choose an account to create.");
		lblNewLabel_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2.setFont(new Font("Arial", Font.PLAIN, 14));
		
		JButton adminAcc_btn = new JButton("<html><div style='text-align: center;'>Admin Account<br></div></html>");		
		adminAcc_btn.setFont(new Font("Arial", Font.BOLD, 14));
		adminAcc_btn.setFocusPainted(false);
		adminAcc_btn.setBackground(Color.WHITE);
		
		JButton cashierAcc_btn = new JButton("<html><div style='text-align: center;'>Cashier Account<br></div></html>");
		cashierAcc_btn.setFont(new Font("Arial", Font.BOLD, 14));
		cashierAcc_btn.setFocusPainted(false);
		cashierAcc_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(15)
							.addComponent(lblNewLabel_1_2_1, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(84)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(adminAcc_btn, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
								.addComponent(cashierAcc_btn, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(68)
							.addComponent(lblNewLabel_1_2_2, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(44)
					.addComponent(lblNewLabel_1_2_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_1_2_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(88)
					.addComponent(adminAcc_btn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cashierAcc_btn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(120, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel create_panel = new JPanel();
		contentPane.add(create_panel, "name_712674005247300");
		
		login_label = new JLabel("Create Cashier Account");
		login_label.setHorizontalAlignment(SwingConstants.CENTER);
		login_label.setFont(new Font("Arial", Font.BOLD, 20));
		
		JLabel lblNewLabel_1_2 = new JLabel("Please enter the required information.");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Arial", Font.PLAIN, 12));
		
		firstName_field = new JTextField();
		firstName_field.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("First name:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 12));
		
		lastName_field = new JTextField();
		lastName_field.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Last name:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Password:");
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton createAcc_btn = new JButton("<html><div style='text-align: center;'>Create Account<br></div></html>");
		
		createAcc_btn.setFont(new Font("Arial", Font.BOLD, 14));
		createAcc_btn.setFocusPainted(false);
		createAcc_btn.setBackground(Color.WHITE);
		
		password_field = new JPasswordField();
		GroupLayout gl_create_panel = new GroupLayout(create_panel);
		gl_create_panel.setHorizontalGroup(
			gl_create_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_create_panel.createSequentialGroup()
					.addGroup(gl_create_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_create_panel.createSequentialGroup()
							.addGap(90)
							.addComponent(lblNewLabel_1_2, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_create_panel.createSequentialGroup()
							.addGap(96)
							.addComponent(login_label, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_create_panel.createSequentialGroup()
							.addGap(125)
							.addComponent(createAcc_btn, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_create_panel.createSequentialGroup()
							.addGap(60)
							.addGroup(gl_create_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
								.addComponent(firstName_field, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
								.addComponent(lastName_field, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
								.addComponent(password_field, Alignment.TRAILING))))
					.addContainerGap(60, Short.MAX_VALUE))
		);
		gl_create_panel.setVerticalGroup(
			gl_create_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_create_panel.createSequentialGroup()
					.addGap(29)
					.addComponent(login_label, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1_2)
					.addGap(29)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(firstName_field, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1_1)
					.addGap(5)
					.addComponent(lastName_field, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1_1_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(password_field, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(createAcc_btn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		create_panel.setLayout(gl_create_panel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "name_780105434665300");
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Account Created Succesfully!");
		lblNewLabel_1_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_1_1.setFont(new Font("Arial", Font.BOLD, 25));
		
		JLabel lblNewLabel_1_2_2_1 = new JLabel("Your account is now active. Your User ID is:");
		lblNewLabel_1_2_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2_1.setFont(new Font("Arial", Font.PLAIN, 14));
		
		JLabel user_label = new JLabel("0000");
		user_label.setHorizontalAlignment(SwingConstants.CENTER);
		user_label.setFont(new Font("Arial", Font.BOLD, 30));
		
		JButton login_btn = new JButton("<html><div style='text-align: center;'>Login<br></div></html>");
		login_btn.setFont(new Font("Arial", Font.BOLD, 14));
		login_btn.setFocusPainted(false);
		login_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(125)
							.addComponent(login_btn, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(15)
							.addComponent(lblNewLabel_1_2_1_1, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(59)
							.addComponent(user_label, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(68)
							.addComponent(lblNewLabel_1_2_2_1, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(89)
					.addComponent(lblNewLabel_1_2_1_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_1_2_2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(45)
					.addComponent(user_label, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(68)
					.addComponent(login_btn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(84, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		
		
		adminAcc_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accType = "admin account";
				panel.setVisible(false);
				create_panel.setVisible(true);
				panel_1.setVisible(false);
			}
		});
		
		cashierAcc_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accType = "cashier account";
				panel.setVisible(false);
				create_panel.setVisible(true);
				panel_1.setVisible(false);
			}
		});

		createAcc_btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	if(firstName_field.getText().trim().isEmpty() || lastName_field.getText().trim().isEmpty() || password_field.getPassword().length == 0){
		    		JOptionPane.showMessageDialog(CreateAcc.this, "Please enter the necessary information.", "Enter Required Information", JOptionPane.WARNING_MESSAGE);
		    	}
		    	else if(password_field.getPassword().length < 8) {
		    		JOptionPane.showMessageDialog(CreateAcc.this, "The password must be at least 8 characters long.", "Invalid Characters Length", JOptionPane.WARNING_MESSAGE);
		    	}
		    	else {
		    		String fName = firstName_field.getText();
			        String lName = lastName_field.getText();
			        String password = getMD5Hash(new String(password_field.getPassword()));
			        

			        String sql = "INSERT INTO users_tb (firstName, lastName, password) VALUES (?, ?, ?)";
			        ResultSet rs = null;

			        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
			             PreparedStatement pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			            
			            // Set the values for the placeholders
			            pst.setString(1, fName);
			            pst.setString(2, lName);
			            pst.setString(3, password);
			            
			            // Execute the INSERT statement
			            int affectedRows = pst.executeUpdate();

			            if (affectedRows > 0) {
			                // Retrieve the generated keys
			                rs = pst.getGeneratedKeys();
			                if (rs.next()) {
			                    int id = rs.getInt(1);
			                    System.out.println("ID: " + id);
			                    
			                    if (accType == "cashier account") {
			                        login_label.setText("Create Cashier Account");
			                        String sqlCashier = "INSERT INTO cashier_tb (user_id) VALUES (?)";
			                        ResultSet rsCashier = null;

			                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
			                             PreparedStatement pstmt = conn.prepareStatement(sqlCashier, PreparedStatement.RETURN_GENERATED_KEYS)) {
			                            
			                            // Set the values for the placeholders
			                            pstmt.setInt(1, id);		                         
			                            
			                            // Execute the INSERT statement
			                            int cashierAffectedRows = pstmt.executeUpdate();
			                            System.out.println("Cashier ID: " + id);
	                                    System.out.println("Account Created!");
	                                    
	                                    firstName_field.setText(" ");
	                                    lastName_field.setText(" ");
	                                    password_field.setText(" ");
	                                    panel.setVisible(false);
	                    				create_panel.setVisible(false);
	                    				panel_1.setVisible(true);
	                    				
	                    				user_label.setText(Integer.toString(id));
			                            
			                        } catch (SQLException ex) {
			                            ex.printStackTrace();
			                        } 
			                    }
			                    else if (accType == "admin account") {
			                        login_label.setText("Create Cashier Account");
			                        String sqlAdmin = "INSERT INTO admin_tb (user_id) VALUES (?)";
			                        ResultSet rsAdmin = null;

			                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
			                             PreparedStatement pstmt = conn.prepareStatement(sqlAdmin, PreparedStatement.RETURN_GENERATED_KEYS)) {
			                            
			                            // Set the values for the placeholders
			                            pstmt.setInt(1, id);	                          
			                            
			                            // Execute the INSERT statement
			                            int adminAffectedRows = pstmt.executeUpdate();
			                            if (adminAffectedRows > 0) {
			                                // Retrieve the generated keys
			                            	rsAdmin = pstmt.getGeneratedKeys();
			                            	System.out.println("Admin ID: " + id);
		                                    System.out.println("Account Created!");
		                                    
		                                    firstName_field.setText(" ");
		                                    lastName_field.setText(" ");
		                                    password_field.setText(" ");
		                                    
		                                    panel.setVisible(false);
		                    				create_panel.setVisible(false);
		                    				panel_1.setVisible(true);
		                    				
		                    				user_label.setText(Integer.toString(id));
			                            }
			                            
			                        } catch (SQLException ex) {
			                            ex.printStackTrace();
			                        } 
			                    }
			                }
			            }
			            
			            System.out.println("Insertion successful!");

			        } catch (SQLException ex) {
			            ex.printStackTrace();
			        } finally {
			            if (rs != null) {
			                try {
			                    rs.close();
			                } catch (SQLException ex) {
			                    ex.printStackTrace();
			                }
			            }
			        }
		    	}
		        
		    }
		});

		
		login_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(CreateAcc.this, "Make sure to keep a copy of your User ID before you log in!", "Save User ID", JOptionPane.OK_CANCEL_OPTION);
				
				if(answer == 0){
					CreateAcc.this.dispose();
                    LoginForm loginForm = new LoginForm();
                    loginForm.setLocationRelativeTo(null);
                    loginForm.setVisible(true);
				}
			}			
		});
		
		
	}
}
