package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import cz.mot.system.gui.PetGUI;

public class CustomSheep extends EntitySheep implements CustomPet {

    private final Player owner;
    private String customName = null;
    private boolean isBaby = false;

    public CustomSheep(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Sheep");
        this.setCustomNameVisible(true);
        //this.setAge(-24000); // výchozí věk jako mládě
    }

    @Override
    public void t_() {
        super.t_();

        if (owner != null && owner.isOnline()) {
            double distance = this.getBukkitEntity().getLocation().distance(owner.getLocation());

            if (distance > 50) {
                this.setPosition(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ());
            }

            if (this.passenger == null) {
                this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.2);
            }
        }
    }

    @Override
    public boolean a(EntityHuman human) {
        if (!human.getBukkitEntity().getUniqueId().equals(owner.getUniqueId())) return false;

        if (this.passenger == null) {
            human.mount(this);
        } else {
            PetGUI.openPetGUI((Player) human.getBukkitEntity(), this);
        }
        return true;
    }

    @Override
    public boolean r(Entity entity) {
        return false;
    }

    @Override
    protected String z() {
        return null;
    }

    // ====== Vlastnosti ovečky ======

    public void setColor(DyeColor color) {
        this.setColor(EnumColor.valueOf(color.name()));
    }

    public DyeColor getSheepColor() {
        return DyeColor.valueOf(this.getColor().name());
    }

    @Override
    public Player getOwnerPlayer() {
        return owner;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0); // -24000 = mládě, 0 = dospělý
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    public void toggleAge() {
        setBaby(!isBaby());
    }

    @Override
    public String getName() {
        return customName != null ? customName : getCustomName();
    }

    public void setName(String name) {
        this.customName = name;
        setCustomName(name);
        setCustomNameVisible(true);
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return this.getBukkitEntity();
    }

    @Override
    public void openPetGUI(Player player) {
        PetGUI.openPetGUI(player, this);
    }

    @Override
    public void setAgeState(boolean isBaby) {
        setBaby(isBaby);
    }
}
