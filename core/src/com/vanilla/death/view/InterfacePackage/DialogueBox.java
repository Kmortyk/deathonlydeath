package com.vanilla.death.view.InterfacePackage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.vanilla.death.controller.DialogueController;
import com.vanilla.death.utils.FontManager;
import com.vanilla.death.utils.ResourceManager;
import org.jetbrains.annotations.Contract;

public class DialogueBox {

    private static DialogueBox object;
    private final int MAX_ANSWERS_BUTTONS = 12;

    private Table dialogueBoxTable, dialogueBoxInnerTable, dialogueBoxAnswersTable;
    private Label textLabel;
    private Image background;
    private Stack tableStack;
    private ScrollPane scrollableAnswers;
    private TextButton[] answersButtons;
    private FontManager fontManager;


    private DialogueBox() {
        fontManager = FontManager.getObject();
        textLabel = new Label("", ResourceManager.getInstance().getInterfaceSkin(), "Kurale", Color.WHITE);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.topLeft);
        dialogueBoxInnerTable = new Table();
        dialogueBoxInnerTable.top();
        dialogueBoxInnerTable.setFillParent(true);
        background = new Image(new NinePatch(ResourceManager.getInstance().getDialogueBoxBackground()));
        tableStack = new Stack(background, dialogueBoxInnerTable);
        dialogueBoxTable = new Table();
        dialogueBoxTable.setFillParent(true);
        dialogueBoxTable.add(tableStack).width(750).height(200).row();
        dialogueBoxTable.add().width(750).height(10); //some space under table
        dialogueBoxTable.bottom();
        dialogueBoxAnswersTable = new Table();
        scrollableAnswers = new ScrollPane(dialogueBoxAnswersTable);
        scrollableAnswers.setScrollingDisabled(true, false);
        answersButtons = new TextButton[MAX_ANSWERS_BUTTONS];
        for (int i = 0; i < MAX_ANSWERS_BUTTONS; i++) {
            answersButtons[i] = new TextButton(null, ResourceManager.getInstance().getInterfaceSkin());
            answersButtons[i].getLabel().setWrap(true);
        }
    }

    void updateDialogueBox(String text) {
        dialogueBoxInnerTable.clear();
        dialogueBoxAnswersTable.clear();
        dialogueBoxTable.clearListeners();
        dialogueBoxTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                DialogueController.getObject().clicked();
            }
        });
        dialogueBoxInnerTable.add().width(725).height(10).row();
        textLabel.setText(text);
        dialogueBoxInnerTable.add(textLabel).width(725).height(175).row();
        dialogueBoxInnerTable.add().width(725).height(10);
    }

    void updateDialogueBox(String text, String[] answers) {
        if (answers.length > MAX_ANSWERS_BUTTONS) try {
            throw new Exception("The answers buttons in the dialog box are larger than the allocated memory for the buttons themselves");
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogueBoxInnerTable.clear();
        dialogueBoxAnswersTable.clear();
        dialogueBoxTable.clearListeners();
        dialogueBoxInnerTable.add().width(725).height(10).row();
        textLabel.setText(text);
        dialogueBoxInnerTable.add(scrollableAnswers).width(725).height(180).row();
        dialogueBoxInnerTable.add().width(725).height(10);
        dialogueBoxAnswersTable.add(textLabel).width(725).height(fontManager.getHeightOfTheWrappedText(textLabel, 715)).colspan(3).row();
        dialogueBoxAnswersTable.add().width(725).height(10).colspan(3).row();
        for (int i = 0; i < answers.length; i++) {
            answersButtons[i].clearListeners();
            answersButtons[i].setText(answers[i]);
            answersButtons[i].getLabel().setFontScale(fontManager.getScaleForLabel(answersButtons[i].getLabel(),190,90, 1));
            if (i % 3 == 0) dialogueBoxAnswersTable.row();
            dialogueBoxAnswersTable.add(answersButtons[i]).width(200).height(100).pad(20);
            final int button_id = i;
            answersButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    DialogueController.getObject().clicked(button_id);
                }
            });
        }
    }

    Table getDialogueBoxTable() {
        return dialogueBoxTable;
    }

    public static DialogueBox getObject() {
        if (object == null) object = new DialogueBox();
        return object;
    }

    @Contract(pure = true)
    public static boolean isUsed() {
        return object != null;
    }

    public void dispose() {
        dialogueBoxTable.clear();
        dialogueBoxAnswersTable.clear();
        dialogueBoxInnerTable.clear();
        textLabel.clear();
        background.clear();
        tableStack.clear();
        scrollableAnswers.clear();
        answersButtons = null;
        object = null;
    }
}
