package net.runes.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.runes.crafting.RuneCrafter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
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
