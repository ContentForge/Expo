package ru.dragonestia.expo.workbench;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Sound;
import lombok.Getter;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.paragraph.Paragraph;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.recipe.Recipe;

import java.util.HashSet;
import java.util.Set;

public abstract class Workbench {

    @Getter private final String id;
    @Getter private final String name;
    private Set<Paragraph> paragraphs;
    private Set<Recipe> recipes;

    public Workbench(String id, String name){
        this.id = id;
        this.name = name;

        init();
    }

    protected void init(){
        Expo main = Expo.getInstance();

        paragraphs = new HashSet<>();
        recipes = new HashSet<>();

        main.getParagraphManager().initWorkbench(this);
        main.getRecipeManager().initWorkbench(this);
    }

    public void addParagraph(Paragraph paragraph){
        paragraphs.add(paragraph);
    }

    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }

    public final void sendToPlayer(Player player) {
        sendToPlayer(player, null);
    }

    public final void sendToPlayer(Player player, Block tappedBlock){
        PlayerData playerData = Expo.getInstance().getPlayerManager().get(player);
        if(!canOpen(playerData)) return;
        open(playerData, tappedBlock);
    }

    protected boolean canOpen(PlayerData playerData){
        return true;
    }

    public abstract void open(PlayerData playerData, Block tappedBlock);

    public void sendCraftForm(PlayerData playerData){
        SimpleForm form = new SimpleForm(name, "Выберите предмет, который вы хотите создать");

        for(Recipe recipe: recipes){
            if(recipe.getDepend() != null && !playerData.isCompletedParagraph(recipe.getDepend())) continue;

            form.addButton(recipe.getFormButton(playerData, this));
        }

        form.send(playerData.getPlayer());
    }

    public void sendStudyForm(PlayerData playerData){
        SimpleForm form = new SimpleForm(name, "Выберите главу, которую вы хотите изучить");

        for(Paragraph paragraph: paragraphs){
            if(!paragraph.canOpen(playerData)) continue;
            if(!playerData.getPlayer().isOp() && paragraph.isDev()) continue;

            form.addButton(paragraph.getFormButton(playerData, this));
        }

        form.send(playerData.getPlayer());
    }

    public Sound getSoundOnCraft(){
        return null;
    }

}
