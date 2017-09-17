package online.popopo.popopo.protection;

import online.popopo.common.config.Property;

import java.util.Map;

public class License {
    public static final String PLAYER_CHANGE_BLOCK = "player_change_block";
    public static final String ENTITY_CHANGE_BLOCK = "entity_change_block";
    public static final String BLOCK_CHANGE_BLOCK = "block_change_block";
    public static final String TIME_CHANGE_BLOCK = "time_change_block";
    public static final String PLAYER_ATTACK_PLAYER = "player_attack_player";
    public static final String ENTITY_ATTACK_PLAYER = "entity_attack_player";
    public static final String PLAYER_ATTACK_ENTITY = "player_attack_entity";
    public static final String ENTITY_ATTACK_ENTITY = "entity_attack_entity";
    public static final String CREATURE_SPAWN = "creature_spawn";
    public static final String ENTITY_EXPLOSION = "entity_explosion";
    public static final String BLOCK_EXPLOSION = "block_explosion";
    public static final String PLAYER_CLICK_BLOCK = "player_click_block";
    public static final String PLAYER_CLICK_ENTITY = "player_click_entity";
    public static final String VEHICLE_CHANGE = "vehicle_change";
    public static final String PLAYER_ENTER_VEHICLE = "player_enter_vehicle";
    public static final String PLAYER_EXIT_VEHICLE = "player_exit_vehicle";
    public static final String PLAYER_CHANGE_HANGING = "player_hanging_change";
    public static final String HANGING_BREAK = "hanging_break";

    @Property(key = "name")
    private String name;

    @Property()
    private Map<String, Boolean> config;

    public String getName() {
        return name;
    }

    public boolean contains(String action) {
        if (config.containsKey(action)) {
            return config.get(action);
        } else {
            return true;
        }
    }
}
