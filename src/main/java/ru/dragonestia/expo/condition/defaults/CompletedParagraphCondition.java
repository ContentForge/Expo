package ru.dragonestia.expo.condition.defaults;

import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.paragraph.Paragraph;
import ru.dragonestia.expo.player.PlayerData;

public class CompletedParagraphCondition extends Condition {

    public CompletedParagraphCondition() {
        super("paragraph");
    }

    @Override
    public Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException {
        Paragraph paragraph = playerData.getMain().getParagraphManager().get(args[0]);
        if(paragraph == null) return new Result("Неизвестная глава["+args[0]+"]", false);

        return new Result((paragraph.isQuest()? "Выполнить задание" : "Изучить главу") + " '"+paragraph.getName()+"'", playerData.isCompletedParagraph(paragraph));
    }

}
