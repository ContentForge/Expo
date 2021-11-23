package ru.dragonestia.expo.item;


public interface AdvItem {

    boolean hasCustomName();
    String getCustomName();
    String getRuName();
    String getIcon();

    default ItemDataDictionary.Data getAdvData(){
        return new ItemDataDictionary.Data(true, hasCustomName()? getCustomName() : getRuName(), getIcon());
    }

}
