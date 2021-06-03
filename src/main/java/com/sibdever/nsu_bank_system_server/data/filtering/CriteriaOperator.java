package com.sibdever.nsu_bank_system_server.data.filtering;

public enum CriteriaOperator {
    EQUALS, NOT_EQUALS, GREATER, GREATER_OR_EQUALS, LESS, LESS_OR_EQUALS;
    public static CriteriaOperator ofSymbol(String s) {
        return switch (s) {
            case ":" -> EQUALS;
            case ">" -> GREATER;
            case "<" -> LESS;
            case "!:" -> NOT_EQUALS;
            case "<:" -> LESS_OR_EQUALS;
            case ">:" -> GREATER_OR_EQUALS;
            default -> throw new IllegalArgumentException();
        };
    }
}
