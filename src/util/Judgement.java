package util;

public class Judgement {
	public static final int MAX = 5; //连多少子算赢
	/**
	 * 根据棋盘状态评判胜负。
	 * 若水平、垂直、斜线或反斜线4个方向中，存在连续MAX个相同颜色的棋子，则胜负已分！
	 * @param data 棋盘状态（二维数组），0无子，1黑子，2白子
	 * @return 返回棋局评判，0尚无胜负，1黑胜，2白胜
	 */
	public static int judge(int[][] data){
		if(hLine(data)>0) return hLine(data);
		if(vLine(data)>0) return vLine(data);
		if(xLine(data)>0) return xLine(data);
		if(rxLine(data)>0) return rxLine(data);
		return 0;
	}
	/** 判断水平方向是否有连续MAX个相同颜色的棋子 */
	private static int hLine(int[][] data){
		int black=0,white=0;
		for(int i=0; i<data.length; i++){
			black=0; white=0;
			for(int j=0; j<data[i].length; j++){
				if(data[i][j]==1){
					black++;
					white = 0;
				}
				else if(data[i][j]==2){
					white++;
					black = 0;
				}else{
					black = white = 0;
				}
				if(black>=MAX) return 1;
				if(white>=MAX) return 2;
			}			
		}
		return 0;
	}	
	/** 垂直线 ，请确保二维数组是规则的 */
	private static int vLine(int[][] data){
		int black=0,white=0;
		for(int i=0; i<data.length; i++){
			black=0; white=0;
			for(int j=0; j<data[i].length; j++){
				if(data[j][i]==1){
					black++;
					white = 0;
				}
				else if(data[j][i]==2){
					white++;
					black = 0;
				}else{
					black = white = 0;
				}
				if(black>=MAX) return 1;
				if(white>=MAX) return 2;
			}			
		}
		return 0;
	}
	/** 斜线（右上-左下）*/
	private static int xLine(int[][] data){
		int black=0,white=0;
		for(int sum=0; sum<=data.length*2; sum++){
			black=0; white=0;
			for(int i=0; i<=sum; i++) {
				int j = sum - i;
				if(i>=data.length || i<0) continue;
				if(j>=data.length || j<0) continue;					
				if(data[i][j]==1){
					black++;
					white = 0;
				}
				else if(data[i][j]==2){
					white++;
					black = 0;
				}else{
					black = white = 0;
				}
				if(black>=MAX) return 1;
				if(white>=MAX) return 2;
			}
		}
		return 0;
	}
	/** 反斜线（左上-右下） */
	private static int rxLine(int[][] data){
		int black=0,white=0;
		for(int sub=-(data.length-1); sub<=data.length-1; sub++){
			black=0; white=0;
			for(int i=0; i<data.length; i++) {
				int j = i - sub;
				if(i>=data.length || i<0) continue;
				if(j>=data.length || j<0) continue;
				if(data[i][j]==1){
					black++;
					white = 0;
				}
				else if(data[i][j]==2){
					white++;
					black = 0;
				}else{
					black = white = 0;
				}
				if(black>=MAX) return 1;
				if(white>=MAX) return 2;
			}			
		}
		return 0;
	}
}