package top.fksoft.simple;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdkUtils.ModConfig;
import jdkUtils.logcat.Logger;
import top.fksoft.simple.controller.UdpController;

/**
 * @author Explo
 */
public class Main extends Application {
    private static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("项目的范例已经启动。");
        ModConfig.setDebug(true);
        logger.debug("调试模式已打开");
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/res/layout/udp.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        UdpController controller = new UdpController();
        fxmlLoader.setController(controller);
        Parent load = fxmlLoader.load();
        Scene value = new Scene(load);
        value.setFill(Color.TRANSPARENT);
        stage.setScene(value);
        stage.setTitle("UDP 测试");
        stage.show();
        controller.init(stage);
    }
}
