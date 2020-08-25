package com.vanilla.death.view.InterfacePackage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vanilla.death.AllScenesScreen;
import com.vanilla.death.MainClass;
import com.vanilla.death.utils.InputManager;

public class InterfaceManager {

    private static InterfaceManager object;

    private Stage interfaceStage;

    @SuppressWarnings("all")
    private InterfaceStates interfaceStates;
    private Table rightCornerTable, pauseMenuTable, usersGUITable, namesGUITable, dialogueBoxTable, inventoryBoxTable;
    public static MainClass mainClass;

    private enum InterfaceStates {
        GAME, PAUSE, EXIT, CHAT, DIALOGUE, INVENTORY
    }

    private InterfaceManager() {
        interfaceStage = new Stage(MainClass.getViewportInterfaceStage());
        rightCornerTable = RightCornerButtons.getObject().getRightCornerTable();
        pauseMenuTable = PauseMenuButtons.getObject().getPauseMenuTable();
        usersGUITable = UsersGUI.getObject().getUsersGUITable();
        namesGUITable = UsersGUI.getObject().getNamesGUITable();
        dialogueBoxTable = DialogueBox.getObject().getDialogueBoxTable();
        inventoryBoxTable = InventoryBox.getObject().getInventoryBoxTable();
        interfaceStage.addActor(rightCornerTable);
        interfaceStage.addActor(pauseMenuTable);
        interfaceStage.addActor(usersGUITable);
        interfaceStage.addActor(dialogueBoxTable);
        interfaceStage.addActor(inventoryBoxTable);
        InputManager.getObject().addInputProcessor(interfaceStage);
        changeInterfaceState("GAME");

        //code for example
        updateUsersGUI(new int[] {
                25, 50, 75, 100
        }, new int[] {
                25, 50, 75, 100
        }, new int[] {
                0, 1, 2, 3
        }, new Color[] {
                Color.BLUE, Color.GREEN, Color.BROWN, Color.GRAY
        }, new Color[] {
                Color.PURPLE, Color.GOLD, Color.CORAL, Color.FIREBRICK
        },new String[] {
                "MrKotyk", "Betmen", "Unicorn", "Hedgehog"
        });
    }

    public static InterfaceManager getObject() {
        if (object == null) {
            object = new InterfaceManager();
        }
        return object;
    }

    void changeInterfaceState(String state) {
        interfaceStates = InterfaceStates.valueOf(state);
        switch (interfaceStates) {
            case GAME:
                rightCornerTable.setVisible(true);
                pauseMenuTable.setVisible(false);
                usersGUITable.setVisible(true);
                namesGUITable.setVisible(false);
                dialogueBoxTable.setVisible(false);
                inventoryBoxTable.setVisible(false);
                break;
            case PAUSE:
                rightCornerTable.setVisible(false);
                pauseMenuTable.setVisible(true);
                usersGUITable.setVisible(true);
                namesGUITable.setVisible(true);
                dialogueBoxTable.setVisible(false);
                inventoryBoxTable.setVisible(false);
                break;
            case CHAT:
                rightCornerTable.setVisible(false);
                pauseMenuTable.setVisible(false);
                usersGUITable.setVisible(true);
                namesGUITable.setVisible(true);
                dialogueBoxTable.setVisible(false);
                inventoryBoxTable.setVisible(false);
                break;
            case DIALOGUE:
                rightCornerTable.setVisible(false);
                pauseMenuTable.setVisible(false);
                usersGUITable.setVisible(false);
                namesGUITable.setVisible(false);
                dialogueBoxTable.setVisible(true);
                inventoryBoxTable.setVisible(false);
                break;
            case INVENTORY:
                rightCornerTable.setVisible(true);
                pauseMenuTable.setVisible(false);
                usersGUITable.setVisible(false);
                namesGUITable.setVisible(false);
                dialogueBoxTable.setVisible(false);
                inventoryBoxTable.setVisible(true);
                break;
            case EXIT:
                mainClass.setScreen(new AllScenesScreen(mainClass));
                break;
        }
    }

    /*public void changePlayerState(int id, int health, int power, int idFace, String name) {
        UsersGUI.getObject().changePlayerState(id, health, power, idFace, name);
    }

    public void createNewPlayer(int health, int power, int idFace, String name) {
        UsersGUI.getObject().createNewPlayer(health, power, idFace, name);
    }

    public void deletePlayer(int id) {
        UsersGUI.getObject().deletePlayer(id);
    }*/

    public void updateUsersGUI(int[] healths, int[] powers, int[] idFaces, Color[] colorsHealth, Color[] colorsPower, String[] names) {
        UsersGUI.getObject().update(healths, powers, idFaces, colorsHealth, colorsPower, names);
    }

    public void createDialogueBox() {
        changeInterfaceState("DIALOGUE");
    }

    public void updateDialogueBox(String text) {
        DialogueBox.getObject().updateDialogueBox(text);
    }

    public void updateDialogueBox(String text, String[] answers) {
        DialogueBox.getObject().updateDialogueBox(text, answers);
    }

    public void stopDialogueBox() {
        changeInterfaceState("GAME");
    }

    public void draw() {
        interfaceStage.act();
        interfaceStage.draw();
    }

    public void dispose() {
        interfaceStage.dispose();
        object = null;
    }

    public static boolean isUsed() {
        return object != null;
    }
}
