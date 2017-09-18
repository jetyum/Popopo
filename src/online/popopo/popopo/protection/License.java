package online.popopo.popopo.protection;

import online.popopo.common.config.Property;

import java.util.Map;

public class License {
    public static final String OPTION_PLAYER = "player";
    public static final String OPTION_ENTITY = "entity";
    public static final String OPTION_BLOCK = "block";
    public static final String OPTION_TIME = "time";
    public static final String OPTION_ALL = "all";

    public static final String CHANGE_BLOCK = "change_block";
    public static final String ATTACK_PLAYER = "attack_player";
    public static final String ATTACK_ENTITY = "attack_entity";
    public static final String SPAWN = "spawn";
    public static final String EXPLOSION = "explosion";
    public static final String CLICK_BLOCK = "click_block";
    public static final String CLICK_ENTITY = "click_entity";
    public static final String CHANGE_VEHICLE = "change_vehicle";
    public static final String ENTER_VEHICLE = "enter_vehicle";
    public static final String EXIT_VEHICLE = "exit_vehicle";
    public static final String CHANGE_HANGING = "change_hanging";

    @Property(key = "name")
    private String name;

    @Property()
    private Map<String, Map<String, Boolean>> config;

    public String getName() {
        return name;
    }

    public boolean allows(String act, String option) {
        if (config.containsKey(act)) {
            Map<String, Boolean> m = config.get(act);

            if (m.containsKey(OPTION_ALL)) {
                return m.get(OPTION_ALL);
            } else if (m.containsKey(option)) {
                return m.get(option);
            }
        }

        return true;
    }
}
