package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.player.PlayerData;

public class NightCondition extends Condition {

    public NightCondition() {
        super("time");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) {
        int time = playerData.getPlayer().getLevel().getTime();

        return new Result("Дождаться глубокой ночи", 15000 < time && time < 21000);
    }

}
