package me.moose.gaia.master.profile.cosmetic;

import lombok.Getter;
import me.moose.gaia.common.cosmetic.data.Cosmetic;
import org.bson.Document;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class ProfileCosmetic {
    private Cosmetic cosmetic;
    private boolean active;

    public ProfileCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
        this.active = false;

    }

    public ProfileCosmetic(Document document) {
        //TODO COSMEITC SHIT
    }


    public Document toDocument() {
        Document document = new Document();

        document.append("cosmetic", cosmetic.getName());
        document.append("active", active);
        return document;
    }
}
