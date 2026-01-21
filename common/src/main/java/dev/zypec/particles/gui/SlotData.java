package dev.zypec.particles.gui;

import dev.zypec.particles.color.data.RGBColorData;
import dev.zypec.particles.util.item.CustomItem;

public record SlotData(ClickListener listener, CustomItem item, RGBColorData colorData, String name) {
}