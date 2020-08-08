package util;

import java.awt.*;

/** 得到最后一个落子。但当两次落子间没有没有保存数据时，
 *  getLast()方法会混绕最后一个黑子和最后一个白子
 *  此时请调用 getLast(int Color)，即指定最后落子的颜色
 */
public class Monitor{
	private int[][] data, data2;
	private Point last;
		
	public Monitor(int[][] data) {
		this.data = data;
		data2 = new int[data.length][data.length];
	}
	
	public Point getLast(){
		Point p = difference();
		if(p != null){
			last = p;
			copy();
		}
		return last;
	}
	//得到指定颜色的最后一个落子
	public Point getLast(int color){
		Point p = difference(color);
		if(p != null){
			last = p;
			copy();
		}
		return last;
	}
	private Point difference(int color){
		for(int i=0; i<data.length; i++ ){
			for(int j=0; j<data[i].length; j++){
				if(data[i][j] != data2[i][j] 
					&& (data[i][j]==color || data2[i][j]==color))
					return new Point(i, j);
			}
		}
		return null;
	}
	private Point difference(){
		for(int i=0; i<data.length; i++ ){
			for(int j=0; j<data[i].length; j++){
				if(data[i][j] != data2[i][j])
					return new Point(i, j);
			}
		}
		return null;
	}
	private void copy(){
		for(int i=0; i<data.length; i++ ){
			for(int j=0; j<data.length; j++){
				data2[i][j] = data[i][j];
			}
		}
	}
}