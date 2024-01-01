package cxy.cxybalanceddiet.config;

import java.util.Map;

public class FoodConfig {
    public static FoodConfig instance = null;
    public Map<String, Double> waterFood;

    public Map<String, Double> fatFood;

    public Map<String, Double> fiberFood;

    public Map<String, Double> proteinFood;

    public FoodConfig() {
        this.fatFood = Map.ofEntries(
                Map.entry("cooked_chicken", 4.8),
                Map.entry("cooked_mutton", 7.05),
                Map.entry("cooked_beef", 2.1),
                Map.entry("cooked_porkchop", 18.5),
                Map.entry("cooked_salmon", 3.9),
                Map.entry("apple", 0.1),
                Map.entry("bread", 1.6),
                Map.entry("carrot", 0.1),
                Map.entry("potato", 0.05),
                Map.entry("melon_slice", 0.1),
                Map.entry("mushroom_stew", 0.2),
                Map.entry("rabbit_stew", 1.0),
                Map.entry("beetroot_soup", 0.1),
                Map.entry("cookie ", 1.2)
                // 可以根据需要添加更多
        );

        this.fiberFood = Map.ofEntries(
                Map.entry("apple", 1.2),
                Map.entry("bread", 1.35),
                Map.entry("carrot", 1.4),
                Map.entry("potato", 1.1),
                Map.entry("melon_slice", 0.2),
                Map.entry("mushroom_stew", 0.5),
                Map.entry("rabbit_stew", 0.8),
                Map.entry("beetroot_soup", 0.9),
                Map.entry("cookie ", 1.2)
                // 可以根据需要添加更多
        );

        this.proteinFood = Map.ofEntries(
                Map.entry("cooked_chicken", 9.65),
                Map.entry("cooked_mutton", 8.5),
                Map.entry("cooked_beef", 12.5),
                Map.entry("cooked_porkchop", 6.6),
                Map.entry("cooked_salmon", 8.6),
                Map.entry("apple", 0.15),
                Map.entry("bread", 4.5),
                Map.entry("carrot", 0.45),
                Map.entry("potato", 1.0),
                Map.entry("melon_slice", 0.3),
                Map.entry("mushroom_stew", 1.5),
                Map.entry("rabbit_stew", 5.0),
                Map.entry("beetroot_soup", 1.0)
                // 可以根据需要添加更多
        );

        this.waterFood = Map.ofEntries(
                Map.entry("milk", 50.1),
                Map.entry("apple", 7.5),
                Map.entry("carrot", 4.5),
                Map.entry("potato", 1.2),
                Map.entry("melon_slice", 15d),
                Map.entry("mushroom_stew", 52.1),
                Map.entry("rabbit_stew", 48.2),
                Map.entry("beetroot_soup", 55.6)
                // 可以根据需要添加更多
        );
    }

    public static FoodConfig getInstance() {
        if (instance == null) {
            synchronized (FoodConfig.class) {
                if (instance == null) {
                    instance = new FoodConfig();
                }
            }
        }
        return instance;
    }

    public Double containsInKey(String name, Map<String, Double> map) {
        for (Map.Entry<String, Double> k : map.entrySet()) {
            if (name.contains(k.getKey())) {
                return k.getValue();
            }
        }
        return 0d;
    }

}
