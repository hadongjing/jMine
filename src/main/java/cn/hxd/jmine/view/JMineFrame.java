package cn.hxd.jmine.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import cn.hxd.jmine.domain.CustomLevelDialog;
import cn.hxd.jmine.domain.Level;
import cn.hxd.jmine.domain.Mine;
import cn.hxd.jmine.domain.MineStatus;

/**
 *
 * @author jaux
 */
public class JMineFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 4297789630349699518L;
	
	private static final String FAILED_AUDIO = "/images/bomb.mid";
	private static final String WIN_AUDIO = "/images/success.mid";
	private JLabel bombsLabel;
	private JPanel minesPanel;
	private JPanel controlPanel;
	JComboBox<Level> levelComboBox;
	private ArrayList<Mine> mines;
	private HashSet<Integer> bombNumbers;
	private Level[] levels ;
	private Level level;
	private int mineSize = 30;
	private long startTime = 0;
	transient Clip clip ;
	/** Creates new form mainFrame */
	public JMineFrame() {
		levels = new Level[]{Level.LOW, Level.MEDIUM, Level.HIGH, Level.CUSTOM};
		mines = new ArrayList<>();
		bombNumbers = new HashSet<>();
		level = Level.LOW;
		initComponents();

	}
	@SuppressWarnings("unchecked")
	private void initComponents() {

		new Thread(()->{
			 try {
				clip = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}).start();
		setResizable(false);
		controlPanel = new  JPanel();
		levelComboBox = new JComboBox<>();
		bombsLabel = new  JLabel();
		JButton refreshButton = new  JButton();
		minesPanel = new  JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("扫雷");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setIconImage(getIconImage());

		controlPanel.setPreferredSize(new java.awt.Dimension(270, 30));
		controlPanel.setLayout(new java.awt.GridLayout(1, 0));

		levelComboBox.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
		levelComboBox.setModel( new DefaultComboBoxModel<>(levels));
		levelComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel label = new JLabel(value.getLabel());
            if (isSelected) {
                label.setBackground(Color.CYAN);
                label.setForeground(Color.BLACK);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }
            return label;
		});
		levelComboBox.addActionListener(evt->{
			System.out.println(evt);
			levelChange((Level)levelComboBox.getSelectedItem());
		});
		controlPanel.add(levelComboBox);

		bombsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		bombsLabel.setText("10");
		bombsLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		bombsLabel.setPreferredSize(new java.awt.Dimension(40, 15));
		controlPanel.add(bombsLabel);

		refreshButton.setIcon(new  ImageIcon(getClass().getResource("/images/fresh.png"))); // NOI18N
		refreshButton.addActionListener(this::refreshMines);
		controlPanel.add(refreshButton);

		getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);

		// jPanel1.setSize(270, 270);
		minesPanel.setFont(new java.awt.Font("Noto Sans CJK SC", 0, 10)); // NOI18N
		minesPanel.setPreferredSize(new java.awt.Dimension(270, 270));
		minesPanel.setLayout(new java.awt.GridLayout(9, 9, 1, 1));

		getContentPane().add(minesPanel, java.awt.BorderLayout.CENTER);
		newGame();
		pack();

		setLocationRelativeTo(null);
	}

	private void levelChange(Level l) {
		Level oldLevel = level;
		if(oldLevel.equals(l)&& !l.equals(Level.CUSTOM)) {
			return;
		}
		if(l.equals(Level.CUSTOM)) {
			CustomLevelDialog dialog = new CustomLevelDialog(this
					, Level.CUSTOM.getRow()
					, Level.CUSTOM.getColumn()
					, Level.CUSTOM.getBombCount());
			dialog.setVisible(true);
			if(dialog.isOk()) {
				this.level = new Level(Level.CUSTOM.getLabel(), dialog.getRows(), dialog.getCols(), dialog.getBombs());
			}else {
				levelComboBox.setSelectedItem(oldLevel);
				return;
			}
		}else {
			this.level = l;
		}
		newGame();
		if(!oldLevel.equals(level)) {
			setLocationRelativeTo(null);
		}

	}

	private void refreshMines(java.awt.event.ActionEvent evt) {
		
		newGame();
	}

	private void mineClicked(Mine mine) {
		if(mine.getStatus() != MineStatus.CLOSED) {
			return;
		}
		int i = mine.open(mines, level);
		if (i == 0) {
			lose();

		} else if (checkWin()) {
			win();
		}
	}

	private void win() {
		long usedTime = System.currentTimeMillis() - startTime;
		String usedTimeText = usedTime/1000+"秒";
		if(usedTime>60*60*1000) {
			usedTimeText = (usedTime/1000)/60/60+"小时"+(usedTime/1000)/60%60+"分"+(usedTime/1000)%60+"秒";
		}else if(usedTime>60*1000) {
			usedTimeText = (usedTime/1000)/60+"分"+(usedTime/1000)%60+"秒";
		}
		playSound(WIN_AUDIO);
		JOptionPane.showMessageDialog(null, "你赢了,用时"+usedTimeText);
		mines.stream().forEach(m->{
			m.setEnabled(false);
		});
	}
	
	private void lose() {
		playSound(FAILED_AUDIO);
		JOptionPane.showMessageDialog(null, "你输了！");
		mines.stream().forEach(m->{
			m.setEnabled(false);
		});
	}
	
	private void playSound(String string) {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(string));
					if(clip.isOpen()) {
						clip.close();
					}
					clip.open(audioInputStream);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();

	}

	private void rightMouseClicked(Mine mine) {
		
		if (mine.getStatus() == MineStatus.CLOSED) {
			mine.flag();
			bombsLabel.setText(Integer.parseInt(bombsLabel.getText()) - 1 + "");
			if (checkWin()) {
				win();
				
			}
		}else if(mine.getStatus() == MineStatus.FLAGGED){
			mine.question();
			bombsLabel.setText(Integer.parseInt(bombsLabel.getText()) + 1 + "");
		}else if(mine.getStatus() == MineStatus.QUESTIONED){
			mine.close();
			
		}

	}

	private void newGame() {
		new Thread(()->{
			if(clip !=null) {
				clip.close();
			}
			
		}).start();
		startTime = 0;
		getContentPane().removeAll();
		int bombCount = level.getBombCount();
		int cols = level.getColumn();
		int rows = level.getRow();
		bombsLabel.setText(bombCount + "");
		controlPanel.setPreferredSize(new java.awt.Dimension(cols * mineSize, mineSize));
		minesPanel.removeAll();
		bombNumbers.clear();
		mines.clear();
		minesPanel.setPreferredSize(new java.awt.Dimension(cols * mineSize, rows * mineSize));
		minesPanel.setLayout(new java.awt.GridLayout(rows, cols));

		while (bombNumbers.size() < bombCount) {
			bombNumbers.add((int) (Math.random() * rows * cols));
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Mine mine = new Mine();
				if (bombNumbers.contains(i * rows + j)) {
					mine.setBomb(true);
					System.out.println(i + 1 + "," + (j + 1));
				}
				mine.addActionListener(e-> {
					mineClicked((Mine)e.getSource());
				});
				mine.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						Mine m = (Mine)evt.getSource();
						if(evt.getButton() == MouseEvent.BUTTON3 && m.isEnabled()) {
							rightMouseClicked(m);
							
						}
					}
					@Override
					public void mousePressed(MouseEvent e) {
						super.mousePressed(e);
						if(startTime == 0) {
							startTime = System.currentTimeMillis();
						}
						Mine m = (Mine)e.getSource();
						if(m.getStatus() == MineStatus.OPENED ) {
							m.hint(mines, level);
						}
						
					}
					@Override
					public void mouseReleased(MouseEvent e) {
						super.mouseReleased(e);
						Mine m = (Mine)e.getSource();
						if(m.getStatus() == MineStatus.OPENED ) {
							int r = m.hintEnd(mines, level);
							if(r == 0) {
								lose();	
							}
						}
					}
				});
				mines.add(mine);
				minesPanel.add(mine);
			}
		}
		getContentPane().add(minesPanel, java.awt.BorderLayout.CENTER);
		getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);
		pack();
	}

	private boolean checkWin() {
		return mines.stream().filter(Mine::isBomb).allMatch(m-> m.getStatus() == MineStatus.FLAGGED);
	}

	
}
