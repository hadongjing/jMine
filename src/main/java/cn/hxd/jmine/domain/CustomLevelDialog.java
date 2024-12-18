package cn.hxd.jmine.domain;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import lombok.Getter;

@Getter
public class CustomLevelDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	private int rows;
	
	private int cols;
	
	private int bombs;
	
	private boolean ok = false;
	
	public CustomLevelDialog(Frame parent, int rows, int cols, int bombs) {
		super(parent, "自定义级别", true);
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new GridLayout(4, 1));
		
		JPanel rowsPanel = new JPanel();
		rowsPanel.setLayout(new FlowLayout());
		JLabel rowsLabel = new JLabel("行数:");
		JTextField rowsField = new JTextField(this.rows + "",10);
		rowsPanel.add(rowsLabel);
		rowsPanel.add(rowsField);
		this.add(rowsPanel);
		
		
		JPanel colsPanel = new JPanel();
		colsPanel.setLayout(new FlowLayout());
		JLabel colsLabel = new JLabel("列数:");
		JTextField colsField = new JTextField(this.cols + "",10);
		colsPanel.add(colsLabel);
		colsPanel.add(colsField);
		this.add(colsPanel);
		
		JPanel bombsPanel = new JPanel();
		bombsPanel.setLayout(new FlowLayout());
		JLabel bombsLabel = new JLabel("雷数:");
		JTextField bombsField = new JTextField(this.bombs + "",10);
		bombsPanel.add(bombsLabel);
		bombsPanel.add(bombsField);
		this.add(bombsPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(buttonPanel);
		
		JButton okButton = new JButton("确定");
		okButton.addActionListener(e -> {
			try {
				rows = Integer.parseInt(rowsField.getText());
				cols = Integer.parseInt(colsField.getText());
				bombs = Integer.parseInt(bombsField.getText());
				this.dispose();
				ok = true;
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "请输入正确的数字", "输入错误", JOptionPane.ERROR_MESSAGE);
			}
		});
		buttonPanel.add(okButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(e -> {
			this.dispose();
		});
		buttonPanel.add(cancelButton);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		
	}
}
