package me.modmuss50.fr;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Matrix4f;

/**
 * Created by Mark on 13/02/2016.
 */
public class ClientStatics {

    public static final Matrix4f matrix = ForgeHooksClient.getMatrix(new ItemTransformVec3f(new Vector3f(0F, 0F, 0.0F), new Vector3f(0F, 0F, 0F), new Vector3f(2.3F, 2.3F, 2.3F)));

}
