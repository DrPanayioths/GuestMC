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
import java.util.ArrayList;

public final class GuestNetwork extends JavaPlugin implements Listener {
    ArrayList<String> uuid = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Guests Are Coming (Plugin Working)");

        uuid.add("8cb855d0-9606-465d-a0a2-6c9787bc6533");

//      To-Do: Check The UUIDS From Config File
//      To-Do: Add Command To Add More In The List
    }

    @Override
    public void onDisable() {
        getLogger().info("Guests Enjoyed Their Stay (Plugin Stopping)");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String player_uuid = player.getUniqueId().toString();
        String nameofplayer = player.getDisplayName();
        if (uuid.contains(player_uuid)) {
            getLogger().info("The Player " + nameofplayer + " Is Not A Guest");
        } else {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

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


}
