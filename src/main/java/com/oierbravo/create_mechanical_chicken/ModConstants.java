package com.oierbravo.create_mechanical_chicken;

import net.minecraft.resources.ResourceLocation;

public class ModConstants {
    public static final String MODID = "create_mechanical_chicken";
    public static final String DISPLAY_NAME = "Create Mechanical Chicken";

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    public static ResourceLocation asResource() {
        return ResourceLocation.fromNamespaceAndPath(MODID,"");
    }
}
