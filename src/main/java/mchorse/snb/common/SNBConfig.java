public class SNBConfig
{
    // ... существующие поля ...
    
    @Config.Comment("Enable GPU acceleration for animations")
    public static boolean enableGPU = true;
    
    @Config.Comment("GPU compute shader group size")
    @Config.RangeInt(min = 64, max = 1024)
    public static int computeGroupSize = 256;
}
