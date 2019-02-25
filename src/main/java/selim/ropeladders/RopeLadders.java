package selim.ropeladders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(RopeLadders.MODID)
public class RopeLadders {

	public static final String MODID = "ropeladders";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@ObjectHolder(MODID)
	public static class Blocks {

		public static final BlockRopeLadder ROPE_LADDER = null;

	}

	@ObjectHolder(MODID)
	public static class Items {

		public static final ItemBlock ROPE_LADDER = null;

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockRopeLadder());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		Item ropeLadder = new ItemBlock(Blocks.ROPE_LADDER, new Item.Properties())
				.setRegistryName(Blocks.ROPE_LADDER.getRegistryName());
		event.getRegistry().register(ropeLadder);
		BlockDispenser.registerDispenseBehavior(ropeLadder, new RopeLadderBehavior());
	}

}