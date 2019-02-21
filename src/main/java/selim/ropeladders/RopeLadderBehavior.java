package selim.ropeladders;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RopeLadderBehavior implements IBehaviorDispenseItem {

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		IBlockState state = source.getBlockState();
		if (!(state.getBlock() instanceof BlockDispenser) || stack == null || stack.isEmpty()
				|| !(stack.getItem().equals(RopeLadders.Items.ROPE_LADDER)))
			return stack;
		World world = source.getWorld();
		BlockPos dispPos = source.getBlockPos();
		EnumFacing facing = state.get(BlockDispenser.FACING);
		BlockPos ladderPos = dispPos.offset(facing);
		IBlockState ladderState = world.getBlockState(ladderPos);
		if (!(ladderState.getBlock() instanceof BlockRopeLadder))
			return stack;
		if (world.getBlockState(ladderPos.offset(EnumFacing.UP)).getBlock()
				.equals(RopeLadders.Blocks.ROPE_LADDER)) {
			stack.grow(1);
			RopeLadders.Blocks.ROPE_LADDER.retractLadder(source.getWorld(), ladderPos);
		} else
			RopeLadders.Blocks.ROPE_LADDER.placeLadder(source.getWorld(), ladderPos, stack, null);
		return stack;
	}

}
