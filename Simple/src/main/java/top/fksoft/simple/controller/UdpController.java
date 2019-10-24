package top.fksoft.simple.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jdkUtils.logcat.Logger;
import top.fksoft.bean.NetworkInfo;
import top.fksoft.server.udp.UdpServer;
import top.fksoft.simple.data.DataPacket;
import top.fksoft.simple.data.DataReceiveBinder;
import top.fksoft.simple.data.UserPacket;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class UdpController {
    private Logger logger = Logger.getLogger(this);
    UdpServer udpServer;
    @FXML
    private JFXToggleButton serverStatus;

    @FXML
    private Label localIp;


    @FXML
    private ChoiceBox<String> choose;


    @FXML
    private ListView<String> remoteList;

    @FXML
    private JFXTextField sendIp;

    @FXML
    private JFXTextArea sendData;

    @FXML
    private Label sendStatus;

    @FXML
    private JFXTextArea receive;
    private Stage stage;
    private int port = 34567;
    String host = "239.0.0.255";


    @FXML
    void close(MouseEvent event) {
        if (udpServer != null) {
            udpServer.close();
        }
        stage.close();
    }
    DataPacket dataPacket = new DataPacket ();

    @FXML
    void send(ActionEvent event) {
        dataPacket.data = sendData.getText();
        String text = sendIp.getText();
        if (udpServer.sendPacket(dataPacket,new NetworkInfo(text.split(":")[0],Integer.parseInt(text.split(":")[1])))) {
            sendStatus.setText("发送成功！");
            sendStatus.setTextFill(Color.GREEN);
        }else {
            sendStatus.setText("发送失败！");
            sendStatus.setTextFill(Color.RED);
        }


    }
    @FXML
    void sendUser(MouseEvent event) throws UnknownHostException {
        UserPacket userPacket = new UserPacket();
        userPacket.user.name = "Test";
        userPacket.user.devicesName = InetAddress.getLocalHost().getHostName();
        udpServer.sendPacket(userPacket,new NetworkInfo(host,port));

    }

    @FXML
    void startServer(ActionEvent event) throws Exception {
        if (serverStatus.isSelected()) {
            if (choose.getSelectionModel().isSelected(0)) {
                logger.info("选中了普通模式");
                udpServer = new UdpServer(new DatagramSocket(port));
            } else {
                logger.info("选中了组播模式");
                InetAddress ipAddress = InetAddress.getByName(host);
                MulticastSocket socket = new MulticastSocket(port);
                socket.setTimeToLive(32);
                socket.joinGroup(ipAddress);
                udpServer = new UdpServer(socket);
                sendIp.setText(String.format("%s:%d",ipAddress.getHostAddress(),port));
                sendIp.setEditable(false);
            }
            logger.info("发送开启UDP服务器请求。");
            localIp.setText(String.format("%s - %s:%d", choose.getSelectionModel().getSelectedItem(), InetAddress.getLocalHost().getHostAddress(), udpServer.getLocalPort()));

            udpServer.bindReceive(new DataReceiveBinder<>((networkInfo, packet) -> {
                logger.info("接收到来自" + networkInfo + "的数据包.");
                javafx.application.Platform.runLater(()->{
                        remoteList.getItems().add(((UserPacket)packet).user.toString());
                });
            }, new UserPacket()));
            udpServer.bindReceive(new DataReceiveBinder<>((networkInfo, packet) -> {
                logger.info("接收到来自" + networkInfo + "的数据包.");
                javafx.application.Platform.runLater(()->{
                    receive.setText(((DataPacket)packet).data);
                });
            }, new DataPacket()));
        } else {
            if (udpServer != null) {
                udpServer.close();
                localIp.setText("服务器未开启");
                sendIp.setEditable(true);
            }
        }
    }

    public void init(Stage stage) {
        this.stage = stage;
        choose.getSelectionModel().selectFirst();
        stage.setOnCloseRequest(windowEvent -> {
            close(null);
        });
    }
}
