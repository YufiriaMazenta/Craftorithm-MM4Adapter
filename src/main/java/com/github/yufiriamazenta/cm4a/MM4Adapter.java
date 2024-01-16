package com.github.yufiriamazenta.cm4a;

import com.github.yufiriamazenta.craftorithm.item.ItemManager;
import com.github.yufiriamazenta.craftorithm.item.ItemProvider;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MM4Adapter extends JavaPlugin {

    @Override
    public void onEnable() {
        ItemManager.INSTANCE.regItemProvider(new ItemProvider() {
            @Override
            public @NotNull String namespace() {
                return "mythic_mobs";
            }

            @Override
            public @Nullable String getItemName(ItemStack itemStack, boolean ignoreAmount) {
                io.lumine.xikage.mythicmobs.items.ItemManager itemManager = MythicMobs.inst().getItemManager();
                if (!isMMItem(itemStack)) {
                    return null;
                } else {
                    String mmItemName = getMMType(itemStack);
                    if (ignoreAmount) {
                        return mmItemName;
                    } else {
                        ItemStack mmItem = itemManager.getItemStack(mmItemName);
                        return mmItemName + " " + itemStack.getAmount() / mmItem.getAmount();
                    }
                }
            }

            @Override
            public @Nullable ItemStack getItem(String itemName) {
                io.lumine.xikage.mythicmobs.items.ItemManager itemManager = MythicMobs.inst().getItemManager();
                Optional<MythicItem> itemOptional = itemManager.getItem(itemName);
                if (!itemOptional.isPresent()) {
                    return null;
                } else {
                    MythicItem mythicItem = itemOptional.get();
                    int amount = mythicItem.getAmount();
                    return BukkitAdapter.adapt(itemOptional.get().generateItemStack(amount));
                }
            }

            public boolean isMMItem(ItemStack item) {
                return MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(item).containsKey("MYTHIC_TYPE");
            }

            public String getMMType(ItemStack item) {
                return MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(item).getString("MYTHIC_TYPE");
            }

        });
    }
}
