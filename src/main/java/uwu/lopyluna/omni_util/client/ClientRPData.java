package uwu.lopyluna.omni_util.client;

public class ClientRPData {
    private static int cachedRP = 0;
    private static boolean cachedActivation = false;

    public static void updateRP(int newRP) {
        cachedRP = newRP;
    }
    public static void updateActivation(boolean bool) {
        cachedActivation = bool;
    }

    public static int getCachedRP() {
        return cachedRP;
    }

    public static boolean getCachedActivation() {
        return cachedActivation;
    }
}
