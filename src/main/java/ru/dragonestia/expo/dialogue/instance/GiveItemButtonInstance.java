package ru.dragonestia.expo.dialogue.instance;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueManager;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class GiveItemButtonInstance extends ButtonInstance{

    private final DialogueManager dialogueManager;

    public GiveItemButtonInstance(DialogueManager dialogueManager) {
        super("give_item");

        this.dialogueManager = dialogueManager;
    }

    @Override
    public void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        if(args.length < 2) throw new DialogueError("Неверное кол-во аргументов");

        Dialogue target;
        if(!args[0].equals("null")){
            target = dialogueManager.getDialogue(args[0]);
            if(target == null) throw new DialogueError("Диалога "+ args[0]+" не существует");
        } else target = null;

        int id, damage = 0, count = 1;
        try {
            id = Integer.parseInt(args[1]);
            if(args.length > 2) damage = Integer.parseInt(args[2]);
            if(args.length > 3) count = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex){
            throw new DialogueError("Введен неверный тип данных в id предмета");
        }
        if(id < 0 || id >= Item.list.length || Item.list[id] == null) throw new DialogueError("Указан неверный ID предмета");
        if(count < 1 || count > 64) throw new DialogueError("Введено неверное количество предмета");

        Item item = Item.get(id, damage, count);
        Player player = playerData.getPlayer();
        PlayerInventory inv = player.getInventory();

        if(inv.canAddItem(item)) inv.addItem(item);
        else player.getLevel().dropItem(player, item);

        target.sendToPlayer(playerData, sender, text, false, target.getText());
    }

}
