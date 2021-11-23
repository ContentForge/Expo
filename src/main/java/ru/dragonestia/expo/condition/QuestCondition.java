package ru.dragonestia.expo.condition;

import ru.dragonestia.expo.player.PlayerData;

public interface QuestCondition {

    void take(PlayerData playerData, String[] args);

}
