package client;
import java.io.Serializable;
import server.Player;

/**

 * @Date: 2019/8/20
 * @version V1.0
 * @Description:用于表示一场比赛：比方信息、棋盘数据、比赛结果
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class Competition{
	Player localPlayer, remotePlayer;
	int[][]data;
	int result;
//	public Competition (Player localPlayer,Player remotePlayer,int[][]data,int result){
//		this.localPlayer=localPlayer;
//		this.remotePlayer=remotePlayer;
//		this.data=data;
//		this.result=result;
//	}

	public Competition() {
		// TODO Auto-generated constructor stub
	}
	//	public Competition() {
//		// TODO Auto-generated constructor stub
//	}
	public Player getLocalPlayer() {
		return localPlayer;
	}
	public void setLocalPlayer(Player localPlayer) {
		this.localPlayer = localPlayer;
	}
	public Player getRemotePlayer() {
		return remotePlayer;
	}
	public void setRemotePlayer(Player remotePlayer) {
		this.remotePlayer = remotePlayer;
	}
	public int[][] getData() {
		return data;
	}
	public void setData(int[][] data) {
		this.data = data;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

}
