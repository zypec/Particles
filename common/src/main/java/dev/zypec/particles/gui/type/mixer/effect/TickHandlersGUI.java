package dev.zypec.particles.gui.type.mixer.effect;

import dev.zypec.particles.Particles;
import dev.zypec.particles.effect.Effect;
import dev.zypec.particles.effect.data.PlayerEffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.gui.GUIManager;
import dev.zypec.particles.gui.config.GUIElements;
import dev.zypec.particles.gui.config.GUISounds;
import dev.zypec.particles.gui.type.mixer.MixerHolder;
import dev.zypec.particles.locale.Translations;
import dev.zypec.particles.util.item.CustomItem;
import dev.zypec.particles.util.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;

import static dev.zypec.particles.gui.type.GUIType.HANDLERS;

public class TickHandlersGUI {

    private static GUIManager MANAGER;
    private static Translations TRANSLATIONS;

    public static ItemStack NONE_ELEMENT = new ItemStack(Material.PAPER);
    public static EnumMap<HandlerEvent, ItemStack> ELEMENTS = new EnumMap<>(HandlerEvent.class);

    public static void configure(GUIManager manager) {
        TickHandlersGUI.MANAGER = manager;
        TRANSLATIONS = Particles.getTranslations();
    }

    public static void setItems() {
        for (var event : HandlerEvent.values()) {
            ELEMENTS.put(event, GUIElements.getItemStack(HANDLERS, event.translationKey(), new ItemStack(Material.PAPER)));
        }
        NONE_ELEMENT = GUIElements.getItemStack(HANDLERS, "none", NONE_ELEMENT);
    }

    public static void open(PlayerEffectData data, MixerHolder mixerHolder, Effect effect) {
        // Variables
        var player = data.player;
        var handlers = effect.mixerCompatibleTickHandlers(mixerHolder);

        // Create inventory
        var holder = new TickHandlersHolder();
        var inventory = Bukkit.createInventory(holder, (handlers.size() / 9 * 9) + 9, effect.getParsedDisplayName());
        holder.setInventory(inventory);
        holder.closeListener(p -> {
            if (!holder.lockCloseListener)
                MANAGER.mixerGUI().open(player, mixerHolder);
        });

        for (int i = 0, handlersSize = handlers.size(); i < handlersSize; i++) {
            var handler = handlers.get(i);
            var selected = mixerHolder.isSelected(handler);
            var eventTranslation = TRANSLATIONS.get("events." + (handler.event == null ? "none" : handler.event.translationKey()));
            holder.setItem(i, new CustomItem(handler.event == null ? NONE_ELEMENT : ELEMENTS.get(handler.event))
                    .setDisplayName(MessageUtils.gui(handler.displayName))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .glow(selected)
                    .addLore(
                            MessageUtils.gui(Translations.HANDLER_EVENT_TYPE, eventTranslation),
                            "",
                            selected ? MessageUtils.gui(Translations.HANDLERS_GUI_SELECTED) : MessageUtils.gui(Translations.HANDLERS_GUI_SELECT),
                            selected ? MessageUtils.gui(Translations.HANDLERS_GUI_UNSELECT) : null,
                            selected && handler.mixerOptions.lockEvent ? MessageUtils.gui(Translations.HANDLER_EVENT_LOCKED, eventTranslation) : null
                    ), event -> {
                if (selected) {
                    mixerHolder.remove(handler);
                    holder.lockCloseListener = true;
                    open(data, mixerHolder, effect);
                    GUISounds.play(player, GUISounds.UNSELECT_HANDLER);
                    MessageUtils.sendParsed(player, Translations.MIXER_GUI_UNSELECTED_HANDLER, effect.getDisplayName(), handler.displayName);
                    return;
                }
                mixerHolder.add(effect, handler);
                holder.lockCloseListener = true;
                open(data, mixerHolder, effect);
                GUISounds.play(player, GUISounds.SELECT_HANDLER);
                MessageUtils.sendParsed(player, Translations.MIXER_GUI_SELECTED_HANDLER, effect.getDisplayName(), handler.displayName);
            });
        }

        player.openInventory(inventory);
    }
}