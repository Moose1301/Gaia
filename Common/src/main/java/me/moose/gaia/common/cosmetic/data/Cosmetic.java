package me.moose.gaia.common.cosmetic.data;

import lombok.Getter;
import lombok.Setter;
import me.moose.gaia.common.cosmetic.type.CosmeticType;
import org.bson.Document;

import java.beans.ConstructorProperties;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class Cosmetic {
    private final UUID uuid;
    private final String name;
    private final String type;
    private float scale;
    private final String resourceLocation;

    @ConstructorProperties({ "name", "type", "resourceLocation", "special" })
    public Cosmetic(String name, String type, String resourceLocation, boolean special) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.resourceLocation = resourceLocation;

        float scale = 1.0f;

        if (type.equals(CosmeticType.DRAGON_WINGS.getName())) {
            scale = 0.13f;
        }
        this.scale = scale;
    }

    @ConstructorProperties({ "name", "type", "scale", "resourceLocation" })
    public Cosmetic(String name, String type, float scale, String resourceLocation) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.scale = scale;
        this.resourceLocation = resourceLocation;
    }

    @ConstructorProperties({ "document" })
    public Cosmetic(Document document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.name = document.getString("name");
        this.type = document.getString("type");
        this.scale = Float.valueOf(document.getString("scale"));
        this.resourceLocation = document.getString("resourceLocation");
    }


    public String getPrettyName() {
        return this.name.replaceAll("_", " ");
    }


    public Document toDocument() {
        final Document document = new Document();

        document.append("uuid", uuid.toString());
        document.append("name", name);
        document.append("type", type);
        document.append("scale", String.valueOf(scale));
        document.append("resourceLocation", resourceLocation);

        return document;
    }
}
