package cz.mot.system.util;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_8_R3.*;

import cz.mot.system.pets.*;

public class CustomEntities {

    public static void registerEntities() {
        registerEntity("Wolf", 95, EntityWolf.class, CustomWolf.class);
        registerEntity("Cow", 92, EntityCow.class, CustomCow.class);
        registerEntity("Rabbit", 101, EntityRabbit.class, CustomRabbit.class);
        registerEntity("Ozelot", 98, EntityOcelot.class, CustomOcelot.class);
        registerEntity("Sheep", 91, EntitySheep.class, CustomSheep.class);
        registerEntity("Ghast", 56, EntityGhast.class, CustomGhast.class);
        registerEntity("Horse", 100, EntityHorse.class, CustomHorse.class);
    }

    @SuppressWarnings("unchecked")
    private static void registerEntity(String name, int id, Class<?> nmsClass, Class<?> customClass) {
        try {
            Field c = EntityTypes.class.getDeclaredField("c"); // name -> class
            Field d = EntityTypes.class.getDeclaredField("d"); // class -> name
            Field f = EntityTypes.class.getDeclaredField("f"); // class -> id

            c.setAccessible(true);
            d.setAccessible(true);
            f.setAccessible(true);

            ((Map<String, Class<?>>) c.get(null)).put(name, customClass);
            ((Map<Class<?>, String>) d.get(null)).put(customClass, name);
            ((Map<Class<?>, Integer>) f.get(null)).put(customClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
