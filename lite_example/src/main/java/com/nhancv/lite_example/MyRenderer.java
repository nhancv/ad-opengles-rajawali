package com.nhancv.lite_example;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.renderer.Renderer;

public class MyRenderer extends Renderer {

    private Object3D maskObj = new Object3D();
    private double offset = 0.005f;

    public MyRenderer(Context context) {
        this(context, false);
    }

    public MyRenderer(Context context, boolean registerForResources) {
        super(context, registerForResources);

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        maskObj.setPosition(maskObj.getX() + offset, maskObj.getY() + offset, maskObj.getZ() + offset);
        maskObj.rotate(maskObj.getRotX() + offset, maskObj.getRotY() + offset, maskObj.getRotZ() + offset, 1f);
        if (Math.abs(maskObj.getX()) > 1) {
            offset = -offset;
            maskObj.setRotation(0, 0, 0);
            maskObj.setPosition(0, 0, 0);
        }
    }

    @Override
    protected void initScene() {
        DirectionalLight directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentScene().addLight(directionalLight);

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try {
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }

        maskObj.setScale(1.2f);
        maskObj.setPosition(0, 0, 0);

        Ornament mask = getVMask();
        LoaderOBJ objParser1 = new LoaderOBJ(mContext.getResources(), mTextureManager, mask.getModelResId());
        try {
            objParser1.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        maskObj = objParser1.getParsedObject();
        maskObj.setScale(mask.getScale());
        maskObj.setPosition(mask.getOffsetX(), mask.getOffsetY(), mask.getOffsetZ());
        maskObj.setRotation(mask.getRotateX(), mask.getRotateY(), mask.getRotateZ());

        maskObj.setMaterial(material);
        getCurrentScene().addChild(maskObj);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    private Ornament getVMask() {
        Ornament ornament = new Ornament();
        ornament.setModelResId(R.raw.v_mask_obj);
        ornament.setImgResId(R.drawable.ic_v_mask);
        ornament.setScale(0.12f);
        ornament.setOffset(0, -0.1f, 0.0f);
        ornament.setRotate(0, 0, 0);
        ornament.setColor(Color.BLACK);
        return ornament;
    }
}
