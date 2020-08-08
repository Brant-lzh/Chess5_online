package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import server.Message;
import client.MessageType;
import server.Player;
import server.Point;
import server.ServerConfig;
import util.GuiUtils;

public class Chess5ClientThread extends Thread {
	
	//private Socket clientSocket;
	Chess5Pane pane;//五子棋盘
	Label lbl_status,lbl_jifen,lbl_duanwei ;//用于显示玩家信息
	ChoiceBox<String> cBox;//用于擂主列表
	Button btn_join_game, btn_join_host,btn_update_host,btn_challenge,btn_guanzhan,btn_renew,btn_alpha;
	RadioButton rbtn_white, rbtn_black;
	Player player;
	DatagramSocket socket = null;
	int isChallenge;
	TextArea ta_qunliao,ta_siliao;
	Competition competition;
	ComboBox<String> cBox_player;
	ComboBox<String> cBox_send_type;
	//Socket clientSocket=null;
	//ServerSocket listenSocket=null;
	public Chess5ClientThread (
			DatagramSocket socket,
			Chess5Pane pane,
			Label lbl_status,
			ChoiceBox<String> cBox,
			Button btn_join_game,
			Button btn_join_host,
			Button btn_update_host,
			Button btn_challenge,
			RadioButton rbtn_white,
			RadioButton rbtn_black,
			Player player,
			TextArea ta_qunliao,
			TextArea ta_siliao,
			Button btn_guanzhan,
			ComboBox<String> cBox_player,
			Button btn_renew,
			Button btn_alpha,
			Label lbl_jifen,
			Label lbl_duanwei,
			ComboBox<String> cBox_send_type
			){
		//super(name);
		this.socket=socket;
		this.pane=pane;
		this.lbl_status=lbl_status;
		this.cBox=cBox;
		this.btn_join_game=btn_join_game;
		this.btn_join_host=btn_join_host;
		this.btn_update_host=btn_update_host;
		this.btn_challenge=btn_challenge;
		this.rbtn_white=rbtn_white;
		this.rbtn_black=rbtn_black;
		this.player=player;
		this.ta_qunliao= ta_qunliao;
		this.ta_siliao=ta_siliao;
		this.btn_guanzhan=btn_guanzhan;
		this.cBox_player=cBox_player;
		this.btn_renew= btn_renew;
		this.btn_alpha= btn_alpha;
		this.lbl_jifen=lbl_jifen;
		this.lbl_duanwei=lbl_duanwei;
		this.cBox_send_type=cBox_send_type;
		//this.listenSocket=listenSocket;
	}

	private void update_info() {
		// TODO Auto-generated method stub
		lbl_status.setText("空闲中.......");
		lbl_jifen.setText("积分： "+player.getScore());
		if(player.getScore()>50){
			lbl_duanwei.setText("段位： 新手");
		}else{
			lbl_duanwei.setText("段位： 菜鸟");
		}
	}

	public void run(){
		//int isChallenge;
	try {
		while(true){
			DatagramPacket pin = new DatagramPacket(new byte[2048],2048);
			socket.receive(pin);
			Message rspMsg = (Message) Message.convertToObj(pin.getData(),0,pin.getLength());

			if(rspMsg.getMsgType()== MessageType.JOIN_HOST){
				if(rspMsg.getStatus() == MessageType.SUCCESS){
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	Chess5Utils.showAlert("加入擂台成功！");
							//cBox.getItems().addAll(player.getName());
							btn_join_host.setDisable(true);
			            }
			        });
				}else if(rspMsg.getStatus() == MessageType.FAIL){
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	Chess5Utils.showAlert("加入擂台失败！");
			            }
			        });
				}
		        Platform.runLater(new Runnable() {
		            public void run() {
						cBox.getItems().clear();
						String list_PlayerName[] = rspMsg.getContent().toString().split(":");
			        	for(int i=0;i<list_PlayerName.length;i++){
			        		cBox.getItems().addAll(list_PlayerName[i]);
			        	}
			        	cBox.getSelectionModel().selectFirst();
		            }
		        });
//			}
//			else if(rspMsg.getMsgType()== MessageType.JOIN_GAME){
//				Platform.runLater(new Runnable() {
//		            public void run() {
//				if(rspMsg.getStatus() == MessageType.SUCCESS){
//			      	Chess5Utils.showAlert("加入成功！");
//					lbl_name.setText("我是"+rspMsg.getFromPlayer().getName());
//			      	btn_join_game.setDisable(true);
//				}else{
//					Chess5Utils.showAlert("加入失败，昵称已经存在！");
//				}
//				String list_PlayerName[] = rspMsg.getContent().toString().split(":");
//	        	for(int i=0;i<list_PlayerName.length;i++){
//	        		cBox.getItems().addAll(list_PlayerName[i]);
//	        	}
//	        	cBox.setValue(list_PlayerName[0]);
//	        	btn_challenge.setDisable(false);
//	        	btn_join_host.setDisable(false);
//		            }
//		        });
			}else if(rspMsg.getMsgType()== MessageType.UPDATE_HOST){
				if(rspMsg.getStatus() == MessageType.SUCCESS){
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	Chess5Utils.showAlert("更新擂台成功！");
			            	cBox.getItems().clear();
							String list_PlayerName[] = rspMsg.getContent().toString().split(":");
				        	for(int i=0;i<list_PlayerName.length;i++){
				        		cBox.getItems().addAll(list_PlayerName[i]);
				        	}
				        	cBox.getSelectionModel().selectFirst();
			            }
			        });
				}else{
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	Chess5Utils.showAlert("更新擂台失败！");
			            }
			        });
				}
			}else if(rspMsg.getMsgType()== MessageType.CHALLENGE){
		        Platform.runLater(new Runnable() {
		            public void run() {
						isChallenge =  Chess5Utils.IsChallenge(rspMsg.getFromPlayer());
						Message msg = new Message();
						if(isChallenge == 1){
							msg.setStatus(MessageType.SUCCESS);
							competition = new Competition();
							competition.setLocalPlayer(player);
							competition.setRemotePlayer(rspMsg.getFromPlayer());
							System.out.println("CHALLENGE"+competition.getLocalPlayer());
							System.out.println("CHALLENGE"+competition.getRemotePlayer());
							pane.setCompetition(competition);
				        	if(rspMsg.getFromPlayer().getColor() == 1){
			        			pane.setColor(2);
			        			rbtn_white.setSelected(true);
			        			rbtn_black.setSelected(false);
			        			pane.setMouseTransparent(false);
			        		}else if(rspMsg.getFromPlayer().getColor() == 2){
			        			pane.setColor(1);
			        			rbtn_white.setSelected(false);
			        			rbtn_black.setSelected(true);
			        		}
				        	//Chess5GUI.playerList(rspMsg.getFromPlayer().getName());
//				        	cBox_player.setValue(rspMsg.getFromPlayer().getName().toString());
//				        	System.out.println("123456"+rspMsg.getToPlayer().getName());
//				        	cBox_player.setValue("不不不");
							btn_challenge.setDisable(true);
							btn_guanzhan.setDisable(true);
							btn_renew.setDisable(true);
							btn_alpha.setDisable(true);
							lbl_status.setText("游戏中.......");

				        	//cBox_player.setValue(rspMsg.getFromPlayer().getName().toString());
							cBox_send_type.getItems().add(rspMsg.getFromPlayer().getName());
							cBox_send_type.setValue(rspMsg.getFromPlayer().getName());
						}else{
							msg.setStatus(MessageType.FAIL);
						}
						msg.setMsgType(MessageType.CHALLENGE_RSP);
						msg.setToPlayer(rspMsg.getFromPlayer());
						msg.setFromPlayer(player);
		    			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(msg),Message.convertToBytes(msg).length,ServerConfig.SERVER_ADDR);
		    			try {
							socket.send(pout);
							System.out.println("发送接收挑战信息");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        });

			}else if(rspMsg.getMsgType()== MessageType.CHALLENGE_RSP){
		        Platform.runLater(new Runnable() {
		            public void run() {
		            	if(rspMsg.getStatus()==MessageType.SUCCESS){
							Chess5Utils.showAlert(rspMsg.getFromPlayer().getName()+"，接收挑战！");
							competition = new Competition();
							competition.setLocalPlayer(player);
							competition.setRemotePlayer(rspMsg.getFromPlayer());
							System.out.println("CHALLENGE_RSP"+competition.getLocalPlayer());
							System.out.println("CHALLENGE_RSP"+competition.getRemotePlayer());
							pane.setCompetition(competition);
							pane.setColor(rspMsg.getToPlayer().getColor());
							//pane.setMouseTransparent(false);
							if(rspMsg.getToPlayer().getColor()==2){
			        			pane.setMouseTransparent(false);
			        		}
				        	//Chess5GUI.playerList(rspMsg.getToPlayer().getName());
							//cBox_player.setValue(rspMsg.getToPlayer().getName().toString());
							cBox_send_type.getItems().add(rspMsg.getFromPlayer().getName());
							cBox_send_type.setValue(rspMsg.getFromPlayer().getName());
							btn_challenge.setDisable(true);
							btn_guanzhan.setDisable(true);
							btn_renew.setDisable(true);
							btn_alpha.setDisable(true);
							lbl_status.setText("游戏中.......");

		            	}else if(rspMsg.getStatus()==MessageType.FAIL){
							Chess5Utils.showAlert(rspMsg.getFromPlayer().getName()+"，不接收挑战！");
		            	}
		            }
		        });
			}else if(rspMsg.getMsgType()== MessageType.PLAY){
				System.out.println("123");
				System.out.println(rspMsg.toString());
				//Point point = new Point();
				Point point = (Point) rspMsg.getContent();
		        Platform.runLater(new Runnable() {
		            public void run() {
		            	//pane.setMouseTransparent(false);

						pane.drawChess(point.row, point.column, point.color);
						//System.out.println("Judgement");
						pane.Judgement();
						//pane.drawChess(point.column,point.row , point.color);
//						System.out.println(pane.getData());
//						//pane.getData();
//						 for(int x=0; x<pane.getData().length; x++) {
//					            for(int y=0; y<pane.getData()[x].length; y++) {
//					                System.out.print(pane.getData()[x][y]+" ");
//					            }
//					            System.out.println();
//					        }


		            }
	        });
				System.out.println(point.toString());
//		        Platform.runLater(new Runnable() {
//		            public void run() {
//		            	if(rspMsg.getStatus()==MessageType.SUCCESS){
//							Chess5Utils.showAlert(rspMsg.getFromPlayer().getName()+"，接收挑战！");
//		            	}else if(rspMsg.getStatus()==MessageType.FAIL){
//							Chess5Utils.showAlert(rspMsg.getFromPlayer().getName()+"，不接收挑战！");
//		            	}
//		            }
//		        });
			}else if(rspMsg.getMsgType() == MessageType.Message_qunliao){
				System.out.println(rspMsg.toString());
		        Platform.runLater(new Runnable() {
		            public void run() {
		    	        ta_qunliao.appendText(Chess5Utils.getTimeString()+"  "+ rspMsg.getFromPlayer().getName()+" : "+rspMsg.getContent()+"\n");
		            }
		        });
			}else if(rspMsg.getMsgType() == MessageType.Message_siliao){
				Platform.runLater(new Runnable() {
		            public void run() {
		    	        ta_siliao.appendText(Chess5Utils.getTimeString()+"  "+ rspMsg.getFromPlayer().getName()+" : "+rspMsg.getContent()+"\n");
		            }
		        });
			}else if(rspMsg.getMsgType() == MessageType.UPDATE_PLAYER){
				if(rspMsg.getStatus() == MessageType.SUCCESS){
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	//Chess5Utils.showAlert("更新在线玩家列表成功！");
			            	cBox_player.getItems().clear();
							String list_PlayerName[] = rspMsg.getContent().toString().split(":");
				        	for(int i=0;i<list_PlayerName.length;i++){
				        		cBox_player.getItems().addAll(list_PlayerName[i]);
				        	}
				        	cBox_player.getSelectionModel().selectFirst();
			            }
			        });
				}else{
			        Platform.runLater(new Runnable() {
			            public void run() {
			            	Chess5Utils.showAlert("更新在线玩家列表失败！");
			            }
			        });
				}
			}else if(rspMsg.getMsgType() == MessageType.GUANZHAN){
				Platform.runLater(new Runnable() {
		            public void run() {
						//isChallenge =  Chess5Utils.IsChallenge(rspMsg.getFromPlayer());
						Message msg = new Message();
//						if(isChallenge == 1){
//							msg.setStatus(MessageType.SUCCESS);
//							competition = new Competition();
//							competition.setLocalPlayer(player);
//							competition.setRemotePlayer(rspMsg.getFromPlayer());
//							System.out.println("CHALLENGE"+competition.getLocalPlayer());
//							System.out.println("CHALLENGE"+competition.getRemotePlayer());
//							pane.setCompetition(competition);
//				        	if(rspMsg.getFromPlayer().getColor() == 1){
//			        			pane.setColor(2);
//			        			pane.setMouseTransparent(false);
//			        		}else if(rspMsg.getFromPlayer().getColor() == 2){
//			        			pane.setColor(1);
//
//			        		}
//						}else{
//							msg.setStatus(MessageType.FAIL);
//						}
						msg.setMsgType(MessageType.GUANZHAN_RSP);
						msg.setToPlayer(rspMsg.getFromPlayer());
						msg.setFromPlayer(player);
						msg.setContent(pane.getData());

		    			DatagramPacket pout  = new DatagramPacket(Message.convertToBytes(msg),Message.convertToBytes(msg).length,ServerConfig.SERVER_ADDR);
						 for(int x=0; x<pane.getData().length; x++) {
					            for(int y=0; y<pane.getData()[x].length; y++) {
					                System.out.print(pane.getData()[x][y]+" ");
					            }
					            System.out.println();
					        }
		    			try {
							socket.send(pout);
							System.out.println("发送接收观战信息");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        });

			}else if(rspMsg.getMsgType() == MessageType.GUANZHAN_RSP){
				Platform.runLater(new Runnable() {
		            public void run() {
		            	Competition competition = new Competition();
		            	rspMsg.getContent();
						 pane.guanzhan((int[][]) rspMsg.getContent());
						 btn_challenge.setDisable(true);
						 btn_guanzhan.setDisable(true);
						 btn_alpha.setDisable(true);
						 lbl_status.setText("观战中.......");
						 System.out.println("12313412341234");
		            }
		        });
			}else if(rspMsg.getMsgType() == MessageType.Result){//整个要取反
				pane.setMouseTransparent(true);
				System.out.println("接收到");
				Platform.runLater(new Runnable() {
		            public void run() {
		            	//rspMsg.getContent();
		            	Chess5Utils.showAlert(((int)rspMsg.getContent()==1?"白子":"黑子") + " 胜");//取反
						btn_renew.setDisable(false);
						if(rbtn_white.isSelected()){
							if((int)rspMsg.getContent()==2){
									player.setScore(player.getScore()-100);
								}else{
									player.setScore(player.getScore()+100);
								}
						}else{
							if((int)rspMsg.getContent()==1){
								player.setScore(player.getScore()-100);
							}else{
								player.setScore(player.getScore()+100);
							}
						}
						update_info();

		            }
		        });
			}else if(rspMsg.getMsgType() == MessageType.GUANZHAN_PLAY){
				Point point = (Point) rspMsg.getContent();
		        Platform.runLater(new Runnable() {
		            public void run() {
						pane.JudgementSelf();
						pane.drawChess(point.row, point.column, point.color);
						pane.JudgementSelf();
		            }
	        });
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//接收数据
		
		
	}

}
