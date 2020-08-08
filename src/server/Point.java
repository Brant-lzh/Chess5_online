package server;

import java.io.Serializable;

/**

 * @Date: 2019/8/20
 * @version V1.0
 * @Description:用于封装落子点的信息，包括行、列值，棋子颜色及动作（添加棋子或是移除棋子，如悔棋）
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class Point implements Serializable {
	
	public int column, row,action;
	public int color;
	
	public Point(int column, int row, int color, int action){
		this.column = column;
		this.row = row;
		this.color = color;
		this.action = action;
	}
	public String toString(){
		return "[" + this.column + ", " + row + "," + color + "," + action + "]";
	}
	
}
