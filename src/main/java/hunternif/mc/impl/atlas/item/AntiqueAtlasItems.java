package hunternif.mc.impl.atlas.item;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class AntiqueAtlasItems {
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(Registries.ITEM, AntiqueAtlasMod.ID);
    public static final DeferredRegister<RecipeSerializer<?>>
            RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AntiqueAtlasMod.ID);

    public static final RegistryObject<Item> EMPTY_ATLAS = ITEMS.register("empty_antique_atlas", () -> new EmptyAtlasItem(new Item.Properties()));
    // Stacks like vanilla's filled map. A filled atlas carries only its atlasID tag, so two
    // atlases stack exactly when they share an atlas number (copies of one map), and atlases
    // of different numbers never merge.
    public static final RegistryObject<Item> ATLAS = ITEMS.register("antique_atlas", () -> new AtlasItem(new Item.Properties().stacksTo(64)));

    static {
        RECIPES.register("atlas_clone", () -> RecipeAtlasCloning.SERIALIZER);
        RECIPES.register("atlas_combine", () -> RecipeAtlasCombining.SERIALIZER);
    }

    public static ItemStack getAtlasFromId(int atlasID) {
        ItemStack atlas = new ItemStack(ATLAS.get());
        atlas.getOrCreateTag().putInt("atlasID", atlasID);

        return atlas;
    }

    /**
     * The items and recipes are always registered: Forge registers content before the config
     * file is read, and a server and client with different settings would otherwise disagree on
     * the item registry. With {@code itemNeeded = false} the items simply go unused.
     */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        RECIPES.register(modBus);
        modBus.addListener(AntiqueAtlasItems::addToCreativeTabs);
    }

    /**
     * Puts the empty atlas in the Tools & Utilities tab, next to vanilla's empty map.
     * On 1.20 an item that is in no creative tab is also missing from JEI, which builds its
     * item list from the tabs. The filled atlas stays out, like vanilla's filled map: one
     * taken from a tab would have no atlas ID of its own.
     */
    private static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EMPTY_ATLAS);
        }
    }
}
