package uwu.lopyluna.omni_util.client;

public class ClientSanityData {
    private static float sanity = 100;

    public static void update(float sanity) {
        ClientSanityData.sanity = sanity;
    }

    public static float getSanity() {
        return sanity;
    }

}
