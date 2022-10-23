package me.moose.gaia.master.cosmetic;

import com.mongodb.client.MongoCollection;
import me.moose.gaia.common.cosmetic.ICosmeticHandler;
import me.moose.gaia.common.cosmetic.data.CommonCosmetic;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.cosmetic.data.Cosmetic;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class CosmeticHandler implements ICosmeticHandler {
    private MongoCollection<Document> collection;
    private Map<String, Cosmetic> cosmetics = new HashMap<>();


    public CosmeticHandler() {
        collection = GaiaMaster.getInstance().getDatabase().getCollection("cosmetics");
        for (Document document : collection.find()) {
            Cosmetic cosmetic = new Cosmetic(document);
            cosmetics.put(cosmetic.getName(), cosmetic);
        }
        Cosmetic cosmetic = new Cosmetic("Testing", "CAPE", "client/capes/cb.png");
        cosmetics.put(cosmetic.getName(), cosmetic);
    }
    public List<CommonCosmetic> getCommonCosmetics() {
        return cosmetics.values().stream().map(Cosmetic::toCommon).collect(Collectors.toList());
    }
    public Map<String, Cosmetic> getCosmetics() {
        return cosmetics;
    }


    public Cosmetic getCosmetic(String name) {
        return cosmetics.get(name);
    }

    @Override
    public CommonCosmetic getCommonCosmetic(String name) {
        return cosmetics.get(name).toCommon();
    }
}
