package ru.dragonestia.expo.recipe;

import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.workbench.Workbench;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class RecipeManager {

    @Getter private final Expo main;
    private HashMap<String, Recipe> recipes;

    public RecipeManager(Expo main){
        this.main = main;

        createExampleRecipe();
        init();
    }

    public void init(){
        recipes = new HashMap<>();
        initRecipes();
    }

    public Recipe get(String id){
        return recipes.getOrDefault(id, null);
    }

    @SneakyThrows
    private void initRecipes(){
        File dir = new File("plugins/Expo/recipes");
        if(!dir.exists()) dir.mkdir();

        for(File file: dir.listFiles()){
            if(!file.isFile()) continue;

            try(FileReader reader = new FileReader(file)){
                Recipe recipe = Expo.getGson().fromJson(reader, Recipe.class);

                recipe.init();
                recipes.put(recipe.getId(), recipe);
            } catch (JsonSyntaxException ex) {
                main.getLogger().error("Произошла ошибка при загрузке рецепта в файле " + file.getName());
            }
        }
    }

    public void initWorkbench(Workbench workbench){
        for(Recipe recipe: recipes.values()){
            if(workbench.getId().equals(recipe.getDepend())) continue;
            workbench.addRecipe(recipe);
        }
    }

    @SneakyThrows
    private void createExampleRecipe(){
        File file = new File("plugins/Expo/example_recipe.json");
        if(!file.exists()) file.createNewFile();

        try(FileWriter writer = new FileWriter(file)){
            writer.write(Expo.getGson().toJson(new Recipe()));
        }
    }

}
