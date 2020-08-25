package com.vanilla.death.model.person.interactions;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mrkotyk on 25.07.17.
 */
public class Dialogue {

    private Array<Phrase> phrases;
    private Array<Answer> curAnswers;
    private int next_id;
    private String text;
    private Quest cur_quest = null;

    public Dialogue(Dialogue dg){
        phrases = dg.getPhrases();
        curAnswers = new Array<Answer>();
        next_id = 1;
        text = "...";
    }

    public Dialogue(Array<Phrase> phrases){
        this.phrases = phrases;
    }

    public void nextPhrase(){
        for(Phrase ph: phrases){
            if(ph.id == next_id){
                next_id = ph.next_id;
                curAnswers = ph.answers;
                text = ph.text;
                if(ph.getAnswers()==null) cur_quest = ph.getQuest();
                return;
            }
        }
    }

    public String getText(){ return text; }

    public boolean hasAnswers() {return curAnswers!=null; }

    public String[] getAnswers(){

        String[] ansText = new String[curAnswers.size];

        for(int i = 0;i<ansText.length;i++){
            ansText[i] = curAnswers.get(i).getText();
        }

        return ansText;
    }

    public void chooseAnswer(int answer_id){

        if(curAnswers==null) return;
        for(Answer ans: curAnswers){
            if(ans.getAnswerId() == answer_id){
                next_id = ans.getNextId();
                cur_quest = ans.getQuest();
                return;
            }
        }

    }

    public Array<Phrase> getPhrases(){ return phrases; }

    public int getNextId(){ return next_id; }

    public void setNextId(int next_id){ this.next_id = next_id; }

    public Quest getQuest(){ return cur_quest; }

}



