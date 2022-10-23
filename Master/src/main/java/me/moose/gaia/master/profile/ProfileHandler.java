package me.moose.gaia.master.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.common.utils.Logger;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.profile.cosmetic.ProfileCosmetic;
import me.moose.gaia.master.profile.friend.Friend;
import me.moose.gaia.master.profile.friend.FriendRequest;
import me.moose.gaia.common.profile.rank.Rank;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class ProfileHandler {
    @Getter
    private Map<UUID, Profile> profiles = new HashMap<>();
    private MongoCollection<Document> collection;

    public ProfileHandler() {
        collection = GaiaMaster.getInstance().getDatabase().getCollection("profiles");

        //TODO LOAD PROFILES!!
    }
    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }
    public Profile getProfileOrLoad(UUID uuid, String username) {
        if(profiles.containsKey(uuid)) {
            return profiles.get(uuid);
        }
        Profile profile = new Profile(uuid, username);
        load(profile);
        return profile;
    }
    public void load(Profile profile) {
        long start = System.currentTimeMillis();
        Document document = this.collection.find(Filters.eq("uuid", profile.getUniqueId())).first();

        if (document == null) {
            this.save(profile);
            return;
        }
        if (document.containsKey("offlineSince")) {
            profile.setOfflineSince(document.getLong("offlineSince"));
        }

        if (document.containsKey("cosmetics")) {
            List<Document> cosmetics = document.getList("cosmetics", Document.class);

            for (Document cosmeticDocument : cosmetics) {
                ProfileCosmetic cosmetic = new ProfileCosmetic(cosmeticDocument);

                if (cosmetic.getCosmetic() == null) {
                    continue;
                }
                profile.getCosmetics().add(cosmetic);
            }
        }

        if (document.containsKey("banned")) {
            profile.setBanned(document.getBoolean("banned"));
        }

        if (document.containsKey("acceptingRequests")) {
            profile.setAcceptingRequests(document.getBoolean("acceptingRequests"));
        }

        if (document.containsKey("friends")) {
            List<Document> friends = document.getList("friends", Document.class);

            for (Document friendDocument : friends) {
                profile.getFriends().add(new Friend(friendDocument));
            }
        }

        if (document.containsKey("friendRequests")) {
            List<Document> friendRequests = document.getList("friendRequests", Document.class);

            for (Document friendDocument : friendRequests) {
                profile.getFriendRequests().add(new FriendRequest(friendDocument));
            }
        }

        if (document.containsKey("rank")) {
            profile.setRank(Rank.valueOf(document.getString("rank")));
        }
        GaiaServer.getLogger().debug("ProfileHandler", "Loaded " + profile.getUsername() + " profile in "
                + (System.currentTimeMillis() - start) + "ms", Logger.DebugType.SUCCESS);
    }

    public void save(Profile profile) {
        final Document document = new Document();

        document.put("uuid", profile.getUniqueId().toString());
        document.put("username", profile.getUsername());
        document.put("banned", profile.isBanned());

        document.put("cosmetics", profile.getCosmetics().stream().map(ProfileCosmetic::toDocument).collect(Collectors.toList()));

        document.put("acceptingRequests", profile.isAcceptingRequests());
        document.put("friends", profile.getFriends().stream().map(Friend::toDocument).collect(Collectors.toList()));
        document.put("friendRequests", profile.getFriendRequests().stream().map(FriendRequest::toDocument).collect(Collectors.toList()));

        document.put("offlineSince", profile.getOfflineSince());
        document.put("rank", profile.getRank().name());


        try {
            GaiaMaster.getInstance().getExecutor().execute(() -> collection.replaceOne(Filters.eq("uuid", profile.getUniqueId().toString()), document, GaiaMaster.UPSERT_OPTIONS));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
