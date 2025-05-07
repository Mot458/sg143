package cz.mot.system.listener;

import cz.mot.system.manager.PetManager;
import cz.mot.system.pets.CustomPet;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PetListener implements Listener {

    private final PetManager petManager;

    public PetListener(PetManager manager) {
        this.petManager = manager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        petManager.removePet(event.getPlayer());
    }

    private boolean isPetEntity(Entity entity) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            CustomPet pet = PetManager.getActivePet(online);
            if (pet != null && pet.getEntity() != null && pet.getEntity().equals(entity)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPetHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        for (Player online : Bukkit.getOnlinePlayers()) {
            CustomPet pet = PetManager.getActivePet(online);
            if (pet == null || pet.getEntity() == null) continue;

            // Porovnáme NMS entity
            net.minecraft.server.v1_8_R3.Entity petNms = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity) pet.getEntity()).getHandle();
            net.minecraft.server.v1_8_R3.Entity clickedNms = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity) entity).getHandle();

            if (petNms.equals(clickedNms)) {
                event.setCancelled(true);

                if (pet.getOwnerPlayer() != null && pet.getOwnerPlayer().getUniqueId().equals(player.getUniqueId())) {
                    pet.openPetGUI(player);
                }
                return;
            }
        }
    }
}
