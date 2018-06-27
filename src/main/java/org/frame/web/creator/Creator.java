package org.frame.web.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.frame.common.lang.StringHelper;
import org.frame.common.util.Properties;
import org.frame.repository.constant.IRepositoryConstant;
import org.frame.repository.sql.jdbc.IJDBC;
import org.frame.repository.sql.jdbc.impl.JDBC;
import org.frame.web.creator.java.IJava;
import org.frame.web.creator.java.impl.Java;
import org.frame.web.creator.jsp.IJsp;
import org.frame.web.creator.jsp.impl.Jsp;

public class Creator {
	
	JComboBox<String> cmb_database = new JComboBox<String>();
	JComboBox<String> cmb_dao = new JComboBox<String>();
	   
	JTextField txt_classname = new JTextField(40);
	JTextField txt_url = new JTextField(40);
	JTextField txt_username = new JTextField(40);
	JTextField txt_password = new JTextField(40);
	
	JList<String> left = new JList<String>();
	JList<String> right = new JList<String>();
	
	DefaultListModel<String> model = new DefaultListModel<String>();
	DefaultListModel<String> leftModel = new DefaultListModel<String>();
	DefaultListModel<String> rightModel = new DefaultListModel<String>();
	
	JTextField txt_package = new JTextField(20);
	JTextField txt_output = new JTextField(20);

	JCheckBox chk_controller = new JCheckBox();
	JCheckBox chk_dao = new JCheckBox();
	JCheckBox chk_model = new JCheckBox();
	JCheckBox chk_service = new JCheckBox();
	JCheckBox chk_jsp = new JCheckBox();
	
	JCheckBox chk_baseDao = new JCheckBox();
	JCheckBox chk_removePrefix = new JCheckBox();
	
	IJDBC dao;
	
	private JPanel init() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());
		
		JPanel database = new JPanel();
		database.setLayout(new GridBagLayout());
		
		TitledBorder titledBorder = new TitledBorder("Database: ");
	    titledBorder.setTitleColor(Color.blue);
	    
	    JLabel lab_database = new JLabel();
	    JLabel lab_classname = new JLabel();
		JLabel lab_url = new JLabel();
		JLabel lab_username = new JLabel();
		JLabel lab_password = new JLabel();
		
		JButton btn_link = new JButton();
		btn_link.setText("link");
		btn_link.addActionListener(new Link_Button_Listenser());
		
		JButton btn_allRight = new JButton();
		btn_allRight.setText(">>");
		btn_allRight.addActionListener(new AllRight_Button_Listenser());
		
		JButton btn_right = new JButton();
		btn_right.setText(">");
		btn_right.addActionListener(new Right_Button_Listenser());
		
		JButton btn_left = new JButton();
		btn_left.setText("<");
		btn_left.addActionListener(new Left_Button_Listenser());
		
		JButton btn_allLeft = new JButton();
		btn_allLeft.setText("<<");
		btn_allLeft.addActionListener(new AllLeft_Button_Listenser());
		
		cmb_database.addItem("DB2");
		cmb_database.addItem("Informix");
		cmb_database.addItem("MySql");
		cmb_database.addItem("Oracle");
		cmb_database.addItem("PostgreSQL");
		cmb_database.addItem("SqlServer");
		cmb_database.addItem("Sybase");
		
		cmb_dao.addItem("Spring");
		cmb_dao.addItem("JDBC");
		cmb_dao.setEnabled(false);
		
		left.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		right.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		ScrollPane leftScrollPanel = new ScrollPane();
		leftScrollPanel.add(left);
		
		ScrollPane rightScrollPanel = new ScrollPane();
		rightScrollPanel.add(right);
		
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new GridBagLayout());
		selectPanel.add(btn_allRight, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(btn_right, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(btn_left, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(btn_allLeft, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		JPanel select = new JPanel();
		select.setLayout(new GridBagLayout());
		select.add(leftScrollPanel, new GridBagConstraints(0, 0, 1, 4, 8.0, 15.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		select.add(selectPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		select.add(rightScrollPanel, new GridBagConstraints(2, 0, 1, 4, 8.0, 15.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(btn_link);
		
		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		checkPanel.add(chk_controller);
		checkPanel.add(chk_dao);
		checkPanel.add(chk_model);
		checkPanel.add(chk_service);
		checkPanel.add(chk_jsp);
		
		JPanel prefixPanel = new JPanel();
		prefixPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		prefixPanel.add(chk_removePrefix);
		
		JPanel baseDaoPanel = new JPanel();
		baseDaoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		baseDaoPanel.add(chk_baseDao);
		baseDaoPanel.add(cmb_dao);
		
		lab_database.setText("database: ");
		lab_classname.setText("classname: ");
		lab_url.setText("database URL: ");
		lab_username.setText("username: ");
		lab_password.setText("password: ");
		
		database.add(lab_database, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		database.add(cmb_database, new GridBagConstraints(1, 0, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(lab_classname, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		database.add(txt_classname, new GridBagConstraints(1, 1, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(lab_url, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		database.add(txt_url, new GridBagConstraints(1, 2, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(lab_username, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		database.add(txt_username, new GridBagConstraints(1, 3, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(lab_password, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		database.add(txt_password, new GridBagConstraints(1, 4, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(buttonPanel, new GridBagConstraints(0, 5, 2, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.add(select, new GridBagConstraints(0, 6, 2, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		database.setBorder(titledBorder);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JLabel lab_package = new JLabel();
		JLabel lab_output = new JLabel();
		JLabel lab_include = new JLabel();
		
		JLabel lab_baseDao = new JLabel();
		JLabel lab_removePrefix = new JLabel();
		
		lab_package.setText("package: ");
		lab_output.setText("output path: ");
		lab_include.setText("create: ");
		//lab_baseDao.setText("use Spring: ");
		//lab_removePrefix.setText("remove prefix: ");
		
		chk_controller.setText("Controller");
		chk_dao.setText("DAO");
		chk_model.setText("Model");
		chk_service.setText("Service");
		chk_jsp.setText("JSP");
		
		chk_removePrefix.setText("remove prefix");
		chk_baseDao.setText("BaseDAO");
		chk_baseDao.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent item) {
				cmb_dao.setEnabled(chk_baseDao.isSelected());
			}
		});
		
		chk_controller.setSelected(true);
		chk_dao.setSelected(true);
		chk_model.setSelected(true);
		chk_service.setSelected(true);
		chk_jsp.setSelected(true);
		
		chk_removePrefix.setSelected(true);
		chk_baseDao.setSelected(false);
		
		JButton btn_submit = new JButton();
		btn_submit.setText("OK");
		btn_submit.addActionListener(new Submit_Button_Listenser());
		
		JPanel submitButtonPanel = new JPanel();
		submitButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		submitButtonPanel.add(btn_submit);
		
		panel.add(lab_package, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		panel.add(txt_package, new GridBagConstraints(1, 0, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		panel.add(lab_output, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		panel.add(txt_output, new GridBagConstraints(1, 1, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		panel.add(lab_include, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		panel.add(checkPanel, new GridBagConstraints(1, 2, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		panel.add(lab_removePrefix, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		panel.add(prefixPanel, new GridBagConstraints(1, 3, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		panel.add(lab_baseDao, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		panel.add(baseDaoPanel, new GridBagConstraints(1, 4, 1, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		panel.add(submitButtonPanel, new GridBagConstraints(0, 5, 2, 1, 5.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	
		result.add(database, BorderLayout.NORTH);
		result.add(panel, BorderLayout.SOUTH);
		
		return result;
	}
	
	private void getDefaultConfig() {
		Properties properties = new Properties(IRepositoryConstant.DEFAULT_CONFIG_PROPERTIES);
		String database = properties.read(IRepositoryConstant.SYSTEM_DATABASE);
		if (!StringHelper.isNull(database)) {
			DefaultComboBoxModel<String> comboxModel = (DefaultComboBoxModel<String>) cmb_database.getModel();
			for (int i = 0; i < comboxModel.getSize(); i++) {
				if (comboxModel.getElementAt(i).toLowerCase().equals(database.toLowerCase())) {
					comboxModel.setSelectedItem(comboxModel.getElementAt(i));
				}
			}
		}
		
		txt_classname.setText(properties.read(IRepositoryConstant.DATASOURCE_CLASSNAME));
		txt_url.setText(properties.read(IRepositoryConstant.DATASOURCE_URL));
		txt_username.setText(properties.read(IRepositoryConstant.DATASOURCE_USERNAME));
		txt_password.setText(properties.read(IRepositoryConstant.DATASOURCE_PASSWORD));
		
		txt_output.setText("d:/" + properties.read(IRepositoryConstant.SYSTEM_NAME));
		txt_package.setText("com." + properties.read(IRepositoryConstant.SYSTEM_NAME));
	}
	
	@SuppressWarnings("unused")
	private void setDefaultConfig() {
		Properties properties = new Properties(IRepositoryConstant.DEFAULT_CONFIG_PROPERTIES);
		properties.write(IRepositoryConstant.SYSTEM_DATABASE, String.valueOf(cmb_database.getSelectedItem()));
		properties.write(IRepositoryConstant.DATASOURCE_CLASSNAME, txt_classname.getText().trim());
		properties.write(IRepositoryConstant.DATASOURCE_URL, txt_url.getText().trim());
		properties.write(IRepositoryConstant.DATASOURCE_USERNAME, txt_username.getText().trim());
		properties.write(IRepositoryConstant.DATASOURCE_PASSWORD, txt_password.getText().trim());
	}
	
	private void show() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1024, 768));
		frame.setLayout(new BorderLayout());
		
		frame.getContentPane().add(this.init(), BorderLayout.CENTER);
		frame.pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int left = (screen.width - frame.getWidth()) / 2;
		int top = (screen.height - frame.getHeight()) / 2;

		this.getDefaultConfig();
		
		frame.setLocation(left, top);
		frame.setTitle("Framework Code Helper");
		frame.setVisible(true);
	}
	
	class AllLeft_Button_Listenser implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			leftModel.removeAllElements();
			
			for (int i = 0; i < model.size(); i++) {
				leftModel.addElement(model.get(i));
			}
			
			rightModel.removeAllElements();
			
			right.updateUI();
			left.updateUI();
		}
		
	}
	
	class AllRight_Button_Listenser implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			rightModel.removeAllElements();
			
			for (int i = 0; i < model.size(); i++) {
				rightModel.addElement(model.get(i));
			}
			
			leftModel.removeAllElements();
			
			right.updateUI();
			left.updateUI();
		}
		
	}
	
	class Left_Button_Listenser implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> selected = right.getSelectedValuesList();
			
			if (selected.size() < 1) {
				selected = new ArrayList<String>();
			}
			
			for (int i = 0; i < leftModel.size(); i++) {
				selected.add(leftModel.get(i));
			}

			for (String delete : selected) {
				rightModel.removeElement(delete);
			}
			
			leftModel.removeAllElements();
			
			for (int i = 0; i < model.size(); i++) {
				if (selected.contains(model.get(i))) {
					leftModel.addElement(model.get(i));
				}
			}
			
			left.updateUI();
			right.updateUI();
		}
		
	}
	
	class Right_Button_Listenser implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> selected = left.getSelectedValuesList();
			
			if (selected.size() < 1) {
				selected = new ArrayList<String>();
			}
			
			for (int i = 0; i < rightModel.size(); i++) {
				selected.add(rightModel.get(i));
			}

			for (String delete : selected) {
				leftModel.removeElement(delete);
			}
			
			rightModel.removeAllElements();
			
			for (int i = 0; i < model.size(); i++) {
				if (selected.contains(model.get(i))) {
					rightModel.addElement(model.get(i));
				}
			}
			
			left.updateUI();
			right.updateUI();
		}
		
	}
	
	class Link_Button_Listenser implements ActionListener {
		
		@Override
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			try {
				String databaseType = String.valueOf(cmb_database.getSelectedItem()).toLowerCase();
				String className = txt_classname.getText().trim();
				String url = txt_url.getText().trim();
				String username = txt_username.getText().trim();
				String password = txt_password.getText().trim();

				if (StringHelper.isNull(databaseType) && StringHelper.isNull(className) && StringHelper.isNull(url) && StringHelper.isNull(username) && StringHelper.isNull(password)) {
					Properties properties = new Properties(IRepositoryConstant.DEFAULT_CONFIG_PROPERTIES);
					databaseType = properties.read(IRepositoryConstant.SYSTEM_DATABASE);
					className = properties.read(IRepositoryConstant.DATASOURCE_CLASSNAME);
					url = properties.read(IRepositoryConstant.DATASOURCE_URL);
					username = properties.read(IRepositoryConstant.DATASOURCE_USERNAME);
					password = properties.read(IRepositoryConstant.DATASOURCE_PASSWORD);
				}

				if (StringHelper.isNull(className) || StringHelper.isNull(url) || StringHelper.isNull(username) || StringHelper.isNull(password)) {
					JOptionPane.showMessageDialog(null, "database infomation invalid", "failed", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//setDefaultConfig();

				dao = new JDBC(className, url, username, password);
				if (dao.getDatabase() != null) {
					String sql = dao.getDatabase().tables();
					List<?> data = dao.select(sql);

					model.removeAllElements();
					leftModel.removeAllElements();
					rightModel.removeAllElements();

					for (int i = 0; i < data.size(); i++) {
						Collection<?> c = ((Map<String, Object>) data.get(i)).values();
						for (Object table : c) {
							model.addElement(String.valueOf(table));
							leftModel.addElement(String.valueOf(table));
						}
					}

					left.setModel(leftModel);
					left.updateUI();

					right.setModel(rightModel);
					right.updateUI();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "can not link to database", "failed", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	class Submit_Button_Listenser implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String pack = txt_package.getText().trim();
			String output = txt_output.getText().trim();
			
			String schema = txt_url.getText().trim();
			if (schema.indexOf("?") != -1) {
				schema = schema.substring(0, schema.indexOf("?"));
				schema = schema.substring(schema.lastIndexOf("/") + 1, schema.length());
			}
			
			List<String> selected = new ArrayList<String>();
			for (int i = 0; i < rightModel.size(); i++) {
				selected.add(rightModel.get(i));
			}
			
			if (selected.size() > 0) {
				String daoType;
				if (chk_baseDao.isSelected()) {
					daoType = String.valueOf(cmb_dao.getSelectedItem()).toLowerCase();
				} else {
					daoType = null;
				}
				
				IJava java = new Java(dao, pack, output, chk_removePrefix.isSelected(), daoType);
				java.create(schema, selected, chk_controller.isSelected(), chk_dao.isSelected(), chk_model.isSelected(), chk_service.isSelected());
				
				if (chk_jsp.isSelected()) {
					IJsp jsp = new Jsp(dao, pack, output, chk_removePrefix.isSelected());
					jsp.create(schema, selected);
				}
				
				JOptionPane.showMessageDialog(null, "OK, now go and get your code!", "We are done", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "You have to select one table at least!", "We have problems", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public static void main(String[] args) {
		new Creator().show();
	}

}
