package subaraki.telepads.handler;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ConfigData {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static
    {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static
    {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    // SERVER
    public static boolean allowDragonBlocking = true;

    public static boolean allowAnvilPearls = true;

    public static boolean disableBeadsUsage = false;
    public static boolean disableNecklaceUsage = false;

    public static String[] tp_locations = new String[] {};

    public static int teleport_seconds = 3;

    public static int expConsume;
    public static int lvlConsume;
    public static boolean consumeLvl;

    // CLIENT
    public static boolean allowParticles = true;

    public static class ServerConfig {

        public final ForgeConfigSpec.BooleanValue allowDragonBlocking;
        public final ForgeConfigSpec.BooleanValue allowAnvilPearls;
        public final ForgeConfigSpec.BooleanValue disableBeadsUsage;
        public final ForgeConfigSpec.BooleanValue disableNecklaceUsage;

        public final ForgeConfigSpec.IntValue exp;
        public final ForgeConfigSpec.IntValue lvl;
        public final ForgeConfigSpec.IntValue teleport_delay;

        public final ConfigValue<List<? extends String>> val;

        ServerConfig(ForgeConfigSpec.Builder builder) {

            builder.push("Teleporting from and to the End");
            allowDragonBlocking = builder.comment("Wether to allow the presence of the Ender Dragon to prevent teleporting away from the End")
                    .translation("config.block.dragon").define("Dragon Teleport Block", true);
            builder.pop();

            builder.push("Teleport Items");
            allowAnvilPearls = builder.comment("Disable creation of Ender Beads here").translation("config.anvil.allow").define("Allow anvil pearl crafting",
                    true);
            disableBeadsUsage = builder.comment("Disable usage of the Ender Beads here").translation("config.beads.allow").define("Allow Ender Bead usage",
                    true);
            disableNecklaceUsage = builder.comment("Dsiabe usage of the Ender Necklace here").translation("config.necklace.allow")
                    .define("Allow Ender Necklace usage", true);
            builder.pop();

            builder.push("Teleportation Details");
            exp = builder.comment(
                    "Penalty cost for teleportation, if set to 0, there will be no exp loss. Set level to 0 if you onyl want to consume an amount of exp.")
                    .translation("config.consume.exp").defineInRange("Experience Consumation", 0, 0, 10000);
            lvl = builder.comment("Penalty cost for teleportation, if set to 0, there will be no exp loss. set experience to 0 if you want to consume levels.")
                    .translation("config.consume.level").defineInRange("Level Consumation", 0, 0, 32);
            teleport_delay = builder.comment("Delay, in seconds, on how long a player must wait on the telepad block before the gui opens")
                    .translation("config.tele.gui.open").defineInRange("Teleport GUI Delay", 3, 1, 60);
            builder.pop();

            val = builder
                    .comment(String.format("[x,y,z,dimension,locationName] locations can be defined in multiple ways :\n"
                            + "exactly(100/64/100/0/Any Name really),\n" + "with margin (-500#1000/64#128/0#500/-1#1,Some Location Name)\n"
                            + "or random (random/random/random/random/LocationNameHere)."
                            + "values can be mixed (-100#5000/random/100/0/yourLocationNameHere) is totally possible"))
                    .defineList("reflection_whitelisted_blocks", Lists.newArrayList(), obj -> obj instanceof String);

        }
    }

    public static class ClientConfig {

        public final ForgeConfigSpec.BooleanValue allowParticles;

        ClientConfig(ForgeConfigSpec.Builder builder) {

            builder.push("Rendering");
            allowParticles = builder.comment("Some people find them annoying. Feel free to disable them here if needed")
                    .translation("config.particles.telepad.allow").define("Allow Particle Spawning", true);
            builder.pop();

        }
    }

    public static void refreshServer()
    {

    }

    public static void refreshClient()
    {

    }
}
