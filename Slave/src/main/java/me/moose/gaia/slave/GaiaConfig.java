package me.moose.gaia.slave;

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
    private static String FILE_NAME = "slave_config.json";

    private String id = "dev";
    private String region = "US";

    private String redisHost = "127.0.0.1";
    private int redisPort = 6379;

    public void load() throws IOException {
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", id);
            object.addProperty("region", region);
            object.addProperty("redisHost", redisHost);
            object.addProperty("redisPort", redisPort);
            FileWriter writer = new FileWriter(file);
            writer.write(GaiaServer.GSON_PRETTY.toJson(object));
            writer.close();
            return;
        }
        FileReader reader = new FileReader(file);
        JsonObject object = GaiaServer.GSON_PRETTY.fromJson(reader, JsonObject.class);
        id = object.get("id").getAsString();
        region = object.get("region").getAsString();
        redisHost = object.get("redisHost").getAsString();
        redisPort = object.get("redisPort").getAsInt();
    }

}
