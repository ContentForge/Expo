package ru.dragonestia.expo.dialogue.instance;

import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class DispatchCommandButtonInstance extends ButtonInstance {

    public DispatchCommandButtonInstance() {
        super("command");
    }

    @Override
    public void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        if(args.length == 0) throw new DialogueError("Неверное кол-во аргументов.");

        playerData.getPlayer().getServer().dispatchCommand(playerData.getPlayer(), String.join(" ", args));
    }

}
