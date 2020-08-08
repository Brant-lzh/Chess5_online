package server;
import util.Monitor;
import java.awt.Point;

public class Alpha1740224170 extends AlphaAdapter{
	public String getName(){return "刘至浩";}; //姓名
	public String getSID(){return "1740224170";};  //学号
	public String getDept(){return "网络技术系";}; //系科
	public String getDesc(){
		return "统计赢法和得分策略：遍历全棋盘找出4个方向能赢的赢法数组，根据在该赢法数组上连子数来得分，从而让电脑找出最佳下棋位子";
	};//策略描述

	private Monitor monitor;
	//	private int n = Alpha.N;
	private int n = 10;
	private int[][] peopleGrades = new int[n+1][n+1];
	private int[][] computerGrades = new int[n+1][n+1];
	private boolean[][][] peopleTable;
	private boolean[][][] computerTable;
	private int[][] chessNumberInState;
	private boolean FirstStep = true,action = true;


	public void setData(int[][] data){
		monitor = new Monitor(data);
		super.setData(data);
	}
	public Point next(){
		int color = getColor()==Alpha.BLACK?Alpha.WHITE:Alpha.BLACK;//对方颜色
		System.out.println( monitor.getLast(color) );
		System.out.println("123");
		int computerChess[] = computerMove();
		Point p = new Point(computerChess[0],computerChess[1]);
		System.out.println(p.x+"  "+p.y);
		return p;

	}

	@Override
	protected double weight(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int[] last(int color){//获得最后一个下棋的坐标
		int last[] = new int [2];
		Point lastChess = monitor.getLast(color);
		last[0] = lastChess.x;
		last[1] = lastChess.y;
		return last;

	}
	public void mouseClick(){
		int last[] = new int [2];
		if(monitor.getLast(color)==null){//判断是电脑先手还是玩家先手
			last=last(Alpha.BLACK);
		}else{
			last=last(Alpha.WHITE);
		}
		int x = last[0];
		int y = last[1];
		for (int i = 0; i < winNumber(); i++) {  //*****
			if (peopleTable[x][y][i] && chessNumberInState[0][i] != 7){   //boolean值玩家该状态是否可能赢//该状态下玩家的棋子数
				chessNumberInState[0][i]++;    //该状态下玩家棋子数加一
			}
			if (computerTable[x][y][i]) {  //boolean值
				chessNumberInState[1][i] = 7;  //7是为了上面判断
			}
		}
	}
	public int winNumber(){//统计能五连子的数组
		int icount = 0;
		for(int i=0;i<data.length;i++)
			for(int j=0;j<(data.length-4);j++){
				icount++;
			}
		//竖
		for(int i=0;i<data.length;i++)
			for(int j=0;j<(data.length-4);j++){
				icount++;
			}
		//右斜
		for(int i=0;i<(data.length-4);i++)
			for(int j=0;j<(data.length-4);j++){
				icount++;
			}
		//左斜
		for(int i=0;i<(data.length-4);i++)
			for(int j=(data.length-1);j>=4;j--){
				icount++;
			}
		return icount;
	}
	public void ReserGame(){
		for(int i=0;i<data.length;i++)
			for(int j=0;j<data.length;j++)
			{
				peopleGrades[i][j] = 0;
				computerGrades[i][j] = 0;
			}
		//遍历所有的五连子可能情况的权值
		//横
		int icount = 0;
		for(int i=0;i<data.length;i++)
			for(int j=0;j<(data.length-4);j++){
				for(int k=0;k<5;k++){
					peopleTable[j+k][i][icount] = true;
					computerTable[j+k][i][icount] = true;
				}
				icount++;
			}
		//竖
		for(int i=0;i<data.length;i++)
			for(int j=0;j<(data.length-4);j++){
				for(int k=0;k<5;k++){
					peopleTable[i][j+k][icount] = true;
					computerTable[i][j+k][icount] = true;
				}
				icount++;
			}
		//右斜
		for(int i=0;i<(data.length-4);i++)
			for(int j=0;j<(data.length-4);j++){
				for(int k=0;k<5;k++){
					peopleTable[j+k][i+k][icount] = true;
					computerTable[j+k][i+k][icount] = true;
				}
				icount++;
			}
		//左斜
		for(int i=0;i<(data.length-4);i++)
			for(int j=(data.length-1);j>=4;j--){  //改了
				for(int k=0;k<5;k++){
					peopleTable[j-k][i+k][icount] = true;
					computerTable[j-k][i+k][icount] = true;
				}
				icount++;
			}
		//System.out.println("赢法"+icount);
		//找到了672种五连赢法，
		for(int i=0;i<=1;i++)  //初始化黑子白子上的每个权值上的连子数
			for(int j=0;j<icount;j++)
				chessNumberInState[i][j] = 0;
	}
	public int[] computerMove() {
		int computerChess[] = new int [2];
		int last[] = new int [2];
		int x,y;
		if(action){
			peopleTable = new boolean[n+1][n+1][winNumber()];
			computerTable = new boolean[n+1][n+1][winNumber()];
			chessNumberInState = new int[2][winNumber()];
			ReserGame();
		}
		if(monitor.getLast(color)!=null){//判断是谁先手
			mouseClick();
			last=last(Alpha.WHITE);
			x = last[0];
			y = last[1];
			x += (x - 7) > 0 ? -1 : 1;   //第一步时占更为靠近中心的一点
			y += (y - 7) > 0 ? -1 : 1;
		}else{
			if(action){
				x = data.length/2;
				y = data.length/2;
			}else{
				mouseClick();
				last=last(Alpha.BLACK);
				x = last[0];
				y = last[1];
			}
		}
		if (!FirstStep) {
			int x1 = x, x2 = x, y1 = y, y2 = y;
			for (int i = 0; i <= (data.length-1); i++)
				for (int j = 0; j <= (data.length-1); j++) {
					peopleGrades[i][j] = 0;
					if (data[i][j] == 0)
						for (int k = 0; k < winNumber(); k++)
							if (peopleTable[i][j][k]) {
								switch (chessNumberInState[0][k]) {//可以通过改动分值来 权重进攻还是防守
									case 1: // 一连子
										peopleGrades[i][j] += 5;
										break;
									case 2: // 两连子
										peopleGrades[i][j] += 50;
										break;
									case 3: // 三连子
										peopleGrades[i][j] += 180;
										break;
									case 4: // 四连子
										peopleGrades[i][j] += 400;
										break;
								}
							}
					computerGrades[i][j] = 0;
					if (data[i][j] == 0)
						for (int k = 0; k < winNumber(); k++)  /****/
							if (computerTable[i][j][k]) {
								switch (chessNumberInState[1][k]) {
									case 1: // 一连子
										computerGrades[i][j] += 5;
										break;
									case 2: // 两连子
										computerGrades[i][j] += 52;
										break;
									case 3: // 三连子
										computerGrades[i][j] += 180;
										break;
									case 4: // 四连子
										computerGrades[i][j] += 400;
										break;
								}
							}
				}
			int maxPeopleGrades = 0, maxComputerGrades = 0;
			for (int i = 0; i < data.length; i++) //寻找最大分数即最优策略
				for (int j = 0; j < data.length; j++) {
					if (data[i][j] == 0
							&& computerGrades[i][j] > maxComputerGrades) {
						maxComputerGrades = computerGrades[i][j];
						x1 = i;
						y1 = j;
					}
					if (data[i][j] == 0
							&& peopleGrades[i][j] > maxPeopleGrades) {
						maxPeopleGrades = peopleGrades[i][j];
						x2 = i;
						y2 = j;
					}
				}
			if (maxComputerGrades > maxPeopleGrades) {
				x = x1;
				y = y1;
			} else {
				x = x2;
				y = y2;
			}
		}
		this.FirstStep = false;
		this.action = false;
		for (int i = 0; i < winNumber(); i++) {
			if (computerTable[x][y][i]
					&& chessNumberInState[1][i] != 7)
				chessNumberInState[1][i]++;    //电脑该状态下的棋子数加一
			if (peopleTable[x][y][i]) {
				peopleTable[x][y][i] = false;
				chessNumberInState[0][i] = 7;
			}
		}
		computerChess[0] = x;
		computerChess[1] = y;
		return computerChess;
	}
}