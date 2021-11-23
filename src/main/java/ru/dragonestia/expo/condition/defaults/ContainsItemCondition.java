package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.condition.QuestCondition;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.util.ItemPattern;

public class ContainsItemCondition extends Condition implements QuestCondition {

    public ContainsItemCondition() {
        super("item");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        ItemPattern pattern = new ItemPattern(String.join(" ", args));

        return new Result(
                "Иметь при себе " + pattern.getRuName() + "("+pattern.getCount()+"шт)",
                playerData.getPlayer().getInventory().contains(pattern.toItem())
        );
    }

    @Override
    public void take(PlayerData playerData, String[] args) {
        ItemPattern pattern = new ItemPattern(String.join(" ", args));

        playerData.getPlayer().getInventory().removeItem(pattern.toItem());
    }

}
