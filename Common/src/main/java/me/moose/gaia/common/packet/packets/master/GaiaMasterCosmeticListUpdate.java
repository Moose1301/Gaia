package me.moose.gaia.common.packet.packets.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.cosmetic.data.Cosmetic;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaMasterCosmeticListUpdate extends GaiaMasterPacket {
    private List<Cosmetic> cosmetics;
    @Override
    public void read(JsonObject object) {
        cosmetics = new ArrayList<>();
        for (JsonElement element : object.getAsJsonArray("cosmetics")) {

        }
    }

    @Override
    public void write(JsonObject object) {
        JsonArray cosmetics = new JsonArray();
        for (Cosmetic cosmetic : this.cosmetics) {
            JsonObject cosmeticObject = new JsonObject();
        }
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
