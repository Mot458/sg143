package cz.mot.system.commands;

import cz.mot.system.Main;
import cz.mot.system.manager.PetManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length < 1) return false;
        String type = args[0];
        PetManager manager = Main.getInstance().getPetManager();
        manager.spawnPet(player, type);
        return true;
    }
}
