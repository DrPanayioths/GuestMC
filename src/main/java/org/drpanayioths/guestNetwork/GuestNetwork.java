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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public final class GuestNetwork extends JavaPlugin implements Listener {

    public List<String> list_of_uuids;
    public List<UUID> uuidList;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Guests Are Coming (Plugin Working)");
        files_gen();

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

        if (uuidList.contains(player_uuid)) {
            player.setGameMode(GameMode.SURVIVAL);
            log(player,"Join: ");
        } else {
            player.setGameMode(GameMode.SPECTATOR);

            BossBar notifier = Bukkit.createBossBar("§7Guest§8: Type §7/why §8To Learn More", BarColor.RED, BarStyle.SOLID);
            notifier.addPlayer(player);
            notifier.setProgress(1);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        log(player,"Left: ");
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

    // Logbook System [Join/Leaves] (Username, Group, Date & Time)
    public void log(Player player,String event) {
        File logsSystem = new File(getDataFolder(), "logs");
        if (!logsSystem.exists()) {
            logsSystem.mkdirs();
        }

        File logs_system = new File(getDataFolder(), "logs");
        String time_file = LocalDate.now().toString() + ".log";
        File log_file = new File(logs_system, time_file);

        UUID player_uuid = player.getUniqueId();
        String user_group;
        if(uuidList.contains(player_uuid)) {
            user_group = "Host";
        } else {
            user_group = "Guest";
        }

        try (FileWriter fw = new FileWriter(log_file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write("[" + time + "] " + player.getName() + " " + event + " " + user_group);
            bw.newLine();

        } catch (IOException e) {
            getLogger().warning("KATI PAEI LATHOS (Easter Egg): " + e.getMessage());
        }

    }



    public void files_gen() {
        // Config File System
        File config_sys = new File(getDataFolder(), "config.yml");
        if (!config_sys.exists()) {
            saveResource("config.yml", true);
        }

        // Messages File System
        File messages_system = new File(getDataFolder(), "messages.yml");
        if (!messages_system.exists()) {
            saveResource("messages.yml", true);
        }
    }
}
