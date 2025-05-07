
package cz.mot.system.pathfinder;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PathfinderGoalFollowOwner extends PathfinderGoal {
    private final EntityInsentient pet;
    private final double speed;
    private final float minDist;
    private final float maxDist;
    private Player owner;

    public PathfinderGoalFollowOwner(EntityInsentient pet, double speed, float minDist, float maxDist) {
        this.pet = pet;
        this.speed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
    }

    @Override
    public boolean a() {
        if (!(pet instanceof EntityTameableAnimal)) return false;

        owner = ((CraftPlayer) ((EntityTameableAnimal) pet).getOwner().getBukkitEntity()).getPlayer();
        if (owner == null) return false;

        return pet.getBukkitEntity().getLocation().distance(owner.getLocation()) > minDist;
    }

    @Override
    public boolean b() {
        return pet.getBukkitEntity().getLocation().distance(owner.getLocation()) > maxDist;
    }

    @Override
    public void c() {}

    @Override
    public void d() {}

    @Override
    public void e() {
        pet.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), speed);
    }
}
