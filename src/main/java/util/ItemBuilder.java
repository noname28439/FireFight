package util;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemBuilder {
    
    
    private ItemStack currentItem;

    public ItemBuilder(final Material type, final int amount) {
       
        this.currentItem = new ItemStack(type, amount);
    }
    
    public ItemBuilder(ItemStack parent) {
        this.currentItem = parent.clone();
    }

	public ItemBuilder setDisplayname(final String name) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.setDisplayName(name);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setNoName() {
        setDisplayname("§8");
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.setLore(Arrays.asList(lore));

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    
    public ItemBuilder removeItemFlags(final ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.removeItemFlags(itemFlags);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addItemFlags(final ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.addItemFlags(itemFlags);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantment(final Enchantment enchantment, final int value) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.addEnchant(enchantment, value, true);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeEnchantment(final Enchantment enchantment) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.removeEnchant(enchantment);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean flag) {
        ItemMeta itemMeta = this.currentItem.getItemMeta();

        itemMeta.setUnbreakable(flag);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(final Color color) {
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) this.currentItem.getItemMeta();

        itemMeta.setColor(color);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder setLeatherArmorColorRGB(final int r, final int g, final int b) {
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) this.currentItem.getItemMeta();

        itemMeta.setColor(Color.fromRGB(r, g, b));

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(final int amount) {
        this.currentItem.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setBannerBaseDyeColor(final DyeColor dyeColor) {
        BannerMeta itemMeta = (BannerMeta) this.currentItem.getItemMeta();

        itemMeta.setBaseColor(dyeColor);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBannerPattern(final int variable, final Pattern pattern) {
        BannerMeta itemMeta = (BannerMeta) this.currentItem.getItemMeta();

        itemMeta.setPattern(variable, pattern);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBannerPatterns(final List<Pattern> patterns) {
        BannerMeta itemMeta = (BannerMeta) this.currentItem.getItemMeta();

        itemMeta.setPatterns(patterns);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBlockState(final BlockState blockState) {
        BlockStateMeta itemMeta = (BlockStateMeta) this.currentItem.getItemMeta();

        itemMeta.setBlockState(blockState);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBookTitle(final String title) {
        BookMeta itemMeta = (BookMeta) this.currentItem.getItemMeta();

        itemMeta.setTitle(title);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBookAuthor(final String author) {
        BookMeta itemMeta = (BookMeta) this.currentItem.getItemMeta();

        itemMeta.setAuthor(author);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBookPage(final int page, final String text) {
        BookMeta itemMeta = (BookMeta) this.currentItem.getItemMeta();

        itemMeta.setPage(page, text);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBookPages(final List<String> pages) {
        BookMeta itemMeta = (BookMeta) this.currentItem.getItemMeta();

        itemMeta.setPages(pages);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setBookPages(final String... pages) {
        BookMeta itemMeta = (BookMeta) this.currentItem.getItemMeta();

        itemMeta.setPages(pages);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setFireworkEffect(final FireworkEffect fireworkEffect) {
        FireworkEffectMeta itemMeta = (FireworkEffectMeta) this.currentItem.getItemMeta();

        itemMeta.setEffect(fireworkEffect);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setFireworkPower(final int power) {
        FireworkMeta itemMeta = (FireworkMeta) this.currentItem.getItemMeta();

        itemMeta.setPower(power);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addFireworkEffect(final FireworkEffect fireworkEffect) {
        FireworkMeta itemMeta = (FireworkMeta) this.currentItem.getItemMeta();

        itemMeta.addEffect(fireworkEffect);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeFireworkEffect(final int variable) {
        FireworkMeta itemMeta = (FireworkMeta) this.currentItem.getItemMeta();

        itemMeta.removeEffect(variable);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addFireworkEffects(final FireworkEffect... fireworkEffects) {
        FireworkMeta itemMeta = (FireworkMeta) this.currentItem.getItemMeta();

        itemMeta.addEffects(fireworkEffects);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addFireworkEffects(final Iterable<FireworkEffect> fireworkEffects) {
        FireworkMeta itemMeta = (FireworkMeta) this.currentItem.getItemMeta();

        itemMeta.addEffects(fireworkEffects);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setMapScaling(final boolean flag) {
        MapMeta itemMeta = (MapMeta) this.currentItem.getItemMeta();

        itemMeta.setScaling(flag);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setPotionMainEffect(final PotionEffectType potionEffectType) {
        PotionMeta itemMeta = (PotionMeta) this.currentItem.getItemMeta();

        itemMeta.setMainEffect(potionEffectType);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addPotionCustomEffect(final PotionEffect potionEffect, final boolean overwrite) {
        PotionMeta itemMeta = (PotionMeta) this.currentItem.getItemMeta();

        itemMeta.addCustomEffect(potionEffect, overwrite);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removePotionCustomEffect(final PotionEffectType potionEffectType) {
        PotionMeta itemMeta = (PotionMeta) this.currentItem.getItemMeta();

        itemMeta.removeCustomEffect(potionEffectType);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(final String skullOwner) {
        SkullMeta itemMeta = (SkullMeta) this.currentItem.getItemMeta();

        itemMeta.setOwner(skullOwner);

        this.currentItem.setItemMeta(itemMeta);
        return this;
    }


    public ItemStack build() {
        return this.currentItem;
    }

}