package org.drpanayioths.guestNetwork;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GuestNetwork extends JavaPlugin implements Listener {
    ArrayList<String> uuid = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Guests Are Coming (Plugin Working)");
        uuid_system();

    //  To-Do: Add Command To Add More In The List
    }

    @Override
    public void onDisable() {
        getLogger().info("Guests Enjoyed Their Stay (Plugin Stopping)");
    }

    // Base System
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {


        List<String> list_of_uuids = getConfig().getStringList("uuids");
        List<UUID> uuidList = list_of_uuids.stream()
                .map(UUID::fromString)
                .toList();

        Player player = event.getPlayer();
        UUID player_uuid = player.getUniqueId();
        String nameofplayer = player.getDisplayName();

        if (uuidList.contains(player_uuid)) {
            System.out.println(nameofplayer + " Is A Host");
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            System.out.println(nameofplayer + " Is A Guest");
            player.setGameMode(GameMode.SPECTATOR);
        }
    }


    // Player Can Learn Why He Got Into Spectator Mode
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String player_uuid = player.getUniqueId().toString();

        if (uuid.contains(player_uuid)) {
            player.sendMessage(ChatColor.GOLD + "You Are On The Exclude List, So You Can Play" + ChatColor.DARK_AQUA + " :)");
            return true;
        } else {
            player.sendMessage(ChatColor.GOLD + "You Are In Spectator Mode Because Your UUID: " + ChatColor.DARK_PURPLE + player_uuid + ChatColor.GOLD + " Is Not Present In The Exclude list");
            return true;
        }
    }

    // Config File System
    public void uuid_system() {
        File config_sys = new File(getDataFolder(), "config.yml");
        if (!config_sys.exists()) {
            saveResource("config.yml", true);
        }
    }

}
