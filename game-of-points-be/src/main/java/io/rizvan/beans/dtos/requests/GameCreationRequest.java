package io.rizvan.beans.dtos.requests;

public class GameCreationRequest {
    private int weaponId;
    private int windowWidth;
    private int windowHeight;
    private String dialogFlowSessionId;

    public GameCreationRequest() {}

    public GameCreationRequest(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public String getDialogFlowSessionId() {
        return dialogFlowSessionId;
    }

    public void setDialogFlowSessionId(String dialogFlowSessionId) {
        this.dialogFlowSessionId = dialogFlowSessionId;
    }
}
