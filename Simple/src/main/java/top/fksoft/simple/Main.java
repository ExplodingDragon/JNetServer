package top.fksoft.simple;

import javafx.application.Application;
import javafx.stage.Stage;
import jdkUtils.logcat.Logger;

/**
 * @author Explo
 */
public class Main extends Application {
    private static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("项目的范例已经启动 ！");
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }
}