package com.vanilla.death.view.InterfacePackage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.vanilla.death.utils.ResourceManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class UsersGUI {

    private static UsersGUI object;

    private TextureAtlas textureAtlas;
    private Image[] faces, powers, healths;
    private Table usersGUITable, namesGUITable;
    private Stack[] playersStacks;
    private Image tempImage;
    @SuppressWarnings("all")
    private Label tempLabel;

    private UsersGUI() {
        textureAtlas = ResourceManager.getInstance().getUsersGUIAtlas();
        faces = getFacesImages();
        powers = getPowersImages();
        healths = getHealthsImages();
        usersGUITable = getConfiguredTable();
        namesGUITable = getConfiguredNameTable();
    }

    public void draw() {
    }

    public static UsersGUI getObject() {
        if (object == null) object = new UsersGUI();
        return object;
    }

    private Table getConfiguredTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().left();
        return table;
    }

    private Table getConfiguredNameTable() {
        Table table = new Table();
        table.setSkin(ResourceManager.getInstance().getInterfaceSkin());
        table.setFillParent(true);
        table.left().bottom();
        return table;
    }

    @NotNull
    @Contract(pure = true)
    Image[] getFacesImages() {
        return new Image[] {
                new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("MrKotykFace")))),
                new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("GG1Face")))),
                new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("UnicornFace")))),
                new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("HedgehogFace"))))
        };
    }

    private Image[] getPowersImages() {
        Image[] powersArray = new Image[11];
        for (int i = 0; i < powersArray.length; i++) {
            powersArray[i] = new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("Power" + (i * 10)))));
        }
        return powersArray;
    }

    private Image[] getHealthsImages() {
        Image[] imagesArray = new Image[11];
        for (int i = 0; i < imagesArray.length; i++) {
            imagesArray[i] = new Image(new SpriteDrawable(new Sprite(textureAtlas.findRegion("HP" + (i * 10)))));
        }
        return imagesArray;
    }

    private Stack[] getConfiguredStack() {
        Stack[] stacks = new Stack[4];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new Stack();
        }
        return stacks;
    }

    void update(int[] healths, int[] powers, int[] idFaces, Color[] colorsHealth, Color[] colorsPower, String[] names) {
        usersGUITable.clear();
        namesGUITable.clear();
        playersStacks = getConfiguredStack();
        for (String name : names) {
            namesGUITable.add(getNameLabel(name)).width(100).pad(5);
        }
        usersGUITable.add(namesGUITable).colspan(0);
        usersGUITable.row();
        for (int i = 0; i < names.length; i++) {
            playersStacks[i].add(getPowerImage(powers[i], colorsPower[i]));
            playersStacks[i].add(getHealthImage(healths[i], colorsHealth[i]));
            playersStacks[i].add(getFaceImage(idFaces[i]));
            final int tempVariable = i;
            playersStacks[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    System.out.println("You pressed to " + tempVariable + " icon");
                    InterfaceManager.getObject().changeInterfaceState("INVENTORY");
                }
            });
            usersGUITable.add(playersStacks[i]).width(100).height(100).pad(5);
        }
    }

    private Label getNameLabel(String name) {
        tempLabel = new Label(name, ResourceManager.getInstance().getInterfaceSkin(), "Kurale", Color.WHITE);
        tempLabel.setWrap(true);
        tempLabel.setAlignment(Align.center);
        tempLabel.setFontScale(0.7f);
        return tempLabel;
    }

    @Contract(pure = true)
    private Image getHealthImage(int health, Color color) {
        tempImage = healths[health / 10];
        tempImage.setColor(color);
        return tempImage;
    }

    @Contract(pure = true)
    private Image getPowerImage(int power, Color color) {
        tempImage = powers[power / 10];
        tempImage.setColor(color);
        return tempImage;
    }

    @Contract(pure = true)
    private Image getFaceImage(int faceID){
        return faces[faceID];
    }

    Table getUsersGUITable() {
        return usersGUITable;
    }

    Table getNamesGUITable() { return namesGUITable; }

    @Contract(pure = true)
    public static boolean usUsed() {
        return object != null;
    }

    public void dispose() {
        textureAtlas.dispose();
        faces = powers = healths = null;
        usersGUITable.clear();
        namesGUITable.clear();
        playersStacks = null;
        tempImage = null;
        object = null;
    }
}
