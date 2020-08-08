package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientInfo {
	static String configPath = "configs/client.properties";
	static Properties prop = new Properties();
	static {	
		try {
			prop.load(new FileInputStream(configPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static String serverAddr = (String)prop.get("server_ip");
	static int serverPort = Integer.parseInt((String)prop.get("server_port"));
	static int serverDataPort = Integer.parseInt((String)prop.get("server_data_port"));
	
	//读取私钥的访问密码	
	public static String password_pk = (String)prop.get("private_key_pass");
	//读取密钥库的访问密码
	public static String password_ks = (String)prop.get("keystore_pass");
	
	public static String keyStorePath = (String)prop.get("key_store_path");
	public static String trustStorePath = (String)prop.get("trust_store_path");

}
