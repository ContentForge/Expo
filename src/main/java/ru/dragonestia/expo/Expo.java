package ru.dragonestia.expo;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import com.google.gson.Gson;
import lombok.Getter;
import ru.dragonestia.expo.command.ExpoCommand;
import ru.dragonestia.expo.command.NpcCommand;
import ru.dragonestia.expo.condition.ConditionManager;
import ru.dragonestia.expo.dialogue.DialogueManager;
import ru.dragonestia.expo.entity.npc.NpcEntity;
import ru.dragonestia.expo.entity.npc.NpcListener;
import ru.dragonestia.expo.entity.npc.ProfileManager;
import ru.dragonestia.expo.item.ItemDataDictionary;
import ru.dragonestia.expo.paragraph.ParagraphManager;
import ru.dragonestia.expo.player.PlayerManager;
import ru.dragonestia.expo.recipe.RecipeManager;
import ru.dragonestia.expo.workbench.WorkbenchManager;

import java.io.File;
import java.util.Arrays;

public class Expo extends PluginBase {

    @Getter private static Expo instance;
    @Getter private final static Gson gson = new Gson();

    @Getter private PlayerManager playerManager;
    @Getter private ItemDataDictionary itemData;
    @Getter private ConditionManager conditionManager;
    @Getter private ParagraphManager paragraphManager;
    @Getter private RecipeManager recipeManager;
    @Getter private WorkbenchManager workbenchManager;
    @Getter private DialogueManager dialogueManager;
    @Getter private ProfileManager profileManager;

    @Override
    public void onLoad() {
        instance = this;

        File dir = new File("plugins/Expo");
        if(!dir.exists()) dir.mkdir();

        itemData = new ItemDataDictionary(this);
        playerManager = new PlayerManager(this);
        conditionManager = new ConditionManager(this);
        paragraphManager = new ParagraphManager(this);
        recipeManager = new RecipeManager(this);
        workbenchManager = new WorkbenchManager(this);
        dialogueManager = new DialogueManager(this);
        profileManager = new ProfileManager(this);

        Entity.registerEntity(NpcEntity.ENTITY_ID, NpcEntity.class);
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(playerManager, this);
        pluginManager.registerEvents(dialogueManager, this);
        pluginManager.registerEvents(new NpcListener(this), this);

        getServer().getCommandMap().registerAll("Expo", Arrays.asList(
                new ExpoCommand(this),
                new NpcCommand(this)
        ));
    }

    @Override
    public void onDisable() {
        for(Player player: getServer().getOnlinePlayers().values())player.close();
    }

    public void reload(){
        getLogger().warning("Начата перезагрузка контента");

        paragraphManager.init();
        recipeManager.init();
        dialogueManager.init();
        workbenchManager.reload();

        getLogger().info("Перезагрузка контента успешно завершена!");
    }

}
