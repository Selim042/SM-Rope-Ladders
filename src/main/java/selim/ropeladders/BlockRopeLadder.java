package selim.ropeladders;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRopeLadder extends BlockLadder {

	public BlockRopeLadder() {
		this.setRegistryName(RopeLadders.MODID, "rope_ladder");
		this.setUnlocalizedName(RopeLadders.MODID + ":rope_ladder");
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setHardness(0.0F);
		this.blockResistance = 0;
		this.lightOpacity = 0;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn,
			BlockPos neighbor) {
		if (!world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(RopeLadders.Blocks.ROPE_LADDER))
			super.neighborChanged(state, world, pos, blockIn, neighbor);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float side, float hitX, float hitY) {
		this.onNeighborChange((IBlockAccess) world, pos, pos);
		if (!world.isRemote) {
			if (!player.isSneaking()
					&& player.inventory.hasItemStack(new ItemStack(RopeLadders.Items.ROPE_LADDER))) {
				if (player.capabilities.isCreativeMode) {
					this.placeLadder(world, pos, ItemStack.EMPTY);
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
					this.placeLadder(world, pos, ladderStack);
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

	public void placeLadder(World world, BlockPos pos, ItemStack ladderStack) {
		int i = 1;
		while (true) {
			BlockPos newPos = pos.add(0, -i, 0);
			if (world.isAirBlock(newPos)
					|| world.getBlockState(newPos).getBlock().isReplaceable(world, newPos)) {
				if (!ladderStack.isEmpty()) {
					ladderStack.shrink(1);
					world.setBlockState(newPos, world.getBlockState(pos));
				} else
					world.setBlockState(newPos, world.getBlockState(pos));
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
				world.setBlockToAir(newPos.add(0, 1, 0));
				break;
			}
			i++;
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if (facing.getAxis().isHorizontal()
				&& this.canAttachTo(world, pos.offset(facing.getOpposite()), facing))
			return this.getDefaultState().withProperty(FACING, facing);
		else {
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
				if (this.canAttachTo(world, pos.offset(enumfacing.getOpposite()), enumfacing))
					return this.getDefaultState().withProperty(FACING, enumfacing);

			return this.getDefaultState();
		}
	}

	private boolean canAttachTo(World world, BlockPos pos, EnumFacing facing) {
		IBlockState iblockstate = world.getBlockState(pos);
		boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
		return !flag && iblockstate.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID
				&& !iblockstate.canProvidePower();
	}

}
