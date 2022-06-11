package fi.dy.masa.minihud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fi.dy.masa.minihud.util.DataStorage;

@Mixin(ChatScreen.class)
public abstract class MixinScreen
{

    @Inject(method = "sendMessage", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V"),
            cancellable = true)
    private void onSendMessage(String msg, boolean addToChat, CallbackInfo ci)
    {
        if (DataStorage.getInstance().onSendChatMessage(MinecraftClient.getInstance().player, msg))
        {
            ci.cancel();
        }
    }
}
