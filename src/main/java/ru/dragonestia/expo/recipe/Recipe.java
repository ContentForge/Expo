package ru.dragonestia.expo.recipe;

import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import lombok.Getter;
import lombok.Setter;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Button;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.item.ItemDataDictionary;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.util.ItemPattern;
import ru.dragonestia.expo.workbench.Workbench;

import java.util.ArrayList;

@Getter
public class Recipe {

    private final static transient int[] countArray = new int[]{1, 2, 3, 5, 8, 16, 24, 32, 48, 54, 64};

    private String id = "test";
    @Setter private String result = "";
    @Setter private String[] regs = new String[]{ "7 0 1" };
    @Setter private String depend = null;
    @Setter private String workbench = "test_workbench";

    private transient ItemPattern cResult;
    private transient ItemPattern[] cRegs;
    private transient ItemDataDictionary itemManager;

    public void init(){
        cResult = new ItemPattern(result);

        cRegs = new ItemPattern[regs.length];
        for(int i = 0; i < regs.length; i++){
            cRegs[i] = new ItemPattern(regs[i]);
        }

        itemManager = Expo.getInstance().getItemData();
    }

    public void sendToPlayer(PlayerData data){
        sendToPlayer(data, null);
    }

    public void sendToPlayer(PlayerData data, Workbench workbench){
        sendToPlayer(data, workbench, null);
    }

    public void sendToPlayer(PlayerData data, Workbench workbench, String error){
        PlayerInventory inv = data.getPlayer().getInventory();
        ItemDataDictionary.Data temp = itemManager.get(cResult.toItem());
        ArrayList<SelectableElement> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder()
                .append("Для создания §3").append(temp.getName()).append("§r§f(§7").append(cResult.getCount()).append("шт§f) вам потребуется:");

        boolean canCraftNext = true;
        for(int constCount: countArray) {
            if (constCount == 1) {
                for (ItemPattern pattern : cRegs) {
                    boolean hasItem = inv.contains(pattern.toItem());
                    sb.append("\n §f- §l§").append(hasItem ? "2" : "4").append(pattern.getRuName()).append("§r ")
                            .append(hasItem ? "2" : "4").append(pattern.getCount()).append("шт§f");

                    if (!hasItem) canCraftNext = false;
                }
            }

            if (canCraftNext) {
                for (ItemPattern pattern : cRegs) {
                    if (inv.contains(pattern.toItem(constCount))) continue;

                    canCraftNext = false;
                    break;
                }

                if(canCraftNext){
                    list.add(new SelectableElement("Количество: §l§4" + (cResult.getCount() * constCount) + "шт", constCount));
                    continue;
                }
            }

            list.add(new SelectableElement("§l§4" + (cResult.getCount() * constCount) + "шт", null));
        }

        CustomForm form = new CustomForm("Создание предмета");
        if(error != null) form.addElement("§l§4" + error);
        form.addElement(sb.toString());

        form.addElement("count", new Dropdown("Количество", list));

        if(data.getPlayer().isOp()){
            form.addElement("§2ID рецепта: §l"+id);
        }

        if(workbench != null){
            form.setNoneHandler(p -> workbench.sendCraftForm(data));
        }

        form.setHandler((p, response) -> {
            Integer count = response.getDropdown("count").getValue().getValue(Integer.class);
            if(count == null){
                sendToPlayer(data, workbench, "Недостаточно ресурсов для создания данного рецепта");
                return;
            }

            Item finalItem = cResult.toItem(count);

            if(inv.canAddItem(finalItem)){
                sendToPlayer(data, workbench, "Недостаточно места в инвентаре для нового предмета");
                return;
            }

            for(ItemPattern target: cRegs){
                inv.removeItem(target.toItem(count));
            }
            inv.addItem(finalItem);

            if(workbench != null && workbench.getSoundOnCraft() != null){
                p.getLevel().addSound(p, workbench.getSoundOnCraft());
            }

            sendToPlayer(data, workbench);
        }).send(data.getPlayer());
    }

    public Button getFormButton(PlayerData playerData, Workbench workbench){
        ItemDataDictionary.Data data = itemManager.get(cResult.toItem());
        return new Button(data.getName(), ImageType.PATH, data.getIcon(), (p, b) -> sendToPlayer(playerData, workbench));
    }

}
