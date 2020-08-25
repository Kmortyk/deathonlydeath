package com.vanilla.death.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.algorithm.PathFinder;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.person.Hero;

/**
 * Created by mrkotyk on 25.06.17.
 */
public class HeroController {

    private Hero hero;
    private Hex[][] hexes;
    private int length;

    //самая конечная точка путешествия игрока, при нажатии устанавливается на кооро=динаты в массиве нажатого гексагона
    private int hexPressedX, hexPressedY;
    //координаты в массиве первой ближайшей клетки
    private int hexNextX, hexNextY;
    //переменные минимального ближайшего расстояния
    private int min_lengthX, min_lengthY;
    //достигли ли нужной позиции по x или по y (глобальные) по отношению к центру изображения гексагона
    //private boolean isDirX = false, isDirY = false;

    public HeroController() {
        this.hero = GameWorld.getInstance().getHero();
        this.hexes = GameWorld.getInstance().getHexes();

        length = hexes.length;

        //используем эти переменные для нахождения наименьших расстояний при поиске пути
        min_lengthX = length;
        min_lengthY = length;

        //это координаты в массиве следующей клетки, возможной для хода, устанавливаем по дефолту на героя, для проверки см ниже
        hexNextX = hero.getI();
        hexNextY = hero.getJ();
    }

    //функция вызываемая со скрина при нажатии на экран
    public void touched(float x, float y) {

        Hex presentHex = hexes[hero.getI()][hero.getJ()];

        switch (hero.STATE) {
            case NONE:
                if(presentHex.getGround().hexBounds.contains(x,y) &&  presentHex.hasItems()){
                  hero.addItems(presentHex.getItems());
                  presentHex.getItems().clear();
                  GameWorld.getInstance().getItems().removeAll(hero.getItems());
                  break;
              }
            case WALK:
                for (Ground gr : GameWorld.getInstance().getGround()) {
                    if (gr != null && gr.hexBounds.contains(x, y)) {
                        hexReleased();
                        toHex(gr.getI(), gr.getJ());
//                        if(!hexes[gr.getI()][gr.getJ()].isFree(hero) && hexNextX==gr.getI() && hexNextY==gr.getJ()){
//                            hero.STATE = Hero.State.NONE;
//
//                            if (hexes[hexNextX][hexNextY].getMob() != null && hexes[hexNextX][hexNextY].getMob().isTalking()) {
//                            hero.STATE = Hero.State.DIALOGUE;
//                            hexes[hexNextX][hexNextY].getMob().STATE = Mob.State.DIALOGUE;
//                            }
//
//                        }
                    }
                }
                break;
            case BATTLEMODE:
                if(hero.atPoint() && BattleModeController.getObject().isMyTurn(hero)){
                    for (Ground gr : GameWorld.getInstance().getGround()) {
                        if (gr != null && gr.hexBounds.contains(x, y)) {
                            toHex(gr.getI(), gr.getJ(), Hero.State.BATTLEMODE);
                        }
                    }

                    Hex nextHex = hexes[hexNextX][hexNextY];

                    if(!nextHex.isFree(hero)){
                        if(nextHex.getMob()!=null){
                            nextHex.getMob().hit(hero.getATTACK());
                            BattleModeController.getObject().stepped(hero);
                        }

                        hero.setAtPoint(true);
                        hexNextX = hero.getI();
                        hexNextY = hero.getJ();
                    }
                }
                break;
        }
    }

    public void toHex(int x, int y) {
        hexPressedX = x;
        hexPressedY = y;
        //если герой уже движется не рассчитываем первую ближайшую клетку, пусть уже дойдет :)
        if (!isMoving()) nextHex();
        //установка состояния игрока, в данном случае состояние ходьбы
        hero.STATE = Hero.State.WALK;
    }

    public void toHex(int x, int y, Hero.State STATE) {
        hexPressedX = x; hexPressedY = y;
        if (hero.atPoint()){
            nextHex();
            if(hexNextX != hero.getI() || hexNextY != hero.getJ()) hero.setAtPoint(false);
        }
        hero.STATE = STATE;
    }

    //метод для переключения игрока в состояние покоя
    public void hexReleased() {
        if (hero.STATE != Hero.State.DEAD) {
            hero.STATE = Hero.State.NONE;
            hero.setAtPoint(false);
        }
    }

    //проверка на движение: достигли ли ближайшей или еще нет
    public boolean isMoving() { return hero.STATE == Hero.State.WALK && !hero.atPoint(); }

    //метод для нахождения первой ближайшей свободной клетки приближающей к конечной точке,
// то есть к гексагону, по которому прошло нажатие
    public void nextHex() {
        /* XOO          XOO
           OHO           OHO
           XOO          XOO
         */
        int I = hero.getI(), J = hero.getJ();
//        hero.setMinDistance(GameWorld.getInstance().getMaxDistance());
////рассматриваем все клетки вокруг игрока, на расстоянии 1
//        for (int i = I - 1; i <= I + 1; i++) {
//
//            for (int j = J - 1; j <= J + 1; j++) {
////не выходим за рамки массива
////проверяем свободнсть клетки
//                if ((i!=I || j!=J) && GameWorld.getInstance().inArray(i,j) && hexes[i][j].getGround() != null) {
//
////по две клетки становятся недоступными при гексагоновом поле, это зависит от четности строки,
//                    //если четная: клетки нечетных рядов левее, левые крайние недоступны
//                    //нечетная: правые крайние недоступны - см сдвиг
//                    switch (I % 2) {
//                        case 0:
//                            if (!(i == I + 1 && j == J - 1) && !(i == I - 1 && j == J - 1)) {
//                                calculateHex(i, j);
//                            }
//                            break;
//                        case 1:
//                            if (!(i == I + 1 && j == J + 1) && !(i == I - 1 && j == J + 1)) {
//                                calculateHex(i, j);
//                            }
//                            break;
//                    }
//                }
//
//            }
//
//        }
        int[] path = PathFinder.getObject().pathFind(I,J,hexPressedX,hexPressedY).toArray();
        int path_length = path.length;

        if(path_length >= 2){
            hexNextX = path[path_length-2];
            hexNextY = path[path_length-1];
        }
        else{
            hexNextX = hero.getI();
            hexNextY = hero.getJ();
            hero.STATE = Hero.State.NONE;
            //Gdx.app.log("HeroController ERROR","Hero path length lower then 2.");
        }
//в конце минимумом ставим максимум, кто знает, как далеко следующая)
        //min_lengthX = length;
       // min_lengthY = length;

        //System.out.println("nextX: " + hexNextX + " nextY: " + hexNextY);
    }

    private void calculateHex(int i, int j) {

        Vector2 nextPosition = hexes[i][j].getGround().hexCenter;
        //Vector2 nextPosition = Ground.getPosition(i,j);
        Vector2 lastPosition = hexes[hexPressedX][hexPressedY].getGround().hexCenter;

        if(Vector2.dst2(nextPosition.x,nextPosition.y,lastPosition.x,lastPosition.y) < hero.getMinDistance()){
            if(hexes[i][j].isFree(hero) || (i==hexPressedX && j==hexPressedY)) {
                hexNextX = i;
                hexNextY = j;
                hero.setMinDistance(Vector2.dst2(nextPosition.x, nextPosition.y, lastPosition.x, lastPosition.y));
            }
        }
    }

    private void setBelongsHex(Ground next, Ground last, Hero hero) {

        if (!last.bounds.contains(hero.bounds)) {
            hexes[hero.getI()][hero.getJ()].setHero(null);

            if (next.bounds.contains(hero.bounds)) {
                hero.setIJ(hexNextX, hexNextY);
                hexes[hexNextX][hexNextY].setHero(hero);
            }
        }
    }


    //главный метод класса...обновляем состояния объектов здесь
    public void update(float delta) {

        updateGraphics();
        hero.checkQuests();


        Hex nextHex = hexes[hexNextX][hexNextY];
        if (nextHex.getGround() == null) return;
        Hex presentHex = hexes[hero.getI()][hero.getJ()];
        if(nextHex == presentHex && hero.atPoint()) return;
        hero.updateLookDir(nextHex.getGround().hexCenter);

        switch (hero.STATE) {

            case WALK:
                //если все-таки даже после всех проверок ближайшая все еще позиция игрока, значит игрок в ловушке и должен стоять,
                //а не крутиться волчком
                //if (hero.getI() == hexNextX && hero.getJ() == hexNextY) hexReleased();
                if (!nextHex.isFree(hero)) {
                        hero.STATE = Hero.State.NONE;
                        if (nextHex.hasTalkingMob()) {
                            DialogueController.getObject().createDialogue(hero, nextHex.getMob());
                        }else{
                            hexReleased();
                            toHex(hero.getI(), hero.getJ());
                        }
                }

                setBelongsHex(nextHex.getGround(), presentHex.getGround(), hero);

                if(hero.update(nextHex.getGround().hexCenter, delta)) {
                    if(presentHex.inBattleMode()){
                        BattleModeController.getObject().getHeroesParty().add(hero);
                        hero.STATE = Hero.State.BATTLEMODE;
                    }
//если достигли конечной точки то стоять, иначе- ищем следующую ближайшую клетку и вперед
                    if (hero.getI() == hexPressedX && hero.getJ() == hexPressedY) {
                        hexReleased();
                    } else {
                        hero.setAtPoint(false);
                        nextHex();
                    }
                }
                break;

            case BATTLEMODE:

                if(BattleModeController.getObject().isMyTurn(hero)) {

                        if (hero.update(nextHex.getGround().hexCenter, delta)) {
                            if (!presentHex.inBattleMode()) { BattleModeController.getObject().removeHero(hero); return; }
                            BattleModeController.getObject().stepped(hero);
                        }
                        setBelongsHex(nextHex.getGround(), presentHex.getGround(), hero);

                }

                BattleModeController.getObject().checkPartys(hero);

                break;
        }
    }


    public void updateGraphics(){
        if(hero.lookDir[0]==1)hero.getGraphics().setAnimation("WalkLeft");
        if(hero.lookDir[0]==-1)hero.getGraphics().setAnimation("WalkRight");
        if(hero.lookDir[0]==0){
            if(hero.getGraphics().getCurrentNameState().equals("WalkLeft")) hero.getGraphics().setSprite("LookLeft");
            if(hero.getGraphics().getCurrentNameState().equals("WalkRight")) hero.getGraphics().setSprite("LookRight");
        }
    }


}

//    private void calculateHex(int i, int j, int I, int J) {
////проверка на ближайшесть: если модуль расстояний предполагаемой клетки и последней
//// меньше модуля позиции игрока и последней, занчит подходит
//        if (Math.abs(i - hexPressedX) <= min_lengthX && Math.abs(j - hexPressedY) <= min_lengthY) {
//
//            if (hexes[i][j].isFree()) {
//                hexNextX = i;
//                hexNextY = j;
//                min_lengthX = Math.abs(i - hexPressedX);
//                min_lengthY = Math.abs(j - hexPressedY);
//
//            } else if (i == hexPressedX && j == hexPressedY) {
//                hexNextX = hexPressedX;
//                hexNextY = hexPressedY;
//            }
////ставим минимумом модуль, то есть расстояние, для следующих проверок, никогда не знаешь какая приблизит ближе
//
//        }
//    }