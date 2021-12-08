package ru.dragonestia.expo.dialogue.instance;

import cn.nukkit.item.Item;
import ru.contentforge.formconstructor.form.element.Button;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.shop.EconomyInstance;
import ru.dragonestia.expo.shop.EconomyManager;
import ru.dragonestia.expo.shop.Product;
import ru.dragonestia.expo.util.ItemPattern;

public class ShopButtonInstance extends ButtonInstance {

    private final EconomyManager economyManager;

    public ShopButtonInstance(Expo main) {
        super("shop");

        this.economyManager = main.getEconomyManager();
    }

    @Override
    public Button getOverrideButton(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError {
        if(args.length < 4) throw new DialogueError("");

        EconomyInstance economyInstance = economyManager.getEconomyInstance(args[0]);
        if(economyInstance == null) throw new DialogueError("Указан неверный id экземпляра магазина");

        boolean buy;
        switch (args[1]){
            case "buy":
                buy = true;
                break;

            case "sell":
                buy = false;
                break;

            default:
                throw new DialogueError("Указан неверный тип страницы магазина");
        }

        int price;
        try {
            price = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex){
            price = 0;
        }
        if(price <= 0) throw new DialogueError("Указана неверная цена товара");

        int id, damage = 0, count = 1;
        try {
            id = Integer.parseInt(args[3]);
            if(args.length > 4) damage = Integer.parseInt(args[4]);
            if(args.length > 5) count = Integer.parseInt(args[5]);
        } catch (NumberFormatException ex){
            throw new DialogueError("Введен неверный тип данных в id предмета");
        }
        if(id < 0 || id >= Item.list.length || Item.list[id] == null) throw new DialogueError("Указан неверный ID предмета");
        if(count < 1 || count > 64) throw new DialogueError("Введено неверное количество предмета");
        ItemPattern pattern = new ItemPattern(id + " " + damage + " " + count);

        return new Product(pattern, price).getFormButton(economyInstance, buy);
    }

}
