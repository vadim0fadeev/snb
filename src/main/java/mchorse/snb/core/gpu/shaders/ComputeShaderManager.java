package mchorse.snb.core.gpu.shaders;

import org.lwjgl.opengl.GL43;
import java.nio.ByteBuffer;
import java.io.IOException;

public class ComputeShaderManager {
    private int computeProgram;
    private boolean initialized;
    
    public void init() throws IOException {
        if (!GPUManager.getInstance().isGPUSupported()) {
            return;
        }
        
        // Загружаем и компилируем compute shader
        computeProgram = GL43.glCreateProgram();
        int shader = loadComputeShader("assets/snb/shaders/animation.comp");
        GL43.glAttachShader(computeProgram, shader);
        GL43.glLinkProgram(computeProgram);
        
        initialized = GL43.glGetProgrami(computeProgram, GL43.GL_LINK_STATUS) == GL43.GL_TRUE;
    }
    
    public void processAnimation(ByteBuffer vertices, ByteBuffer bones) {
        if (!initialized) return;
        
        GL43.glUseProgram(computeProgram);
        // Установка данных и dispatch
        dispatchCompute(vertices.capacity() / 16); // 16 байт на вершину
    }
    
    private void dispatchCompute(int vertexCount) {
        int groupSize = 256;
        int numGroups = (vertexCount + groupSize - 1) / groupSize;
        GL43.glDispatchCompute(numGroups, 1, 1);
        GL43.glMemoryBarrier(GL43.GL_SHADER_STORAGE_BARRIER_BIT);
    }
}
