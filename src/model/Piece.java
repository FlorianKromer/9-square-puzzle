package model;

public class Piece {

	private String letter;
	private int top;
	private int bottom;
	private int right;
	private int left;
	private boolean estPose;

	public static final int TOP = 0;

	public static final int RIGHT = 1;

	public static final int BOTTOM = 2;

	public static final int LEFT = 3;

	public Piece(String letter) {
		this.letter = letter;
		this.estPose = false;
	}
	
	/**
	 * Permet de pivoter une pièce
	 * Surement optimisable...
	 */
	public Piece pivoter(){
		int rightTmp = this.top;
		int bottomTmp = this.right;
		int leftTmp = this.bottom;
		int topTmp = this.left;
		this.top = topTmp;
		this.right = rightTmp;
		this.left = leftTmp;
		this.bottom = bottomTmp;
		return this;
	}

	public void setValueAt(int direction, int value){
		switch (direction) {
		case TOP:
			this.top = value;
			break;
		case BOTTOM:
			this.bottom = value;
			break;
		case LEFT:
			this.left = value;
			break;
		case RIGHT:
			this.right = value;
			break;

		default:
			break;
		}
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public String concat(String s, int size){
		int r  = size - s.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < r/2; i++) {
			sb.append(" ");
		}
		sb.append(s);
		for (int i = 0; i < r - r/2; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		
		return "+----------+"+"\n|"+
		concat(top+"", 10)+"|\n|"+
		concat(left+"", 4)+letter+concat(right+"", 4)+" |\n|"+
		concat(bottom+"", 10)+"|\n"+
				"+----------+"+"\n";

	}

	public boolean compareRight(Piece p2){
		System.out.println(this.right +  " : "  + p2.left);
		return this.right + p2.left == 0;
	}
	public boolean compareTop(Piece p2){
		return this.top + p2.bottom == 0;
	}
	
	public boolean compareLeft(Piece p2){
		return this.left + p2.right == 0;
	}
	
	public boolean compareBottom(Piece p2){
		return this.bottom + p2.top == 0;
	}

	public void prendre(){
		this.estPose = true;
	}
	
	public void retirer(){
		this.estPose = false;
	}
	
	public boolean isPose(){
		return estPose;
	}


}
