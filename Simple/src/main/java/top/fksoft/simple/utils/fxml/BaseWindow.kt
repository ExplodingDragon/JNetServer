package top.fksoft.simple.utils.fxml

import javafx.scene.Node

interface BaseWindow {

    fun onCreate(args:String)

    fun <T :Node> findNodeById(id:String):T

    fun onShow()

    fun onHide()

    fun onDestroy()

}