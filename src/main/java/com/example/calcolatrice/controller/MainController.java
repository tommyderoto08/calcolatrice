package com.example.calcolatrice.controller;

import com.example.calcolatrice.model.Espressione;
import com.example.calcolatrice.model.ExpressionException;
import com.example.calcolatrice.model.Operatore;
import com.example.calcolatrice.model.Parentesi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class MainController {
    @FXML
    public Button btn1;
    @FXML
    public Button btn2;
    @FXML
    public Button btn3;
    @FXML
    public Button btn4;
    @FXML
    public Button btn5;
    @FXML
    public Button btn6;
    @FXML
    public Button btn7;
    @FXML
    public Button btn8;
    @FXML
    public Button btn9;
    @FXML
    public Button btn0;
    @FXML
    public Button indietro;
    @FXML
    public Button canc;
    @FXML
    public Button uguale;
    @FXML
    public Button add;
    @FXML
    public Button sott;
    @FXML
    public Button div;
    @FXML
    public Button molt;
    @FXML
    public Button pot;
    @FXML
    public Button pAperta;
    @FXML
    public Button pChiusa;
    @FXML
    public Label testo;

    private ArrayList<String> simboli= new ArrayList<>();

    public void initialize(){
        simboli.add("");
        testo.setText("");
        indietro.setDisable(true);
        canc.setDisable(true);
    }

    public void modifica(ActionEvent actionEvent) {
        String a = ((Button)actionEvent.getSource()).getText();
        switch(simboli.getLast()){
            case "+", "-", "/", "*", "(", "":
                if(a.equals("+") || a.equals("-") || a.equals("/") || a.equals("*") || a.equals("^") || a.equals(")")){
                    break;
                }
                simboli.add(a);
                break;
            default:
                simboli.add(a);
                break;
        }
        aggiorna();
    }

    public void aggiorna(){
        String s = "";
        for(String n : simboli){
            s += n;
        }
        testo.setText(s);
        if(simboli.size() > 1){
            indietro.setDisable(false);
            canc.setDisable(false);
        }else{
            indietro.setDisable(true);
            canc.setDisable(true);
        }
    }

    public void inputKeyboard(KeyEvent keyEvent) throws ExpressionException {
        KeyCode c = keyEvent.getCode();
        System.out.println(c);

        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT7){
            simboli.add(Operatore.DIV.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT8){
            simboli.add(Parentesi.PARENTESI_APERTA.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.DIGIT9){
            simboli.add(Parentesi.PARENTESI_CHIUSA.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && c == KeyCode.PLUS){
            simboli.add(Operatore.MULT.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && keyEvent.getText().equals("ì")){
            simboli.add(Operatore.POW.toString());
            aggiorna();
            return;
        }
        if(keyEvent.isShiftDown() && keyEvent.getText().equals("0")){
            risultato();
            return;
        }

        switch (c){
            case DIGIT0, DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9,
                 NUMPAD0, NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4, NUMPAD5, NUMPAD6, NUMPAD7, NUMPAD8, NUMPAD9:
                simboli.add(keyEvent.getText());
                aggiorna();
                break;
            case ADD, PLUS:
                simboli.add(Operatore.ADD.toString());
                aggiorna();
                break;
            case MINUS, SUBTRACT:
                simboli.add(Operatore.SUB.toString());
                aggiorna();
                break;
            case DIVIDE:
                simboli.add(Operatore.DIV.toString());
                aggiorna();
                break;
            case MULTIPLY:
                simboli.add(Operatore.MULT.toString());
                aggiorna();
                break;
            case BACK_SPACE:
                indietro();
                break;
            case DELETE:
                cancella();
                break;
            default:
                break;
        }
    }

    public void indietro(){
        simboli.removeLast();
        aggiorna();
    }

    public void cancella(){
        simboli.removeAll(simboli);
        simboli.add("");
        aggiorna();
    }

    public void risultato() throws ExpressionException {
        Espressione e = new Espressione(testo.getText());
        testo.setText(e.calcRPN().toString());
        simboli.clear();
        simboli.add(testo.getText());
    }
}
