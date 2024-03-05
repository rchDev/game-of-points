package io.rizvan.beans.dtos.requests;

public class PlayerCreationRequest {
    private int weaponId;

    public PlayerCreationRequest() {}

    public PlayerCreationRequest(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }
}
