package binnie.extrabees;

import binnie.core.api.genetics.IBreedingSystem;
import binnie.core.gui.BinnieGUIHandler;
import binnie.core.liquid.ManagerLiquid;
import binnie.extrabees.genetics.gui.analyst.AnalystPagePlugin;
import binnie.extrabees.gui.ExtraBeesGUID;
import binnie.genetics.api.GeneticsApi;
import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import forestry.api.recipes.RecipeManagers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleSpeciesRegisterEvent;
import forestry.core.gui.GuiIdRegistry;
import forestry.core.gui.GuiType;
import forestry.core.proxy.Proxies;

import binnie.core.Binnie;
import binnie.core.Constants;
import binnie.extrabees.alveary.TileEntityExtraBeesAlvearyPart;
import binnie.extrabees.genetics.BeeBreedingSystem;
import binnie.extrabees.genetics.ExtraBeeDefinition;
import binnie.extrabees.genetics.ExtraBeesFlowers;
import binnie.extrabees.genetics.effect.ExtraBeesEffect;
import binnie.extrabees.init.BlockRegister;
import binnie.extrabees.init.ItemRegister;
import binnie.extrabees.init.RecipeRegister;
import binnie.extrabees.items.ItemHoneyCrystal;
import binnie.extrabees.items.ItemMiscProduct;
import binnie.extrabees.items.types.EnumHoneyComb;
import binnie.extrabees.proxy.ExtraBeesCommonProxy;
import binnie.extrabees.utils.AlvearyMutationHandler;
import binnie.extrabees.utils.MaterialBeehive;
import binnie.extrabees.utils.config.ConfigHandler;
import binnie.extrabees.utils.config.ConfigurationMain;
import binnie.extrabees.worldgen.ExtraBeesWorldGenerator;

@Mod(
	modid = ExtraBees.MODID,
	name = "Binnie's Extra Bees",
	dependencies = "required-after:" + Constants.CORE_MOD_ID
)
public class ExtraBees {

	public static final String MODID = "extrabees";

	@Mod.Instance(MODID)
	public static ExtraBees instance;

	@SidedProxy(clientSide = "binnie.extrabees.proxy.ExtraBeesClientProxy", serverSide = "binnie.extrabees.proxy.ExtraBeesCommonProxy")
	public static ExtraBeesCommonProxy proxy;

	public static IBreedingSystem beeBreedingSystem;

	public ExtraBees(){
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static ConfigHandler configHandler;

	public static Material materialBeehive;
	public static Block hive;
	public static Block alveary;
	public static Block ectoplasm;
	public static Item comb;
	public static Item propolis;
	public static Item honeyDrop;
	public static ItemHoneyCrystal honeyCrystal;
	public static Item dictionary;
	public static ItemMiscProduct itemMisc;
	public static Item dictionaryBees;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		materialBeehive = new MaterialBeehive();
		File configFile = new File(event.getModConfigurationDirectory(), "forestry/extrabees/main.conf");
		configHandler = new ConfigHandler(configFile);
		configHandler.addConfigurable(new ConfigurationMain());
		BlockRegister.preInitBlocks();
		ItemRegister.preInitItems();
		try {
			Method m = GuiIdRegistry.class.getDeclaredMethod("registerGuiHandlers", GuiType.class, List.class);
			m.setAccessible(true);
			m.invoke(null, new Object[]{GuiType.Tile, Lists.newArrayList(TileEntityExtraBeesAlvearyPart.class)});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Proxies.render.registerModels();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new BinnieGUIHandler(ExtraBeesGUID.values()));

		beeBreedingSystem = new BeeBreedingSystem();
		Binnie.GENETICS.registerBreedingSystem(beeBreedingSystem);

		GeneticsApi.registerAnalystPagePlugin(new AnalystPagePlugin());
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent evt) {
		configHandler.reload(true);
		EnumHoneyComb.addSubtypes();
		ExtraBeesWorldGenerator extraBeesWorldGenerator = new ExtraBeesWorldGenerator();
		extraBeesWorldGenerator.doInit();
		GameRegistry.registerWorldGenerator(extraBeesWorldGenerator, 0);
		ExtraBeesEffect.doInit();
		ExtraBeesFlowers.doInit();
		ExtraBeeDefinition.doInit();
	}

	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent evt) {
		BlockRegister.postInitBlocks();
		RecipeRegister.postInitRecipes();
		//Register mutations
		AlvearyMutationHandler.registerMutationItems();
		RecipeManagers.carpenterManager.addRecipe(
				100,
				Binnie.LIQUID.getFluidStack(ManagerLiquid.WATER, 2000),
				ItemStack.EMPTY,
				new ItemStack(dictionaryBees),
				"X#X", "YEY", "RDR",
				'#', Blocks.GLASS_PANE,
				'X', Items.GOLD_INGOT,
				'Y', "ingotTin",
				'R', Items.REDSTONE,
				'D', Items.DIAMOND,
				'E', Items.EMERALD
		);
	}
	
	@SubscribeEvent
	public void onRegisterSpecies(AlleleSpeciesRegisterEvent<IAlleleBeeSpecies> event){
		if(event.getRoot() != BeeManager.beeRoot){
			return;
		}
		ExtraBeeDefinition.doPreInit();
	}

	@SubscribeEvent
	public void onMissingItem(RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
			if (entry.key.toString().equals("genetics:dictionary")) {
				ResourceLocation newTotem = new ResourceLocation("extrabees:dictionary");
				Item value = ForgeRegistries.ITEMS.getValue(newTotem);
				if (value != null) {
					entry.remap(value);
				}
			}
		}
	}
}
