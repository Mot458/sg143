package cz.mot.system.pets;

import net.minecraft.server.v1_8_R3.*;

import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWolf;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import cz.mot.system.gui.PetGUI;
import cz.mot.system.manager.PetManager;

public class CustomWolf extends EntityWolf implements CustomPet {

    private final Player owner;
    private DyeColor collarColor = DyeColor.RED;
    private String customName = null;

    private ArmorStand collarStand;
    
    private DyeColor dyeColor = DyeColor.RED;

    public CustomWolf(org.bukkit.World world, Player owner) {
        super(((CraftWorld) world).getHandle());
        this.owner = owner;

        this.setCustomName(owner.getName() + "'s Wolf");
        this.setCustomNameVisible(true);
        this.setTamed(false); // není ochočený
        this.setSitting(false);

        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(0.0D);

        spawnCollar();
    }
    
    private void spawnCollar() {
        if (collarStand != null && !collarStand.isDead()) collarStand.remove();

        Location wolfLoc = this.getBukkitEntity().getLocation();
        double yOffset = isBaby() ? -0.6 : -0.4;

        // Posun dolů a zarovnání s natočením vlka
        Location loc = wolfLoc.clone().add(0, yOffset, 0);
        float yaw = wolfLoc.getYaw();

        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityArmorStand nmsArmorStand = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());

        nmsArmorStand.setInvisible(true);
        nmsArmorStand.setSmall(true);
        nmsArmorStand.setBasePlate(false);
        nmsArmorStand.setGravity(false);
        nmsArmorStand.setCustomNameVisible(false);

        // Nastavení směru natočení (yaw)
        nmsArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), yaw, 0);

        ArmorStand bukkitArmorStand = (ArmorStand) nmsArmorStand.getBukkitEntity();
        bukkitArmorStand.setHelmet(new ItemStack(Material.WOOL, 1, dyeColor.getWoolData()));

        collarStand = bukkitArmorStand;

        world.addEntity(nmsArmorStand);
    }

    /*private void spawnCollar() {
    	Location loc = this.getBukkitEntity().getLocation().clone()
    		    .add(this.getBukkitEntity().getLocation().getDirection().normalize().multiply(0.15)) // dopředu
    		    .add(0, getCollarYOffset(), 0); // dolů
        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.setSmall(true);
        stand.setHelmet(new ItemStack(Material.WOOL, 1, collarColor.getWoolData()));
        this.collarStand = stand;
    }*/

    private double getCollarYOffset() {
        return isBaby() ? 0.25 : 0.5; //baby : adult
    }
    
    public void syncCollarPosition() {
        if (collarStand != null && !collarStand.isDead()) {
            Location newLoc = this.getBukkitEntity().getLocation().clone().add(0, getCollarYOffset(), 0);
            collarStand.teleport(newLoc);
        }
    }

    @Override
    public void setAgeState(boolean isBaby) {
        this.setBaby(isBaby);
    }


/*    private void spawnCollarVisual() {
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("ShadowPets"), () -> {
            Location loc = this.getBukkitEntity().getLocation().clone().add(0, getCollarYOffset(), 0);
            collarStand = loc.getWorld().spawn(loc, ArmorStand.class);
            collarStand.setVisible(false);
            collarStand.setGravity(false);
            collarStand.setSmall(true);
            collarStand.setMarker(true);
            collarStand.setHelmet(new ItemStack(Material.WOOL, 1, collarColor.getWoolData()));
        }, 1L);
    }*/
    
    /*private void spawnCollar() {
        Location loc = this.getBukkitEntity().getLocation().clone()
            .add(0, isBaby() ? 0.2 : 0.35, 0); // různé Y podle věku

        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.setSmall(true);
        stand.setHelmet(new ItemStack(Material.WOOL, 1, dyeColor.getWoolData()));

        this.collarStand = stand;
    }

    private double getCollarYOffset() {
        return isBaby() ? 0.1 : 0.3; // Pro mládě nižší obojek
    }
    
    public void syncCollarPosition() {
        if (this.collarStand != null) {
            Location newLoc = this.getBukkitEntity().getLocation().clone()
                .add(0, isBaby() ? 0.2 : 0.35, 0);
            this.collarStand.teleport(newLoc);
        }
    }*/
    


 /*   @Override
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

        if (collarStand != null && !collarStand.isDead()) {
            Location targetLoc = this.getBukkitEntity().getLocation().clone().add(0, getCollarYOffset(), 0);
            collarStand.teleport(targetLoc);
        }
    }*/
    
    
    /*@Override
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

        if (collarStand != null && !collarStand.isDead()) {
            Location targetLoc = this.getBukkitEntity().getLocation().clone().add(0, getCollarYOffset(), 0);
            collarStand.teleport(targetLoc);

            // Nastavení natočení obojku podle směru vlka
            ((CraftArmorStand) collarStand).getHandle().yaw = this.yaw;
        }
    }*/
    
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

        if (collarStand != null && !collarStand.isDead()) {
            Location wolfLoc = this.getBukkitEntity().getLocation();
            Location collarLoc = wolfLoc.clone().add(0, getCollarYOffset(), 0);
            collarStand.teleport(collarLoc);

            // otočení obojku podle yaw vlka
            ((CraftArmorStand) collarStand).getHandle().yaw = this.yaw;
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

            super.g(this.aL, this.aM);
        } else {
            super.g(side, forward);
        }
    }

    public void setCollarColor(DyeColor color) {
        this.collarColor = color;
        if (collarStand != null && !collarStand.isDead()) {
            collarStand.setHelmet(new ItemStack(Material.WOOL, 1, color.getWoolData()));
        }
    }

    public DyeColor getCustomCollarColor() {
        return this.collarColor;
    }

    @Override
    protected String z() {
        return null;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return this.getBukkitEntity();
    }

    @Override
    public Player getOwnerPlayer() {
        return owner;
    }
    
    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean baby) {
        if ((this.getAge() < 0) == baby) return; // Pokud už má požadovaný věk, nic nedělej

        Player player = Bukkit.getPlayer(this.getOwnerUUID());
        if (player == null) return;

        Location loc = this.getBukkitEntity().getLocation();

        // Vytvořit nového vlka
        World bukkitWorld = player.getWorld();
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();

        CustomWolf newWolf = new CustomWolf(bukkitWorld, player);
        newWolf.setPosition(loc.getX(), loc.getY(), loc.getZ());

        newWolf.setAge(baby ? -24000 : 0);
        newWolf.ageLocked = true;

        // Přizpůsobit pozici obojku pro mládě
        if (newWolf.collarStand != null) {
            Location collarLoc = newWolf.getBukkitEntity().getLocation().clone()
                .add(0, baby ? 0.2 : 0.35, 0); // mládě nižší, dospělý vyšší
            newWolf.collarStand.teleport(collarLoc);
        }
        
        newWolf.syncCollarPosition();

        nmsWorld.addEntity(newWolf);

        // Nahraď původního mazlíčka novým
        PetManager.replacePet(player, newWolf, true);

        player.sendMessage("§aMazlíček je nyní " + (baby ? "mládě" : "dospělý") + ".");
    }


    
    
    /*@Override
    public boolean isBaby() {
        return super.isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
    }
    

/*    public boolean isBaby() {
        if (getBukkitEntity() instanceof Ageable) {
            Ageable ageable = (Ageable) getBukkitEntity();
            return ageable.getAge() < 0;
        } else if (getBukkitEntity() instanceof Wolf) {
            EntityWolf wolf = (EntityWolf) ((CraftWolf) getBukkitEntity()).getHandle();
            return wolf.getAge() < 0;
        }
        return false;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
        this.ageLocked = true;
        this.getDataWatcher().watch(12, (byte) (baby ? 1 : 0)); // přepíná model server-side

        // Vynutit přenačtení entity na klientovi
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("ShadowPets"), () -> {
            Player player = owner;
            if (player.isOnline()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                    new PacketPlayOutEntityDestroy(this.getId())
                );
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                    new PacketPlayOutSpawnEntityLiving(this)
                );
            }
        }, 1L);
    }*/

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

    /*@Override
    public void setAgeState(boolean isBaby) {
        this.setAge(isBaby ? -24000 : 0);
    }*/

    @Override
    public void openPetGUI(Player player) {
        PetGUI.openPetGUI(player, this);
    }

    @Override
    public void die() {
        super.die();
        if (collarStand != null && !collarStand.isDead()) {
            collarStand.remove();
        }
    }
}
