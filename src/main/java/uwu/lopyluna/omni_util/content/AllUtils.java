package uwu.lopyluna.omni_util.content;

import net.minecraft.world.level.Level;

public class AllUtils {

    public static int getHourOfTime(Level level) {
        var time = (level.dayTime() + 6000f) % 12000f;
        float multi = time / 12000f;
        int hour = (int) (multi * 12f);
        if (hour == 0) hour = 12;
        return hour;
    }

    public static boolean isAmOfTime(Level level) {
        return ((level.dayTime() + 6000f) % 24000f) < 12000f;
    }

    public enum TimeCycle {
        SUNRISE("Sunrise", 5, true),
        DAY_PRE("Day", 6, true),
        AFTERNOON("Afternoon", 10, true),
        DAY_POST("Day", 4, false),
        SUNSET("Sunset", 6, false),
        MOONRISE("Moonrise", 7, false),
        NIGHT_PRE("Night", 8, false),
        MIDNIGHT("Midnight", 12, false),
        NIGHT_POST("Night", 3, true),
        MOONSET("Moonset", 4, true);

        final String label;
        final int hour;
        final boolean am;

        TimeCycle(String label,  int hour, boolean am) {
            this.label = label;
            this.hour = hour;
            this.am = am;
        }

        public boolean hasDaylight() {
            return this == SUNRISE || this == DAY_PRE || this == AFTERNOON || this == DAY_POST || this == SUNSET;
        }

        public static TimeCycle fromHour(int hour, boolean am) {
            hour = ((hour - 1 + 12) % 12) + 1;
            TimeCycle closest = null;
            int smallestDiff = Integer.MAX_VALUE;
            for (TimeCycle cycle : values()) {
                if (cycle.am != am) continue;
                int diff = Math.abs(cycle.hour - hour);
                if (diff < smallestDiff) {
                    smallestDiff = diff;
                    closest = cycle;
                }
            }
            if (closest == null) {
                for (TimeCycle cycle : values()) {
                    int diff = Math.abs(cycle.hour - hour);
                    if (diff < smallestDiff) {
                        smallestDiff = diff;
                        closest = cycle;
                    }
                }
            }
            if (hour == 12 && am) closest = MIDNIGHT;
            if (hour == 12 && !am) closest = AFTERNOON;
            if (closest == null) closest = AFTERNOON;
            return closest;
        }

        public String getLabel() {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(int hour) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(int hour, boolean am) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(boolean am) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }

        public int toGameTime() {
            return ((((hour + 16) % 12) * 1000) + (am ? 0 : 12000)) % 24000;
        }

        public int getHour() {
            return hour;
        }

        public int getFullHour() {
            return hour + (am ? 0 : 12);
        }

        public boolean isAm() {
            return am;
        }
    }
}
