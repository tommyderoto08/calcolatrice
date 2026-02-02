package com.example.calcolatrice.controller;

import com.example.calcolatrice.model.Espressione;
import com.example.calcolatrice.model.ExpressionException;
import com.example.calcolatrice.model.Frazione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
            case "+", "-", "x", ":", "(", "":
                if(a.equals("+") || a.equals("-") || a.equals("x") || a.equals(":") || a.equals("^") || a.equals(")")){
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
        //TODO;
        Espressione e = new Espressione(testo.getText());
        e.risolvi();
    }
}
