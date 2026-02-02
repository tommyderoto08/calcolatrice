package com.example.calcolatrice.model;

public enum Parentesi {
    PARENTESI_APERTA('('), PARENTESI_CHIUSA(')');
    private char simbolo;

    Parentesi(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public static Parentesi getParentesi(char simbolo){
        if(simbolo == '(')
            return PARENTESI_APERTA;
        else if(simbolo == ')')
            return PARENTESI_CHIUSA;
        return null;
    }

    @Override
    public String toString(){
        return Character.toString(simbolo);
    }
}
