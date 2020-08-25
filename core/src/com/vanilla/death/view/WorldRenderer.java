package com.vanilla.death.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vanilla.death.MainClass;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.view.InterfacePackage.DialogueBox;
import com.vanilla.death.view.InterfacePackage.InterfaceManager;
import com.vanilla.death.view.InterfacePackage.InventoryBox;

/**
 * Created by mrkotyk on 25.06.17.
 *///
public class WorldRenderer {

    private ShapeRenderer renderer;

    private int width,height;
    private float pointPixelsX, pointPixelsY;

    private SpriteBatch spriteBatch;

    private BitmapFont font;
    private CameraScreen cameraScreen;
    private Table table;
    private Label label;
    private Stage stage;
    private InterfaceManager interfaceManager;
    private float elapsedTime = 0f;


    //тестовые костыли
    //private GraphicRepresentation graphicRepresentation;



    public WorldRenderer(MainClass mainClass){
        //camera = new OrthographicCamera(CAMWIDTH,CAMHEIGHT);
        //SetCamera(CAMWIDTH/ 2f, CAMHEIGHT / 2f);

        renderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        //font = new BitmapFont();
        //font.setColor(1f,0f,0f,1f);

        //camera = new Camera(mainClass, spriteBatch, 42, 75, 25, 42);
        //camera.setDebugMode(true);
        //mainClass.setViewport(camera.getOrthographicCamera(), 50, 50);
        //cameraScreen = CameraScreen.getObject();
        cameraScreen = CameraScreen.getObject();
        cameraScreen.setEdges(105, 60);
        mainClass.setViewport(cameraScreen.getOrthographicCamera(), 50, 50);
        //cameraScreen.setActiveViewport();
        table = new Table();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/fonts/Kurale.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderWidth = 1;
        parameter.color = Color.RED;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = Color.BLACK;
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        label = new Label("Evic", labelStyle);
        label.setSize(1, 1);
        label.setPosition(8, 1);
        label.setFontScale(0.05f);


        stage = new Stage();
        stage.addActor(label);
        stage.getViewport().setCamera(cameraScreen.getOrthographicCamera());

        /*interfaceManagerOld = new InterfaceManagerOld(mainClass, camera, new Vector2[] {
                new Vector2(1, 50), new Vector2(0, 1),
                new Vector2(80, 2), new Vector2(80, 50)
        }, 0.1f, spriteBatch);*/
        interfaceManager = InterfaceManager.getObject();

        //тестовые костыли в будущем считывать с json файла
//        graphicRepresentation = new GraphicRepresentation("sprites/gameObjects/Characters/GG1/GG1TextureAtlas.atlas",
//                new String[] {
//                        "LookLeft", "LookRight"
//                }, new float[] {
//                1f/20f, 1f/20f
//        }, new String[] {
//                "WalkLeft", "WalkRight"
//        });
//        graphicRepresentation.setSprite("LookLeft");
//        graphicRepresentation.setAnimation("WalkLeft");
        InventoryBox.getObject().addPlayer(0, "Mr.Kotyk");
        InventoryBox.getObject().addPlayer(1, "BetmenBroni");
        InventoryBox.getObject().updateStatsOfPlayer(0, new int[] {
                25, 50, 75, 100
        });
        InventoryBox.getObject().updateStatsOfPlayer(1, new int[] {
                100, 75, 50, 25
        });
        InventoryBox.getObject().updateInventory(0, new int[] {
                2, 0, 1
        });
        InventoryBox.getObject().updateInventory(1, new int[] {
                1
        });
        InventoryBox.getObject().setCells(0);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        //pointPixelsX = width/CAMWIDTH;
        //pointPixelsY = height/CAMHEIGHT;
        pointPixelsX = pointPixelsY = 1;
    }

    private void drawGround(){
        renderer.setProjectionMatrix(cameraScreen.getCombined());
        //тип устанавливаем...а данном случае с заливкой
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(new Color(1, 0, 0, 1));

            for(Ground gr: GameWorld.getInstance().getGround()){

                //drawPosition.set(gr.position.x, gr.position.y,0);
                //CameraConvert(drawPosition);
                //gr.getGroundSprite().setCenter(drawPosition.x,drawPosition.y);
                //gr.getGroundSprite().setScale(0.05f);
                //gr.getGroundSprite().draw(spriteBatch);


                //renderer.rect(gr.bounds.x, gr.bounds.y, gr.bounds.width, gr.bounds.height);
                if(gr==null) continue;

                    spriteBatch.draw(gr.getGroundTexture(),
                            gr.position.x * pointPixelsX, gr.position.y * pointPixelsY,
                            gr.bounds.getWidth() * pointPixelsX,
                            gr.bounds.getHeight() * pointPixelsY);


                    if(GameWorld.getInstance().getHexes()[gr.getI()][gr.getJ()].inBattleMode()){
                        renderer.polygon(gr.hexBounds.getTransformedVertices());
                    }
                //renderer.polygon(gr.hexBounds.getTransformedVertices());


            }

        renderer.end();
    }

    /*private void drawHero(){
        renderer.setProjectionMatrix(cameraScreen2.getCombined());
        Hero hero = GameWorld.getInstance().getHero();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        Rectangle rect = hero.bounds;
        //float x1 = hero.position.x + rect.x;
        //float y1 = hero.position.y + rect.y;
        renderer.setColor(new Color(1, 0, 0, 1));

        float x1 = hero.bounds.x;
        float y1 = hero.bounds.y;
        renderer.rect(x1, y1, hero.bounds.width, hero.bounds.height);

        renderer.end();
    }*/

    private void drawHeroes(){
        renderer.setProjectionMatrix(cameraScreen.getCombined());

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(new Color(0, 1, 0, 1));

        spriteBatch.begin();
        for (Hero hero : GameWorld.getInstance().getHeroes()) {

            float x1 = hero.bounds.x;
            float y1 = hero.bounds.y;

            //renderer.rect(x1, y1, hero.bounds.width, hero.bounds.height);
            hero.getGraphics().draw(spriteBatch, x1, y1, elapsedTime);
        }
        spriteBatch.end();

        //float x1 = hero.position.x + rect.x;
        //float y1 = hero.position.y + rect.y;
        renderer.end();
    }

    private void drawMobs(){
        renderer.setProjectionMatrix(cameraScreen.getCombined());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(new Color(1, 0, 0, 1));



        for (Mob mb : GameWorld.getInstance().getMobs()) {

            if(mb!=null) {

                float x1 = mb.bounds.x;
                float y1 = mb.bounds.y;

                switch(mb.STATE){
                    case BATTLEMODE:
                        label.setText(mb.getHP()+"");
                        break;
                    case DIALOGUE:
                        label.setText(mb.getDialogue().getText());
                        break;
                    case DEAD:
                        label.setText("DEAD");
                        break;
                    default:
                        label.setText(mb.name);
                        break;
                }

                renderer.rect(x1, y1, mb.bounds.width, mb.bounds.height);
                Vector2 v2 = mb.getPosition(mb.getI() - mb.lookDir[0], mb.getJ() - mb.lookDir[1]);
                //renderer.rect(v2.x,v2.y, mb.bounds.width, mb.bounds.height);
                renderer.ellipse(v2.x,v2.y, mb.bounds.width, mb.bounds.height);
                //table.setPosition(x1*pointPixelsX, (y1+mb.boundsHeight*2)*pointPixelsY);
                //table.setPosition(x1, y1 + 5);
                label.setPosition(x1 - 2, y1 + 8);
                //table.draw(spriteBatch, 1);
                //font.draw(spriteBatch, "Evic", x1*pointPixelsX, (y1+mb.boundsHeight*2)*pointPixelsY);
            }
        }

        renderer.end();
    }


    private void drawItems(){
        renderer.setProjectionMatrix(cameraScreen.getCombined());

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(new Color(1, 0.5f, 0.5f, 1));


        for (Item item : GameWorld.getInstance().getItems()) {
            float x1 = item.bounds.x;
            float y1 = item.bounds.y;

            //renderer.rect(x1, y1, item.bounds.width, item.bounds.height);
            renderer.circle(x1,y1, item.bounds.width);
        }

        //float x1 = hero.position.x + rect.x;
        //float y1 = hero.position.y + rect.y;
        renderer.end();
    }


    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        cameraScreen.updatePosition(GameWorld.getInstance().getHero().position.x, GameWorld.getInstance().getHero().position.y);
        spriteBatch.setProjectionMatrix(cameraScreen.getCombined());
        spriteBatch.begin();
        drawGround();
        //font.draw(spriteBatch, "Evic", 3, 5);
        spriteBatch.end();
        drawItems();
        drawMobs();
        drawHeroes();

        stage.act();
        stage.draw();
        interfaceManager.draw();
    }

    public Vector3 CameraConvert(Vector3 touchPosition){
        cameraScreen.getOrthographicCamera().unproject(touchPosition);

        return touchPosition;
    }

    public void dispose() {
        renderer.dispose();
        spriteBatch.dispose();
        font.dispose();
        cameraScreen.dispose();
        table.clear();
        label.clear();
        stage.dispose();
    }

}
