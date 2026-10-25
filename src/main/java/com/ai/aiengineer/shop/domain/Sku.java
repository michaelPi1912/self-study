package com.ai.aiengineer.shop.domain;

public class Sku {

    private final String code;

    public Sku(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sku other)) {
            return false;
        }
        return code.equals(other.code);
    }
}
