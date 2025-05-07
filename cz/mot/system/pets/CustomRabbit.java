package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

import cz.mot.system.gui.PetGUI;

public class CustomRabbit extends EntityRabbit implements CustomPet {

    private final Player owner;
    private String customName = null;
    private boolean isBaby = false;

    public CustomRabbit(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Rabbit");
        this.setCustomNameVisible(true);
        this.setRabbitType(0); // výchozí typ: hnědý

        this.goalSelector.a(0, new PathfinderGoalFloat(this)); // základní cíl - plavání
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
                this.getNavigation().a(
                    owner.getLocation().getX(),
                    owner.getLocation().getY(),
                    owner.getLocation().getZ(),
                    1.2
                );
            }
        }
    }

    public int getRabbitVariant() {
        return this.getRabbitType();
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

            super.g(this.aL, this.aM);
        } else {
            super.g(side, forward);
        }
    }

    @Override
    protected String z() {
        return null;
    }

    public void setRabbitType(Rabbit.Type type) {
        this.setRabbitType(convertBukkitType(type));
    }

    private int convertBukkitType(Rabbit.Type type) {
        switch (type) {
            case WHITE:
                return 1;
            case BLACK:
                return 2;
            case BLACK_AND_WHITE:
                return 3;
            case GOLD:
                return 4;
            case SALT_AND_PEPPER:
                return 5;
            case BROWN:
            default:
                return 0;
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
	public void openPetGUI(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAgeState(boolean isBaby) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public org.bukkit.entity.Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}
}
