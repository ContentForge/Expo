package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.player.PlayerData;

public class NotReadDialogueCondition extends Condition {

    public NotReadDialogueCondition() {
        super("not_read");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        String[] tmp = String.join(" ", args).split(" ", 2);

        return new Result(tmp.length > 1? tmp[1] : "", !playerData.isReadDialogue(tmp[0]));
    }

}
