package me.moose.gaia.master.api;

import io.javalin.Javalin;
import me.moose.gaia.common.GaiaServer;
import me.moose.gaia.master.GaiaMaster;
import me.moose.gaia.master.server.Server;

/**
 * @author Moose1301
 * @date 10/24/2022
 */
public class APIServer {
    private Javalin app;


    public APIServer() {
        app = Javalin.create(config -> {
            config.showJavalinBanner = false;
        });

        app.get("metrics", ctx -> {
            StringBuilder sb = new StringBuilder();
            sb.append("gaia_profiles_loaded " + GaiaMaster.getInstance().getProfileHandler().getProfiles().size() + "\n");
            int profilesOnline = 0;
            for (Server server : GaiaMaster.getInstance().getServerHandler().getServers()) {
                profilesOnline += server.getProfiles().size();
            }
            sb.append("gaia_profiles_online " + profilesOnline + "\n");
            sb.append("gaia_servers_connected " + GaiaMaster.getInstance().getServerHandler().getServers().size() + "\n");
            sb.append("gaia_master_memory_usage " + GaiaMaster.getInstance().getServerHandler().getMemoryUsage() + "\n");
            sb.append("gaia_master_memory_free " + GaiaMaster.getInstance().getServerHandler().getMemoryFree() + "\n");
            sb.append("gaia_master_memory_max " + GaiaMaster.getInstance().getServerHandler().getMemoryMax() + "\n");
            for (Server server : GaiaMaster.getInstance().getServerHandler().getServers()) {
                String suffix = "{id=\"%id%\", region=\"%region%\"} ";
                suffix = suffix.replace("%id%", server.getId()).replace("%region%", server.getRegion());
                String prefix = "gaia_slave_" + server.getRealId() + "_";

                sb.append(prefix + "connected_users" + suffix + (server.getProfiles().size() + server.getUnauthorizedUsers()) + "\n");
                sb.append(prefix + "authorized_users" + suffix + server.getProfiles().size() + "\n");
                sb.append(prefix + "unauthorized_users" + suffix + server.getUnauthorizedUsers()  + "\n");
                sb.append(prefix + "memory_usage" + suffix + server.getMemoryFree()  + "\n");
                sb.append(prefix + "memory_free" + suffix + server.getMemoryUsage()  + "\n");
                sb.append(prefix + "memory_max" + suffix + server.getMemoryMax()  + "\n");
            }
            ctx.result(sb.toString());
        });
        app.start(GaiaMaster.getInstance().getConfig().getApiPort());

    }


}
