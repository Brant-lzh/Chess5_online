package client;

import java.awt.Font;
import java.awt.TextField;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Optional;

import server.Message;
import server.MessageType;
import server.Player;
import server.Point;
import server.ServerConfig;
import util.GuiUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @version V1.0
 * @Date: 2019/8/20
 * @Description:五子棋在线游戏用户界面主窗口
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */

public class Chess5GUI extends Application {

    Chess5Pane pane;//五子棋盘
    Label lbl_name;//用于显示玩家信息
    Label lbl_status, lbl_jifen, lbl_duanwei;
    ChoiceBox<String> cBox;//用于擂主列表
    Button btn_join_game, btn_join_host, btn_update_host, btn_challenge, btn_alpha, btn_guanzhan, btn_update_player, btn_renew, btn_send,btn_preview,btn_img;
    RadioButton rbtn_white, rbtn_black;
    static Player player;
    static DatagramSocket s = null;
    TextArea ta_qunliao, ta_siliao, ta_sends;
    ComboBox<String> cBox_send_type;
    ComboBox<String> cBox_player;//用于玩家列表

    public Chess5GUI() {
        try {
//			start(new Stage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO Auto-generated method stub

        pane = new Chess5Pane();
        pane.setMouseTransparent(true);//屏蔽棋盘上鼠标事件
        //pane.setCompetition(competition);
        FlowPane fPane_01 = new FlowPane();
        fPane_01.setAlignment(Pos.BOTTOM_CENTER);

//		for(int n=0; n<5; n++){
//			Circle c = new Circle(20-n*2, Color.BLACK);
//			fPane_01.getChildren().add(c);
//		}


        Button btn_join_game = new Button("加入游戏");
        btn_join_game.setOnAction(this::btnJoinGameHandler);

        fPane_01.getChildren().add(btn_join_game);
//		for(int n=0; n<5; n++){
//			Circle c = new Circle(12+n*2, Color.WHITE);
//			fPane_01.getChildren().add(c);
//		}

        FlowPane fPane_title = new FlowPane();
        fPane_title.setAlignment(Pos.BOTTOM_CENTER);
        fPane_title.setMinHeight(70);

        Label lb_title = new Label("五 子 棋 游 戏 ");
        lb_title.setStyle("-fx-font-size: 40;-fx-text-fill: white;");
        fPane_title.getChildren().add(lb_title);


        btn_join_host = new Button("加入擂台");
        btn_join_host.setDisable(true);
        btn_join_host.setOnAction(this::btnJoinHostHandler);

        btn_update_host = new Button("更新擂台");
        btn_update_host.setDisable(true);
        btn_update_host.setOnAction(this::btnUpdateHostHandler);

        cBox = new ChoiceBox<String>();

        btn_challenge = new Button("挑战");
        btn_challenge.setDisable(true);
        btn_challenge.setOnAction(this::btnChallengeHandler);

        btn_renew = new Button("重新准备");
        btn_renew.setDisable(true);
        btn_renew.setOnAction(this::btnRenewHandler);

        btn_alpha = new Button("人机对战");
        btn_alpha.setDisable(true);
        btn_alpha.setOnAction(this::btnAlphaHandler);

        btn_update_player = new Button("更新在线玩家列表");
        btn_update_player.setDisable(true);
        btn_update_player.setOnAction(this::btnUpdatePlayerHandler);

        Label player_name = new Label("在线玩家列表:");
        player_name.setStyle("-fx-text-fill: white;-fx-font-size: 20;");
        cBox_player = new ComboBox<String>();
        btn_guanzhan = new Button("观战");
        btn_guanzhan.setDisable(true);
        btn_guanzhan.setOnAction(this::btnGuanzhanHandler);

        ToggleGroup tg = new ToggleGroup();
        rbtn_white = new RadioButton("白子先手");
        rbtn_white.setToggleGroup(tg);
        rbtn_white.setSelected(true);
        rbtn_white.setStyle("-fx-text-fill: white;");
        rbtn_black = new RadioButton("黑子后手");
        rbtn_black.setToggleGroup(tg);
        rbtn_black.setStyle("-fx-text-fill: white;");
        FlowPane fPane_02 = new FlowPane(btn_join_host, btn_update_host, cBox, btn_challenge, btn_alpha);
        fPane_02.setAlignment(Pos.CENTER);
        fPane_02.setHgap(40);
        //fPane_02.setStyle("");

        FlowPane fPane_03 = new FlowPane(rbtn_white, rbtn_black, btn_renew);
        fPane_03.setAlignment(Pos.CENTER);
        fPane_03.setHgap(40);
        fPane_03.setStyle("-fx-border-width:5;-fx-padding: 8;");

        FlowPane fPane_04 = new FlowPane(btn_update_player, player_name, cBox_player, btn_guanzhan);
        fPane_04.setAlignment(Pos.CENTER);
        fPane_04.setHgap(40);
        fPane_04.setStyle("-fx-border-width:5;-fx-padding: 8;");


        lbl_status = new Label("未加入游戏......");
        FlowPane fPane_status = new FlowPane(lbl_status);
        fPane_status.setAlignment(Pos.CENTER);
        fPane_status.setStyle("-fx-border-color:#f5cc8a;-fx-background-color:#fff4e1;-fx-border-width:5;-fx-font-size: 24;");


        VBox box = new VBox(fPane_title, fPane_01);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setStyle("-fx-border-color:#65420a;-fx-background-color:#55270f;-fx-border-width:5");

        VBox boxChildren = new VBox();
        boxChildren.setAlignment(Pos.CENTER);
        boxChildren.setSpacing(10);
        boxChildren.setStyle("-fx-border-color:#65420a;-fx-background-color:#55270f;-fx-border-width:5");


        //Label lbl_player_name = new Label("昵称 ：高手搜索");
        lbl_name = new Label("昵称 ：......");
        lbl_jifen = new Label("积分： .....");
        lbl_duanwei = new Label("段位：......");
        VBox vb_player_info = new VBox(lbl_name, lbl_jifen, lbl_duanwei);
        vb_player_info.setStyle("-fx-border-color:#f5cc8a;-fx-background-color:#f5cc8a;-fx-border-width:5;-fx-font-size: 24;");
        /* 群聊窗口 */
        ta_qunliao = new TextArea();
        ta_qunliao.setPrefSize(400, 180);
        ScrollPane pane_qunliao_info = new ScrollPane(ta_qunliao);
        TitledPane pane_qunliao = new TitledPane("大厅", pane_qunliao_info);

        /* 私聊窗口 */
        ta_siliao = new TextArea();
        ta_siliao.setPrefSize(400, 180);
        ScrollPane pane_siliao_info = new ScrollPane(ta_siliao);
        TitledPane pane_siliao = new TitledPane("聊天窗口", pane_siliao_info);

        /* 发送窗口 */
        ta_sends = new TextArea();
        ta_sends.setPrefSize(400, 100);
        ScrollPane pane_sends_info = new ScrollPane(ta_sends);
        TitledPane pane_sends = new TitledPane("发送窗口", pane_sends_info);
        /* 发送和按钮事件 */
        btn_send = new Button("发送");
        btn_send.setDisable(true);
        btn_send.setOnAction(this::btnSend);
        /* 关闭和按钮事件 */
//        Button btn_close = new Button("关闭");
//        btn_close.setOnAction(this::btnClose);
        /* 发送图片和按钮事件 */
        btn_img = new Button("选择图片");
        btn_img.setDisable(true);
        //btn_img.setOnAction(this::btnImg);
        btn_preview = new Button("预览图片");
        btn_preview.setDisable(true);
        //btn_preview.setOnAction(this::btnPreview);
        /* 窗口抖动 */
//        Button btn_shake = new Button("窗口抖动");
        //btn_shake.setOnAction(this::btnShake);
        /* 按钮大小 */
        btn_send.setPrefSize(70, 30);
//        btn_close.setPrefSize(70, 30);

        cBox_send_type = new ComboBox<String>();
        cBox_send_type.getItems().addAll("群聊");
        cBox_send_type.setValue("群聊");
        //cBox_send_type.getItems().
        FlowPane pane_sends_zujian = new FlowPane();
        pane_sends_zujian.setPadding(new Insets(11, 12, 13, 14));
        pane_sends_zujian.setHgap(5);//设置控件之间的垂直间隔距离
        pane_sends_zujian.setVgap(5);//设置控件之间的水平间隔距离

        pane_sends_zujian.getChildren().addAll(btn_send, cBox_send_type, btn_img, btn_preview);


        VBox vb_r = new VBox(vb_player_info, pane_qunliao, pane_siliao, pane_sends, pane_sends_zujian);

        HBox hbox = new HBox();

        VBox vb_l = new VBox(fPane_02, fPane_03, fPane_04, fPane_status, pane);

        //hbox.getChildren(box,pane_02);
        hbox.getChildren().add(vb_l);
        //hbox.getChildren().add(boxChildren);
        hbox.getChildren().add(vb_r);
        box.getChildren().add(hbox);

        Scene scene = new Scene(box, 1000, 888);
        stage.setScene(scene);
        stage.setTitle("五子棋");
        stage.show();
    }

    /**
     * 当玩家点击“加入游戏”按钮后，会响应该方法进行处理：会弹出对话框输入玩家的昵称；发送加入游戏的信息给服务器，
     * 并等待服务器回复确认，同时更新擂台列表信息
     *
     * @param e
     */
    public void btnJoinGameHandler(ActionEvent e) {
        //pane.setMouseTransparent(false);
        String PlayerName = Chess5Utils.getPlayName();

        try {
            System.out.println(ClientConfig.LOCAL_ADDR);
            s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            //SocketAddress LOCAL_ADDR = new InetSocketAddress("127.0.0.1", 5005);
            //System.out.println(LOCAL_ADDR);
            //s = new DatagramSocket(LOCAL_ADDR);
            player = new Player(PlayerName, ClientConfig.LOCAL_ADDR, 0);
            Message joinGame = new Message();
            joinGame.setMsgType(1);
            joinGame.setFromPlayer(player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(joinGame), Message.convertToBytes(joinGame).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
            DatagramPacket pin = new DatagramPacket(new byte[1024], 1024);
            s.receive(pin);//接收数据
            //System.out.println(pin);
            Message msg = (Message) Message.convertToObj(pin.getData(), 0, pin.getLength());
            if (msg.getStatus() == 1) {
                Chess5Utils.showAlert("加入成功！");
                lbl_name.setText("昵称 ：" + PlayerName);
                lbl_status.setText("空闲中.......");
                lbl_jifen.setText("积分： " + player.getScore());
                lbl_duanwei.setText("段位： 菜鸟");
            } else {
                Chess5Utils.showAlert("加入失败，昵称已经存在！");
            }
            String list_PlayerName[] = msg.getContent().toString().split(":");
            for (int i = 0; i < list_PlayerName.length; i++) {
                cBox.getItems().addAll(list_PlayerName[i]);
            }
            cBox.setValue(list_PlayerName[0]);
            //s.close();
//	        Platform.runLater(new Runnable() {
//	            public void run() {
//	            	btn_join_game.setDisable(true);
//	            }
//	        });

            btn_challenge.setDisable(false);
            btn_join_host.setDisable(false);
            btn_alpha.setDisable(false);
            btn_guanzhan.setDisable(false);
            btn_renew.setDisable(false);
            btn_update_player.setDisable(false);
            btn_update_host.setDisable(false);
            btn_send.setDisable(false);
            btn_preview.setDisable(false);
            btn_img.setDisable(false);

            Chess5ClientThread t1 = new Chess5ClientThread(
                    s,
                    pane,
                    lbl_status,
                    cBox,
                    btn_join_game,
                    btn_join_host,
                    btn_update_host,
                    btn_challenge,
                    rbtn_white,
                    rbtn_black,
                    player,
                    ta_qunliao,
                    ta_siliao,
                    btn_guanzhan,
                    cBox_player,
                    btn_renew,
                    btn_alpha,
                    lbl_jifen,
                    lbl_duanwei,
                    cBox_send_type
            );
            t1.start();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnJoinHostHandler(ActionEvent e) {
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message JoinHost = new Message();
            JoinHost.setMsgType(2);
            JoinHost.setFromPlayer(player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(JoinHost), Message.convertToBytes(JoinHost).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
//			DatagramPacket pin = new DatagramPacket(new byte[512],512);
//			s.receive(pin);//接收数据
//			Message msg = (Message) Message.convertToObj(pin.getData(),0,pin.getLength());
//			if(msg.getStatus() == 1){
//				Chess5Utils.showAlert("加入擂台成功！");
//				cBox.getItems().addAll(player.getName());
//				btn_join_host.setDisable(true);
////				DatagramPacket challenge = new DatagramPacket(new byte[512],512);
////				s.receive(challenge);//接收数据
////				Message challenge_msg = (Message) Message.convertToObj(challenge.getData(),0,challenge.getLength());
////				System.out.println(challenge_msg.toString());
////				Chess5Thread t1 = new Chess5Thread(s);
////				t1.start();
//			}else{
//				Chess5Utils.showAlert("加入擂台失败！");
//			}
            //System.out.println("close");
            //s.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void btnUpdateHostHandler(ActionEvent e) {
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message UpdateHost = new Message();
            UpdateHost.setMsgType(5);
            UpdateHost.setFromPlayer(player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(UpdateHost), Message.convertToBytes(UpdateHost).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void btnChallengeHandler(ActionEvent e) {
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message Challenge = new Message();
            Challenge.setMsgType(MessageType.CHALLENGE);
            if (rbtn_white.isSelected()) {
                player.setColor(2);
            } else {
                player.setColor(1);
            }
            Challenge.setFromPlayer(player);

            //System.out.println(rbtn_white.isSelected());
            String Challenge_Name = cBox.getValue();
//			Challenge.setToPlayer(ServerConfig.getPlayer(Challenge_Name));
            Challenge.setContent(Challenge_Name);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(Challenge), Message.convertToBytes(Challenge).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
//			DatagramPacket pin = new DatagramPacket(new byte[512],512);
//			s.receive(pin);//接收数据
//			Message rspmsg = (Message) Message.convertToObj(pin.getData(),0,pin.getLength());
//			System.out.println(rspmsg);
//			if(rspmsg.getStatus() == 1){
//				Chess5Utils.showAlert("挑战开始！");
//				btn_challenge.setDisable(false);
//			}else{
//				Chess5Utils.showAlert("挑战失败！");
//			}
//			//s.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnAlphaHandler(ActionEvent e) {
        //setMouseTransparent(true);
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message Alpha = new Message();
            Alpha.setMsgType(MessageType.Alpha);
            if (rbtn_white.isSelected()) {
                player.setColor(2);
            } else {
                player.setColor(1);
            }
            Alpha.setFromPlayer(player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(Alpha), Message.convertToBytes(Alpha).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnSend(ActionEvent e) {
        //setMouseTransparent(true);
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message message = new Message();
            String sent_type = cBox_send_type.getValue();
            String msg_content = ta_sends.getText();
            //String Sends_Name = cBox_player.getValue();
            Player Send_Player = new Player(sent_type, null, 0);
//			System.out.println(sent_type);
//			System.out.println(msg_sent);
            message.setFromPlayer(player);
            if (sent_type.equals("群聊")) {
                message.setMsgType(MessageType.Message_qunliao);
            } else {
                message.setMsgType(MessageType.Message_siliao);
                message.setToPlayer(Send_Player);
                ta_siliao.appendText(Chess5Utils.getTimeString() + "  " + player.getName() + " : " + msg_content + "\n");
            }
            //System.out.println(rbtn_white.isSelected());
//			String Challenge_Name = cBox.getValue();
//			Challenge.setToPlayer(ServerConfig.getPlayer(Challenge_Name));
            message.setContent(msg_content);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(message), Message.convertToBytes(message).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
            ta_sends.clear();
//			DatagramPacket pin = new DatagramPacket(new byte[512],512);
//			s.receive(pin);//接收数据
//			Message rspmsg = (Message) Message.convertToObj(pin.getData(),0,pin.getLength());
//			System.out.println(rspmsg);
//			if(rspmsg.getStatus() == 1){
//				Chess5Utils.showAlert("挑战开始！");
//				btn_challenge.setDisable(false);
//			}else{
//				Chess5Utils.showAlert("挑战失败！");
//			}
//			//s.close();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnGuanzhanHandler(ActionEvent e) {
        //setMouseTransparent(true);
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message message = new Message();
            message.setMsgType(MessageType.GUANZHAN);
            String guanzhan_player = cBox_player.getValue();
            Player Guanzhan_Player = new Player(guanzhan_player, null, 0);
            message.setFromPlayer(player);
            message.setToPlayer(Guanzhan_Player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(message), Message.convertToBytes(message).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnUpdatePlayerHandler(ActionEvent e) {
        //setMouseTransparent(true);
        try {
            //DatagramSocket s = new DatagramSocket(ClientConfig.LOCAL_ADDR);
            Message UpdatePlayer = new Message();
            UpdatePlayer.setMsgType(MessageType.UPDATE_PLAYER);
            UpdatePlayer.setFromPlayer(player);
            DatagramPacket pout = new DatagramPacket(Message.convertToBytes(UpdatePlayer), Message.convertToBytes(UpdatePlayer).length, ClientConfig.SERVER_ADDR);
            s.send(pout);//发送数据
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void btnRenewHandler(ActionEvent e) {
        pane.clear();
        btn_alpha.setDisable(false);
        btn_challenge.setDisable(false);
        btn_guanzhan.setDisable(false);
        lbl_status.setText("空闲中.......");
        cBox_send_type.getItems().clear();
        cBox_send_type.getItems().add("群聊");
        cBox_send_type.setValue("群聊");
    }


    public static void main(String[] args) {
        //Chess5Utils.denglu();
        Application.launch(args);
    }


}
