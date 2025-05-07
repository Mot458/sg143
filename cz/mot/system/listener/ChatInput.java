package cz.mot.system.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cz.mot.system.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInput implements Listener {

    private static final Map<UUID, Consumer<String>> inputMap = new HashMap<>();

    public static void await(Player player, Consumer<String> callback) {
        inputMap.put(player.getUniqueId(), callback);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!inputMap.containsKey(uuid)) return;

        event.setCancelled(true);
        Consumer<String> callback = inputMap.remove(uuid);

        // Musíme zpracovat na hlavním vlákně
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            callback.accept(event.getMessage());
        });
    }
}
