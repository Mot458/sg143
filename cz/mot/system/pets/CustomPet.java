package cz.mot.system.pets;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface CustomPet {
    public abstract void setBaby(boolean baby);
    public abstract boolean isBaby();
    void setName(String name);
    String getName();
    void openPetGUI(Player player);
    void setAgeState(boolean isBaby);

    /**
     * Vrací Bukkit entitu tohoto mazlíčka.
     */
    Entity getEntity();

    /**
     * Vrací Bukkit hráče – majitele mazlíčka.
     */
    Player getOwnerPlayer();
}
