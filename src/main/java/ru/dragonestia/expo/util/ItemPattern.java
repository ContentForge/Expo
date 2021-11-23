package ru.dragonestia.expo.util;

import cn.nukkit.item.Item;
import lombok.Getter;
import ru.dragonestia.expo.Expo;

@Getter
public class ItemPattern {

    private final int id;
    private final int damage;
    private final int count;

    public ItemPattern(String pattern){
        String[] args = pattern.split(" ");
        int tempId = 0, tempDamage = 0, tempCount = 1;

        try{
            if(args.length > 0) tempId = Integer.parseInt(args[0]);
            if(args.length > 1) tempDamage = Integer.parseInt(args[1]);
            if(args.length > 2) tempCount = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignore){

        }

        id = tempId;
        damage = tempDamage;
        count = tempCount;
    }

    public ItemPattern(Item item){
        id = item.getId();
        damage = item.getDamage();
        count = item.getCount();
    }

    public Item toItem(){
        return toItem(1);
    }

    public Item toItem(int m){
        return Item.get(id, damage, count * m);
    }

    public String getRuName(){
        return Expo.getInstance().getItemData().get(toItem()).getName();
    }

    @Override
    public String toString(){
        return id + " " + damage + " " + count;
    }

}
