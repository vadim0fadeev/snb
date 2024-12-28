package mchorse.snb.core.gpu;

import org.lwjgl.opengl.GL43;
import net.minecraft.client.Minecraft;

public class GPUManager {
    private static GPUManager instance;
    private boolean isGPUSupported;
    
    private GPUManager() {
        checkGPUSupport();
    }
    
    public static GPUManager getInstance() {
        if (instance == null) {
            instance = new GPUManager();
        }
        return instance;
    }
    
    private void checkGPUSupport() {
        try {
            // Проверяем поддержку compute shaders
            isGPUSupported = GL43.glGetString(GL43.GL_VERSION).compareTo("4.3") >= 0;
        } catch (Exception e) {
            isGPUSupported = false;
        }
    }
    
    public boolean isGPUSupported() {
        return isGPUSupported;
    }
}
