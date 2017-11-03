import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class aplication extends Application {
	BorderPane root = new BorderPane();
	TextArea txarea = new TextArea();
	BufferedReader out;
	BufferedWriter in;
	Socket soket; 
	String ipAdress = "10.0.0.1";
	String s = "";
	String message = "";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("CHAT-Client");
		root = border();
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();
	}
	
	public HBox login() {
		Label label1 = new Label("Name:");
		TextField textField = new TextField ();
		Button login = new Button("Login");
		HBox hb = new HBox();
		hb.getChildren().addAll(label1, textField, login);
		hb.setSpacing(10);
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//root.getChildren().
				root.setBottom(chat());
				soket = new Socket();
				try {
					soket.connect(new InetSocketAddress(ipAdress, 1234));
					s += "Connected to localhost in port 1234\n";
					
					out = new BufferedReader(new InputStreamReader(soket.getInputStream()));
					in = new BufferedWriter(new OutputStreamWriter(soket.getOutputStream()));
					
					s += "Jsem pripojen\n Ted muzes psat\n";
					txarea.setText(s);
					
				}  catch (UnknownHostException unknownHost) {
					System.err.println("You are trying to connect to an unknown host!");
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		
		return hb;
	}
	
	public HBox chat() {
		TextField txField = new TextField();
		txField.setMinSize(600, 10);
		
		Button btn = new Button();
		btn.setText("Send Message");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					in.write(txField.getText());
					in.flush();
					out.readLine();
					txField.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		Button btLogout = new Button("Logout");
		btLogout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					in.close();
					out.close();
					soket.close();
					root.setBottom(login());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		HBox hBox = new HBox();
		hBox.getChildren().addAll(txField,btn,btLogout);
		return hBox;
	}
	
	public BorderPane border() {
		BorderPane chatPane = new BorderPane();
		
		txarea.setMinSize(700, 400);
		txarea.setEditable(false);
		
		txarea.textProperty().addListener(new ChangeListener<Object>() {
			
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		    	
		    	txarea.setScrollTop(Double.MAX_VALUE);
		    	//System.out.println("Hallo");
		    }
		});
		
		ListView<String> list = new ListView<>();
		list.setMaxSize(100, 400);
		ObservableList<String> items = FXCollections.observableArrayList("", "", "", "");
		list.setItems(items);
		
		chatPane.setBottom(login());
		chatPane.setRight(list);
		chatPane.setCenter(txarea);
		return chatPane;
	}

}
