package com.vanilla.death.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.vanilla.death.model.GameObject;
import com.vanilla.death.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;

//This class should be nominated for the award "The most commented code 2017"
//I'm sorry, i have no internet connection when wrote it

public class GraphicRepresentation {
    //All game objects have object of this class

    //I can use enum type, but it consume too much memory
    // 0 means that GameObject not draw
    // 1 - sprite draw, not animation
    // 2 - animation draw with keyframes
    private int state = 0;

    //This string variable contains current name of state:
    private String currentNameState;
    //Current sprite and animation:
    private Image currentImage;
    private Animation<TextureAtlas.AtlasRegion> currentAnimation;

    //Storage for animations and images
    private ArrayMap<String, Animation<TextureAtlas.AtlasRegion>> animations;
    private ArrayMap<String, Image> images;
    private Affine2 affine2; //use for transform TextureRegion (Animations)
    //Better store this variables than calculate it every render from TextureRegion
    private float textureWidth;
    private float textureHeight;
    private float halfTextureWidth;
    private float halfTextureHeight;
    //End of bad variables
    @SuppressWarnings("all") //delete warning that constant may be local variable
    public static final float SCALE_FACTOR = GameObject.scale * 0.7f;
    //and... much more private variables...

    //What may be better than simple empty (almost) constructor
    public GraphicRepresentation(@NotNull String path, @NotNull String[] spriteNames,
                                 @NotNull float[] animationFrames, @NotNull String[] animationNames) {
        loadResources(path, spriteNames, animationFrames, animationNames);
    }

    //This method should be performed when GameObject is creating...
    private void loadResources(String path, String[] spriteNames, float[] animationFrames, String[] animationNames) {
        //Checking that all is ok
        if (animationFrames.length != animationNames.length)
            throw new RuntimeException("Amount of animation frames not equals amount of animations");
        //Initialize:
        affine2 = new Affine2(); //transform animation
        affine2.scale(SCALE_FACTOR, SCALE_FACTOR);
        images = new ArrayMap<String, Image>(spriteNames.length); //all static images, i don't like draw sprites
        animations = new ArrayMap<String, Animation<TextureAtlas.AtlasRegion>>(animationNames.length);
        TextureAtlas objectAtlas;
        try {
            objectAtlas = ResourceManager.getInstance().getTextureAtlasByPath(path);
        } catch (Exception e) {
            if (path.isEmpty()) {
                Gdx.app.log("GraphicRepresentation load()", "Texture Atlas is not set");
            } else {
                Gdx.app.log("GraphicRepresentation load()", "Couldn't load textures on " + path);
            }
            textureWidth = ResourceManager.getInstance().getErrorSprite().getWidth();
            textureHeight = ResourceManager.getInstance().getErrorSprite().getHeight();
            halfTextureWidth = textureWidth / 2 * SCALE_FACTOR; //scale manually
            halfTextureHeight = textureHeight / 2 * SCALE_FACTOR;
            return;
        }
        Image imageTemp; //for scaling and sizing
        Sprite spriteTemp; //too
        Array<TextureAtlas.AtlasRegion> tempAtlasRegions; //OMG big count of variables
        TextureAtlas.AtlasRegion[] tempArrayFromAtlasRegion = new TextureAtlas.AtlasRegion[0]; //init made by IDEA
        //Loading sprites from atlas
        for (String spriteName : spriteNames) {
            //Treat all possible cases
            try {
                spriteTemp = new Sprite(objectAtlas.findRegion(spriteName));
            } catch (Exception e) {
                Gdx.app.log("GraphicRepresentation load()","Couldn't load sprite " + spriteName);
                spriteTemp = ResourceManager.getInstance().getErrorSprite();
            }
            spriteTemp.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            imageTemp = new Image(new SpriteDrawable(spriteTemp));
            images.put(spriteName, imageTemp);
            //some small than hexagons
            imageTemp.setSize(imageTemp.getWidth() * SCALE_FACTOR, imageTemp.getHeight() * SCALE_FACTOR);
        }
        //Loading animations from atlas
        for (int i = 0; i < animationNames.length; i++) {
            //too much try catch? yeah, resource can not found and game should not crash because of this
            tempArrayFromAtlasRegion = objectAtlas.findRegions(animationNames[i]).
                    toArray(TextureAtlas.AtlasRegion.class);
            if (tempArrayFromAtlasRegion.length == 0) { //animation with 0 frames can not be, alright?
                tempArrayFromAtlasRegion = ResourceManager.getInstance().getErrorAtlasRegions().
                        toArray(TextureAtlas.AtlasRegion.class);
                animationFrames[i] = 1f/2f; //error animation frames
            }
            //Array processing for scaling animations
            for (TextureAtlas.AtlasRegion aTempArrayFromAtlasRegion : tempArrayFromAtlasRegion) {
                aTempArrayFromAtlasRegion.getTexture().setFilter(Texture.TextureFilter.Linear,
                        Texture.TextureFilter.Linear);
            }
            tempAtlasRegions = new Array<TextureAtlas.AtlasRegion>(tempArrayFromAtlasRegion.length);
            tempAtlasRegions.addAll(tempArrayFromAtlasRegion);
            animations.put(animationNames[i],
                    new Animation<TextureAtlas.AtlasRegion>(animationFrames[i], tempAtlasRegions));

        } //may will call exceptions
        textureWidth = tempArrayFromAtlasRegion[0].originalWidth; //scale by Affine
        textureHeight = tempArrayFromAtlasRegion[0].originalHeight;
        halfTextureWidth = textureWidth / 2 * SCALE_FACTOR; //scale manually
        halfTextureHeight = textureHeight / 2 * SCALE_FACTOR;
    }

    //Do it so that draw simple sprite
    public void setSprite(@NotNull String name) {
        state = 1;
        if (images.containsKey(name)) {
            currentNameState = name;
            currentImage = images.get(name);
        } else {
            currentNameState = "Sprite not found";
            currentImage = ResourceManager.getInstance().getErrorImage();
            Gdx.app.log("GraphicRepresentation setSprite()", "Sprite name: " + name + " not found");
        }
    }

    //Do it before draw animation
    public void setAnimation(@NotNull String name) {
        state = 2;
        if (animations.containsKey(name)) {
            currentNameState = name;
            currentAnimation = animations.get(name);
        } else {
            currentNameState = "Animation not found";
            currentAnimation = ResourceManager.getInstance().getErrorAnimation();
            Gdx.app.log("GraphicRepresentation setAnimation()", "Animation name: " + name + " not found");
        }
    }

    //Just in case
    @SuppressWarnings("unused")
    public String getCurrentNameState() {
        return currentNameState;
    }

    //Perform in WorldRenderer
    //What should be drawn is controlled by GameObject (may be? I don't know who will control this process)
    public void draw(SpriteBatch spriteBatch, float x, float y, float elapsedTime) {
        switch (state) {
            //Nothing to draw, just break it
            case 0: break;
            //Draw sprite
            case 1:
                currentImage.setPosition(x, y, Align.center); //thanks to Image that it have align
                currentImage.draw(spriteBatch, 1); //alpha may be used for fade out
                break;
            //Draw animation and use elapsedTime
            case 2:
                //OMG what is it???
                affine2.m02 = x - halfTextureWidth; //m02 and m12 is X and Y coordinate, are you seriously??
                affine2.m12 = y - halfTextureHeight; //minus half of texture because render begin from left corner of texture
                spriteBatch.draw(currentAnimation.getKeyFrame(elapsedTime, true),
                        textureWidth, textureHeight, affine2); //simple and clear
                break;
            //Wrong state call exception
            default:
                throw new RuntimeException("Wrong graphic state of GameObject");
        }
    }

    //Dispose me, when dispose GameObject
    public void dispose() {
        state = 0;
        currentImage = null;
        currentAnimation = null;
        animations.clear();
        images.clear();
    }

}
