package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import cz.mot.system.gui.PetGUI;

public class CustomGhast extends EntityGhast implements CustomPet {

    private final Player owner;
    private String customName = null;

    public CustomGhast(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Pet");
        this.setCustomNameVisible(true);

        this.goalSelector = new PathfinderGoalSelector(((WorldServer) world).methodProfiler);
        this.targetSelector = new PathfinderGoalSelector(((WorldServer) world).methodProfiler);
    }

    @Override
    public void t_() {
        super.t_();

        if (owner != null && owner.isOnline()) {
            double distance = this.getBukkitEntity().getLocation().distance(owner.getLocation());

            // Teleport při větší vzdálenosti
            if (distance > 50) {
                Location loc = owner.getLocation();
                this.setPosition(loc.getX(), loc.getY(), loc.getZ());
            }

            // Následování, pokud nikdo nesedí
            if (this.passenger == null) {
                this.getNavigation().a(
                    owner.getLocation().getX(),
                    owner.getLocation().getY(),
                    owner.getLocation().getZ(),
                    1.2
                );
            }

            // Ovládání při jízdě
            if (this.passenger instanceof EntityPlayer) {
                EntityPlayer rider = (EntityPlayer) this.passenger;

                this.yaw = rider.yaw;
                this.pitch = rider.pitch * 0.5f;
                this.aI = this.yaw;
                this.aK = this.yaw;

                float forward = rider.aM;
                float side = rider.aL;
                double speed = 0.4;

                double yawRad = Math.toRadians(this.yaw);
                double dx = -Math.sin(yawRad) * forward * speed;
                double dz = Math.cos(yawRad) * forward * speed;
                double sx = Math.cos(yawRad) * side * speed;
                double sz = Math.sin(yawRad) * side * speed;

                this.motX = dx + sx;
                this.motZ = dz + sz;

                if (rider.motY > 0.1D) {
                    this.motY = 0.5;
                } else if (rider.isSneaking()) {
                    this.motY = -0.5;
                } else {
                    this.motY = 0;
                }
            }
        }
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
            super.g(this.aL, this.aM);
        } else {
            super.g(side, forward);
        }
    }

    @Override
    public boolean a(EntityHuman human) {
        if (!human.getBukkitEntity().getUniqueId().equals(owner.getUniqueId())) return false;

        // Kliknutí = nasednutí nebo otevření GUI
        if (this.passenger == null) {
            human.mount(this);
        } else {
            PetGUI.openPetGUI((Player) human.getBukkitEntity(), this);
        }
        return true;
    }

    public void c(EntityHuman human) {
        // Hitnutí = otevření GUI pro vlastníka
        if (!human.getBukkitEntity().getUniqueId().equals(owner.getUniqueId())) return;
        PetGUI.openPetGUI((Player) human.getBukkitEntity(), this);
    }

    @Override
    public boolean damageEntity(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean r(Entity entity) {
        return false;
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

    public Player getOwnerPlayer() {
        return owner;
    }

    @Override
    public void setBaby(boolean isBaby) {
        // Ghast nemá baby variantu – metoda prázdná dle specifikace
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
