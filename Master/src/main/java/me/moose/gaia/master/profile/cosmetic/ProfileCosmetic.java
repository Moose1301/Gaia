package me.moose.gaia.master.profile.cosmetic;

import lombok.Getter;
import me.moose.gaia.common.profile.cosmetic.CommonProfileCosmetic;
import me.moose.gaia.master.cosmetic.data.Cosmetic;
import org.bson.Document;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class ProfileCosmetic {
    private Cosmetic cosmetic;
    private boolean active;
    private CommonProfileCosmetic commonCosmetic;
    public ProfileCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
        this.active = false;
        this.commonCosmetic = new CommonProfileCosmetic(cosmetic.toCommon(), active);
    }

    public ProfileCosmetic(Document document) {
        //TODO COSMETIC SHIT
    }

    public void setActive(boolean active) {
        this.commonCosmetic = new CommonProfileCosmetic(cosmetic.toCommon(), active);
        this.active = active;
    }
    public CommonProfileCosmetic toCommon() {
        return commonCosmetic;
    }

    public Document toDocument() {
        Document document = new Document();

        document.append("cosmetic", cosmetic.getName());
        document.append("active", active);
        return document;
    }
}
