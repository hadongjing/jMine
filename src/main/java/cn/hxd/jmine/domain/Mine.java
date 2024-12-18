package cn.hxd.jmine.domain;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jaux
 */
@Getter
@Setter
public class Mine extends JButton {

	private static final String BOMB_ICON_PATH = "/images/bomb.png";
	private static final String FLAG_ICON_PATH = "/images/flag.png";
	private static final String WRONG_ICON_PATH = "/images/wrong.png";
	private static final String QUESTION_ICON_PATH = "/images/question.png";
	
	private static final int MARGIN_SIZE = 2;
	
    private boolean bomb = false;
    
    private MineStatus status = MineStatus.CLOSED;
    

    public Mine() {
		super();
		setMargin(new Insets(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE));
    }
    
    public void close() {
    	setEnabled(true);
    	setIcon(null);
    	setDisabledIcon(null);
    	setStatus(MineStatus.CLOSED);
    }
    
    public void flag() {
    	ImageIcon flagIcon = new ImageIcon(getClass().getResource(FLAG_ICON_PATH));
		// change icon size to mine size
		flagIcon.setImage(flagIcon.getImage().getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_SMOOTH));
    	setDisabledIcon(flagIcon);
		setIcon(flagIcon);
		setStatus(MineStatus.FLAGGED);
    }
    
    
    public void wrong() {
    	ImageIcon wrongIcon = new ImageIcon(getClass().getResource(WRONG_ICON_PATH));
    	// change icon size to mine size
    	wrongIcon.setImage(wrongIcon.getImage().getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_SMOOTH));
    	setDisabledIcon(wrongIcon);
    	setIcon(wrongIcon);
    }
    
	public void question() {
		ImageIcon questionIcon = new ImageIcon(getClass().getResource(QUESTION_ICON_PATH));
		// change icon size to mine size
		questionIcon.setImage(questionIcon.getImage().getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_SMOOTH));
		setDisabledIcon(questionIcon);
		setIcon(questionIcon);
		setStatus(MineStatus.QUESTIONED);
	}
	
	public void explode() {
		if(this.bomb && this.status != MineStatus.FLAGGED) {
			
			ImageIcon bombIcon = new ImageIcon(getClass().getResource(BOMB_ICON_PATH));
			// change icon size to mine size
			bombIcon.setImage(bombIcon.getImage().getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_SMOOTH));
	    	setDisabledIcon(bombIcon);
			setIcon(bombIcon);
		}
	}
    
	public void hint(List<Mine> mines, Level l) {
		getSurroundMines(mines, l).forEach(m ->{
			if(m.getStatus() == MineStatus.CLOSED) {
				m.setEnabled(false);
			}
		});
	}
    
	public int hintEnd(List<Mine> mines, Level l) {
		List<Mine> surroundMines = getSurroundMines(mines, l);
		int surroundBombs = getSurroundBombs(mines, l);
		int flaggedMines  = (int)surroundMines.stream().filter(m ->m.getStatus() == MineStatus.FLAGGED).count();
		if(surroundBombs == flaggedMines) {
			return openSurroundMines(mines, l);
		}else {
			
			surroundMines.forEach(m ->{
				if(m.getStatus() == MineStatus.CLOSED) {
					m.setEnabled(true);
				}
			});
		}
		return 1;
	}
	
	
    public int open(List<Mine> mines, Level l) {
        if (isBomb()) {
            for (Mine m : mines) {
                
                if (m.isBomb()) {
                    m.explode();
                }else if(m.getStatus() == MineStatus.FLAGGED) {
	                m.wrong();
                }
                m.setEnabled(false);
            }
            return 0;
        } else {
            this.status = MineStatus.OPENED;
            int surroundBombs = getSurroundBombs(mines, l);
			if (surroundBombs == 0) {
                openSurroundMines(mines, l);

            } else {
                String surroundBombsText = surroundBombs + "";
//				setText(surroundBombsText);
                setDisabledIcon(new StringIcon(surroundBombsText,  getColorByBombCount(surroundBombs)));
                setIcon(new StringIcon(surroundBombsText, getColorByBombCount(surroundBombs)));
            }
			if (this.isEnabled()) {
				setEnabled(false);
			}

            return 1;
        }
    }

    private Color getColorByBombCount(int count) {
	    switch(count) {
	    case 1:
	    	return Color.BLUE;
	    case 2:
	    	return new Color(55, 104, 28);
	    case 3:
	    	return new Color(171, 111, 0);
	    case 4:
	    	return Color.RED;
	    case 5:
	    	return Color.MAGENTA;
	    case 6:
	    	return Color.PINK;
	    case 7:
	    	return Color.CYAN;
	    case 8:
	    	return Color.GRAY;
	    default:
	    	return Color.BLACK;
	    }
    }
    
    private int getIconWidth() {
		return this.getWidth() - 2*MARGIN_SIZE;
	}
    
    private int getIconHeight() {
		return this.getHeight() - 2*MARGIN_SIZE;
	}
    
    public List<Mine> getSurroundMines(List<Mine> mines, Level l){
    	int rows = l.getRow();
    	int cols = l.getColumn();
    	final int n = mines.indexOf(this);
    	List<Mine> surroundMines = new ArrayList<>();
    	/*
    	 * 1 2 3
    	 * 4 5 6
    	 * 7 8 9
    	 */
    	if (n % cols != 0) {
    		surroundMines.add(mines.get(n - 1)) ; // 4
    	}
    	if ((n + 1) % cols != 0) {
    		surroundMines.add(mines.get(n + 1)); // 6
    	}
    	if ((n + 1) % cols != 0 && n >= cols ) {
    		surroundMines.add( mines.get(n - cols + 1)); // 3
    	}
    	if (n >= cols ) {
    		surroundMines.add( mines.get(n - cols)); // 2
    	}
    	if (n % cols != 0 && n >= cols ) {
    		surroundMines.add( mines.get(n - cols - 1)); // 1
    	}
    	if (n % cols != 0 && n < cols * (rows - 1)) {
    		surroundMines.add( mines.get(n + cols - 1)); // 7
    	}
    	if (n < cols * (rows - 1) ) {
    		surroundMines.add( mines.get(n + cols)); // 8
    	}
    	if ((n + 1) % cols != 0 && n <  cols * (rows - 1) ) {
    		surroundMines.add( mines.get(n + 1 + cols)); // 9
    	}
    	return surroundMines;
    }
    public int getSurroundBombs(List<Mine> mines, Level l){
    	List<Mine> surroundMines = getSurroundMines(mines, l);
        return (int)surroundMines.stream().filter(Mine::isBomb).count();
    }
    public int openSurroundMines(List<Mine> mines, Level l){
    	List<Mine> surroundMines = getSurroundMines(mines, l);
    	int result = 9;
		for(Mine m : surroundMines) {
			if(m.status == MineStatus.CLOSED) {
				result = Math.min(result, m.open(mines, l));
			}
		}
		return result;
		
    }
		
}
