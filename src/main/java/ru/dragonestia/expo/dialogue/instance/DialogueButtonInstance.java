package ru.dragonestia.expo.dialogue.instance;

import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueManager;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class DialogueButtonInstance extends ButtonInstance {

    private final DialogueManager dialogueManager;

    public DialogueButtonInstance(DialogueManager dialogueManager) {
        super("dialogue");

        this.dialogueManager = dialogueManager;
    }

    @Override
    public void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        if(args.length == 0) throw new DialogueError("Неверное кол-во аргументов");

        Dialogue target = dialogueManager.getDialogue(args[0]);
        if(target == null) throw new DialogueError("Диалога "+ args[0]+" не существует");

        target.sendToPlayer(playerData, sender, text, false, target.getText());
    }

}
