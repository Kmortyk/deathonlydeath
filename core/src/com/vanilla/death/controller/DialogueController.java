package com.vanilla.death.controller;

import com.vanilla.death.model.Hex;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.model.person.interactions.Dialogue;
import com.vanilla.death.view.InterfacePackage.InterfaceManager;

/**
 * Created by mrkotyk on 08.08.17.
 */
public class DialogueController {

    private Hero hero;
    private Mob mob;
    private Dialogue dialogue;

    private static DialogueController object;

    public static DialogueController getObject(){
        if (object == null) {
            object = new DialogueController();
        }
        return object;
    }

    public void createDialogue(Hero hero, Mob mob){
        this.hero = hero;
        this.mob = mob;
        dialogue = mob.getDialogue();

        hero.STATE = Hero.State.DIALOGUE;
        mob.STATE = Mob.State.DIALOGUE;
        InterfaceManager.getObject().createDialogueBox();
        updateDialogue();
    }

    public void updateDialogue(){
        dialogue.nextPhrase();
        if(dialogue.hasAnswers()) InterfaceManager.getObject().updateDialogueBox(dialogue.getText(), dialogue.getAnswers());
        else InterfaceManager.getObject().updateDialogueBox(dialogue.getText());
    }

    public void clicked(int button_id){

            dialogue.chooseAnswer(button_id);

            if(dialogue.getQuest()!=null){
                hero.addQuest(dialogue.getQuest());
            }

            if(isLastPhrase()){
                stopDialogue();
                return;
            }
            updateDialogue();
    }

    public void clicked(){
        if(dialogue.getQuest()!=null) hero.addQuest(dialogue.getQuest());
        if(isLastPhrase()) stopDialogue();
        else  updateDialogue();
    }


    public void stopDialogue(){
        hero.STATE = Hero.State.NONE;
        mob.STATE = Mob.State.NONE;
        dialogue.setNextId(dialogue.getNextId()*(-1));
        InterfaceManager.getObject().stopDialogueBox();
    }

    public boolean isLastPhrase(){ return dialogue.getNextId() < 0; }

    public boolean isClickForExit(){ return isLastPhrase() && !dialogue.hasAnswers();}

    public void dispose(){
        hero = null;
        mob = null;
        dialogue = null;
        object = null;
    }

}
