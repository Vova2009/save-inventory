package ru.vova_2009.saveinventory.data;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.equipment.EquipmentInventory;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.property.EquipmentSlotType;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Vova 2009
 */
@ConfigSerializable
public class InventoryData {

    @Setting
    private final List<SlotData> grid = new ArrayList<>();

    @Setting
    private final List<SlotData> hotbar = new ArrayList<>();

    @Setting
    private final List<EquipmentSlotData> equipment = new ArrayList<>();

    @Setting
    private ItemStack offHand = ItemStack.empty();

    public InventoryData() {
    }

    public InventoryData(Player player) {
        PlayerInventory inventory = player.getInventory()
                .query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));
        Iterator<Slot> mainGridIterator = inventory.getMainGrid().<Slot>slots().iterator();
        while (mainGridIterator.hasNext()) {
            Slot slot = mainGridIterator.next();
            addSlot(slot, grid);
        }
        Iterator<Slot> hotbarIterator = inventory.getHotbar().<Slot>slots().iterator();
        while (hotbarIterator.hasNext()) {
            Slot slot = hotbarIterator.next();
            addSlot(slot, hotbar);
        }
        Iterator<Slot> equipmentIterator = inventory.getEquipment().<Slot>slots().iterator();
        while (equipmentIterator.hasNext()) {
            Slot slot = equipmentIterator.next();
            Optional<EquipmentSlotType> slotPos = slot.getInventoryProperty(EquipmentSlotType.class);
            Optional<ItemStack> itemStack = slot.peek();
            if (slotPos.isPresent() && itemStack.isPresent()) {
                EquipmentSlotType pos = slotPos.get();
                ItemStack stack = itemStack.get();
                equipment.add(new EquipmentSlotData(pos.getValue(), stack));
            }
        }
        inventory.getOffhand().peek().ifPresent(itemStack -> offHand = itemStack);
    }


    private void addSlot(Slot slot, List<SlotData> slots) {
        Optional<SlotPos> slotPos = slot.getInventoryProperty(SlotPos.class);
        Optional<ItemStack> itemStack = slot.peek();
        if (slotPos.isPresent() && itemStack.isPresent()) {
            SlotPos pos = slotPos.get();
            ItemStack stack = itemStack.get();
            slots.add(new SlotData(pos.getX(), pos.getY(), stack));
        }
    }

    public void fillInventory(Player player) {
        PlayerInventory inventory = player.getInventory()
                .query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));
        GridInventory mainGrid = inventory.getMainGrid();
        Iterator<SlotData> gridIterator = grid.iterator();
        while (gridIterator.hasNext()) {
            SlotData data = gridIterator.next();
            mainGrid.set(data.x, data.y, data.stack);
        }
        Hotbar hotbarInventory = inventory.getHotbar();
        Iterator<SlotData> hotbarIterator = hotbar.iterator();
        while (hotbarIterator.hasNext()) {
            SlotData data = hotbarIterator.next();
            hotbarInventory.set(new SlotIndex(data.x), data.stack);
        }
        EquipmentInventory equipmentInventory = inventory.getEquipment();
        Iterator<EquipmentSlotData> equipmentSlotDataIterator = equipment.iterator();
        while (equipmentSlotDataIterator.hasNext()) {
            EquipmentSlotData data = equipmentSlotDataIterator.next();
            equipmentInventory.set(data.equipmentType, data.stack);
        }
        player.setItemInHand(HandTypes.OFF_HAND, offHand);
    }





    @ConfigSerializable
    private static class SlotData {

        @Setting
        private int x = 0;
        @Setting
        private int y = 0;
        @Setting
        private ItemStack stack = ItemStack.empty();

        public SlotData() {

        }

        public SlotData(int x, int y, ItemStack stack) {
            this.x = x;
            this.y = y;
            this.stack = stack;
        }
    }

    @ConfigSerializable
    private static class EquipmentSlotData {

        @Setting
        private EquipmentType equipmentType = EquipmentTypes.ANY;

        @Setting
        private ItemStack stack = ItemStack.empty();


        public EquipmentSlotData() {
        }

        public EquipmentSlotData(EquipmentType equipmentType, ItemStack stack) {
            this.equipmentType = equipmentType;
            this.stack = stack;
        }
    }
}
