package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @Date: 2019/8/20
 * @version V1.0
 * @Description:用于封装信息
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class Message implements Serializable {
	int msgType;//信息类型，参看类MessageType中的定义
	int status;//信息的状态：分为成功和失败 1 2
	Object content;//信息的内容
	Player fromPlayer;//信息的发送方
	Player toPlayer;//信息的接收方
	
	public Player getFromPlayer() {
		return fromPlayer;
	}
	public void setFromPlayer(Player fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	public Player getToPlayer() {
		return toPlayer;
	}
	public void setToPlayer(Player toPlayer) {
		this.toPlayer = toPlayer;
	}	
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public String toString(){
		return "["+msgType +"," + status + ", " + fromPlayer + ", " + toPlayer + ", " + content;
	}
	
	/**
	 * 将信息Message的对象转换成字节数组（传递给DatagramPacket），用于socket通信
	 * @param obj
	 * @return
	 */
	public static byte[] convertToBytes(server.Message obj){
		try(
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream ois = new ObjectOutputStream(baos);
			) {
			ois.writeObject(obj);
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * 将字节数组（从DatagramPacket中获取）转换成Message的对象。
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 */
	public static server.Message convertToObj(byte[] bytes, int offset, int length){
		try (
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
		){
			return (server.Message) ois.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;	
	}
}
