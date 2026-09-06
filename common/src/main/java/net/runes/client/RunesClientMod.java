package net.runes.client;

public class RunesClientMod {
    public static void init() {
        // Screen registration is loader-specific and lives in each platform's client entrypoint
        // (Fabric: ScreenRegistry.register; Forge: HandledScreens.register from FMLClientSetupEvent).
        // On 1.20.1 `HandledScreens.register` and its `Provider` interface are vanilla-private: Fabric API
        // widens them with an access widener, Forge patches them public. `common` has neither on this
        // line (there is no Forgified Fabric API), so the call cannot live here.
    }
}
