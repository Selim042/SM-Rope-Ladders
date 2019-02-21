package selim.ropeladders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
@Mod(modid = RopeLadders.MODID, name = RopeLadders.NAME, version = RopeLadders.VERSION)
public class RopeLadders {

	public static final String MODID = "ropeladders";
	public static final String NAME = "Rope Ladders";
	public static final String VERSION = "2.0.0";
	@Mod.Instance(value = MODID)
	public static RopeLadders instance;
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
		event.getRegistry().register(
				new ItemBlock(Blocks.ROPE_LADDER).setRegistryName(Blocks.ROPE_LADDER.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Items.ROPE_LADDER, 0,
				new ModelResourceLocation(Items.ROPE_LADDER.getRegistryName(), "inventory"));
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ROPE_LADDER,
				new RopeLadderBehavior());
	}

}