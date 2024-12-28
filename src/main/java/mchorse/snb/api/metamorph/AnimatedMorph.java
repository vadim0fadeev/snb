package mchorse.snb.api.metamorph;

// ... существующие импорты ...
import mchorse.snb.api.gpu.GPUManager;
import mchorse.snb.api.gpu.shaders.ComputeShaderManager;

public class AnimatedMorph extends AbstractMorph implements IBodyPartProvider
{
    // ... существующие поля ...
    private ComputeShaderManager computeShader;
    private boolean useGPU;

    public AnimatedMorph()
    {
        super();
        this.initGPU();
    }

    private void initGPU()
    {
        useGPU = GPUManager.getInstance().isGPUSupported();
        if (useGPU) {
            computeShader = new ComputeShaderManager();
            try {
                computeShader.init();
            } catch (IOException e) {
                useGPU = false;
                e.printStackTrace();
            }
        }
    }

    // Модифицируем метод renderOnScreen для использования GPU
    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnScreen(EntityPlayer player, int x, int y, float scale, float alpha)
    {
        this.initiateAnimator();

        if (this.animator != null && this.animator.animation != null)
        {
            if (useGPU) {
                this.renderWithGPU(x, y, scale, alpha);
            } else {
                this.renderWithCPU(x, y, scale, alpha);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderWithGPU(int x, int y, float scale, float alpha)
    {
        try {
            // Подготовка данных для GPU
            AnimationMesh mesh = this.animator.animation.meshes.get(0);
            BOBJArmature armature = mesh.armature;
            
            // Обработка анимации на GPU
            computeShader.processAnimation(
                mesh.getCurrentVertices(),
                armature.getBoneTransforms(),
                mesh.getVertexWeights()
            );
            
            // Рендеринг результатов
            this.renderMeshes(x, y, scale, alpha);
        } catch (Exception e) {
            useGPU = false;
            renderWithCPU(x, y, scale, alpha);
            e.printStackTrace();
        }
    }

    // Оригинальный метод рендеринга переименован
    @SideOnly(Side.CLIENT)
    private void renderWithCPU(int x, int y, float scale, float alpha)
    {
        // ... существующий код рендеринга ...
    }

    @Override
    public void update()
    {
        super.update();
        
        if (useGPU && computeShader != null)
        {
            // Обновление GPU буферов если необходимо
            computeShader.update();
        }
    }
}
