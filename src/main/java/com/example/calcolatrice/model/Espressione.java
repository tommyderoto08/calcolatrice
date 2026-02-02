package com.example.calcolatrice.model;

import java.util.*;

public class Espressione {
    private String inputExpr; //espressione in input
    private ArrayList tokenList;
    private ArrayList validTokensList;
    private ArrayList rpnExpression;
    private Frazione risultato;

    public Espressione(String inputExpr) {
        this.inputExpr = inputExpr;
        tokenList = new ArrayList();
        validTokensList = new ArrayList();
        rpnExpression = new ArrayList();
    }

    public Frazione getRisultato() {
        return risultato;
    }

    public ArrayList getValidTokensList() {
        return validTokensList;
    }

    public ArrayList getTokenList() {
        return tokenList;
    }

    public ArrayList getRpnExpression() {
        return rpnExpression;
    }

    public String getInputExpr() {
        return inputExpr;
    }

    public void scanner() throws ExpressionException {
        long numeratore = 0;
        int contaParentesi = 0;
        int posizione = 0;
        boolean inLetturaNumero = false;
        for (char c : inputExpr.toCharArray()) {
            switch (c) {
                case '+', '-', '*', '/', '^':
                    if (inLetturaNumero)
                        tokenList.add(new Frazione(numeratore, 1));
                    tokenList.add(Operatore.getOperatore(c));
                    inLetturaNumero = false;
                    break;
                case '(', ')':
                    if (inLetturaNumero)
                        tokenList.add(new Frazione(numeratore, 1));
                    tokenList.add(Parentesi.getParentesi(c));
                    if ((c == Parentesi.PARENTESI_APERTA.getSimbolo()))
                        contaParentesi++;
                    else {
                        contaParentesi--;
                        if (contaParentesi < 0)
                            throw new ExpressionException(
                                    "Espressione non valida",
                                    "L'espressione contiene parentesi non bilanciate in posizione " + posizione
                            );
                    }
                    inLetturaNumero = false;
                    break;
                case '1', '2', '3', '4', '5', '6', '7', '8', '9', '0':
                    if(inLetturaNumero){
                        numeratore = 10 * numeratore + Integer.valueOf(Character.toString(c));
                    }else{
                        numeratore = Integer.valueOf(Character.toString(c));
                        inLetturaNumero = true;
                    }
                    break;
                case ' ':
                    //non considero gli spazi
                    break;
                default:
                    throw new ExpressionException(
                            "Carattere non valido",
                            "L'espressione contiene un carattere non valido in posizione " + posizione
                    );
            }
            posizione++;
        }

        if (inLetturaNumero)
            tokenList.add(new Frazione(numeratore, 1));

        if (contaParentesi != 0)
            throw new ExpressionException(
                    "Espressione non valida",
                    "L'espressione contiene parentesi non bilanciate"
            );

    }


    /**
     * Algoritmo ShuntingYards
     * traduce una espressione valida in espressione RPN
     */
    public void shuntingYard() throws ExpressionException {
        Stack<Object> operatori = new Stack<>();
        Stack<Object> queue = new Stack<>();
        int posizione = 0;

        for(Object token : validTokensList){
            if(token instanceof Frazione){
                queue.push(token);
            }
            if (token instanceof Operatore){
                while(!operatori.isEmpty() && operatori.peek() instanceof Operatore){
                    if(((Operatore)operatori.peek()).getPriorità() >= ((Operatore)token).getPriorità()){
                        queue.push(operatori.pop());
                    }else{
                        break;
                    }
                }
                operatori.push(token);
            }
            if(token == Parentesi.PARENTESI_APERTA){
                operatori.push(token);
            }
            if(token == Parentesi.PARENTESI_CHIUSA) {
                while (operatori.peek() != Parentesi.PARENTESI_APERTA) {
                    queue.push(operatori.pop());
                }
                operatori.pop();
            }
            posizione++;
        }
        while(!operatori.isEmpty()){
            if(operatori.peek() instanceof Operatore){
                queue.push(operatori.pop());
            }else {
                throw new ExpressionException("Syntax error", "L'espressione contiene parentesi non bilanciate in posizione " + posizione);
            }

        }
    }

    /**
     * Esegue l'analisi sintattica dell'espressione tokenizzata
     */
    public void parser() throws ExpressionException {
        /*
            stato = 0: in attesa di espressione
            stato = 1: letto Operatore
            stato = 2: letto Operando (Frazione)
            stato = 3: letta Parentesi Chiusa
         */
        int stato = 0;
        for (Object token : tokenList) {
            switch (stato) {
                case 0:
                    /*-- stato 0 ----- in attesa di espressione -------------------------------*/
                    if (token instanceof Operatore) {
                        if(!token.equals("+") && !token.equals("-")){
                            throw new ExpressionException(
                                    "Espressione non valida",
                                    token + " non può essere inserito per primo");
                        }
                        validTokensList.add(token);
                        stato = 1;
                    } else if (token instanceof Parentesi) {
                        if(token.equals(Parentesi.PARENTESI_CHIUSA)){
                            throw new ExpressionException(
                                    "Espressione non valida",
                                    token + " deve seguir una parentesi aperta");
                        }
                    } else if (token instanceof Frazione) {
                        validTokensList.add(token);
                        stato = 2;
                    }

                    break;
                case 1:
                    /*-- stato 1 ----- letto Operatore -----------------------------*/
                    if (token instanceof Operatore) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                token + " non può seguire un altro operatore");
                    } else if (token instanceof Frazione) {
                        validTokensList.add(token);
                        stato = 2;
                    } else if (token instanceof Parentesi) {
                        if(token.equals(Parentesi.PARENTESI_CHIUSA)){
                            throw new ExpressionException(
                                    "Espressione non valida",
                                    token + " non può seguire un operatore");
                        }
                        validTokensList.add(token);
                        stato = 0;
                    }
                    break;
                case 2:
                    /*-- stato 2 ----- letto Operando (Frazione) -----------------------------------------*/
                    if (token instanceof Operatore) {
                        validTokensList.add(token);
                        stato = 1;
                    } else if (token instanceof Frazione) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                token + " non può seguire un operando");
                    } else if (token instanceof Parentesi) {
                        if(token.equals(Parentesi.PARENTESI_APERTA)){
                            throw new ExpressionException(
                                    "Espressione non valida",
                                    token + " non può seguire un operatore");
                        }
                        validTokensList.add(token);
                        stato = 3;
                    }
                    break;
                case 3:
                    /*-- stato 3 ----- letta Parentesi Chiusa ---------------------------------------------*/
                    if (token instanceof Operatore) {
                        validTokensList.add(token);
                        stato = 1;
                    } else if (token instanceof Frazione) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                token + " non può seguire una parentesi chiusa");
                    } else if (token instanceof Parentesi) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                token + " non può seguire un'altra parentesi");
                    }
            }
        }
        //non deve terminare con un operatore (stato 1)
        if (stato == 1)
            throw new ExpressionException(
                    "Espressione non valida",
                    "L'espressione termina non può terminare con " + tokenList.getLast());
    }

    /**
     * valuta l'espressione RPN  rpnExpression e scrive il risultato
     * nella variabile risultato
     */
    public void calcRPN() {
        Frazione operando1, operando2, risultatoParziale = null;
        Deque<Frazione> stackOperandi = new ArrayDeque<>();
        //Stack<Frazione> stackOperandi = new Stack<>();
        for (Object token : rpnExpression) {
            if (token instanceof Frazione) {
                //aggiungo l'operatore allo stackOperandi
                stackOperandi.push((Frazione) token);
            } else {
                //si tratta di un operatore...
                //tolgo l'elemento in cima allo stackOperandi e lo assegno a operando2
                operando2 = stackOperandi.pop();
                //tolgo l'elemento in cima allo stackOperandi e lo assegno a operando1
                operando1 = stackOperandi.pop();
                //eseguo l'operazione operando1 operatore operando2:
                try {
                    switch ((Operatore) token) {
                        case Operatore.ADD:
                            risultatoParziale = operando1.sum(operando2);
                            break;
                        case Operatore.SUB:
                            risultatoParziale = operando1.sub(operando2);
                            break;
                        case Operatore.MULT:
                            risultatoParziale = operando1.mult(operando2);
                            break;
                        case Operatore.DIV:
                            risultatoParziale = operando1.div(operando2);
                            break;
                        case Operatore.POW:
                            risultatoParziale = operando1.pow(operando2);
                            break;
                    }
                } catch (EmptyStackException ex) {
                    throw ex;
                }
                //aggiungo il risultato allo stack operandi
                stackOperandi.push(risultatoParziale);
            }
        }
        this.risultato = stackOperandi.pop();
    }

    public static Frazione risolvi(String stringInput) throws ExpressionException, ArithmeticException{
        Espressione espressione = new Espressione(stringInput);
        //da inputExpr a tokenList
        espressione.scanner();
        //da tokenList a validTokenList
        espressione.parser();
        //da valid TokenList a RPNExpr
        espressione.shuntingYard();
        //da RPNExpr a risultato
        espressione.calcRPN();
        return espressione.getRisultato();
    }

    public void risolvi() throws ExpressionException, ArithmeticException {
        //da inputExpr a tokenList
        scanner();
        //da tokenList a validTokenList
        parser();
        //da valid TokenList a RPNExpr
        shuntingYard();
        //da RPNExpr a risultato
        calcRPN();
    }
}
