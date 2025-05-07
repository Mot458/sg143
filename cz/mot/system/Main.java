package cz.mot.system;

import cz.mot.system.commands.PetCommand;
import cz.mot.system.gui.PetGUI;
import cz.mot.system.listener.ChatInput;
import cz.mot.system.listener.PetGUIListener;
import cz.mot.system.listener.PetListener;
import cz.mot.system.manager.PetManager;
import cz.mot.system.util.CustomEntities;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private PetManager petManager;

    @Override
    public void onEnable() {
        instance = this;
        petManager = new PetManager();
        
        CustomEntities.registerEntities();
        
        getCommand("pet").setExecutor(new PetCommand());
        getServer().getPluginManager().registerEvents(new PetListener(petManager), this);
        getServer().getPluginManager().registerEvents(new PetGUIListener(), this);
        getServer().getPluginManager().registerEvents(new PetGUI(), this);
        getServer().getPluginManager().registerEvents(new ChatInput(), this);

    }

    public static Main getInstance() {
        return instance;
    }

    public PetManager getPetManager() {
        return petManager;
    }
}
