package ru.dragonestia.expo.dialogue.instance;

import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class QuestionButtonInstance extends ButtonInstance {

    public QuestionButtonInstance() {
        super("question");
    }

    @Override
    public void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        dialogue.sendToPlayer(
                playerData,
                sender,
                text,
                true,
                args.length == 0? null : new String[]{Dialogue.format(String.join(" ", args), playerData.getPlayer(), sender)}
        );
    }

}
