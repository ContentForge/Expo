package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.player.PlayerData;

public class RainCondition extends Condition {

    public RainCondition() {
        super("rain");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        return new Result("Дождаться дождя", playerData.getPlayer().getLevel().isRaining());
    }

}
