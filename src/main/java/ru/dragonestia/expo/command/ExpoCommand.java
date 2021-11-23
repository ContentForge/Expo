package ru.dragonestia.expo.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.dragonestia.expo.Expo;

public class ExpoCommand extends Command {

    private final Expo main;

    public ExpoCommand(Expo main) {
        super("expo", "Управление плагином ExpoLib");

        this.main = main;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof Player){
            if(!sender.isOp()){
                sender.sendMessage("Саси :D");
                return false;
            }
        }

        if(args.length == 0 || (args[0].equalsIgnoreCase("help"))){
            sender.sendMessage("reload - Перезагрузить контент");
            sender.sendMessage("ruconvert - Конвертировать словари из плагина RuItems");
            sender.sendMessage("item - Задать название предмета и его иконку");
            return true;
        }

        switch (args[0].toLowerCase()){
            case "reload":
                main.reload();
                break;

            case "ruconvert":
                main.getItemData().convertFromRuItemsPlugin();
                break;

            default:
                sender.sendMessage("Неверная суб-команда!");
                return false;
        }
        return true;
    }

}
