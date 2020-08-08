package server;
import java.awt.*;
import java.awt.Point;

public interface Alpha{
	public final int KONG = 0;  //无子
	public final int BLACK = 1; //黑棋
	public final int WHITE = 2; //白棋
	public final int N = 10;//设置棋盘大小

	//核心方法：返回下一步位置
	public Point next();

	//用于系统向Alpha对象注入棋盘引用
	public void setData(int[][] data);
	public int[][] getData();

	//用于系统向Alpha对象注入分配颜色
	public void setColor(int color);
	public int getColor();
}