package server;

/**

 * @Date: 2019/8/20
 * @version V1.0
 * @Description:用于定义信息类型
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class MessageType {
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int JOIN_GAME = 1;//申请加入游戏的请求信息
	public static final int JOIN_HOST = 2;//申请加入擂主的请求信息
	public static final int CHALLENGE = 3;//挑战擂主的请求信息
	public static final int CHALLENGE_RSP = 4;//挑战擂主的请求信息
	public static final int UPDATE_HOST = 5;//更新擂主列表的信息
	public static final int PLAY = 6;//玩家下了一步棋的信息
	public static final int Alpha = 7;//人机对战
	public static final int Message_siliao = 8;//人机对战
	public static final int Message_qunliao = 9;//人机对战
	public static final int UPDATE_PLAYER = 10;//更新在线玩家列表的信息
	public static final int GUANZHAN = 11;//观战
	public static final int GUANZHAN_RSP = 12;//观战回复
	public static final int Result = 13;//结果
	public static final int GUANZHAN_PLAY = 14;//观战下棋
}
