package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static online.popopo.popopo.protection.License.*;

public class BlockListener implements Listener {
    private final Judge judge;

    public BlockListener(Plugin p, Judge j) {
        this.judge = j;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private boolean canPlayerChangeBlock(Player p, Block b) {
        return judge.can(p, b, PLAYER_CHANGE_BLOCK);
    }

    private boolean canEntityChangeBlock(Block b) {
        return judge.can(b, ENTITY_CHANGE_BLOCK);
    }

    private boolean canBlockChangeBlock(Block b) {
        return judge.can(b, BLOCK_CHANGE_BLOCK);
    }

    private boolean canTimeChangeBlock(Block b) {
        return judge.can(b, TIME_CHANGE_BLOCK);
    }

    private boolean canPistonMove(List<Block> b, BlockFace f) {
        for (Block b1 : b) {
            Block b2 = b1.getRelative(f);

            if (!canBlockChangeBlock(b1)) return false;
            if (!canBlockChangeBlock(b2)) return false;
        }

        return true;
    }

    private boolean canBlockFromTo(Block from, Block to) {
        return canTimeChangeBlock(from)
                && canTimeChangeBlock(to);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();

        e.setCancelled(!canPlayerChangeBlock(p, b));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();

        e.setCancelled(!canPlayerChangeBlock(p, b));
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Block b = e.getBlockClicked();
        Player p = e.getPlayer();

        e.setCancelled(!canPlayerChangeBlock(p, b));
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Block b = e.getBlockClicked();
        Player p = e.getPlayer();

        e.setCancelled(!canPlayerChangeBlock(p, b));
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canEntityChangeBlock(b));
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canTimeChangeBlock(b));
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canTimeChangeBlock(b));
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canTimeChangeBlock(b));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canTimeChangeBlock(b));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        BlockFace face = e.getDirection();
        List<Block> list = e.getBlocks();

        e.setCancelled(!canPistonMove(list, face));
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        BlockFace face = e.getDirection();
        List<Block> list = e.getBlocks();

        e.setCancelled(!canPistonMove(list, face));
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        Block from = e.getBlock();
        Block to = e.getToBlock();

        e.setCancelled(!canBlockFromTo(from, to));
    }
}
