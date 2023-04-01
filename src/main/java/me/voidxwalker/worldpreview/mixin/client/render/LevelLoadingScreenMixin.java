package me.voidxwalker.worldpreview.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Point;
import java.util.Iterator;
import me.voidxwalker.worldpreview.OldSodiumCompatibility;
import me.voidxwalker.worldpreview.WorldPreview;
import me.voidxwalker.worldpreview.mixin.access.WorldRendererMixin;
import net.minecraft.class_1041;
import net.minecraft.class_1159;
import net.minecraft.class_1160;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_2588;
import net.minecraft.class_304;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_3195;
import net.minecraft.class_339;
import net.minecraft.class_3928;
import net.minecraft.class_3953;
import net.minecraft.class_4184;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_5348;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_3928.class})
public abstract class LevelLoadingScreenMixin extends class_437 {
  private boolean worldpreview_showMenu;
  
  protected LevelLoadingScreenMixin(class_2561 title) {
    super(title);
  }
  
  @Inject(method = {"<init>"}, at = {@At("TAIL")})
  public void worldpreview_init(class_3953 progressProvider, CallbackInfo ci) {
    WorldPreview.calculatedSpawn = true;
    WorldPreview.freezePreview = false;
    class_304.method_1437();
  }
  
  @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"))
  public void worldpreview_stopBackgroundRender(class_3928 instance, class_4587 matrixStack) {
    if (WorldPreview.camera == null)
      instance.method_25420(matrixStack); 
  }
  
  @ModifyVariable(method = {"render"}, at = @At("STORE"), ordinal = 2)
  public int worldpreview_moveLoadingScreen(int i) {
    if (WorldPreview.camera == null)
      return i; 
    return (worldpreview_getChunkMapPos()).x;
  }
  
  @ModifyVariable(method = {"render"}, at = @At("STORE"), ordinal = 3)
  public int moveLoadingScreen2(int i) {
    if (WorldPreview.camera == null)
      return i; 
    return (worldpreview_getChunkMapPos()).y;
  }
  
  @Inject(method = {"render"}, at = {@At("HEAD")})
  public void worldpreview_render(class_4587 matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    if (WorldPreview.world != null && WorldPreview.clientWorld != null && WorldPreview.player != null && !WorldPreview.freezePreview) {
      if (((WorldRendererMixin)WorldPreview.worldRenderer).getWorld() == null && WorldPreview.calculatedSpawn) {
        ((OldSodiumCompatibility)WorldPreview.worldRenderer).worldpreview_setWorldSafe(WorldPreview.clientWorld);
        WorldPreview.showMenu = true;
        this.worldpreview_showMenu = true;
        worldpreview_initWidgets();
      } 
      if (((WorldRendererMixin)WorldPreview.worldRenderer).getWorld() != null) {
        class_2338 spawnPos = WorldPreview.player.method_24515();
        class_2338 structure = WorldPreview.world.method_8503().method_30002().method_8487(class_3195.field_24857, spawnPos, 5, false);
        if (structure == null) {
          WorldPreview.inPreview = true;
          WorldPreview.kill = -1;
          return;
        } 
        class_304.method_1437();
        WorldPreview.kill = 0;
        if (this.worldpreview_showMenu != WorldPreview.showMenu) {
          if (!WorldPreview.showMenu) {
            this.field_22786.clear();
          } else {
            worldpreview_initWidgets();
          } 
          this.worldpreview_showMenu = WorldPreview.showMenu;
        } 
        (class_310.method_1551()).field_1773.method_22974().method_3313(0.0F);
        if (WorldPreview.camera == null) {
          WorldPreview.player.method_5808(WorldPreview.player.method_23317(), WorldPreview.player.method_23318() + (WorldPreview.player.method_5829()).field_1325 - (WorldPreview.player.method_5829()).field_1322, WorldPreview.player.method_23321(), 0.0F, 0.0F);
          WorldPreview.camera = new class_4184();
          WorldPreview.camera.method_19321((class_1922)WorldPreview.world, (class_1297)WorldPreview.player, (this.field_22787.field_1690.field_1850 > 0), (this.field_22787.field_1690.field_1850 == 2), 0.2F);
          WorldPreview.player.method_5808(WorldPreview.player.method_23317(), WorldPreview.player.method_23318() - 1.5D, WorldPreview.player.method_23321(), 0.0F, 0.0F);
          WorldPreview.inPreview = true;
          WorldPreview.log(Level.INFO, "Starting Preview at (" + WorldPreview.player.method_23317() + ", " + Math.floor(WorldPreview.player.method_23318()) + ", " + WorldPreview.player.method_23321() + ")");
        } 
        class_4587 matrixStack = new class_4587();
        matrixStack.method_23760().method_23761().method_22672(worldpreview_getBasicProjectionMatrix());
        class_1159 matrix4f = matrixStack.method_23760().method_23761();
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(matrix4f);
        RenderSystem.matrixMode(5888);
        class_4587 m = new class_4587();
        m.method_22907(class_1160.field_20703.method_23214(WorldPreview.camera.method_19329()));
        m.method_22907(class_1160.field_20705.method_23214(WorldPreview.camera.method_19330() + 180.0F));
        WorldPreview.worldRenderer.method_22710(m, 0.2F, 1000000L, false, WorldPreview.camera, (class_310.method_1551()).field_1773, (class_310.method_1551()).field_1773.method_22974(), matrix4f);
        class_1041 window = this.field_22787.method_22683();
        RenderSystem.clear(256, class_310.field_1703);
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, window.method_4489() / window.method_4495(), window.method_4506() / window.method_4495(), 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode(5888);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
        class_308.method_24211();
        worldpreview_renderPauseMenu(matrices, mouseX, mouseY, delta);
      } 
    } 
  }
  
  private void worldpreview_renderPauseMenu(class_4587 matrices, int mouseX, int mouseY, float delta) {
    if (WorldPreview.showMenu) {
      Iterator<class_339> iterator = this.field_22791.listIterator();
      while (iterator.hasNext())
        ((class_339)iterator.next()).method_25394(matrices, mouseX, mouseY, delta); 
    } else {
      method_27534(matrices, this.field_22793, (class_5348)new class_2588("menu.paused"), this.field_22789 / 2, 10, 16777215);
    } 
  }
  
  private Point worldpreview_getChunkMapPos() {
    switch (WorldPreview.chunkMapPos) {
      case 1:
        return new Point(this.field_22789 - 45, this.field_22790 - 75);
      case 2:
        return new Point(this.field_22789 - 45, 105);
      case 3:
        return new Point(45, 105);
    } 
    return new Point(45, this.field_22790 - 75);
  }
  
  public class_1159 worldpreview_getBasicProjectionMatrix() {
    class_4587 matrixStack = new class_4587();
    matrixStack.method_23760().method_23761().method_22668();
    matrixStack.method_23760().method_23761().method_22672(class_1159.method_4929(this.field_22787.field_1690.field_1826, this.field_22787.method_22683().method_4489() / this.field_22787.method_22683().method_4506(), 0.05F, (this.field_22787.field_1690.field_1870 * 16) * 4.0F));
    return matrixStack.method_23760().method_23761();
  }
  
  private void worldpreview_initWidgets() {
    method_25411((class_339)new class_4185(this.field_22789 / 2 - 102, this.field_22790 / 4 + 24 - 16, 204, 20, (class_2561)new class_2588("menu.returnToGame"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 - 102, this.field_22790 / 4 + 48 - 16, 98, 20, (class_2561)new class_2588("gui.advancements"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 + 4, this.field_22790 / 4 + 48 - 16, 98, 20, (class_2561)new class_2588("gui.stats"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 - 102, this.field_22790 / 4 + 72 - 16, 98, 20, (class_2561)new class_2588("menu.sendFeedback"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 + 4, this.field_22790 / 4 + 72 - 16, 98, 20, (class_2561)new class_2588("menu.reportBugs"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 - 102, this.field_22790 / 4 + 96 - 16, 98, 20, (class_2561)new class_2588("menu.options"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 + 4, this.field_22790 / 4 + 96 - 16, 98, 20, (class_2561)new class_2588("menu.shareToLan"), ignored -> {
          
          }));
    method_25411((class_339)new class_4185(this.field_22789 / 2 - 102, this.field_22790 / 4 + 120 - 16, 204, 20, (class_2561)new class_2588("menu.returnToMenu"), buttonWidgetX -> {
            this.field_22787.method_1483().method_4881();
            WorldPreview.kill = -1;
            buttonWidgetX.field_22763 = false;
          }));
  }
  
  public void method_25410(class_310 client, int width, int height) {
    method_25423(client, width, height);
    worldpreview_initWidgets();
  }
}

