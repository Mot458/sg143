package cz.mot.system.manager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import cz.mot.system.gui.PetGUI;
import cz.mot.system.pets.*;

import java.util.HashMap;
import java.util.UUID;

public class PetManager {

    public static final HashMap<UUID, Entity> activePets = new HashMap<>();
    public static void spawnPet(Player player, String type) {
        // Odstraň předchozího mazlíčka
        removePet(player);

        World bukkitWorld = player.getWorld();
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        Location loc = player.getLocation();

        Entity pet = null;

        switch (type.toLowerCase()) {
            case "ghast":
                pet = new CustomGhast(bukkitWorld, player);
                break;
            case "cow":
                pet = new CustomCow(bukkitWorld, player);
                break;
            case "wolf":
                pet = new CustomWolf(bukkitWorld, player);
                break;
            case "ocelot":
                pet = new CustomOcelot(bukkitWorld, player);
                break;
            case "rabbit":
                pet = new CustomRabbit(bukkitWorld, player);
                break;
            case "sheep":
                pet = new CustomSheep(bukkitWorld, player);
                break;
            case "horse":
                pet = new CustomHorse(bukkitWorld, player);
                break;
            default:
                player.sendMessage("§cNeplatný typ mazlíčka.");
                return;
        }

        if (pet != null) {
            pet.setPosition(loc.getX(), loc.getY(), loc.getZ());
            nmsWorld.addEntity(pet);
            activePets.put(player.getUniqueId(), pet);

            player.sendMessage("§aMazlíček " + type + " byl vytvořen.");
        } else {
            player.sendMessage("§cMazlíček nebyl vytvořen.");
        }
        System.out.println("Pet byl přidán do světa: " + pet.getName());
    }

    public static void removePet(Player player) {
        Entity existing = activePets.remove(player.getUniqueId());
        if (existing != null && !existing.dead) {
            existing.die();
        }
    }

    public static boolean hasPet(Player player) {
        return activePets.containsKey(player.getUniqueId());
    }

    public static Entity getPet(Player player) {
        return activePets.get(player.getUniqueId());
    }
    
    public void handlePetInteraction(org.bukkit.entity.Entity entity, Player player) {
        if (!(entity instanceof CraftEntity)) return;

        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        // Kontrola, zda jde o mazlíčka a vlastníka
        if (nmsEntity instanceof EntityInsentient) {
            if (nmsEntity.passenger != null) return;

            if (nmsEntity instanceof CustomGhast ||
                nmsEntity instanceof CustomCow ||
                nmsEntity instanceof CustomHorse ||
                nmsEntity instanceof CustomWolf ||
                nmsEntity instanceof CustomOcelot ||
                nmsEntity instanceof CustomRabbit ||
                nmsEntity instanceof CustomSheep) {

                // Otevření GUI
            	PetGUI.openPetGUI(player, (CustomPet) nmsEntity);
            }
        }
    }
    
    public static CustomPet getActivePet(Player player) {
        Entity pet = activePets.get(player.getUniqueId());
        if (pet instanceof CustomPet) {
            return (CustomPet) pet;
        }
        return null;
    }
    
    public static void replacePet(Player player, Entity newPet, boolean ride) {
        // Nejprve odstraníme stávající mazlíčka
        removePet(player);

        // Přidáme nového mazlíčka do mapy
        activePets.put(player.getUniqueId(), newPet);

        // Nastavíme hráče jako pasažéra, pokud je to požadováno
        if (ride) {
            newPet.getBukkitEntity().setPassenger(player);
        }
    }

}
