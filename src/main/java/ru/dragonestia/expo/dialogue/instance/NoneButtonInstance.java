package ru.dragonestia.expo.dialogue.instance;

import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class NoneButtonInstance extends ButtonInstance {

    public NoneButtonInstance() {
        super("none");
    }

    @Override
    public void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        if(args.length == 0) return;

        playerData.getPlayer().sendMessage("§2§l"+sender.getNpcName()+"§f: §r§7"+Dialogue.format(String.join(" ", args), playerData.getPlayer(), sender)+"§ы");
    }

}
