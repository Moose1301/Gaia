package me.moose.gaia.common.packet.packets.master.cosmetics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.cosmetic.data.CommonCosmetic;
import me.moose.gaia.common.packet.handler.IGaiaSlavePacketHandler;
import me.moose.gaia.common.packet.packets.master.GaiaMasterPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GaiaMasterCosmeticListUpdate extends GaiaMasterPacket {
    private List<CommonCosmetic> cosmetics;
    @Override
    public void read(JsonObject object) {
        cosmetics = new ArrayList<>();
        for (JsonElement element : object.getAsJsonArray("cosmetics")) {
            JsonObject cosmeticObject = element.getAsJsonObject();
            UUID uuid = UUID.fromString(cosmeticObject.get("uuid").getAsString());
            String name = cosmeticObject.get("name").getAsString();
            String type = cosmeticObject.get("type").getAsString();
            float scale = cosmeticObject.get("scale").getAsFloat();
            String resourceLocation = cosmeticObject.get("resourceLocation").getAsString();
            cosmetics.add(new CommonCosmetic(uuid, name, type, scale, resourceLocation));
        }
    }

    @Override
    public void write(JsonObject object) {
        JsonArray cosmetics = new JsonArray();
        for (CommonCosmetic cosmetic : this.cosmetics) {
            JsonObject cosmeticObject = new JsonObject();
            cosmeticObject.addProperty("uuid", cosmetic.getUuid().toString());
            cosmeticObject.addProperty("name", cosmetic.getName());
            cosmeticObject.addProperty("type", cosmetic.getType());
            cosmeticObject.addProperty("scale", cosmetic.getScale());
            cosmeticObject.addProperty("resourceLocation", cosmetic.getResourceLocation());
            cosmetics.add(cosmeticObject);
        }
        object.add("cosmetics", cosmetics);
    }

    @Override
    public void handle(IGaiaSlavePacketHandler handler) {
        handler.handle(this);
    }
}
