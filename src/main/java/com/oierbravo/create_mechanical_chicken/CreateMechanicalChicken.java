package com.oierbravo.create_mechanical_chicken;

import com.mojang.logging.LogUtils;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.create_mechanical_chicken.infrastructure.data.ModDataGen;
import com.oierbravo.create_mechanical_chicken.ponders.ModPonderPlugin;
import com.oierbravo.create_mechanical_chicken.registrate.*;
import com.oierbravo.mechanicals.utility.RegistrateLangBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import static com.oierbravo.create_mechanical_chicken.ModConstants.MODID;

@Mod(MODID)
public class CreateMechanicalChicken
{

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab(ModCreativeTabs.MAIN_TAB.getKey());
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

        ModCreativeTabs.register(modEventBus);

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
        new RegistrateLangBuilder(MODID, registrate())
            .add("itemGroup.create_mechanical_chicken:main", "Create Mechanical Chicken")
            .addRaw("config.jade.plugin_create_mechanical_chicken.data", "Mechanical chicken data")
            .addRaw("block.create_mechanical_chicken.mechanical_chicken.tooltip", "MECHANICAL Chicken")
            .addRaw("block.create_mechanical_chicken.mechanical_chicken.tooltip.summary", "Chicken _Egg_ generator.")
            .add("chicken.tooltip.progress", "Progress: %d%%")
            .add("ponder.mechanical_chicken.header", "Generates chicken eggs")
            .add("ponder.mechanical_chicken.text_1", "The Mechanical Chicken uses rotational force and an specific fluid to generate eggs")
            .add("ponder.mechanical_chicken.text_2", "Its powered from the bottom")
            .add("ponder.mechanical_chicken.text_3", "Fluid input can ONLY go from the FRONT side")
            .add("ponder.mechanical_chicken.text_4", "Egg output is ONLY located on the BACK side")
            .add("ponder.mechanical_chicken.text_5", "Can only be extracted by automation")
            .add("recipe", "Mechanical Chicken Recipe");


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

    public static void registerLang(String id, String text){
        registerLangCustom(MODID.toLowerCase() + "." + id, text);
    }

    public static void registerLangCustom(String id, String text){
        registrate().addRawLang(id, text);
    }
}
