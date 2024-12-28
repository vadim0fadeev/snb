package mchorse.snb.core.gpu;

import net.minecraftforge.common.config.Config;

@Config(modid = "snb")
public class GPUConfig {
    @Config.Comment("Enable GPU acceleration for animations")
    public static boolean enableGPU = true;
    
    @Config.Comment("Compute shader group size")
    @Config.RangeInt(min = 64, max = 1024)
    public static int computeGroupSize = 256;
}
