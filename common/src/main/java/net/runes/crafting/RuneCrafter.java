package net.runes.crafting;

public interface RuneCrafter {
    void setLastRuneCrafted(int time);
    int getLastRuneCrafted();

    default boolean shouldPlayRuneCraftingSound(int age) {
        return age > (getLastRuneCrafted() + RuneCrafting.SOUND_DELAY);
    }

    default void onPlayedRuneCraftingSound(int age) {
        setLastRuneCrafted(age);
    }
}
