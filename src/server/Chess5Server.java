package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.embed.HostDragStartListener;


/**
 * @Date: 2019/8/20
 * @version V1.0
 * @Description:五子棋在线游戏服务器
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class Chess5Server {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		server.Chess5Server cs = new server.Chess5Server();
		cs.service();
	}

	private List<String> guanzhanList;//被观战人:观战人
	public Chess5Server(){
		init();
	}
	public void init(){	
		String[] hostStrings = {};
		System.out.println("服务器已经启动................");
		//String [] hostStrings = {"冲天鹤","独孤求败","西门吹雪","无名小卒","武松",};
				//"无面郎君","小李飞刀","你是二","刘一手","水上飞","五上至尊","冷面杀手","不死草"};		
//		for(String str : hostStrings){
//			Player player = new Player(str, new InetSocketAddress("localhost", 5000),0);
//			ServerConfig.addHost(str, player);
//			System.out.println(str);
//		}
		//先准备一个List
		//List<String> guanzhanList=new ArrayList<>();
//		guanzhanList.add("啊啊啊:不不不");
//		guanzhanList.add("b");
//		guanzhanList.add("c");
		//List转String
//		String[] guanzhanList_arry=guanzhanList.toArray(new String[guanzhanList.size()]);
//		for(String s:guanzhanList_arry){
//		   System.out.println(s);
//		}
//		System.out.println(ServerConfig.getHostNamesList());
//		System.out.println(ServerConfig.getPlayerMap());
	}
	
	public void service (){
		
	   try(DatagramSocket socket = new DatagramSocket(server.ServerConfig.SERVER_ADDR);
		){
	    while(true){
	      try{
			DatagramPacket pin = new DatagramPacket(new byte[2048], 2048);
			socket.receive(pin);
			server.Message msg = (server.Message) server.Message.convertToObj(pin.getData(),0,pin.getLength());
			System.out.println(msg);
			server.Message rspMsg = new server.Message();
			/**
			 * 1.如果是“JOIN_GAME”，表示有用户要加入游戏，则检查昵称是否已经存在，如果存在，则拒绝；将当前的擂主名单返回给新加入玩家；
			 * 2.如果是“JOIN_HOST”，表示有玩家要加入擂台，则检查是否已经加入，如果是，则拒绝；将当前的擂主名单返回给新加入擂台的玩家；
			 * 3.如果是“CHALLENGE”，表示有玩家发出了一个挑战擂主的请求，将该请求转发给对应的擂主；
			 * 4.如果是“CHALLENGE_RSP”，表示有被挑战的擂主的回复了挑战请求，将该回复转发给发起挑战的玩家；
			 * 5.如果是“PLAY”，表示正在比赛中的一方下了棋子，发出一个请求告诉对方棋子的信息，服务器转发该信息给另一方。
			 */
			
			
			if(msg.getMsgType()== server.MessageType.JOIN_GAME){
				System.out.println("JOIN_GAME");
				String PlayerName =  msg.getFromPlayer().getName();
				if(server.ServerConfig.containPlayer(PlayerName)){
					rspMsg.setStatus(2);
					System.out.println("玩家已存在");
				}else{
					rspMsg.setStatus(1);
					System.out.println("玩家不存在");
					server.ServerConfig.addPlayer(msg.getFromPlayer().getName(),msg.getFromPlayer());
				}
				rspMsg.setFromPlayer(msg.getFromPlayer());
				rspMsg.setContent(server.ServerConfig.getHostNamesList());
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
				System.out.println(msg.getFromPlayer().getAddress());
				socket.send(pout);
			} else if(msg.getMsgType()== server.MessageType.JOIN_HOST){
				System.out.println("JOIN_HOST");
				String PlayerName =  msg.getFromPlayer().getName();
				System.out.println(PlayerName);
				if(server.ServerConfig.containHost(PlayerName)){
					rspMsg.setStatus(server.MessageType.FAIL);
					System.out.println("擂主已存在");
				}else{
					rspMsg.setStatus(server.MessageType.SUCCESS);
					System.out.println("擂主不存在");
					server.ServerConfig.addHost(msg.getFromPlayer().getName(),msg.getFromPlayer());
				}
				rspMsg.setContent(server.ServerConfig.getHostNamesList());
				rspMsg.setMsgType(server.MessageType.JOIN_HOST);
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//				System.out.println(msg.getFromPlayer().getAddress());
				socket.send(pout);
			} else if(msg.getMsgType()== server.MessageType.UPDATE_HOST){
				System.out.println("UPDATE_HOST");
				String PlayerName =  msg.getFromPlayer().getName();
				System.out.println(PlayerName);
				rspMsg.setStatus(server.MessageType.SUCCESS);
				rspMsg.setContent(server.ServerConfig.getHostNamesList());
				rspMsg.setMsgType(server.MessageType.UPDATE_HOST);
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//				System.out.println(msg.getFromPlayer().getAddress());
				socket.send(pout);
			} else if(msg.getMsgType() == server.MessageType.CHALLENGE){
				System.out.println("CHALLENGE");
				
				String PlayerName =  msg.getFromPlayer().getName();
				String ChallengeName = msg.getContent().toString();
				
//				System.out.println("发送请求挑战玩家"+PlayerName);
//				System.out.println("被挑战玩家"+ChallengeName);
				
//				String list_PlayerName[] = ServerConfig.getHostNamesList().split(":");
//	        	for(int i=0;i<list_PlayerName.length;i++){
//	        		//cBox.getItems().addAll(list_PlayerName[i]);
//	        		if(list_PlayerName[i].equals(ChallengeName)){
//	        			rspMsg.setStatus(3);
//	        			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(rspMsg),Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//	        			socket.send(pout);
//	        		}
//	        	}
				//Player ChallengePlayer = ServerConfig.getPlayer(ChallengeName);
				rspMsg.setMsgType(server.MessageType.CHALLENGE);
    			rspMsg.setStatus(server.MessageType.SUCCESS);
    			rspMsg.setFromPlayer(msg.getFromPlayer());
    			rspMsg.setToPlayer(server.ServerConfig.getPlayer(ChallengeName));
    			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length, server.ServerConfig.getPlayer(ChallengeName).getAddress());
    			socket.send(pout);
//				if(ServerConfig.containHost(PlayerName)){
//					rspMsg.setStatus(2);
//					System.out.println("擂台已存在");
//				}else{
//					rspMsg.setStatus(1);
//					System.out.println("擂台不存在");
//					ServerConfig.addHost(msg.getFromPlayer().getName(),msg.getFromPlayer());
//				}
//				rspMsg.setStatus(1);
//				rspMsg.setContent(ServerConfig.getHostNamesList());
//				DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(rspMsg),Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//				System.out.println(msg.getFromPlayer().getAddress());
//				socket.send(pout);
//				
			} else if(msg.getMsgType() == server.MessageType.CHALLENGE_RSP){
				rspMsg.setMsgType(server.MessageType.CHALLENGE_RSP);
				rspMsg.setStatus(server.MessageType.SUCCESS);
				rspMsg.setFromPlayer(msg.getFromPlayer());
				rspMsg.setToPlayer(msg.getToPlayer());
    			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getToPlayer().getAddress());
    			socket.send(pout);
			} else if(msg.getMsgType()== server.MessageType.PLAY){
				server.Message guanzhan_msg = new server.Message();
				System.out.println(msg.toString());
				rspMsg = msg; 
				
				boolean isGuanzhanRen = server.ServerConfig.containGuanzhan(msg.getFromPlayer().getName());
				boolean isGuanzhanRen2 = server.ServerConfig.containGuanzhan(msg.getToPlayer().getName());
				if(isGuanzhanRen){
					server.Player Guanzhan_player = server.ServerConfig.getGuanzhanPlayer(msg.getFromPlayer().getName());
					guanzhan_msg.setContent(msg.getContent());
					guanzhan_msg.setMsgType(server.MessageType.GUANZHAN_PLAY);
					guanzhan_msg.setToPlayer(Guanzhan_player);
	    			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(guanzhan_msg), server.Message.convertToBytes(guanzhan_msg).length,guanzhan_msg.getToPlayer().getAddress());
	    			socket.send(pout);
				}else if(isGuanzhanRen2){
					server.Player Guanzhan_player = server.ServerConfig.getGuanzhanPlayer(msg.getToPlayer().getName());
					guanzhan_msg.setContent(msg.getContent());
					guanzhan_msg.setMsgType(server.MessageType.GUANZHAN_PLAY);
					guanzhan_msg.setToPlayer(Guanzhan_player);
	    			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(guanzhan_msg), server.Message.convertToBytes(guanzhan_msg).length,guanzhan_msg.getToPlayer().getAddress());
	    			socket.send(pout);
				}
				
//    			String[] guanzhanList_arry=guanzhanList.toArray(new String[guanzhanList.size()]);
//
//    			for(String s:guanzhanList_arry){
//     			   System.out.println(s);
//     			   System.out.println("11111");
//     			   String guanzhan_list[] = s.split(":");
// 		        	for(int i=0;i<guanzhan_list.length;i++){
// 		        		//cBox.getItems().addAll(list_PlayerName[i]);
// 		        		if(
// 		        			msg.getFromPlayer().getName().equals(guanzhan_list[0])
// 		        			||  msg.getToPlayer().getName().equals(guanzhan_list[0])
// 		        				){
// 		        			Message guanzhan_msg = new Message();
// 		        			guanzhan_msg.toPlayer=ServerConfig.getPlayer(guanzhan_list[1]);
// 		        			guanzhan_msg.setMsgType(MessageType.PLAY);
// 		        			guanzhan_msg.setContent(msg.getContent());
// 		        			DatagramPacket guanzhan_pout  = new DatagramPacket(Message.convertToBytes(guanzhan_msg),Message.convertToBytes(guanzhan_msg).length,guanzhan_msg.getToPlayer().getAddress());
// 		        			socket.send(guanzhan_pout);
// 		        			System.out.println("观战下棋发送成功");
// 		        		}
// 		        	}
//     			}
//    			
    			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getToPlayer().getAddress());
    			socket.send(pout);
    			

    			//String list_PlayerName[] = rspMsg.getContent().toString().split(":");
				//System.out.println("222222");
			} else if(msg.getMsgType()== server.MessageType.Alpha){
				int color = msg.getFromPlayer().color;
//				System.out.println(color);
				Alpha alpha = new Alpha1740224170();
				alpha.setColor( Alpha.WHITE ); //机器执黑先手
//				System.out.println(alpha.next());
//				System.out.println(alpha.getData());
//				System.out.println("111");
//				rspMsg = msg; 
//    			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(rspMsg),Message.convertToBytes(rspMsg).length,msg.getToPlayer().getAddress());
//    			socket.send(pout);
				
			} else if(msg.getMsgType()== server.MessageType.Message_qunliao){
//				System.out.println(ServerConfig.getPlayerMap());
				HashMap<String, server.Player> PlayerMap = server.ServerConfig.getPlayerMap();
				for (server.Player player : PlayerMap.values()) {
					System.out.println(player.getAddress());
					rspMsg = msg;
					DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,player.getAddress());
	    			socket.send(pout);
				}
			}else if(msg.getMsgType()== server.MessageType.Message_siliao){
				rspMsg = msg;
				rspMsg.setToPlayer(server.ServerConfig.getPlayer(msg.getToPlayer().getName()));
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,rspMsg.getToPlayer().getAddress());
    			socket.send(pout);
			}else if(msg.getMsgType()== server.MessageType.UPDATE_PLAYER){
				System.out.println("UPDATE_PLAYER");
				String PlayerName =  msg.getFromPlayer().getName();
//				System.out.println(PlayerName);
				rspMsg.setToPlayer(msg.getToPlayer());
				rspMsg.setStatus(server.MessageType.SUCCESS);
				rspMsg.setContent(server.ServerConfig.getPlayerNamesList());
				rspMsg.setMsgType(server.MessageType.UPDATE_PLAYER);
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//				System.out.println(msg.getFromPlayer().getAddress());
				socket.send(pout);
			}else if(msg.getMsgType()== server.MessageType.GUANZHAN){
				System.out.println("GUANZHAN");
				rspMsg=msg;
				server.Player toplayer = server.ServerConfig.getPlayer(msg.toPlayer.getName());
				rspMsg.setToPlayer(toplayer);
				//rspMsg.setContent(ServerConfig.getPlayerNamesList());
				rspMsg.setMsgType(server.MessageType.GUANZHAN);
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,rspMsg.getToPlayer().getAddress());
//				System.out.println(rspMsg.getToPlayer().getAddress());
				socket.send(pout);
			}else if(msg.getMsgType()== server.MessageType.GUANZHAN_RSP){
				System.out.println("GUANZHAN_RSP");
				//guanzhanList.add(rspMsg.getFromPlayer()+":"+rspMsg.getToPlayer());
//				String[] guanzhanList_arrytets=guanzhanList.toArray(new String[guanzhanList.size()]);
//				for(String s:guanzhanList_arrytets){
//				   System.out.println(s);
//				}
//				System.out.println("333333");
				server.ServerConfig.addGuanzhan(msg.getFromPlayer().getName(), msg.getToPlayer());
				rspMsg=msg;
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,rspMsg.getToPlayer().getAddress());
//				System.out.println(rspMsg.getContent());
				socket.send(pout);

			}else if(msg.getMsgType()== server.MessageType.Result){
				System.out.println("Result");
				rspMsg.setMsgType(server.MessageType.Result);
				rspMsg.setContent(msg.getContent());
				rspMsg.setToPlayer(msg.getFromPlayer());
				DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,rspMsg.getToPlayer().getAddress());
				System.out.println("发送出去  "+rspMsg.getContent()+" "+rspMsg.getToPlayer().getName());
				socket.send(pout);
				
				rspMsg.setToPlayer(msg.getToPlayer());
				DatagramPacket pout1  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,rspMsg.getToPlayer().getAddress());
				System.out.println("发送出去  "+rspMsg.getContent()+" "+rspMsg.getToPlayer().getName());
				socket.send(pout1);
			}
	      }catch(Exception e) {
				// TODO: handle exception
		  }
	    } //while
	    
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}


}
