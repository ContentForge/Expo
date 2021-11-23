package ru.dragonestia.expo.paragraph;

import cn.nukkit.level.Sound;
import lombok.Getter;
import lombok.Setter;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Button;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.condition.Condition;
import ru.dragonestia.expo.condition.ConditionManager;
import ru.dragonestia.expo.condition.QuestCondition;
import ru.dragonestia.expo.event.PlayerLearnedParagraphEvent;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.workbench.Workbench;

@Getter
public final class Paragraph {

    private String id;
    @Setter private String name = "";
    @Setter private String text = "";
    @Setter private String icon = "";
    @Setter private String[] conditions = new String[0];
    @Setter private String depend = null;
    @Setter private boolean dev = false;
    @Setter private String workbench = null;
    @Setter private boolean quest = false;

    public Paragraph(String id){
        this.id = id;
    }

    public boolean canOpen(PlayerData playerData){
        if(dev && playerData.getPlayer().isOp()) return true;
        if(playerData.isCompletedParagraph(this)) return false;
        return depend == null || playerData.isCompletedParagraph(depend);
    }

    public void sendToPlayer(PlayerData data, Workbench workbench){
        if(data.isCompletedParagraph(this) && !quest){
            data.getPlayer().sendMessage("§cВы уже изучили данную главу.");
            return;
        }
        if(!canOpen(data)) return;

        SimpleForm form = new SimpleForm(name + (dev || data.getPlayer().isOp()? ("["+id+"]") : ""), text);
        boolean canComplete = true;

        if(conditions.length != 0){
            StringBuilder sb = new StringBuilder("Для завершения "+(quest? "задания" : "главы")+" потребуется:");

            ConditionManager conditionManager = Expo.getInstance().getConditionManager();
            for(String line: conditions){
                Condition.Result result = conditionManager.handleCondition(data, line);
                sb.append("\n §f- ").append(result.getResultText()).append("§f");
                if(!result.isCompleted()) canComplete = false;

                if(dev){
                    sb.append("§8[").append(line).append("]§f");
                }
            }

            form.addContent(sb.toString());
        }

        if(canComplete){
            form.addButton("Завершить", ImageType.PATH, quest? "textures/ui/realms_green_check" : "textures/items/book_writable", (p, b) -> {
                if(quest){
                    ConditionManager conditionManager = Expo.getInstance().getConditionManager();
                    for(String line: conditions){
                        String[] cmd = line.split(" ", 2);
                        if(cmd.length == 0) continue;

                        Condition condition = conditionManager.getCondition(cmd[0]);
                        if(!(condition instanceof QuestCondition)) continue;

                        ((QuestCondition) condition).take(data, cmd.length == 1? new String[0] : cmd[1].split(" "));
                    }
                }

                p.getLevel().addSound(p, quest? Sound.RANDOM_TOTEM : Sound.BLOCK_CARTOGRAPHY_TABLE_USE);
                if(quest) p.sendMessage("§eЗадание §l"+name+"§r§e было успешно выполнено!");
                else p.sendMessage("§eГлава §l"+name+"§r§e была успешно изучена!");
                data.completeParagraph(this);

                PlayerLearnedParagraphEvent event = new PlayerLearnedParagraphEvent(p, data, this);
                p.getServer().getPluginManager().callEvent(event);
            });
        }

        if(workbench != null){
            form.addButton("Назад", (p, b) -> workbench.sendStudyForm(data));
        }

        form.send(data.getPlayer());
    }

    public Button getFormButton(PlayerData playerData, Workbench workbench){
        String buttonText = name;
        if(dev) buttonText += "§l§d[dev]§r";
        return new Button(buttonText, ImageType.PATH, icon, (p, b) -> sendToPlayer(playerData, workbench));
    }

    @Override
    public boolean equals(Object object){
        if(object == null || hashCode() != object.hashCode() || !getClass().isInstance(object)) return false;
        return id.equals(((Paragraph) object).id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

}
