package ru.dragonestia.expo.shop;

import cn.nukkit.Player;

public interface EconomyInstance {

    void addMoney(Player player, int value);

    void takeMoney(Player player, int value);

    int getMoney(Player player);

    default boolean hasMoney(Player player, int value){
        return getMoney(player) > value;
    }

    default String getMoneyFormat(int value){
        return value + "\uE102";
    }

}
