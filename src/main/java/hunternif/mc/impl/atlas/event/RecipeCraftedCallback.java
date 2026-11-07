package hunternif.mc.impl.atlas.event;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import hunternif.mc.impl.atlas.util.event.EventResult;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * Callback right when a item is crafted using a recipe.
 * <p>
 * Listeners must return an {@link EventResult}: this is a {@link EventFactory#createEventResult} event, whose
 * dispatcher casts every listener's return value to {@code EventResult}. Returning anything else (such as
 * {@code InteractionResult}) throws a ClassCastException on every craft.
 * <p>
 * Upon return:
 * - {@link EventResult#pass()} lets the remaining listeners run.
 * - an interrupting result stops the remaining listeners.
 */
public interface RecipeCraftedCallback {

    Event<RecipeCraftedCallback> EVENT = EventFactory.createEventResult(RecipeCraftedCallback.class);

    EventResult onCrafted(Player player, Level world, Recipe recipe, ItemStack result, Container ingredients);
}
