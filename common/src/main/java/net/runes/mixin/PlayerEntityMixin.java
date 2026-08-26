package net.runes.mixin;

import net.minecraft.world.entity.player.Player;
import net.runes.crafting.RuneCrafter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerEntityMixin implements RuneCrafter {
    private int lastRuneCrafted = 0;
    @Override
    public void setLastRuneCrafted(int time) {
        lastRuneCrafted = time;
    }
    @Override
    public int getLastRuneCrafted() {
        return lastRuneCrafted;
    }
}
