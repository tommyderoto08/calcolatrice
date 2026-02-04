package com.example.calcolatrice.model;

public enum Operatore {
    ADD('+', 2), SUB('-', 2), MULT('*', 3), DIV('/', 3), POW('^', 1);
    private char simbolo;
    private int priorità;

    Operatore(char simbolo, int priorità) {
        this.priorità = priorità;
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
    public static Operatore getOperatore(char simbolo){
        for(Operatore op: Operatore.values())
            if(op.getSimbolo() == simbolo)
                return op;
        return null;
    }

    public int getPriorità() {
        return priorità;
    }

    @Override
    public String toString() {
        return Character.toString(simbolo);
    }
}
