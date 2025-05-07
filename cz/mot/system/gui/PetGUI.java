package cz.mot.system.gui;

import cz.mot.system.Main;
import cz.mot.system.listener.ChatInput;
import cz.mot.system.manager.PetManager;
import cz.mot.system.pets.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PetGUI implements Listener {

    private static final Map<UUID, Map<Integer, Runnable>> guiPetMap = new HashMap<>();

    public static void openPetGUI(Player player, CustomPet pet) {
        Inventory gui = Bukkit.createInventory(null, 27, "§8Mazlíček");
        Map<Integer, Runnable> actionMap = new HashMap<>();

        gui.setItem(10, createItem(Material.NAME_TAG, "§ePřejmenovat mazlíčka"));
        actionMap.put(10, () -> {
            player.closeInventory();
            player.sendMessage("§7Napiš nový název mazlíčka do chatu. (Napiš §ccancel§7 pro zrušení)");
            ChatInput.await(player, message -> {
                if (message.equalsIgnoreCase("cancel")) {
                    player.sendMessage("§cPřejmenování zrušeno.");
                    return;
                }

                pet.setName("§f" + message.replace("&", "§")); // podpora barev
                player.sendMessage("§aMazlíček byl přejmenován na §f" + message.replace("&", "§"));
            });
        });

        gui.setItem(12, createItem(Material.MONSTER_EGG, "§ePřepnout věk"));
        actionMap.put(12, () -> {
            boolean newState = !pet.isBaby();
            pet.setBaby(newState);
            player.sendMessage("§aMazlíček je nyní " + (newState ? "mládě." : "dospělý."));
            openPetGUI(player, pet);
        });

        // Rozšířená menu podle typu
        if (pet instanceof CustomSheep) {
            gui.setItem(14, createItem(Material.WOOL, "§aZměnit barvu vlny"));
            actionMap.put(14, () -> openWoolColorGUI(player, (CustomSheep) pet));
        } else if (pet instanceof CustomWolf) {
            gui.setItem(14, createItem(Material.WOOL, "§aZměnit obojek"));
            actionMap.put(14, () -> openCollarColorGUI(player, (CustomWolf) pet));
        } else if (pet instanceof CustomOcelot) {
            gui.setItem(14, createItem(Material.FISHING_ROD, "§aZměnit typ ocelota"));
            actionMap.put(14, () -> openOcelotTypeGUI(player, (CustomOcelot) pet));
        } else if (pet instanceof CustomRabbit) {
            gui.setItem(14, createItem(Material.CARROT_ITEM, "§aZměnit typ králíka"));
            actionMap.put(14, () -> openRabbitTypeGUI(player, (CustomRabbit) pet));
        } else if (pet instanceof CustomHorse) {
            gui.setItem(14, createItem(Material.SADDLE, "§aZměnit typ koně"));
            actionMap.put(14, () -> openHorseTypeGUI(player, (CustomHorse) pet));
        }

        guiPetMap.put(player.getUniqueId(), actionMap);
        player.openInventory(gui);
    }

    // ===== Pod-GUI =====

    public static void openWoolColorGUI(Player player, CustomSheep sheep) {
        Inventory gui = Bukkit.createInventory(null, 27, "Barva vlny");
        Map<Integer, Runnable> colorMap = new HashMap<>();
        DyeColor[] colors = DyeColor.values();

        for (int i = 0; i < colors.length; i++) {
            DyeColor color = colors[i];
            ItemStack item = new ItemStack(Material.WOOL, 1, color.getWoolData());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§f" + capitalize(color.name().toLowerCase()));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
            int finalI = i;
            colorMap.put(i, () -> {
                sheep.setColor(color);
                player.sendMessage("§aBarva změněna na §f" + capitalize(color.name().toLowerCase()));
                openPetGUI(player, sheep);
            });
        }

        guiPetMap.put(player.getUniqueId(), colorMap);
        player.openInventory(gui);
    }

    public static void openCollarColorGUI(Player player, CustomWolf wolf) {
        Inventory gui = Bukkit.createInventory(null, 27, "Změnit obojek");
        Map<Integer, Runnable> map = new HashMap<>();
        DyeColor[] colors = DyeColor.values();

        for (int i = 0; i < colors.length; i++) {
            DyeColor color = colors[i];
            ItemStack item = new ItemStack(Material.WOOL, 1, color.getWoolData());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§f" + capitalize(color.name().toLowerCase()));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
            map.put(i, () -> {
            	wolf.setCollarColor(color);
                player.sendMessage("§aObojek změněn na §f" + capitalize(color.name().toLowerCase()));
                openPetGUI(player, wolf);
            });
        }

        guiPetMap.put(player.getUniqueId(), map);
        player.openInventory(gui);
    }

    public static void openOcelotTypeGUI(Player player, CustomOcelot ocelot) {
        Inventory gui = Bukkit.createInventory(null, 9, "Typ ocelota");
        Map<Integer, Runnable> map = new HashMap<>();

        CustomOcelot.OcelotType[] types = CustomOcelot.OcelotType.values();
        for (int i = 0; i < types.length; i++) {
            CustomOcelot.OcelotType type = types[i];
            ItemStack item = new ItemStack(Material.RAW_FISH);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§f" + capitalize(type.name().toLowerCase()));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
            map.put(i, () -> {
                ocelot.setOcelotType(type);
                player.sendMessage("§aTyp změněn na §f" + capitalize(type.name().toLowerCase()));
                openPetGUI(player, ocelot);
            });
        }

        guiPetMap.put(player.getUniqueId(), map);
        player.openInventory(gui);
    }

    public static void openRabbitTypeGUI(Player player, CustomRabbit rabbit) {
        Inventory gui = Bukkit.createInventory(null, 18, "Typ králíka");
        Map<Integer, Runnable> map = new HashMap<>();

        Rabbit.Type[] types = Arrays.stream(Rabbit.Type.values())
                .filter(t -> t != Rabbit.Type.THE_KILLER_BUNNY)
                .toArray(Rabbit.Type[]::new);

        for (int i = 0; i < types.length; i++) {
            Rabbit.Type type = types[i];
            ItemStack item = new ItemStack(Material.CARROT_ITEM);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§f" + capitalize(type.name().toLowerCase().replace("_", " ")));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
            map.put(i, () -> {
                rabbit.setRabbitType(type);
                player.sendMessage("§aTyp změněn na §f" + capitalize(type.name().toLowerCase().replace("_", " ")));
                openPetGUI(player, rabbit);
            });
        }

        guiPetMap.put(player.getUniqueId(), map);
        player.openInventory(gui);
    }

    public static void openHorseTypeGUI(Player player, CustomHorse horse) {
        Inventory gui = Bukkit.createInventory(null, 9, "Typ koně");
        Map<Integer, Runnable> map = new HashMap<>();

        CustomHorse.HorseType[] types = CustomHorse.HorseType.values();
        for (int i = 0; i < types.length; i++) {
            CustomHorse.HorseType type = types[i];
            ItemStack item = new ItemStack(Material.SADDLE);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§f" + capitalize(type.name().toLowerCase()));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
            map.put(i, () -> {
                horse.setHorseType(type);
                player.sendMessage("§aTyp změněn na §f" + capitalize(type.name().toLowerCase()));
                openPetGUI(player, horse);
            });
        }

        guiPetMap.put(player.getUniqueId(), map);
        player.openInventory(gui);
    }

    // ===== Event Listener =====

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if (!guiPetMap.containsKey(uuid)) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        // Jen pokud hráč kliká do GUI, zrušíme akci
        if (event.getView().getTopInventory().equals(clickedInventory)) {
            event.setCancelled(true);
        }

        int slot = event.getRawSlot();
        if (slot < 0 || slot >= event.getInventory().getSize()) return;

        Map<Integer, Runnable> actionMap = guiPetMap.get(uuid);
        if (!actionMap.containsKey(slot)) return;

        CustomPet pet = PetManager.getActivePet(player);
        if (pet == null || !pet.getOwnerPlayer().getUniqueId().equals(uuid)) {
            player.sendMessage("§cTento mazlíček ti nepatří.");
            player.closeInventory();
            return;
        }

        Bukkit.getScheduler().runTask(Main.getInstance(), actionMap.get(slot));
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        guiPetMap.remove(event.getPlayer().getUniqueId());
    }

    // ===== Utility =====

    private static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String capitalize(String input) {
        if (input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
