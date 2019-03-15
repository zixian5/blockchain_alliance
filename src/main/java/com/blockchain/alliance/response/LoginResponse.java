package com.blockchain.alliance.response;

public class LoginResponse {
    private boolean result;
    private int type;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
