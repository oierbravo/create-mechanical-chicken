package com.oierbravo.create_mechanical_chicken;

import com.mojang.logging.LogUtils;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.create_mechanical_chicken.infrastructure.config.MConfigs;
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

        MConfigs.register(modLoadingContext, modContainer);


        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::registerCapabilities);

        modEventBus.addListener(ModDataGen::gatherData);

        ModFluids.register();
        generateLangEntries();
    }
    private void generateLangEntries(){
        new RegistrateLangBuilder<>(MODID, registrate())
            .addCreativeTab("Create Mechanical Chicken")
            .addJade("Mechanical chicken data")
            .add("chicken.tooltip.progress", "Progress: %d%%")
            .addBlockTooltipSummary("mechanical_chicken", "Chicken _Egg_ generator.")
            .addPonderHeader("mechanical_chicken","Generates chicken eggs")
            .addPonderText(1, "mechanical_chicken", "The Mechanical Chicken uses rotational force and an specific fluid to generate eggs")
            .addPonderText(2, "mechanical_chicken", "Its powered from the bottom")
            .addPonderText(3, "mechanical_chicken", "Fluid input can ONLY go from the FRONT side")
            .addPonderText(4, "mechanical_chicken", "Egg output is ONLY located on the BACK side")
            .addPonderText(5, "mechanical_chicken", "Can only be extracted by automation")
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
