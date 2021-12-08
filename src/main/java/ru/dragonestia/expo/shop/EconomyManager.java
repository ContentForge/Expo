package ru.dragonestia.expo.shop;

import lombok.Getter;
import ru.dragonestia.expo.Expo;

import java.util.HashMap;

public class EconomyManager {

    @Getter private final Expo main;
    private final HashMap<String, EconomyInstance> economyInstanceMap = new HashMap<>();

    public EconomyManager(Expo main){
        this.main = main;
    }

    public void registerEconomyInstance(String id, EconomyInstance instance){
        economyInstanceMap.put(id, instance);
    }

    public EconomyInstance getEconomyInstance(String id){
        return economyInstanceMap.getOrDefault(id, null);
    }

}
