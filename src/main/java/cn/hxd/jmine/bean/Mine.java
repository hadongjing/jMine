/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hxd.jmine.bean;

import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author jaux
 */
public class Mine extends JButton {

    private boolean bomb = false;
    private Icon bombIcon;

    /**
     * @return the isBomb
     */
    public boolean isBomb() {
        return bomb;
    }

    /**
     * @param isBomb the isBomb to set
     */
    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public Mine() {
        bombIcon = new ImageIcon(getClass().getResource("/cn/hxd/jmine/resources/bomb.gif"));
    }

    public int doPress(ArrayList<Mine> mines, int l) {
        if (isBomb()) {
            for (Mine m : mines) {
                
                if (m.isBomb()) {
                    m.setDisabledIcon(bombIcon);
                    m.setIcon(bombIcon);
                }
                m.setEnabled(false);
            }
            return 0;
        } else if (!isBomb()) {
            if (this.isEnabled()) {
                setEnabled(false);
            }
           
            if (getSurBombs(mines, l) == 0) {
                doPressSur(mines, l);

            } else {
                setText(getSurBombs(mines, l) + "");
            }

            return 1;
        }
        return 2;
    }

    public int getSurBombs(ArrayList<Mine> mines, int l)
    {
        int bombNum = 0;
            final int n = mines.indexOf(this);
            if (n % l != 0 && mines.get(n - 1).isBomb()) {
                bombNum++;
            }
            if ((n + 1) % l != 0 && mines.get(n + 1).isBomb()) {
                bombNum++;
            }
            if ((n + 1) % l != 0 && n >= l && mines.get(n - l + 1).isBomb()) {
                bombNum++;
            }
            if (n >= l && mines.get(n - l).isBomb()) {
                bombNum++;
            }
            if (n % l != 0 && n >= l && mines.get(n - l - 1).isBomb()) {
                bombNum++;
            }
            if (n % l != 0 && n < l * (l - 1) && mines.get(n + l - 1).isBomb()) {
                bombNum++;
            }
            if (n < l * (l - 1) && mines.get(n + l).isBomb()) {
                bombNum++;
            }
            if ((n + 1) % l != 0 && n < l * (l - 1) && mines.get(n + 1 + l).isBomb()) {
                bombNum++;
            }
            return bombNum;
    }
    public void doPressSur(ArrayList<Mine> mines, int l){
         final int n = mines.indexOf(this);
        if (n % l != 0) {
                    if (mines.get(n - 1).isEnabled()) {
                        mines.get(n - 1).doPress(mines, l);
                    }
                }
                if ((n + 1) % l != 0) {
                    if (mines.get(n + 1).isEnabled()) {
                        mines.get(n + 1).doPress(mines, l);
                    }
                }
                if ((n + 1) %l != 0 && n >= l) {
                    if (mines.get(n - l+1).isEnabled()) {
                        mines.get(n - l+1).doPress(mines, l);
                    }
                }
                if (n >= l) {
                    if (mines.get(n - l).isEnabled()) {
                        mines.get(n - l).doPress(mines, l);
                    }
                }
                if (n % l != 0 && n >= l) {
                    if (mines.get(n - l-1).isEnabled()) {
                        mines.get(n - l-1).doPress(mines, l);
                    }
                }
                if (n % l != 0 && n <l*(l-1)) {
                    if (mines.get(n + l-1).isEnabled()) {
                        mines.get(n + l-1).doPress(mines, l);
                    }
                }
                if (n < l*(l-1)) {
                    if (mines.get(n + l).isEnabled()) {
                        mines.get(n + l).doPress(mines, l);
                    }
                }
                if ((n + 1) % l != 0 && n < l*(l-1)) {
                    if (mines.get(n + l+1).isEnabled()) {
                        mines.get(n + l+1).doPress(mines, l);
                    }
                }
    }
}
