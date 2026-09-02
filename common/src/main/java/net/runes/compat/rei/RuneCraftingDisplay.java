package net.runes.compat.rei;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;
import net.runes.crafting.RuneCrafting;
import net.runes.crafting.RuneCraftingRecipe;

import java.util.List;
import java.util.Optional;

/**
 * REI's view of a single altar recipe: base + addition → result.
 * <p>
 * Built on the <b>logical server</b> by {@link RunesReiCommonPlugin} and shipped to the client by REI's own
 * display sync — since 1.21.2 vanilla no longer sends recipes to the client at all, so there is nothing for a
 * client-side plugin to read. That sync is why {@link #SERIALIZER} is mandatory rather than decorative: REI
 * refuses to accept a server-side display whose serializer is not registered.
 * <p>
 * Not client-only: the server constructs this class.
 */
public class RuneCraftingDisplay extends BasicDisplay {
    public static final CategoryIdentifier<RuneCraftingDisplay> CATEGORY =
            CategoryIdentifier.of(RunesMod.ID, RuneCrafting.NAME);

    public static final DisplaySerializer<RuneCraftingDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(RuneCraftingDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(RuneCraftingDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(RuneCraftingDisplay::getDisplayLocation)
            ).apply(instance, RuneCraftingDisplay::new)),
            PacketCodec.tuple(
                    EntryIngredient.streamCodec().collect(PacketCodecs.toList()), RuneCraftingDisplay::getInputEntries,
                    EntryIngredient.streamCodec().collect(PacketCodecs.toList()), RuneCraftingDisplay::getOutputEntries,
                    PacketCodecs.optional(Identifier.PACKET_CODEC), RuneCraftingDisplay::getDisplayLocation,
                    RuneCraftingDisplay::new));

    public RuneCraftingDisplay(RecipeEntry<RuneCraftingRecipe> entry) {
        this(List.of(EntryIngredients.ofIngredient(entry.value().base()),
                        EntryIngredients.ofIngredient(entry.value().addition())),
                List.of(EntryIngredients.of(entry.value().result())),
                Optional.of(entry.id().getValue()));
    }

    public RuneCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CATEGORY;
    }

    @Override
    public DisplaySerializer<RuneCraftingDisplay> getSerializer() {
        return SERIALIZER;
    }
}
