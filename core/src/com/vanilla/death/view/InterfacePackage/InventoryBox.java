package com.vanilla.death.view.InterfacePackage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.vanilla.death.utils.FontManager;
import com.vanilla.death.utils.PagedScrollPane;
import com.vanilla.death.utils.ResourceManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

//Class in singleTone
@SuppressWarnings("FieldCanBeLocal") //FIXME
public class InventoryBox {

    private static InventoryBox object;
    private Table inventoryBoxTable, inventoryBoxInnerTable, inventoryTable, textTable, inventoryTableOuter, textOuterTable;
    private Table[] userTables, statTables;
    private Label textInfo;
    private Label[] namesOfPlayers;
    private Stack inventoryBoxBackground;
    private ScrollPane inventoryScrollPane, textScrollPane;
    private PagedScrollPane usersPagedScrollPane;
    private Container<Table> inventoryBackgroundContainer, textBackgroundContainer;
    private Container[] cellContainers;
    private ProgressBar[] progressBars;
    private int countOfPlayers = 0;
    private Image[] facesImages, imagesInInventory, itemsImages;
    private String[] itemsInformation;
    private I18NBundle localizationBundle;
    //private String[] informationInInventory; May be useful in future

    private InventoryBox() {

        //init localization
        localizationBundle = ResourceManager.getInstance().getLocalizationBundle();

        //FIXME GET ITEMS IMAGES FROM GRAPHIC_REPRESENTATION
        TextureAtlas errorAtlas = new TextureAtlas("sprites/gameObjects/ErrorSprites/ErrorAtlas.atlas");
        itemsImages = new Image[] {
                new Image(errorAtlas.findRegion("MissedSprite")),
                new Image(errorAtlas.findRegion("MissedSprite")),
                new Image(errorAtlas.findRegion("MissedSprite"))
        };
        for (int i = 0; i < itemsImages.length; i++) {
            final int id = i;
            itemsImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    textInfo.setText(itemsInformation[id]);
                }
            });
        }
        itemsInformation = new String[] {
                "Это нулевой предмет",
                "Это первый предмет",
                "Это второй предмет"
        };
        //initialization of items store
        imagesInInventory = new Image[100];
        //informationInInventory = new String[100];

        //in users layout
        namesOfPlayers = new Label[4];
        for (int i = 0; i < 4; i++) {
            namesOfPlayers[i] = new Label("Name of player", FontManager.getObject().getDefaultSkin());
        }

        //stats layout
        progressBars = new ProgressBar[16];
        for (int i = 0; i < 16; i++) {
            progressBars[i] = new ProgressBar(0, 100, 1, false, FontManager.getObject().getDefaultSkin());
            progressBars[i].setValue(6.25f * (i + 1));
        }
        statTables = new Table[4];
        for (int i = 0; i < 4; i++) {
            Label[] statNames = getStatNames();
            statTables[i] = new Table();
            statTables[i].defaults().width(100).height(25).space(5);
            statTables[i].add(statNames[0]);
            statTables[i].add(progressBars[4 * i]);
            statTables[i].add(statNames[1]);
            statTables[i].add(progressBars[1 + (4 * i)]);
            statTables[i].row();
            statTables[i].add(statNames[2]);
            statTables[i].add(progressBars[2 + (4 * i)]);
            statTables[i].add(statNames[3]);
            statTables[i].add(progressBars[3 + (4 * i)]);
        }
        //progressBar.setValue(50);

        //users layout
        usersPagedScrollPane = new PagedScrollPane();
        usersPagedScrollPane.setFlingTime(0);
        usersPagedScrollPane.setPageSpacing(0);
        facesImages = UsersGUI.getObject().getFacesImages();
        userTables = new Table[4];
        for (int i = 0; i < 4; i++) {
            userTables[i] = new Table();
        }
        usersPagedScrollPane.setActionInScroll(new Runnable() {
            @Override
            public void run() {
                setCells(PagedScrollPane.currentPage);
            }
        });

        //inventory layout
        SpriteDrawable cellImage = new SpriteDrawable(new Sprite(ResourceManager.getInstance().getInventoryBoxAtlas().findRegion("Cell")));
        cellContainers = new Container[25]; //inventory for 25 objects
        inventoryTable = new Table();
        inventoryTable.center();
        inventoryTable.defaults().width(95).height(95).pad(0.5f);
        for (int i = 0; i < 25; i++) {
            if (i % 5 == 0) inventoryTable.row();
            cellContainers[i] = new Container<Image>();
            cellContainers[i].setBackground(cellImage);
            inventoryTable.add(cellContainers[i]);
        }
        inventoryScrollPane = new ScrollPane(inventoryTable);

        //text layout
        textInfo = new Label(localizationBundle.get("InventoryNotChosenItem"), FontManager.getObject().getDefaultSkin());
        textInfo.setWrap(true);
        textInfo.setAlignment(Align.topLeft);
        textTable = new Table();
        textTable.align(Align.topLeft);
        textTable.add(textInfo).width(225).height(FontManager.getObject().getHeightOfTheWrappedText(textInfo, 225));
        textScrollPane = new ScrollPane(textTable);
        textOuterTable = new Table();
        textOuterTable.add(textScrollPane).width(225).height(300);

        //background set
        inventoryTableOuter = new Table();
        inventoryTableOuter.center();
        inventoryTableOuter.add(inventoryScrollPane).width(485).height(306);
        inventoryBackgroundContainer = new Container<Table>(inventoryTableOuter);
        inventoryBackgroundContainer.setBackground(getTableBackground(485, 310));
        textBackgroundContainer = new Container<Table>(textOuterTable);
        textBackgroundContainer.setBackground(getTableBackground(235, 310));

        //inner layout
        inventoryBoxInnerTable = new Table();
        inventoryBoxInnerTable.defaults().pad(5);
        inventoryBoxInnerTable.add(usersPagedScrollPane).width(735).height(60).colspan(2).row();
        inventoryBoxInnerTable.add(inventoryBackgroundContainer).width(485).height(310);
        inventoryBoxInnerTable.add(textBackgroundContainer).width(235).height(310);
        //inventoryBoxInnerTable.debug();

        //inventory background
        inventoryBoxBackground = new Stack(new Image(new NinePatch(ResourceManager.getInstance().getInventoryBoxAtlas().
                findRegion("BackgroundPatched"))), new Image(getTableBackground(750, 400)),
                inventoryBoxInnerTable);

        //global layout
        inventoryBoxTable = new Table();
        inventoryBoxTable.setFillParent(true);
        inventoryBoxTable.center();
        inventoryBoxTable.add(inventoryBoxBackground).width(750).height(400).row();
        inventoryBoxTable.add().width(750).height(30);
    }

    public void addPlayer(int faceID, String namePlayer) {
        namesOfPlayers[countOfPlayers].setText(namePlayer);
        userTables[countOfPlayers].clear();
        userTables[countOfPlayers].defaults().pad(5);
        userTables[countOfPlayers].add(facesImages[faceID]).width(50).height(50);
        userTables[countOfPlayers].add(namesOfPlayers[countOfPlayers]).width(190).height(50);
        userTables[countOfPlayers].add(statTables[countOfPlayers]).width(465).height(50);
        userTables[countOfPlayers].setBackground(getTableBackground(735, 60));
        usersPagedScrollPane.addPage(userTables[countOfPlayers], 735);
        countOfPlayers++;
    }

    public void updateStatsOfPlayer(int playerID, int[] stats) {
        if (stats.length != 4) throw new UnsupportedOperationException("Wrong stats");
        for (int i = 0; i < stats.length; i++) {
            progressBars[(4 * playerID) + i].setValue(stats[i]);
        }
    }

    /*public void updateInventory(int playerID, ArrayList<Item> items) {
        //FIXME FUTURE RELEASE
        for (int i = 0; i < cellContainers.length; i++) {
            cellContainers[i].clear();
        }
    }*/

    public void updateInventory(int playerID, int[] items) {
        int j = 0;
        for (int i = 25 * playerID; i < 25 * (playerID + 1); i++) {
            if (j < items.length) {
                imagesInInventory[i] = itemsImages[items[j]];
                //informationInInventory[i] = itemsInformation[items[j]];
                j++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void setCells(int playerID) {
        for (int i = 0; i < 25; i++) {
            cellContainers[i].setActor(imagesInInventory[i + 25 * playerID]);
        }
        textInfo.setText(localizationBundle.get("InventoryNotChosenItem"));
    }

    @NotNull
    private SpriteDrawable getTableBackground(int width, int height) {
        Pixmap outline = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        outline.setColor(Color.BROWN);
        outline.drawRectangle(0,0,width, height);
        outline.drawRectangle(1,1,width - 2, height - 2);
        Texture tempTexture = new Texture(outline);
        outline.dispose();
        return new SpriteDrawable(new Sprite(tempTexture));
    }

    private Label[] getStatNames() {
        Label[] statNames = new Label[] {
                new Label(localizationBundle.get("InventoryStatHealth"), FontManager.getObject().getDefaultSkin()),
                new Label(localizationBundle.get("InventoryStatPower"), FontManager.getObject().getDefaultSkin()),
                new Label(localizationBundle.get("InventoryStatLevel"), FontManager.getObject().getDefaultSkin()),
                new Label(localizationBundle.get("InventoryStatProgress"), FontManager.getObject().getDefaultSkin())
        };
        for (Label label : statNames) {
            label.setFontScale(0.5f);
            label.setAlignment(Align.right);
        }
        return statNames;
    }

    Table getInventoryBoxTable() {
        return inventoryBoxTable;
    }

    //Must be disposed in MainClass
    public void dispose() {
        object = null;
    }

    public static InventoryBox getObject() {
        if (object == null) {
            object = new InventoryBox();
        }
        return object;
    }

    @Contract(pure = true)
    public static boolean isUsed() {
        return object != null;
    }
}
