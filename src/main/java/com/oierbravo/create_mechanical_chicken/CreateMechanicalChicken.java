package com.oierbravo.create_mechanical_chicken;

import com.mojang.logging.LogUtils;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.create_mechanical_chicken.infrastructure.data.ModDataGen;
import com.oierbravo.create_mechanical_chicken.ponders.ModPonderPlugin;
import com.oierbravo.create_mechanical_chicken.registrate.*;
import com.oierbravo.mechanical_lemon_lib.register.LemonCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateMechanicalChicken.MODID)
public class CreateMechanicalChicken
{
    // Directly reference a slf4j logger
    public static final String MODID = "create_mechanical_chicken";
    public static final String DISPLAY_NAME = "Create Mechanical Chicken";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab(LemonCreativeModeTabs.MAIN_TAB.getKey());
    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateMechanicalChicken(IEventBus modEventBus, ModContainer modContainer)
    {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        //ModCreativeTabs.register(modEventBus);

        ModRegistration.init();
        ModFluids.register();

        ModConfigs.register(modLoadingContext, modContainer);


        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::registerCapabilities);

        modEventBus.addListener(ModDataGen::gatherData);

        ModFluids.register();
        generateLangEntries();
    }
    private void generateLangEntries(){

        //registerLangCustom("itemGroup.create_mechanical_chicken:main", "Create Mechanical Chicken");
        registerLangCustom("config.jade.plugin_create_mechanical_chicken.data", "Mechanical chicken data");
        registerLang("chicken.tooltip.progress", "Progress: %d%%");

        registerLangCustom("block.create_mechanical_chicken.mechanical_chicken.tooltip", "MECHANICAL Chicken");
        registerLangCustom("block.create_mechanical_chicken.mechanical_chicken.tooltip.summary", "Chicken _Egg_ generator.");

        registerLang("ponder.mechanical_chicken.header", "Generates chicken eggs");
        registerLang("ponder.mechanical_chicken.text_1", "The Mechanical Chicken uses rotational force and an specific fluid to generate eggs");
        registerLang("ponder.mechanical_chicken.text_2", "Its powered from the bottom");
        registerLang("ponder.mechanical_chicken.text_3", "Fluid input can ONLY go from the FRONT side");
        registerLang("ponder.mechanical_chicken.text_4", "Egg output is ONLY located on the BACK side");
        registerLang("ponder.mechanical_chicken.text_5", "Can only be extracted by automation");

        registerLang("recipe", "Mechanical Chicken Recipe");


    }
    @net.neoforged.bus.api.SubscribeEvent
    public void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        MechanicalChickenBlockEntity.registerCapabilities(event);
    }
    private void doClientStuff(final FMLClientSetupEvent event) {

        ModPartials.init();
        PonderIndex.addPlugin(new ModPonderPlugin());

    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void registerLang(String id, String text){
        registerLangCustom(MODID.toLowerCase() + "." + id, text);
    }

    public static void registerLangCustom(String id, String text){
        registrate().addRawLang(id, text);
    }
    public static LangBuilder lang() {
        return new LangBuilder(MODID);
    }

}
