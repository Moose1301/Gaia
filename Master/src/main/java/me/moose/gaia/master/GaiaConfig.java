package me.moose.gaia.master;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.moose.gaia.common.GaiaServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@Getter
public class GaiaConfig {
    private static String FILE_NAME = "master_config.json";

    private String mongoHost = "127.0.0.1";
    private int mongoPort = 27017;
    private String mongoDatabase = "gaia";

    private String redisHost = "127.0.0.1";
    private int redisPort = 6379;

    private int apiPort = 8081;

    public void load() throws IOException {
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            JsonObject object = new JsonObject();
            object.addProperty("mongoHost", mongoHost);
            object.addProperty("mongoPort", mongoPort);
            object.addProperty("mongoDatabase", mongoDatabase);

            object.addProperty("redisHost", redisHost);
            object.addProperty("redisPort", redisPort);
            object.addProperty("apiPort", apiPort);
            FileWriter writer = new FileWriter(file);
            writer.write(GaiaServer.GSON_PRETTY.toJson(object));
            writer.close();
            return;
        }
        FileReader reader = new FileReader(file);
        JsonObject object = GaiaServer.GSON_PRETTY.fromJson(reader, JsonObject.class);

        mongoHost = object.get("mongoHost").getAsString();
        mongoPort = object.get("mongoPort").getAsInt();
        mongoDatabase = object.get("mongoDatabase").getAsString();

        redisHost = object.get("redisHost").getAsString();
        redisPort = object.get("redisPort").getAsInt();
        if(object.has("apiPort")) {
            apiPort = object.get("apiPort").getAsInt();
        }
    }
}
