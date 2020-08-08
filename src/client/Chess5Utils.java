package client;

import java.awt.TextField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;



import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
//
import javafx.util.Pair;
import server.Player;


public class Chess5Utils {
    public static String getTimeString(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("hh:mm:ss");
        return format.format(date);
    }
    
	public static void showAlert(String msg){
		
		Alert alert = new Alert(AlertType.INFORMATION);
//		TextField field = new TextField();
//		//alert.setContentText(field);
//		//field.show();
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.show();
		//return msg;

	}
		// The Java 8 way to get the response value (with lambda expression).
		//result.ifPresent(name -> System.out.println("Your name: " + name));

		// lambda表达式获取输入值
		//result.ifPresent(value -> info.setText(info.getText() + "\nbtn8(lambda) 输入了"+value));
	
	public static int IsChallenge(Player player){
		Alert alert = new Alert(AlertType.CONFIRMATION);

		alert.setGraphic(null);
		//dialog.setTitle("昵称");
		alert.setHeaderText("");
		String chess = null;
		if(player.getColor() == 1){
			chess="黑子";
		}else if(player.getColor() == 2){
			chess="白子";
		}
		alert.setContentText(player.getName()+"，想挑战你!"+chess+"，是否迎战？");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
			System.out.println("OK");
			return 1;
		} else {
		    // ... user chose CANCEL or closed the dialog
			System.out.println("CANCEL");
			return 2;
		}

	}
	
	
//	public static void showInputAlert1(){
//		TextInputDialog dialog = new TextInputDialog();
//		dialog.setGraphic(null);
//		//dialog.setTitle("昵称");
//		dialog.setHeaderText("");
//		dialog.setContentText("请输入你的昵称:");
//		Optional<String> result = dialog.showAndWait();
//		if (result.isPresent()){
//		    System.out.println("Your name: " + result.get());
//		}
//
//	}
	
	public static String getPlayName(){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setGraphic(null);
		//dialog.setTitle("昵称");
		dialog.setHeaderText("");
		dialog.setContentText("请输入你的昵称:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    //System.out.println("Your name: " + result.get());
		}
		return result.get();
	}
	
	public static int denglu(){
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("自定义对话框");
		dialog.setHeaderText("头部内容");

		// 设置头部图片
		//dialog.setGraphic(new ImageView(this.getClass().getResource("favicon64-w.png").toString()));

		ButtonType loginButtonType = new ButtonType("登录");
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField name = new TextField();
	//	name.setPromptText("用户名");
		PasswordField password = new PasswordField();
		password.setPromptText("密码");

		grid.add(new Label("用户名:"), 0, 0);
		//grid.add(name, 1, 0);
		grid.add(new Label("密码:"), 0, 1);
		grid.add(password, 1, 1);

		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// 是哟功能 Java 8 lambda 表达式进行校验
//		name.textProperty().addListener((observable, oldValue, newValue) -> {
//		loginButton.setDisable(newValue.trim().isEmpty() || password.getText().trim().isEmpty());
//		});

		password.textProperty().addListener((observable, oldValue, newValue) -> {
		loginButton.setDisable(newValue.trim().isEmpty() || name.getText().trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// 默认光标在用户名上
		Platform.runLater(() -> name.requestFocus());

		// 登录按钮后，将结果转为username-password-pair
		dialog.setResultConverter(dialogButton -> {
		if (dialogButton == loginButtonType) {
		return new Pair<>(name.getText(), password.getText());
		}
		return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
//		info.setText("btn10\n" + usernamePassword.getKey() + " : " + usernamePassword.getValue());
		});
		return 0;
	}


	
}
