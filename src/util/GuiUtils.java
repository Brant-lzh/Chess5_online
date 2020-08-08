package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiUtils {
	public static void showAlert(String msg){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.show();
	}
    public static String getCurrentTimeString(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return format.format(date);
    }
    public static String getTimeString(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("hh:mm:ss");
        return format.format(date);
    }
}
