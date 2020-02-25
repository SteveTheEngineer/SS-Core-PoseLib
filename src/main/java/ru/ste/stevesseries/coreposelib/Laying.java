package ru.ste.stevesseries.coreposelib;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import ru.ste.stevesseries.corebase.*;

import java.lang.reflect.*;
import java.util.*;

public class Laying implements Pose {

    public Map< UUID, PlayerData > POSED;

    public Laying() {
        POSED = new HashMap<>();
    }
    @Override
    public void transform(Player p) {
        if(POSED.containsKey(p.getUniqueId())) {
            return;
        }
        Location l = p.getLocation().getBlock().getRelative(getFace(p.getLocation().getYaw()).getOppositeFace()).getLocation().add(0.5, 0, 0.5);
        l.setYaw(p.getLocation().getYaw());
        l.setPitch(p.getLocation().getPitch());
        try {
            Object entityPlayer = NMSUtil.getHandle(p);
            Object entityPlayerA = NMSUtil.getNMSClass("EntityPlayer").getConstructor(NMSUtil.getNMSClass("MinecraftServer"), NMSUtil.getNMSClass("WorldServer"), Class.forName("com.mojang.authlib.GameProfile"), NMSUtil.getNMSClass("PlayerInteractManager")).newInstance(entityPlayer.getClass().getMethod("getMinecraftServer").invoke(entityPlayer), entityPlayer.getClass().getMethod("getWorldServer").invoke(entityPlayer), entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer), entityPlayer.getClass().getField("playerInteractManager").get(entityPlayer));
            entityPlayerA.getClass().getMethod("setPosition", double.class, double.class, double.class).invoke(entityPlayerA, l.getX(), l.getY(), l.getZ());
            POSED.put(p.getUniqueId(), new PlayerData(entityPlayerA, getFace(p.getLocation().getYaw()).getOppositeFace(), l, p.getAllowFlight(), Objects.requireNonNull(p.getEquipment()).getHelmet() != null ? Objects.requireNonNull(p.getEquipment()).getHelmet().clone() : null, Objects.requireNonNull(p.getEquipment()).getChestplate() != null ? Objects.requireNonNull(p.getEquipment()).getChestplate().clone() : null, Objects.requireNonNull(p.getEquipment()).getLeggings() != null ? Objects.requireNonNull(p.getEquipment()).getLeggings().clone() : null, Objects.requireNonNull(p.getEquipment()).getBoots() != null ? Objects.requireNonNull(p.getEquipment()).getBoots().clone() : null));
            if(p.getEquipment().getItemInMainHand().getType() != Material.AIR) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), p.getEquipment().getItemInMainHand().clone());
                p.getEquipment().setItemInMainHand(null);
            }
            if(p.getEquipment().getItemInOffHand().getType() != Material.AIR) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), p.getEquipment().getItemInOffHand().clone());
                p.getEquipment().setItemInOffHand(null);
            }
            if(p.getEquipment().getHelmet() != null) {
                p.getEquipment().setHelmet(null);
            }
            if(p.getEquipment().getChestplate() != null) {
                p.getEquipment().setChestplate(null);
            }
            if(p.getEquipment().getLeggings() != null) {
                p.getEquipment().setLeggings(null);
            }
            if(p.getEquipment().getBoots() != null) {
                p.getEquipment().setBoots(null);
            }
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0F);
            p.teleport(l.clone().subtract(0, 1.3, 0));
      /*Bukkit.getOnlinePlayers().forEach(op -> {
          if(op.getUniqueId() != p.getUniqueId()) op.hidePlayer(p);
      });*/
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static BlockFace getFace(float yawv) {
        float yaw = yawv;
        if(yaw < 0) {
            yaw += 360;
        }
        if(yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        }
        else if(yaw < 135) {
            return BlockFace.WEST;
        }
        else if(yaw < 225) {
            return BlockFace.NORTH;
        }
        else if(yaw < 315) {
            return BlockFace.EAST;
        }
        return BlockFace.NORTH;
    }
    @Override
    public void normalize(Player p) {
        if(!POSED.containsKey(p.getUniqueId())) {
            return;
        }
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p.setAllowFlight(POSED.get(p.getUniqueId()).wf);
        p.setFlying(false);
        p.setFlySpeed(0.1F);
        p.teleport(POSED.get(p.getUniqueId()).pos);
        if(POSED.get(p.getUniqueId()).helmet != null) {
            Objects.requireNonNull(p.getEquipment()).setHelmet(POSED.get(p.getUniqueId()).helmet.clone());
        }
        if(POSED.get(p.getUniqueId()).chestplate != null) {
            Objects.requireNonNull(p.getEquipment()).setChestplate(POSED.get(p.getUniqueId()).chestplate.clone());
        }
        if(POSED.get(p.getUniqueId()).leggings != null) {
            Objects.requireNonNull(p.getEquipment()).setLeggings(POSED.get(p.getUniqueId()).leggings.clone());
        }
        if(POSED.get(p.getUniqueId()).boots != null) {
            Objects.requireNonNull(p.getEquipment()).setBoots(POSED.get(p.getUniqueId()).boots.clone());
        }
        POSED.remove(p.getUniqueId());
    /*Bukkit.getOnlinePlayers().forEach(op -> {
        op.showPlayer(p);
    });*/
    }

    @Override
    public void showFor(Player op, Player p) {
    /*EntityPlayer entityPlayer = POSED.get(p.getUniqueId()).ep;
    BlockFace blockFace = POSED.get(p.getUniqueId()).bf;
    Location l = POSED.get(p.getUniqueId()).pos;
    CraftPlayer cp = (CraftPlayer) p.getPlayer();
    EntityPlayer h = cp.getHandle();
    entityPlayer.setPosition(l.getX(), l.getY(), l.getZ());
    BlockPosition bedBlockPosition = new BlockPosition(l.getBlockX(), 1, l.getBlockZ());

    Bed b = (Bed) Material.RED_BED.createBlockData();
    b.setPart(Bed.Part.FOOT);
    b.setFacing(blockFace);
    op.sendBlockChange(new Location(l.getWorld(), l.getBlockX(), 1, l.getBlockZ()), b);

    PlayerConnection playerConnection = ((CraftPlayer) op).getHandle().playerConnection;

    PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

    //playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
    playerConnection.sendPacket(packetPlayOutNamedEntitySpawn);

    entityPlayer.e(bedBlockPosition);

    playerConnection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), false));
    playerConnection.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(entityPlayer.getId(), (short) 0, (short) (-0.35), (short) 0, false));

    //if (p.getEquipment().getHelmet() != null) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(p.getEquipment().getHelmet())));
    //if (p.getEquipment().getChestplate() != null) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(p.getEquipment().getChestplate())));
    //if (p.getEquipment().getLeggings() != null) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(p.getEquipment().getLeggings())));
    //if (p.getEquipment().getBoots() != null) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(p.getEquipment().getBoots())));
    //if (p.getEquipment().getItemInMainHand().getType() != Material.AIR) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(p.getEquipment().getItemInMainHand())));
    //if (p.getEquipment().getItemInOffHand().getType() != Material.AIR) playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(p.getEquipment().getItemInOffHand())));*/
        Object entityPlayer = POSED.get(p.getUniqueId()).ep;
        BlockFace blockFace = POSED.get(p.getUniqueId()).bf;
        Location l = POSED.get(p.getUniqueId()).pos;
        try {
            entityPlayer.getClass().getMethod("setPosition", double.class, double.class, double.class).invoke(entityPlayer, l.getX(), l.getY(), l.getZ());
        }
        catch(IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
        Object bedBlockPosition;
        try {
            bedBlockPosition = NMSUtil.getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(l.getBlockX(), 1, l.getBlockZ());
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }

        Bed b = ( Bed ) Material.RED_BED.createBlockData();
        b.setPart(Bed.Part.FOOT);
        b.setFacing(blockFace);
        op.sendBlockChange(new Location(l.getWorld(), l.getBlockX(), 1, l.getBlockZ()), b);

        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutNamedEntitySpawn").getConstructor(NMSUtil.getNMSClass("EntityHuman")).newInstance(entityPlayer));
        }
        catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        if(POSED.get(p.getUniqueId()).helmet != null) {
            try {
                // ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new
                // PacketPlayOutEntityEquipment((int)
                // entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), EnumItemSlot.HEAD,
                // CraftItemStack.asNMSCopy(p.getEquipment().getHelmet())));
                NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityEquipment").getConstructor(int.class, NMSUtil.getNMSClass("EnumItemSlot"), NMSUtil.getNMSClass("ItemStack")).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), NMSUtil.getNMSClass("EnumItemSlot").getField("HEAD").get(null), NMSUtil.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, POSED.get(p.getUniqueId()).helmet)));
            }
            catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if(POSED.get(p.getUniqueId()).chestplate != null) {
            try {
                // ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new
                // PacketPlayOutEntityEquipment((int)
                // entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), EnumItemSlot.HEAD,
                // CraftItemStack.asNMSCopy(p.getEquipment().getHelmet())));
                NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityEquipment").getConstructor(int.class, NMSUtil.getNMSClass("EnumItemSlot"), NMSUtil.getNMSClass("ItemStack")).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), NMSUtil.getNMSClass("EnumItemSlot").getField("CHEST").get(null), NMSUtil.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, POSED.get(p.getUniqueId()).chestplate)));
            }
            catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if(POSED.get(p.getUniqueId()).leggings != null) {
            try {
                // ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new
                // PacketPlayOutEntityEquipment((int)
                // entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), EnumItemSlot.HEAD,
                // CraftItemStack.asNMSCopy(p.getEquipment().getHelmet())));
                NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityEquipment").getConstructor(int.class, NMSUtil.getNMSClass("EnumItemSlot"), NMSUtil.getNMSClass("ItemStack")).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), NMSUtil.getNMSClass("EnumItemSlot").getField("LEGS").get(null), NMSUtil.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, POSED.get(p.getUniqueId()).leggings)));
            }
            catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if(POSED.get(p.getUniqueId()).boots != null) {
            try {
                // ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new
                // PacketPlayOutEntityEquipment((int)
                // entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), EnumItemSlot.HEAD,
                // CraftItemStack.asNMSCopy(p.getEquipment().getHelmet())));
                NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityEquipment").getConstructor(int.class, NMSUtil.getNMSClass("EnumItemSlot"), NMSUtil.getNMSClass("ItemStack")).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), NMSUtil.getNMSClass("EnumItemSlot").getField("FEET").get(null), NMSUtil.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, POSED.get(p.getUniqueId()).boots)));
            }
            catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        try {
            entityPlayer.getClass().getMethod("e", NMSUtil.getNMSClass("BlockPosition")).invoke(entityPlayer, bedBlockPosition);
        }
        catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityMetadata").getConstructor(int.class, NMSUtil.getNMSClass("DataWatcher"), boolean.class).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), entityPlayer.getClass().getMethod("getDataWatcher").invoke(entityPlayer), false));
        }
        catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        setRotation(p, op, p.getLocation().getYaw());
        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove").getConstructor(int.class, short.class, short.class, short.class, boolean.class).newInstance(entityPlayer.getClass().getMethod("getId").invoke(entityPlayer), ( short ) 0, ( short ) (-0.35), ( short ) 0, false));
        }
        catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void hideFor(Player op, Player p) {
        Object entityPlayer = POSED.get(p.getUniqueId()).ep;
        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(( Object ) new int[] {( int ) entityPlayer.getClass().getMethod("getId").invoke(entityPlayer)}));
        }
        catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        // playerConnection.sendPacket(new
        // PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep));
    }
    public void setRotation(Player p, Player op, float yawc) {
        byte yaw = Byte.valueOf(( byte ) (( int ) (yawc * 256.0F / 360.0F)));
        switch(POSED.get(p.getUniqueId()).bf.getOppositeFace()) {
            case NORTH:
                yaw -= Byte.MAX_VALUE;
                break;
            case EAST:
                yaw += Math.floor(Byte.MAX_VALUE / 2);
                break;
            case WEST:
                yaw -= Math.floor(Byte.MAX_VALUE / 2);
                break;
        }
        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutEntityHeadRotation").getConstructor(NMSUtil.getNMSClass("Entity"), byte.class).newInstance(POSED.get(p.getUniqueId()).ep, yaw));
        }
        catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
    }
    public boolean isThereALayingPlayer(Location l) {
        if(POSED.size() <= 0) {
            return false;
        }
        for(PlayerData d : POSED.values()) {
            if(l.distanceSquared(d.pos) <= 0.5) {
                return true;
            }
            if(l.getBlock().getRelative(d.bf.getOppositeFace()).getLocation().distanceSquared(l) <= 0.5) {
                return true;
            }
        }
        return false;
    }
    public void animationDamage(Player p, Player op) {
        try {
            NMSUtil.sendPacket(op, NMSUtil.getNMSClass("PacketPlayOutAnimation").getConstructor(NMSUtil.getNMSClass("Entity"), int.class).newInstance(POSED.get(p.getUniqueId()).ep, 1));
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static class PlayerData {
        public Object ep;
        public BlockFace bf;
        public Location pos;
        public boolean wf;
        public ItemStack helmet, chestplate, leggings, boots;

        public PlayerData(Object ep, BlockFace bf, Location pos, boolean wf, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
            this.ep = ep;
            this.bf = bf;
            this.pos = pos;
            this.wf = wf;
            this.helmet = helmet;
            this.chestplate = chestplate;
            this.leggings = leggings;
            this.boots = boots;
        }
    }
}
