package util;

public class MyUtil {
	private static int id = 0;
	public static void assertTrue(boolean t){
		if(t)
			System.out.println(++id + "号测试通过.");
		else
			System.out.println(++id + "号测试失败.");
	}
	
	/** 用于将棋盘状态（二维数组）打印出来 */
	public static void dump(int[][] data){
		System.out.println("----------");
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data[i].length; j++){
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
	}
}