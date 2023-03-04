package fr.TheSakyo.EvhoArena.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Kits {

    FIRST_KIT(new ItemStack[] {

            new ItemStack(Material.LEATHER_HELMET),
            customItemStack(Material.CHAINMAIL_CHESTPLATE, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(2)),
            customItemStack(Material.CHAINMAIL_LEGGINGS, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(2)),
            new ItemStack(Material.LEATHER_BOOTS),
            customItemStack(Material.IRON_SWORD, List.of(Enchantment.DAMAGE_ALL), List.of(2)),
            new ItemStack(Material.GOLDEN_APPLE, 8),
            customItemStack(Material.FISHING_ROD, List.of(Enchantment.DURABILITY), List.of(1)),
            new ItemStack(Material.COOKED_BEEF, 10),
            new ItemStack(Material.LAVA_BUCKET),
            new ItemStack(Material.WATER_BUCKET)
    }),
    SECOND_KIT(new ItemStack[] {

            new ItemStack(Material.GOLDEN_HELMET),
            customItemStack(Material.IRON_CHESTPLATE, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(1)),
            customItemStack(Material.IRON_LEGGINGS, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(1)),
            new ItemStack(Material.GOLDEN_BOOTS),
            customItemStack(Material.IRON_SWORD, List.of(Enchantment.DAMAGE_ALL), List.of(2)),
            new ItemStack(Material.GOLDEN_APPLE, 6),
            customItemStack(Material.FISHING_ROD, List.of(Enchantment.DURABILITY), List.of(1)),
            new ItemStack(Material.COOKED_BEEF, 10),
            new ItemStack(Material.LAVA_BUCKET),
            new ItemStack(Material.WATER_BUCKET),
            customItemStack(Material.BOW, List.of(Enchantment.ARROW_FIRE), List.of(1)),
            new ItemStack(Material.ARROW, 10),
    }),
    THIRD_KIT(new ItemStack[] {

            customItemStack(Material.IRON_HELMET, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(2)),
            new ItemStack(Material.DIAMOND_CHESTPLATE),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            customItemStack(Material.IRON_BOOTS, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(2)),
            new ItemStack(Material.DIAMOND_SWORD),
            new ItemStack(Material.GOLDEN_APPLE, 3),
            customItemStack(Material.FISHING_ROD, List.of(Enchantment.DURABILITY), List.of(1)),
            new ItemStack(Material.COOKED_BEEF, 10),
            new ItemStack(Material.LAVA_BUCKET),
            new ItemStack(Material.WATER_BUCKET)
    }),
    FOUR_KIT(new ItemStack[] {

            new ItemStack(Material.DIAMOND_HELMET),
            customItemStack(Material.IRON_CHESTPLATE, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(1)),
            customItemStack(Material.IRON_LEGGINGS, List.of(Enchantment.PROTECTION_ENVIRONMENTAL), List.of(1)),
            new ItemStack(Material.DIAMOND_BOOTS),
            new ItemStack(Material.DIAMOND_SWORD),
            new ItemStack(Material.GOLDEN_APPLE, 5),
            customItemStack(Material.FISHING_ROD, List.of(Enchantment.DURABILITY), List.of(1)),
            new ItemStack(Material.COOKED_BEEF, 10),
            new ItemStack(Material.LAVA_BUCKET),
            new ItemStack(Material.WATER_BUCKET),
            customItemStack(Material.BOW, List.of(Enchantment.ARROW_FIRE), List.of(1)),
            new ItemStack(Material.ARROW, 10)
    }),
    FIVE_KIT(new ItemStack[] {

            new ItemStack(Material.NETHERITE_HELMET),
            new ItemStack(Material.DIAMOND_CHESTPLATE),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            new ItemStack(Material.NETHERITE_BOOTS),
            new ItemStack(Material.NETHERITE_SWORD),
            new ItemStack(Material.GOLDEN_APPLE, 2),
            customItemStack(Material.FISHING_ROD, List.of(Enchantment.DURABILITY), List.of(1)),
            new ItemStack(Material.COOKED_BEEF, 10),
            new ItemStack(Material.LAVA_BUCKET),
            new ItemStack(Material.WATER_BUCKET)
    });
    private final Collection<ItemStack> equipments;

    Kits(ItemStack... equipments) { this.equipments = List.of(equipments); }

    private static ItemStack customItemStack(Material material, List<Enchantment> enchantments, List<Integer> levels) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        for(int i = 0; i < enchantments.size() ; i++) { meta.addEnchant(enchantments.get(i), levels.get(i), true); }
        itemStack.setItemMeta(meta);

        return itemStack;
    }

                /* ----------------------------------------------------------------------------- */

    public Collection<ItemStack> getEquipments() { return this.equipments; }

    public static Collection<ItemStack> getKits(Kits... kits) {

        List<ItemStack> equipmentList = new ArrayList<>();
        Kits[] kitsList = kits;
        int kitsLength = kits.length;

        for(int i = 0; i < kitsLength; ++i) {

            Kits kit = kitsList[i];
            equipmentList.addAll(kit.getEquipments());
        }

        return equipmentList;
    }

    public static Kits randomKits() {

        Kits[] kits = values();
        return kits[new Random().nextInt(kits.length)];
    }
}
