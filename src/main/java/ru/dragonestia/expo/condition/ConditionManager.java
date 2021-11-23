package ru.dragonestia.expo.condition;

import lombok.Getter;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.condition.defaults.*;
import ru.dragonestia.expo.player.PlayerData;

import java.util.HashMap;

public class ConditionManager {

    @Getter private final Expo main;
    private final HashMap<String, Condition> conditions = new HashMap<>();

    public ConditionManager(Expo main){
        this.main = main;

        initDefaultConditions();
    }

    private void initDefaultConditions(){
        registerCondition(new NeverCondition());
        registerCondition(new ContainsItemCondition());
        registerCondition(new CompletedParagraphCondition());
        registerCondition(new NotCompletedParagraphCondition());
        registerCondition(new NightCondition());
        registerCondition(new DayCondition());
        registerCondition(new ReadDialogueCondition());
        registerCondition(new NotReadDialogueCondition());
        registerCondition(new RainCondition());
    }

    public void registerCondition(Condition condition){
        conditions.put(condition.getName(), condition);
    }

    public Condition.Result handleCondition(PlayerData playerData, String command){
        String[] cmd = command.split(" ", 2);
        if(cmd.length == 0 || !conditions.containsKey(cmd[0])) return new Condition.Result("Не прописано условие", false);

        Condition.Result result;
        try{
            result = conditions.get(cmd[0]).handle(playerData, cmd.length > 1? cmd[1].split(" ") : new String[0]);

            return result != null? result : new Condition.Result("Условие вернуло null[" + command + "]", false);
        } catch (IndexOutOfBoundsException ex){
            return new Condition.Result("Ошибка с количеством аргументов[" + command + "]", false);
        }
    }

    public Condition getCondition(String conditionId){
        return conditions.getOrDefault(conditionId, null);
    }

}
