package ru.dragonestia.expo.shop;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import lombok.Getter;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Button;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.contentforge.formconstructor.form.element.StepSlider;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.item.ItemDataDictionary;
import ru.dragonestia.expo.util.ItemPattern;

import java.util.ArrayList;

public class Product {

    private static final transient int[] COUNT_ARR = new int[]{1, 2, 3, 5, 7, 10, 16, 24, 32, 48, 56, 64};

    @Getter private final ItemPattern pattern;
    @Getter private final int basePrice;

    public Product(ItemPattern pattern, int basePrice){
        this.pattern = pattern;
        this.basePrice = basePrice;
    }

    public void sendBuyForm(Player player, EconomyInstance economyInstance){
        CustomForm form = new CustomForm("Покупка предмета");
        PlayerInventory inv = player.getInventory();

        ArrayList<SelectableElement> countList = new ArrayList<>();
        boolean canNext = true;
        for(int count: COUNT_ARR){
            boolean cantGet = false;
            int currentPrice = count * basePrice;
            String currentText = (count * pattern.getCount()) + "шт за "+ economyInstance.getMoneyFormat(currentPrice);
            if(canNext){
                if(economyInstance.hasMoney(player, currentPrice)){
                    if(inv.canAddItem(pattern.toItem(count))){
                        countList.add(new SelectableElement("§2§l" + currentText, count));
                        continue;
                    }
                    cantGet = true;
                }
                canNext = false;
                countList.add(new SelectableElement("§4§l" + currentText + (cantGet? "§4[Не поместится в инвентарь]" : ""), null));
            }

        }

        form.addElement("Вы собираетесь купить §3"+pattern.getRuName().replaceAll("§.", "")+"§f по цене §b"+economyInstance.getMoneyFormat(basePrice)+"§f за §b"+pattern.getCount()+"шт§f.")
                .addElement("У вас в наличаи есть §b"+economyInstance.getMoneyFormat(economyInstance.getMoney(player)) + "§f.")
                        .addElement("count", new StepSlider("Количество", countList));

        form.send(player, (p, data) -> {
            Integer count = data.getStepSlider("count").getValue().getValue(Integer.class);
            if(count == null){
                p.sendMessage("§cНедостаточно средств для покупки данного предмета!");
                return;
            }
            Item item = pattern.toItem(count);
            if(!inv.canAddItem(item)){
                p.sendMessage("§cНедостаточно места в инвентаре для нового предмета!");
                return;
            }

            int totalPrice = basePrice * count;
            economyInstance.takeMoney(p, totalPrice);
            inv.addItem(item);

            p.sendMessage("§eВы успешно приобрели §l"+item.getCount()+"шт "+pattern.getRuName()+"§r§e за §l"+economyInstance.getMoneyFormat(totalPrice)+"§r§e!");
        });
    }

    public void sendSellForm(Player player, EconomyInstance economyInstance){
        CustomForm form = new CustomForm("Продажа предмета");
        PlayerInventory inv = player.getInventory();

        ArrayList<SelectableElement> countList = new ArrayList<>();
        boolean canNext = true;
        for(int count: COUNT_ARR){
            int currentPrice = count * basePrice;
            String currentText = (count * pattern.getCount()) + "шт за "+ economyInstance.getMoneyFormat(currentPrice);
            if(canNext){
                if(inv.contains(pattern.toItem(count))){
                    countList.add(new SelectableElement("§2§l" + currentText, count));
                    continue;
                }
                canNext = false;
                countList.add(new SelectableElement("§4§l" + currentText, null));
            }

        }

        form.addElement("Вы собираетесь продать §3"+pattern.getRuName().replaceAll("§.", "")+"§f по цене §b"+economyInstance.getMoneyFormat(basePrice)+"§f за §b"+pattern.getCount()+"шт§f.")
                .addElement("count", new StepSlider("Количество", countList));

        form.send(player, (p, data) -> {
            Integer count = data.getStepSlider("count").getValue().getValue(Integer.class);
            if(count == null){
                p.sendMessage("§cНет столько предметов для продажи!");
                return;
            }

            Item item = pattern.toItem(count);
            int totalPrice = basePrice * count;
            economyInstance.addMoney(p, totalPrice);
            inv.removeItem(item);

            p.sendMessage("§eВы успешно продали §l"+item.getCount()+"шт "+pattern.getRuName()+"§r§e за §l"+economyInstance.getMoneyFormat(totalPrice)+"§r§e!");
        });
    }

    public Button getFormButton(EconomyInstance economyInstance, boolean buy){
        ItemDataDictionary.Data data = Expo.getInstance().getItemData().get(pattern.toItem());

        return new Button(
                data.getName() + "\n§l" + (buy? "Купить" : "Продать") + " за " + economyInstance.getMoneyFormat(basePrice),
                ImageType.PATH,
                data.getIcon(),
                (p, b) -> {
                    if(buy) sendBuyForm(p, economyInstance);
                    else sendSellForm(p, economyInstance);
                }
        );
    }

}
