package top.fksoft.simple.utils.fxml

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.layout.VBox
import jdkUtils.logcat.Logger
import java.lang.NullPointerException
import java.lang.RuntimeException


abstract class Fragment : BaseWindow {

    private val privateLogcat = Logger.getLogger(Fragment::class)
    protected val logger = Logger.getLogger(javaClass)

    private var root = VBox()
    protected lateinit var title: String
    private val rootWidth = ChangeListener { _: ObservableValue<out Number>, old: Number, newValue: Number ->
        privateLogcat.debug("$title resize width: ${newValue.toDouble()}")
        updateWidth(oldValue = old.toDouble(), newValue = newValue.toDouble())
    }
    private val rootHeight = ChangeListener { _: ObservableValue<out Number>, old: Number, newValue: Number ->
        privateLogcat.debug("$title resize height: ${newValue.toDouble()}")
        updateHeight(oldValue = old.toDouble(), newValue = newValue.toDouble())
    }


    override fun onCreate(args: String) {
        title = "${javaClass.name}@${hashCode().toString(16).toUpperCase()}"
        privateLogcat.debug("$title onCreated.")
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
        root.widthProperty().addListener(rootWidth)
        root.heightProperty().addListener(rootHeight)

    }

    protected fun setContentNode(child: Node) {

    }

    override fun onShow() {
        privateLogcat.debug("$title show.")
    }

    override fun onHide() {
        privateLogcat.debug("$title hide.")
    }

    override fun <T : Node> findNodeById(id: String): T {
        val lookup = root.lookup(id) ?: throw NullPointerException("id{$id} not found!")
        try {
            return lookup as T
        } catch (e: Exception) {
            throw RuntimeException("find id{$id} but there is ${lookup.javaClass.name}. ", e)
        }
    }


    override fun onDestroy() {
        privateLogcat.debug("$title onDestroy")
        root.widthProperty().removeListener(rootWidth)
        root.heightProperty().removeListener(rootHeight)
    }

    protected open fun updateWidth(oldValue: Double, newValue: Double) {

    }

    protected open fun updateHeight(oldValue: Double, newValue: Double) {

    }

    final fun load() = root

}