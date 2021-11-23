package ru.dragonestia.expo.item;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;

import java.io.*;
import java.util.HashMap;

public class ItemDataDictionary {

    public static final String DICTIONARY_PATH = "plugins/Expo/item_dictionary.yml";

    @Getter private final Expo main;
    private final HashMap<String, Data> items = new HashMap<>();
    private final Data noneData = new Data(false, "Ничего", "");

    public ItemDataDictionary(Expo main){
        this.main = main;

        load();
    }

    @SneakyThrows
    private void load(){
        File dictionaryFile = new File(DICTIONARY_PATH);

        if(!dictionaryFile.exists()){
            main.getLogger().warning("Не найден словарь предметов. Идет перенос данных со вложенного словаря");

            main.saveResource("item_dictionary.yml",false);

            main.getLogger().info("Успешный перенос данных с исходного словаря предметов");
        }

        Config config = new Config(dictionaryFile, Config.YAML);
        for(String key: config.getRootSection().getKeys()){
            items.put(key, new Data(true, config.getString(key + ".name"), config.getString(key + ".icon")));
        }
    }

    public void convertFromRuItemsPlugin(){
        File namesFile = new File("plugins/items.yml");
        File iconsFile = new File("plugins/icons.yml");

        if(!namesFile.exists() || !iconsFile.exists()){
            main.getLogger().error("Отсутствуют словари с иконками и именами предметов! Отмена операции.");
            return;
        }

        main.getLogger().info("Запуск процесса переноса данных из плагина RuItems");

        Config namesConfig = new Config(namesFile, Config.YAML);
        Config iconsConfig = new Config(iconsFile, Config.YAML);
        for(String key: namesConfig.getRootSection().getKeys()){
            put(key, namesConfig.getString(key), iconsConfig.getString(key, ""));
        }

        main.getLogger().info("Перенос данных со словарей иконок и названий предметов успешно произведен!");
    }

    private String getItemLocalId(Item item){
        if(item == null || item.isNull()) return null;
        if(item.isTool() || item.isArmor()) return String.valueOf(item.getId());
        return item.getId() + "_" + item.getDamage();
    }

    public Data get(Item item){
        String localId = getItemLocalId(item);
        if(localId == null) return noneData;

        if(item instanceof AdvItem) return ((AdvItem) item).getAdvData();

        Data data = items.getOrDefault(localId, new Data(false, item.getName(), ""));
        if(data.contains && item.hasCustomName()){
            data = new Data(true, item.getCustomName(), data.icon);
        }

        return data;
    }

    private void put(String localId, String name, String icon){
        items.put(localId, new Data(true, name, icon));

        Config config = new Config(DICTIONARY_PATH, Config.YAML);
        config.set(localId + ".name", name);
        config.set(localId + ".icon", icon);
        config.save();
    }

    public void put(Item item, String name, String icon){
        put(getItemLocalId(item), name, icon);
    }

    @Getter
    public static class Data {
        private final boolean contains;
        private final String name;
        private final String icon;

        public Data(boolean contains, String name, String icon){
            this.contains = contains;
            this.name = name;
            this.icon = icon;
        }
    }

}
