import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.CardLayout;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class Discount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JTextField textField;
	private JTextField customerName_field;
	private JTextField customerAddress_field;
	private JTextField idNumber_field;
	
	private String customerName;
	private String customerAddress;
	private String idNumber;
	private int discountRate;
	
	private DiscountFrameListener listener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Discount frame = new Discount(new DiscountFrameListener() {
			            @Override
			            public void onDiscountEntered(String customerName, String customerAddress, String idNumber, int discountRate) {
			                System.out.println(customerName);
			            }
			        });
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	 private void applyDiscount() {
	        if (listener != null) {
	        	listener.onDiscountEntered(customerName, customerAddress, idNumber, discountRate);
	        }
	        dispose();
	    }

    private void addRow(String discountOption, String discountRate) {
        Object[] row = {discountOption, discountRate};
        model.addRow(row);
    }
	/**
	 * Create the frame.
	 */
    
    
	public Discount(DiscountFrameListener listener) {
		
		 this.listener = listener;
		 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 283);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel selectId_panel = new JPanel();
		selectId_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(selectId_panel, "name_786488751529000");
		selectId_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
		selectId_panel.add(scrollPane, BorderLayout.CENTER);
		
		String[] columnNames = {"Discount Option", "Discount Rate"};
		model = new DefaultTableModel(columnNames, 0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // Make all cells non-editable
		        return false;
		    }
		};
		
		table = new JTable(model);
		table.setFocusable(false);
		scrollPane.setViewportView(table);
		
		TableColumnModel dailySalesColumnModel = table.getColumnModel();
		
		  //Remove re sizable
        for (int i = 0; i < dailySalesColumnModel.getColumnCount(); i++) {
            TableColumn column = dailySalesColumnModel.getColumn(i);
            column.setResizable(false);
        }
		
        //Remove reordering
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		
		
		
		addRow("Senior Citizen ID", "20%");
        addRow("PWD ID", "20%");
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setPreferredSize(new Dimension(10, 50));
		selectId_panel.add(panel, BorderLayout.SOUTH);
		
		JButton cancel_btn = new JButton("Cancel");
		cancel_btn.setFocusable(false);
		cancel_btn.setFont(new Font("Arial", Font.BOLD, 12));
		cancel_btn.setMargin(new Insets(2, 2, 2, 2));
		cancel_btn.setBackground(Color.WHITE);
		
		JButton select_btn = new JButton("Select");
		select_btn.setFocusable(false);
		select_btn.setFont(new Font("Arial", Font.BOLD, 12));
		select_btn.setMargin(new Insets(2, 2, 2, 2));
		select_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(277, Short.MAX_VALUE)
					.addComponent(select_btn, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cancel_btn, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(21))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(select_btn)
						.addComponent(cancel_btn))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		//Set column width
	    columnModel.getColumn(0).setPreferredWidth(150);   
        columnModel.getColumn(1).setPreferredWidth(10);  

		
		JPanel idInfo_panel = new JPanel();
		idInfo_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(idInfo_panel, "name_786518397336400");
		idInfo_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
		idInfo_panel.add(panel_1, BorderLayout.CENTER);
		
		customerName_field = new JTextField();
		customerName_field.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Customer Name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		customerAddress_field = new JTextField();
		customerAddress_field.setColumns(10);
		
		idNumber_field = new JTextField();
		idNumber_field.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Address:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblNewLabel_1_1 = new JLabel("ID Number:");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel login_label = new JLabel("Enter the Customer's Details");
		login_label.setHorizontalAlignment(SwingConstants.CENTER);
		login_label.setFont(new Font("Arial", Font.BOLD, 18));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(customerAddress_field, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
								.addComponent(customerName_field, 274, 274, 274)
								.addComponent(idNumber_field, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(70)
							.addComponent(login_label, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(67, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(login_label, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(customerName_field, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(customerAddress_field, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(idNumber_field, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1_1))
					.addContainerGap(358, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(10, 50));
		panel_2.setBorder(null);
		idInfo_panel.add(panel_2, BorderLayout.SOUTH);
		
		JButton confirm_btn = new JButton("Confirm");
		confirm_btn.setFocusable(false);
		confirm_btn.setMargin(new Insets(2, 2, 2, 2));
		confirm_btn.setFont(new Font("Arial", Font.BOLD, 12));
		confirm_btn.setBackground(Color.WHITE);
		
		JButton cancel_btn_1 = new JButton("Cancel");
		cancel_btn_1.setFocusable(false);
		cancel_btn_1.setMargin(new Insets(2, 2, 2, 2));
		cancel_btn_1.setFont(new Font("Arial", Font.BOLD, 12));
		cancel_btn_1.setBackground(Color.WHITE);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 452, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(277, Short.MAX_VALUE)
					.addComponent(confirm_btn, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cancel_btn_1, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(21))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 50, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(confirm_btn)
						.addComponent(cancel_btn_1))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		textField = new JTextField();
		textField.setMargin(new Insets(4, 4, 4, 4));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setColumns(10);
		
		
		
		select_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Get selected row index
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) {
                    // Get data from model based on selected row
                    String discountOption = (String) model.getValueAt(selectedRowIndex, 0);
                    discountRate = Integer.parseInt(((String) model.getValueAt(selectedRowIndex, 1)).replaceAll("[^\\d.]", ""));
                    
                    selectId_panel.setVisible(false);
            		idInfo_panel.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(Discount.this, "Select discount option.");
                }
				
			}
		});
		
		confirm_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(customerName_field.getText().trim().isEmpty() || customerAddress_field.getText().trim().isEmpty() || idNumber_field.getText().trim().isEmpty()){
		    		JOptionPane.showMessageDialog(Discount.this, "Please enter the necessary information.", "Enter Required Information", JOptionPane.WARNING_MESSAGE);
		    	}
				else {
					customerName = customerName_field.getText();
					customerAddress = customerAddress_field.getText();
					idNumber = idNumber_field.getText();
					
//					Transaction transaction = new Transaction(customerName, customerAddress, idNumber, discountRate);
					
					applyDiscount();
					
					System.out.println(customerName);
					System.out.println(customerAddress);
					System.out.println(idNumber);
					
					dispose();
				}
				
			}
		});
		
		cancel_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		cancel_btn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
	}
}
