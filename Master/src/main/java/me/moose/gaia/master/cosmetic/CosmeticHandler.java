package me.moose.gaia.master.cosmetic;

import com.mongodb.client.MongoCollection;
import me.moose.gaia.common.cosmetic.ICosmeticHandler;
import me.moose.gaia.common.cosmetic.data.Cosmetic;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class CosmeticHandler implements ICosmeticHandler {
    private MongoCollection<Document> collection;
    private Map<String, Cosmetic> cosmetics = new HashMap<>();

    public CosmeticHandler() {

    }
    @Override
    public Map<String, Cosmetic> getCosmetics() {
        return null;
    }

    @Override
    public void addCosmetic(Cosmetic cosmetic) {

    }

    @Override
    public Optional<Cosmetic> getCosmetic(String name) {
        return Optional.empty();
    }
}
