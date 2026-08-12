package net.runes.client;

public class RunesClientMod {
    public static void init() {
        // Screen registration is loader-specific and lives in each platform's client entrypoint
        // (Fabric: HandledScreens.register; NeoForge: RegisterMenuScreensEvent) — mirroring SpellEngine.
        // HandledScreens.register is a vanilla-private method that Fabric API widens; calling it from
        // common crashes on NeoForge (IllegalAccessError) without Forgified Fabric API.
    }
}
