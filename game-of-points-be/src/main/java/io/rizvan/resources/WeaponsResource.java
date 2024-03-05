package io.rizvan.resources;

import io.rizvan.beans.RangedWeapon;
import io.rizvan.beans.WeaponCache;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/weapons")
public class WeaponsResource {

    @Inject
    WeaponCache weaponCache;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RangedWeapon> getWeaponList() {
        return weaponCache.getWeapons();
    }
}
