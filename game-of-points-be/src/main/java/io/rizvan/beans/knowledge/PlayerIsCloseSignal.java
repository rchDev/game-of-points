package io.rizvan.beans.knowledge;

public class PlayerIsCloseSignal {
    private boolean isClose = false;

    public PlayerIsCloseSignal(boolean isClose) {
        this.isClose = isClose;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
