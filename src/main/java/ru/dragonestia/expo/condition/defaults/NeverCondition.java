package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.player.PlayerData;

import java.util.Random;

public class NeverCondition extends Condition {

    private final Random random = new Random();

    public NeverCondition() {
        super("never");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        String[] texts = new String[]{
                "Рак на горе свистнул",
                "Стать успешным человеком",
                "HalfLife 3 вышел"
        };

        return new Result(texts[random.nextInt(texts.length)], false);
    }

}
