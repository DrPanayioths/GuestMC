package org.drpanayioths.guestNetwork;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;

public final class GuestNetwork extends JavaPlugin implements Listener {

    public List<String> list_of_uuids;
    public List<UUID> uuidList;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Guests Are Coming (Plugin Working)");
        uuid_sys();
        messages_sys();

        // Put All UUIDS Into A List
        list_of_uuids = getConfig().getStringList("uuids");
        uuidList = list_of_uuids.stream()
                .map(UUID::fromString)
                .toList();
    }

    @Override
    public void onDisable() {
        getLogger().info("Guests Enjoyed Their Stay (Plugin Stopping)");
    }

    // Base System
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID player_uuid = player.getUniqueId();
        String nameofplayer = player.getDisplayName();

        if (uuidList.contains(player_uuid)) {
            System.out.println(nameofplayer + " Is A Host");
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            System.out.println(nameofplayer + " Is A Guest");
            player.setGameMode(GameMode.SPECTATOR);

            BossBar notifier = Bukkit.createBossBar("§7Guest§8: Type §7/why §8To Learn More", BarColor.RED, BarStyle.SOLID);
            notifier.addPlayer(player);
            notifier.setProgress(1);
        }
    }


    // Player Can Learn Why He Got Into Spectator Mode
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID senderUUID = player.getUniqueId();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);
        String exist_perms = messages.getString("no_spectator");
        String no_perms = messages.getString("spectator");

        if (uuidList.contains(senderUUID)) {
            player.sendMessage(ChatColor.GOLD + "You Are On The Exclude List, So You Can Play" + ChatColor.DARK_AQUA + " " + no_perms);
        } else {
            player.sendMessage(ChatColor.WHITE + "You Are In Spectator Mode Because Your UUID: " + ChatColor.AQUA + senderUUID + ChatColor.GRAY + " " + exist_perms);
        }
        return true;
    }

    // Config File System
    public void uuid_sys() {
        File config_sys = new File(getDataFolder(), "config.yml");
        if (!config_sys.exists()) {
            saveResource("config.yml", true);
        }
    }

    // Messages File System
    public void messages_sys() {
        File messages_system = new File(getDataFolder(), "messages.yml");
        if (!messages_system.exists()) {
            saveResource("messages.yml", true);
        }
    }
}
