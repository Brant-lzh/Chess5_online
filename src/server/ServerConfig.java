package server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;

/**

 * @Date: 2019/8/20
 * @version V1.0
 * @Description:用于定义服务器要用到的一些常量及相关变量
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */

public class ServerConfig {
	public final static SocketAddress SERVER_ADDR
		= new InetSocketAddress("127.0.0.1", 6000);//服务器开启的UDP端口
	//public final static int CLIENT_LISTEN_PORT_DEFAULT = 5000;
	private static HashMap<String, server.Player> hostMap
			= new HashMap<String, server.Player>();//用于存放擂主信息
	private static HashMap<String, server.Player> playerMap
			= new HashMap<String, server.Player>();//用于存放所有玩家的信息
	private static HashMap<String, server.Player> guanzhanMap
			= new HashMap<String, server.Player>();//用于存放观战信息 //被观战人名字 ，观战人
	
	public static void addGuanzhan(String name, server.Player player){
		guanzhanMap.put(name, player);
	}
	public static server.Player getGuanzhanPlayer(String name){
		return guanzhanMap.get(name);
	}
	public static boolean containGuanzhan(String name){
		return guanzhanMap.containsKey(name);
	}
	
	public static void addHost(String name, server.Player player){
		hostMap.put(name, player);
	}

	public static boolean containHost(String name){
		return hostMap.containsKey(name);
	}
	public static HashMap<String, server.Player> getPlayerMap(){
		return playerMap;
	}
	public static boolean containPlayer(String name){
		return playerMap.containsKey(name);
	}
	public static server.Player getPlayer(String name){
		return playerMap.get(name);
	}
	public static void addPlayer(String name, server.Player player){
		playerMap.put(name, player);
	}
	
	public static String getHostNamesList(){
		String str = "";
		Iterator<String> it = hostMap.keySet().iterator();
		while(it.hasNext()){
			str = str  + it.next() + ":";		
		}
		System.out.println("当期名单:"+str);
		return str;
	}
	
	public static String getPlayerNamesList(){
		String str = "";
		Iterator<String> it = playerMap.keySet().iterator();
		while(it.hasNext()){
			str = str  + it.next() + ":";		
		}
		System.out.println("玩家:"+str);
		return str;
	}
	
	
}
