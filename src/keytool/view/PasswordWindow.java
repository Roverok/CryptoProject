package keytool.view;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

<<<<<<< HEAD
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle;


public class PasswordWindow extends JDialog {

	private static final long serialVersionUID = -4537433709703714282L;

	private javax.swing.JButton BtnCancel;
    private javax.swing.JButton BtnValidate;
    private javax.swing.JLabel LblPassword;
    private javax.swing.JPasswordField FldPassword;

    public PasswordWindow() {
    	initComponents();
    }
    
    private void initComponents() {
        FldPassword = new JPasswordField();
        LblPassword = new JLabel();
        BtnCancel = new JButton();
        BtnValidate = new JButton();

        this.setTitle("Mot de passe");
        LblPassword.setText("Mot de passe");

        BtnCancel.setText("Annuler");

        BtnValidate.setText("Valider");


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(FldPassword, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BtnCancel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(BtnValidate))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(LblPassword)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblPassword)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnCancel)
                    .addComponent(BtnValidate))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
    
=======
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PasswordWindow extends JFrame {
	private static final long serialVersionUID = 6203909852639047109L;

	private JButton BtnCancel;
	private JButton BtnValidate;
	private JLabel LblPassword;
	private JTextField FldPassword;


	public PasswordWindow() {
		initComponents();
	}
	
	private void initComponents() {
		LblPassword = new JLabel();
		FldPassword = new JTextField();
		BtnCancel = new JButton();
		BtnValidate = new JButton();

		setTitle("Mot de passe");
		getContentPane().setLayout(new java.awt.GridLayout(8, 2, 10, 10));

		LblPassword.setText("Mot de passe");
		getContentPane().add(LblPassword);
		getContentPane().add(FldPassword);
		
		BtnCancel.setText("Annuler");
		getContentPane().add(BtnCancel);
		BtnValidate.setText("Valider");
		getContentPane().add(BtnValidate);

		pack();
	}
	
>>>>>>> master
	public void resetField() {
		this.FldPassword.setText("");
	}
	
	public void addBtnCancelListener(ActionListener actLst) {
    	BtnCancel.addActionListener(actLst);
	}
	
	public void addBtnValidateListener(ActionListener actLst) {
    	BtnValidate.addActionListener(actLst);
	}
	
<<<<<<< HEAD
	public void addPasswordWindowListener(WindowListener wdLst) {
    	this.addWindowListener(wdLst);
	}
	
	public char[] getPasswordField() {
		return this.FldPassword.getPassword();
	}

=======
	public void addCWWindowListener(WindowListener wdLst) {
    	this.addWindowListener(wdLst);
	}
	
	public String getPasswordField() {
		return this.FldPassword.getText();
	}
>>>>>>> master
}
