package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.List;

import static online.popopo.popopo.protection.License.*;

public class BlockListener implements Listener {
    private final Judge judge;

    public BlockListener(Judge j) {
        this.judge = j;
    }

    private boolean canChangeBlock(Block b, String o) {
        return judge.allows(b, CHANGE_BLOCK, o);
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_ENTITY));
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        Block b = e.getBlock();

        e.setCancelled(!canChangeBlock(b, OPTION_TIME));
    }

    private boolean canBlockFromTo(Block from, Block to) {
        return canChangeBlock(from, OPTION_TIME)
                && canChangeBlock(to, OPTION_TIME);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        Block from = e.getBlock();
        Block to = e.getToBlock();

        e.setCancelled(!canBlockFromTo(from, to));
    }

    private boolean canPistonMove(List<Block> b, BlockFace f) {
        for (Block b1 : b) {
            Block b2 = b1.getRelative(f);

            if (!canChangeBlock(b1, OPTION_BLOCK)) {
                return false;
            }

            if (!canChangeBlock(b2, OPTION_BLOCK)) {
                return false;
            }
        }

        return true;
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
}
