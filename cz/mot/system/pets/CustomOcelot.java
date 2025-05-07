package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import cz.mot.system.gui.PetGUI;

public class CustomOcelot extends EntityOcelot implements CustomPet {

    private final Player owner;
    private String customName = null;
    private boolean isBaby = false;

    public CustomOcelot(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Ocelot");
        this.setCustomNameVisible(true);
        this.setAge(0); // Dospělý

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0f));
        this.goalSelector.a(2, new PathfinderGoalFollowOwner(this, 1.0, 5.0f, 2.0f));
        this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));

        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(0.0D); // Zakázat útok
    }

    @Override
    public void t_() {
        super.t_();

        if (owner != null && owner.isOnline()) {
            double distance = this.getBukkitEntity().getLocation().distance(owner.getLocation());

            if (distance > 50) {
                Location loc = owner.getLocation();
                this.setPosition(loc.getX(), loc.getY(), loc.getZ());
            }

            if (this.passenger == null) {
                this.getNavigation().a(
                    owner.getLocation().getX(),
                    owner.getLocation().getY(),
                    owner.getLocation().getZ(),
                    1.2
                );
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
    public void g(float side, float forward) {
        if (this.passenger instanceof EntityHuman) {
            EntityHuman human = (EntityHuman) this.passenger;

            this.yaw = human.yaw;
            this.pitch = human.pitch * 0.5F;
            this.lastYaw = this.yaw;

            this.aM = forward;
            this.aL = side;

            if (this.aM <= 0.0F) this.aM *= 0.25F;

            this.S = 1.0F;
            this.P = this.bI();
            super.g(this.aL, this.aM);
        } else {
            super.g(side, forward);
        }
    }

    @Override
    protected void cm() {
        // Nepřidávej PathfinderGoalAvoidPlayer
    }

    @Override
    public void setSitting(boolean flag) {
        // Ignoruj – zákaz sezení
    }

    public enum OcelotType {
        WILD,
        BLACK,
        RED,
        SIAMESE
    }

    public void setOcelotType(OcelotType type) {
        super.setCatType(convertToNmsType(type));
    }

    private int convertToNmsType(OcelotType type) {
        switch (type) {
            case BLACK: return 1;
            case RED: return 2;
            case SIAMESE: return 3;
            default: return 0;
        }
    }

    public Player getOwnerPlayer() {
        return owner;
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
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0); // -24000 = mládě, 0 = dospělý
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
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
