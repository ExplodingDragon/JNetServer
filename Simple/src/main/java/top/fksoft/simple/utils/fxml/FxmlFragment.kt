package top.fksoft.simple.utils.fxml

import javafx.scene.Node
import java.net.URL


abstract class FxmlFragment : Fragment() {

    abstract val fxmlUrl:URL

    abstract val controller :Controller

    override fun onCreate(args: String) {
        super.onCreate(args)
    }

    override fun <T : Node> findNodeById(id: String): T {
        controller.javaClass.
        return super.findNodeById(id)
    }
}