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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber
@Mod(RopeLadders.MODID)
public class RopeLadders {

	public static final String MODID = "ropeladders";
	public static final String NAME = "Rope Ladders";
	public static final String VERSION = "2.0.0";
	// @Mod.Instance(value = MODID)
	// public static RopeLadders instance;
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
		event.getRegistry().register(new ItemBlock(Blocks.ROPE_LADDER, new Item.Properties())
				.setRegistryName(Blocks.ROPE_LADDER.getRegistryName()));
	}

	// @OnlyIn(Dist.CLIENT)
	// @SubscribeEvent
	// public static void registerModels(ModelRegistryEvent event) {
	// ModelLoader.setCustomModelResourceLocation(Items.ROPE_LADDER, 0,
	// new ModelResourceLocation(Items.ROPE_LADDER.getRegistryName(),
	// "inventory"));
	// }

	private void initClient(final FMLClientSetupEvent event) {
		BlockDispenser.registerDispenseBehavior(Items.ROPE_LADDER, new RopeLadderBehavior());
	}

	private void initServer(final FMLDedicatedServerSetupEvent event) {
		BlockDispenser.registerDispenseBehavior(Items.ROPE_LADDER, new RopeLadderBehavior());
	}

}