package server;
import java.awt.Point;

public abstract class AlphaAdapter implements Alpha, server.Student {
	protected int[][] data; //棋盘二维数组(通常是10*10)	
	protected int color;    //机器执黑还是执白：1黑；2白
	
	public void setData(int[][] data){this.data = data;}
	public int[][] getData() { return this.data; }
	public void setColor(int color){this.color  = color;}
	public int getColor() { return color; }	
	public String getDesc() {return "**策略。基于...";}
	public String getName(){return "某某";}
	public String getSID(){return "150001";}
	public String getDept(){return "网络系";}
	
	/**
	 * 计算点（x,y）的权重
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return 权重，越大越有价值，位置已有棋子时返回0
	 */	 
	protected abstract double weight(int x, int y);
	
	public Point next(){
		//p、max分别代表最有价值的点和权重
		Point p = null;
		double max = -1;
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data[i].length; j++) {								
				if(weight(i, j) > max){
					p = new Point(i, j);
					max = weight(i, j);
				}
			}
		}
		return p;
	}	
}