package hunternif.mc.impl.atlas.item;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class AntiqueAtlasItems {
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(Registries.ITEM, AntiqueAtlasMod.ID);
    public static final DeferredRegister<RecipeSerializer<?>>
            RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AntiqueAtlasMod.ID);

    public static final RegistryObject<Item> EMPTY_ATLAS = ITEMS.register("empty_antique_atlas", () -> new EmptyAtlasItem(new Item.Properties()));
    public static final RegistryObject<Item> ATLAS = ITEMS.register("antique_atlas", () -> new AtlasItem(new Item.Properties().stacksTo(1)));

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
    }
}
