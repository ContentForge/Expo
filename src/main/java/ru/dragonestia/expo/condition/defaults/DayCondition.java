package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.player.PlayerData;

public class DayCondition extends Condition {

    public DayCondition() {
        super("day");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        int time = playerData.getPlayer().getLevel().getTime();

        return new Result("Дождаться дня", 1000 < time && time < 12000);
    }

}
