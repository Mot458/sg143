package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import cz.mot.system.gui.PetGUI;

public class CustomCow extends EntityCow implements CustomPet {

    private final Player owner;
    private boolean isBaby = false;
    private boolean isMooshroom = false;
    private String customName = null;

    public CustomCow(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Cow");
        this.setCustomNameVisible(true);
        this.setAge(isBaby ? -24000 : 0);
        this.persistent = true;

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3D);
    }

    @Override
    public void t_() {
        super.t_();
        followOwner();
    }

    private void followOwner() {
        if (owner == null || !owner.isOnline()) return;

        double distance = this.getBukkitEntity().getLocation().distance(owner.getLocation());
        if (distance > 20) {
            this.setPositionRotation(owner.getLocation().getX(), owner.getLocation().getY(),
                    owner.getLocation().getZ(), owner.getLocation().getYaw(), owner.getLocation().getPitch());
        } else {
            this.getNavigation().a(owner.getLocation().getX(),
                    owner.getLocation().getY(),
                    owner.getLocation().getZ(), 1.0);
        }
    }

    @Override
    public boolean a(EntityHuman human) {
        // Cow není jezditelná
        return false;
    }

    @Override
    public void m() {
        super.m();
        this.setInvisible(false);
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {
        // Neháže loot
    }

    @Override
    public boolean damageEntity(DamageSource source, float amount) {
        return false;
    }

    @Override
    public Player getOwnerPlayer() {
        return owner;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0); // -24000 = mládě, 0 = dospělý
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setAgeState(boolean isBaby) {
        this.setAge(isBaby ? -24000 : 0);
    }

    @Override
    public String getName() {
        return customName != null ? customName : getCustomName();
    }

    @Override
    public void setName(String name) {
        this.customName = name;
        setCustomName(name);
        setCustomNameVisible(true);
    }

    public boolean isMooshroom() {
        return isMooshroom;
    }

    public void setMooshroom(boolean mooshroom) {
        this.isMooshroom = mooshroom;
    }

    @Override
    public void openPetGUI(Player player) {
        PetGUI.openPetGUI(player, this);
    }
}
