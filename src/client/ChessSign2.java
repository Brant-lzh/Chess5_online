package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import week_13_server1.Message;
import week_13_server1.MessageType;
import week_13_server1.MyUtils;
import week_13_server1.SecurityUtils;
import week_13_server1.Status;
import week_13_server1.User;

/**

 * @Date: 2019/11/20
 * @version V1.0
 * @Description:用户登录与注册界面窗口:基于SSL Socket通信,登录或注册成功后再弹出聊天主窗口
 * @Project: 网络编程技术
 * @Copyright: All rights reserved
 */
public class ChessSign2 extends Application {
	boolean result = false;
	boolean result2 = false;
	SSLSocket s = null;
	SSLSocketFactory factory = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	Button btn_regist, btn_login;
	TextField tf_name;
	PasswordField pf;
	Stage initStage;

	@Override
	public void start(Stage stage) throws Exception {

		try {
			// 获取jks类型的密钥库，ks保存本主机的公钥和私钥的密钥库,tks是保存来访者的公钥的密钥库
			KeyStore ks = KeyStore.getInstance("JKS");// 本机的密钥库
			KeyStore tks = KeyStore.getInstance("JKS");// 可信任的其它来访主机的公钥库

			// 加载密钥库文件，需要提供口令
			ks.load(new FileInputStream(ClientInfo.keyStorePath), ClientInfo.password_ks.toCharArray());
			tks.load(new FileInputStream(ClientInfo.trustStorePath), ClientInfo.password_ks.toCharArray());

			// 为相应的密钥库创建一个KeyManagerFactory，TrustManagerFactory
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

			kmf.init(ks, ClientInfo.password_pk.toCharArray());
			tmf.init(tks);

			// 为使用的算法创建一个SSLContext,
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			// context.init(null,tmf.getTrustManagers(),
			// null);//如果服务器不需要验证客户端，则可以不用初始化客户的密钥库

			factory = context.getSocketFactory();

		} catch (IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException
				| KeyStoreException | UnrecoverableKeyException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		s = (SSLSocket) factory.createSocket(ClientInfo.serverAddr, ClientInfo.serverPort);
		// 注意：先创建输出流对象，再创建输入流对象，服务器则相反
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		System.out.println("Successful to load all the info to initiate client!");
		initStage = stage;
		// TODO Auto-generated method stub
		Label lb_name = new Label("用户:");
		tf_name = new TextField();

		Label lb_passwd = new Label("密码：");
		pf = new PasswordField();

		btn_regist = new Button("注册");
		btn_regist.setOnAction(this::btnRegistHandler);
		btn_login = new Button("登录");
		btn_login.setOnAction(this::btnLoginHandler);


		GridPane gPane = new GridPane();
		gPane.setAlignment(Pos.CENTER);
		gPane.setPrefWidth(300);
		gPane.setPrefHeight(200);
		gPane.setVgap(30);
		gPane.setHgap(20);
		gPane.addRow(0, lb_name, tf_name);
		gPane.addRow(1, lb_passwd, pf);
		gPane.addRow(2, btn_regist, btn_login);

		Scene scene = new Scene(gPane);
		stage.setTitle("五子棋小游戏");
		stage.setScene(scene);
		stage.show();
	}

	public void btnRegistHandler(ActionEvent e) {

		if (!register()) {
			return;
		}

	}

	public void btnLoginHandler(ActionEvent e) {

		if (!login()) {
			return;
		} else {
			try {
				new Chess5GUI();// 打开聊天窗口
				initStage.close();// 关闭注册登录窗口
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			;

		}

	}

	public boolean register() {

		try {
			System.out.println("Client is ready!");
			String passwd = SecurityUtils.getHash(pf.getText(), "MD5");// 应用MD5加密密码
			User user = new User(tf_name.getText(), passwd);
			Message msg = new Message();
			msg.setMsgType(MessageType.MSG_REG);
			msg.setContent(user);
			oos.writeObject(msg);
			oos.flush();
			System.out.println("sent: " + msg);
			msg = (Message) ois.readObject();
			System.out.println("received: " + msg);
			if (msg.getStatus() == Status.SUCCESS) {
				result = true;
				MyUtils.showAlert("注册成功！");
			} else {
				MyUtils.showAlert("注册失败，请重新注册！");
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public boolean login() {

		try {
			System.out.println("Client is ready!");
			String passwd = SecurityUtils.getHash(pf.getText(), "MD5");// 应用MD5加密密码
			User user = new User(tf_name.getText(), passwd);
			Message msg = new Message();
			msg.setMsgType(MessageType.MSG_LOG);
			msg.setContent(user);
			oos.writeObject(msg);
			oos.flush();
			System.out.println("sent: " + msg);
			msg = (Message) ois.readObject();
			System.out.println("received: " + msg);
			oos.close();
			ois.close();
			if (msg.getStatus() == Status.SUCCESS) {
				result2 = true;
			} else {
				MyUtils.showAlert("登录失败，请重新登录！");
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result2;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
