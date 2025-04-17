package com.oierbravo.create_mechanical_chicken.registrate;

import com.oierbravo.create_mechanical_chicken.ModLang;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.mechanicals.utility.MechanicalLangIdGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.oierbravo.create_mechanical_chicken.ModConstants.MODID;

public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(ModLang.translate(MechanicalLangIdGenerator.creativeTabId("main")).component())
                    .icon(ModRegistration.MECHANICAL_CHICKEN_BLOCK::asStack)
                    .build());

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }
}
