package me.logan.campwarpsv2.Utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemStackSerializer {
    private static final Logger logger = Logger.getLogger(ItemStackSerializer.class.getName());

    public static String serializeItemStack(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while serializing ItemStack", e);
            return null;
        }
    }

    public static ItemStack deserializeItemStack(String serializedItem) {
        try {
            byte[] data = Base64.getDecoder().decode(serializedItem);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
            return itemStack;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while deserializing ItemStack", e);
            return null;
        }
    }
}
