package cn.hxd.jmine.domain;

public class Level implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Level LOW = new Level("初级", 9, 9, 10);
	public static final Level MEDIUM = new Level("中级", 16, 16, 40);
	public static final Level HIGH = new Level("高级", 25, 25, 120);
	public static final Level CUSTOM = new Level("自定义", 25, 25, 120);

	private String label;
	private int column;
	private int row;
	private int bombCount;

	public Level(String label, int column, int row, int bombCount) {
		this.label = label;
		this.column = column;
		this.row = row;
		this.bombCount = bombCount;
	}

	public String getLabel() {
		return label;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public int getBombCount() {
		return bombCount;
	}

}
