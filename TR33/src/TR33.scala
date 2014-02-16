/**
 * Created by ts on 16.02.14.
 */

import swing._
import swing.Dialog._
import java.awt.color
import scala.collection.mutable.Queue
import scala.collection.mutable

import scala.collection._
import swing.event._
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.TextField
import scala.swing.event.Key
import scala.swing.event.KeyPressed

object TR extends App {
  //extends Appextends SwingApplication
  //Taschenrechner... History berechneter Ergebnisse
  //Klammerebenen. 1+(3*5)... 3x5+1 = 16
  //               1+(3x5(... 1+(15+(

  var a: Int = 0
  //Zähler für zurückliegende Berechnungsergebnisse (ungeachtet Klammerebenen)
  var b: Int = 0
  //Zähler für zurückgehen innerhalb der Liste der Berechnungsergebnisse durch btnPrevRes bzw. btnNextRes
  var z: Int = 0
  //Zähler für Klammerebenen
  var stellenE: Float = 0
  var neueEingabe: Boolean = false
  //neueEingabe wir true gesetzt, sobald eine neue Eingabe begonnen werden soll
  var llZahl = mutable.LinkedList(0.0)
  //Liste mit Zwischenergebnissen auf Klammerebene
  //zählen mit zählerKLammer
  var neueZahl = 0.0
  var llVZ = mutable.LinkedList("=")
  //zählen mit zählerKlammer
  var neuesVZ: String = "="
  var label: Label = new Label("12")
  var llErgeb = mutable.LinkedList(0.0)
  //Liste mit vorangegangenen Ergebnissen für prev/nex-Button
  //new Array[Double](1)
  var Mem: Double = 0
  //var (setE, istNeg, setKomma, eIstNeg) = (false, false, false, false)
  var window = new MainFrame
  var (btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9) = (new Button("0"), new Button("1"), new Button("2"), new Button("3"),
    new Button("4"), new Button("5"), new Button("6"), new Button("7"), new Button("8"), new Button("9"))
  var btnC: Button = new Button("C")
  var (btnMPlus, btnRM, btnMDel) = (new Button("M+"), new Button("Rec M"), new Button("Del M"))
  var (btnPlus, btnMin, btnMal, btnDiv, btnPt, btnEnt) = (new Button("+"), new Button("-"), new Button("x"), new Button("/"), new Button("."), new Button("="))
  var (btnEx, btnE, btnPM, btnEPM, btnBack, btnPrevRes, btnNextRes, btnKlaAuf, btnKlaZu) = (new Button("Exit"), new Button("E"), new Button("+/-"), new Button("E+/-"),
    new Button("<"), new Button("<< Ergebn."), new Button("Ergeb. >>"), new Button("("), new Button(")"))
  var textField: TextField = new TextField("0")

  window.contents = new BoxPanel(Orientation.Vertical) {
    contents += label
    contents += textField
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btn1
      contents += btn2
      contents += btn3
      contents += btnPlus
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btn4
      contents += btn5
      contents += btn6
      contents += btnMin
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btn7
      contents += btn8
      contents += btn9
      contents += btnMal
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btnPt
      contents += btn0
      contents += btnBack
      contents += btnDiv
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btnKlaAuf
      contents += btnKlaZu
      //contents += btnBack
      //contents += btnDiv
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btnC
      contents += btnPM
      contents += btnE
      contents += btnEPM
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      //contents += btnC
      contents += btnMPlus
      contents += btnMDel
      contents += btnRM
      contents += btnEnt
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += btnPrevRes
      contents += btnNextRes
      contents += btnEx
    }
  }
  //btnEnt.defaultButton = true
  //btnEnt.background = (0,0,220)
  window.listenTo(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
    btnC, btnMPlus, btnRM, btnMDel,
    btnPlus, btnMin, btnMal, btnDiv, btnPt, btnEnt,
    btnEx, btnE, btnPM, btnEPM, btnBack, btnPrevRes, btnNextRes, btnKlaAuf, btnKlaZu)
  window.reactions += {
    //case KeyPressed(_, Key.Enter, _, _) => label.text_=(textField.text)
    //case KeyPressed(_, Key.Alphanumeric, _, _) => label.text_=("6")//textField.text)
    case ButtonClicked(button) =>
      if (button.text >= "0" && button.text <= "9") {
        if (neueEingabe) {
          //&& !textField.text.contains('E')
          textField.text = button.text
          neueEingabe = false
        }
        else textField.text += button.text
        buttonsActivate()
      }
      if (button == btnC) {
        resetAll()
      }
      if (button == btnMPlus) {
        Mem += textField.text.toDouble
        neueEingabe = true
        buttonsActivate()
        //showMessage(message = "MPls")
      }
      if (button == btnRM) {
        textField.text = Mem toString()
        buttonsActivate()
      }
      if (button == btnMDel) {
        Mem = 0.0
        buttonsActivate()
      }
      if (button == btnPlus) rechne(textField.text.toDouble, "+")
      if (button == btnMin) rechne(textField.text.toDouble, "-")
      if (button == btnMal) rechne(textField.text.toDouble, "x")
      if (button == btnDiv) rechne(textField.text.toDouble, "/")
      if (button == btnPt ) { //&& !textField.text.contains('.') && !textField.text.contains('E') - brauchts nicht
        textField.text += "."
        buttonsActivate()
      }
      if (button == btnEx) System.exit(0)
      if (button == btnE) { // && !textField.text.contains('E') -- brauchts ebenfalls nicht
        textField.text += "E"
        buttonsActivate()
      }
      if (button == btnEnt) rechne(textField.text.toDouble, "=")
      if (button == btnPM) {
        if (textField.text(0) == '-') textField.text = textField.text.tail else textField.text = '-' + textField.text //strInp
      }
      if (button == btnEPM) {
        if (textField.text.tail.contains('-')) textField.text = textField.text.replaceFirst("E-", "E") //textField.text.contains('E') &&
        else textField.text = textField.text.replaceFirst("E", "E-")
        //buttonsActivate()
      }
      if (button == btnBack) {
        textField.text = textField.text.init
        if (textField.text == "") {
          textField.text = "0"
        }
        buttonsActivate()
        neueEingabe = false
      }
      if (button == btnPrevRes) {
        b += 1
        label.text = b.toString
        if (b >= 0 && b <= a) textField.text = llErgeb(a - b).toString
        buttonsActivate()
      }
      if (button == btnNextRes) {
        b -= 1
        label.text = b.toString
        if (b >= 0 && b <= a) textField.text = llErgeb(a - b).toString

        buttonsActivate()
      }
      if (button == btnKlaAuf) {
        z += 1
        btnKlaZu.enabled = true
      }
      if (button == btnKlaZu) {
        //zählerKlammer -= 1
        rechne(textField.text.toDouble, ")")
        z -= 1
      }
  }

  window.title = "Taschenrechner"
  window.visible = true

  def resetAll() {
    btnEPM.enabled = false
    btnRM.enabled = false
    btnMDel.enabled = false
    btnC.enabled = false
    btnPrevRes.enabled = false
    btnNextRes.enabled = false
    btnKlaZu.enabled = false
    textField.text = "0"
    llErgeb = mutable.LinkedList(0.0)
    b = 0
    a = 0
    z = 0
    llZahl = mutable.LinkedList(0.0)
    neueZahl = 0.0
    neueEingabe = true
  }

  def buttonsActivate() = {
    if (textField.text == "0" || textField.text == "" || textField.text == "-") {
      textField.text = "0"
      //btnE.enabled = false
      btnBack.enabled = false
      btnC.enabled = false
    } else {
      btnC.enabled = true
      btnBack.enabled = true
    }
    //if (textField.text == "" || textField.text == "-") textField.text = "0"
    if (a > 0 && a >= b) btnPrevRes.enabled = true else btnPrevRes.enabled = false
    if (b > 0) btnNextRes.enabled = true else btnNextRes.enabled = false
    if (textField.text.contains('E')) {
      btnPt.enabled = false
      btnE.enabled = false
      btnEPM.enabled = true
    } else {
      btnPt.enabled = true
      btnE.enabled = true
      btnEPM.enabled = false
    }
    if (Mem == 0.0) {
      btnRM.enabled = false
      btnMDel.enabled = false
    } else {
      btnRM.enabled = true
      btnMDel.enabled = true
    }
    if (textField.text.contains('.') || textField.text.contains('E')) btnPt.enabled = false else btnPt.enabled = true
    if (z > 0) btnKlaZu.enabled = true else btnKlaZu.enabled = false
  }

  def rechne(neueZahl: Double, neuesVZ: String) = {
    a += 1 // a ist Zähler für zurückliegende Berechnungsergebnisse
    if (neuesVZ == "(") {
      llVZ = llVZ :+ neuesVZ
      z += 1
      //rechne(neueZahl, "=") haut irgendwie mit der Rekursion nicht hin
    }
    if (neuesVZ == ")") {
      z -= 1
      //rechne(llZahl.last, "=")
      llVZ = mutable.LinkedList(llVZ.init.toString())
    }
    if (llVZ.last == "+") llZahl(z) += neueZahl
    if (llVZ.last == "-") llZahl(z) -= neueZahl
    if (llVZ.last == "x") llZahl(z) *= neueZahl
    if (llVZ.last == "/") llZahl(z) /= neueZahl
    if (llVZ.last == "=") llZahl(z) = neueZahl //zahl(zählerKlammer) = temp//.toDouble
    llVZ = llVZ :+ neuesVZ //VZ(zählerKlammer) = neuesVZ
    textField.text = llZahl(z).toString
    stellenE = 0
    neueEingabe = true
    buttonsActivate()
    llErgeb = llErgeb :+ llZahl(z) // neueZahl // arrZahl = Liste mit zurückliegenden Berechnungsergebnissen
    label.text = llErgeb.last.toString() //(a).toString // a.toString + " " +
  }
}
