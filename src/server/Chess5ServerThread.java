package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Chess5ServerThread extends Thread {
    DatagramSocket socket = null;

    public Chess5ServerThread(DatagramSocket socket) {
        this.socket = socket;

    }
    public void run(){
try{
		DatagramPacket pin = new DatagramPacket(new byte[1024], 1024);
		socket.receive(pin);
		
//		Chess5ServerThread t1 = new Chess5ServerThread(socket,pin);
//		t1.start();
//		
		server.Message msg = (server.Message) server.Message.convertToObj(pin.getData(),0,pin.getLength());

		System.out.println(msg);
		server.Message rspMsg = new server.Message();
		//DatagramPacket pout  = new DatagramPacket(msg.getBytes(),msg.getBytes().length,serverAddr);
		//System.out.println(rspMsg);
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
			rspMsg.setContent(server.ServerConfig.getHostNamesList());
			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
			System.out.println(msg.getFromPlayer().getAddress());
			socket.send(pout);
		} else if(msg.getMsgType()== server.MessageType.JOIN_HOST){
			System.out.println("JOIN_HOST");
			String PlayerName =  msg.getFromPlayer().getName();
			System.out.println(PlayerName);
			if(server.ServerConfig.containHost(PlayerName)){
				rspMsg.setStatus(2);
				System.out.println("擂主已存在");
			}else{
				rspMsg.setStatus(1);
				System.out.println("擂主不存在");
				server.ServerConfig.addHost(msg.getFromPlayer().getName(),msg.getFromPlayer());
			}
			//ServerConfig.
			rspMsg.setContent(server.ServerConfig.getHostNamesList());
			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
			System.out.println(msg.getFromPlayer().getAddress());
			socket.send(pout);
		} else if(msg.getMsgType()== server.MessageType.UPDATE_HOST){
			System.out.println("UPDATE_HOST");
			String PlayerName =  msg.getFromPlayer().getName();
			System.out.println(PlayerName);
//			if(ServerConfig.containHost(PlayerName)){
//				rspMsg.setStatus(2);
//				System.out.println("擂台已存在");
//			}else{
//				rspMsg.setStatus(1);
//				System.out.println("擂台不存在");
//				ServerConfig.addHost(msg.getFromPlayer().getName(),msg.getFromPlayer());
//			}
			rspMsg.setStatus(1);
			rspMsg.setContent(server.ServerConfig.getHostNamesList());
			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
			System.out.println(msg.getFromPlayer().getAddress());
			socket.send(pout);
		} else if(msg.getMsgType() == server.MessageType.CHALLENGE){
			System.out.println("CHALLENGE");
			String PlayerName =  msg.getFromPlayer().getName();
			String ChallengeName = msg.getContent().toString();
			System.out.println("发送请求挑战玩家"+PlayerName);
			System.out.println("被挑战玩家"+ChallengeName);
//			String list_PlayerName[] = ServerConfig.getHostNamesList().split(":");
//        	for(int i=0;i<list_PlayerName.length;i++){
//        		//cBox.getItems().addAll(list_PlayerName[i]);
//        		if(list_PlayerName[i].equals(ChallengeName)){
//        			rspMsg.setStatus(3);
//        			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(rspMsg),Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//        			socket.send(pout);
//        		}
//        	}
			server.Player ChallengePlayer = server.ServerConfig.getPlayer(ChallengeName);
			rspMsg.setStatus(3);
			DatagramPacket pout  = new DatagramPacket(server.Message.convertToBytes(rspMsg), server.Message.convertToBytes(rspMsg).length,ChallengePlayer.getAddress());
			socket.send(pout);
//			if(ServerConfig.containHost(PlayerName)){
//				rspMsg.setStatus(2);
//				System.out.println("擂台已存在");
//			}else{
//				rspMsg.setStatus(1);
//				System.out.println("擂台不存在");
//				ServerConfig.addHost(msg.getFromPlayer().getName(),msg.getFromPlayer());
//			}
//			rspMsg.setStatus(1);
//			rspMsg.setContent(ServerConfig.getHostNamesList());
//			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(rspMsg),Message.convertToBytes(rspMsg).length,msg.getFromPlayer().getAddress());
//			System.out.println(msg.getFromPlayer().getAddress());
//			socket.send(pout);
//			
		} else if(msg.getMsgType() == server.MessageType.CHALLENGE_RSP){
			
		} else if(msg.getMsgType()== server.MessageType.PLAY){

		}	
      }catch(Exception e) {
			// TODO: handle exception
	  }
    }
}