package cz.mot.system.pets;

import cz.mot.system.gui.PetGUI;
import cz.mot.system.pathfinder.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_8_R3.*;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;

public class CustomHorse extends EntityHorse implements CustomPet {

    private final Player owner;
    private String customName = null;
    private boolean isBaby = false;

    public CustomHorse(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Horse");
        this.setCustomNameVisible(true);
        this.setAge(0);
        this.setTame(true);

        this.goalSelector = new PathfinderGoalSelector(((WorldServer) world).methodProfiler);
        this.targetSelector = new PathfinderGoalSelector(((WorldServer) world).methodProfiler);
        this.goalSelector.a(1, new PathfinderGoalFollowOwner(this, 1.0D, 3.0F, 2.0F));
    }

    @Override
    public void g(float sideMot, float forMot) {
        if (this.passenger == null) {
            super.g(sideMot, forMot);
        }
    }

    @Override
    public boolean a(EntityHuman human) {
        if (!human.getBukkitEntity().getUniqueId().equals(owner.getUniqueId())) return false;
        human.mount(this);
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void c(EntityHuman human) {
        if (!human.getBukkitEntity().getUniqueId().equals(owner.getUniqueId())) return;
        PetGUI.openPetGUI((Player) human.getBukkitEntity(), this);
    }

    public void setVariant(Variant variant) {
        this.setType(getNMSVariant(variant));
    }

    private int getNMSVariant(Variant variant) {
        switch (variant) {
            case HORSE: return 0;
            case DONKEY: return 1;
            case MULE: return 2;
            case UNDEAD_HORSE: return 3;
            case SKELETON_HORSE: return 4;
            default: return 0;
        }
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

    public int getVariant() {
        return this.datawatcher.getByte(19);
    }

    public void setVariant(int variant) {
        this.datawatcher.watch(19, (byte) variant);
    }

    public enum HorseType {
        NORMAL,
        DONKEY,
        MULE,
        SKELETON,
        UNDEAD
    }

    public void setHorseType(HorseType type) {
        this.setType(convertToNmsType(type));
    }

    private int convertToNmsType(HorseType type) {
        switch (type) {
            case DONKEY: return 1;
            case MULE: return 2;
            case SKELETON: return 3;
            case UNDEAD: return 4;
            default: return 0;
        }
    }

    @Override
    public Player getOwnerPlayer() {
        return owner;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return this.getBukkitEntity();
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

    @Override
    public void openPetGUI(Player player) {
        PetGUI.openPetGUI(player, this);
    }
}
