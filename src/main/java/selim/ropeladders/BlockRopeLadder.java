package selim.ropeladders;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockRopeLadder extends BlockLadder {

	public BlockRopeLadder() {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.0f));
		this.setRegistryName(RopeLadders.MODID, "rope_ladder");
		// this.setUnlocalizedName(RopeLadders.MODID + ":rope_ladder");
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (group.equals(ItemGroup.DECORATIONS))
			items.add(new ItemStack(this));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn,
			BlockPos neighbor) {
		if (!world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(RopeLadders.Blocks.ROPE_LADDER))
			super.neighborChanged(state, world, pos, blockIn, neighbor);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		this.neighborChanged(state, world, pos, this, pos);
		if (!world.isRemote) {
			if (!player.isSneaking()
					&& player.inventory.hasItemStack(new ItemStack(RopeLadders.Items.ROPE_LADDER))) {
				if (player.isCreative()) {
					this.placeLadder(world, pos, ItemStack.EMPTY, player, side);
					return true;
				}
				ItemStack ladderStack = null;
				for (ItemStack invStack : player.inventory.mainInventory) {
					if (!invStack.isEmpty()
							&& invStack.getItem().equals(RopeLadders.Items.ROPE_LADDER)) {
						ladderStack = invStack;
						continue;
					}
				}
				if (ladderStack != null) {
					this.placeLadder(world, pos, ladderStack, player, side);
					return true;
				}
			} else if (player.isSneaking()) {
				this.retractLadder(world, pos);
				if (!player.isCreative())
					if (!player.inventory
							.addItemStackToInventory(new ItemStack(RopeLadders.Items.ROPE_LADDER)))
						world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1,
								pos.getZ() + 0.5, new ItemStack(RopeLadders.Items.ROPE_LADDER)));
				return true;
			}
		}
		return true;
	}

	public void placeLadder(World world, BlockPos pos, ItemStack ladderStack,
			@Nullable EntityPlayer player, EnumFacing facing) {
		int i = 1;
		while (true) {
			BlockPos newPos = pos.add(0, -i, 0);
			// EnumFacing facing = world.getBlockState(pos.)
			if (world.isAirBlock(newPos) || world.getBlockState(newPos).isReplaceable(
					new BlockItemUseContext(world, player, ladderStack, pos, facing, 0, 0, 0))) {
				boolean waterlogged = world.hasWater(newPos);
				if (!ladderStack.isEmpty()) {
					ladderStack.shrink(1);
					world.setBlockState(newPos,
							world.getBlockState(pos).with(BlockRopeLadder.WATERLOGGED, waterlogged));
				} else
					world.setBlockState(newPos,
							world.getBlockState(pos).with(BlockRopeLadder.WATERLOGGED, waterlogged));
				break;
			} else if (!world.getBlockState(newPos).getBlock().equals(RopeLadders.Blocks.ROPE_LADDER))
				break;
			i++;
		}
	}

	public void retractLadder(World world, BlockPos pos) {
		int i = 1;
		while (true) {
			BlockPos newPos = pos.add(0, -i, 0);
			if (!world.getBlockState(newPos).getBlock().equals(RopeLadders.Blocks.ROPE_LADDER)) {
				world.removeBlock(newPos.add(0, 1, 0));
				break;
			}
			i++;
		}
	}

	@Override
	public IBlockState getStateForPlacement(IBlockState state, EnumFacing facing, IBlockState state2,
			IWorld world, BlockPos pos1, BlockPos pos2, EnumHand hand) {
		if (facing.getAxis().isHorizontal()
				&& this.canAttachTo(world, pos1.offset(facing.getOpposite()), facing))
			return this.getDefaultState().with(FACING, facing);
		else {
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
				if (this.canAttachTo(world, pos1.offset(enumfacing.getOpposite()), enumfacing))
					return this.getDefaultState().with(FACING, enumfacing);

			return this.getDefaultState();
		}
	}

	private boolean canAttachTo(IBlockReader world, BlockPos pos, EnumFacing facing) {
		IBlockState iblockstate = world.getBlockState(pos);
		boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
		return !flag && iblockstate.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID
				&& !iblockstate.canProvidePower();
	}

}
