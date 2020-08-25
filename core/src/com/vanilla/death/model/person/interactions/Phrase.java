package com.vanilla.death.model.person.interactions;

import com.badlogic.gdx.utils.Array;
import org.luaj.vm2.Lua;

import java.util.ArrayList;

/**
 * Created by mrkotyk on 26.07.17.
 */
public class Phrase{
    int id, next_id;
    String text;
    Array<Answer> answers;
     private Quest quest = null;


    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getNextId() {return next_id;}

    public void setNextId(int next_id) {this.next_id = next_id;}

    public String getText() {return text;}

    public void setText(String text) {this.text = text;}

    public Array<Answer> getAnswers() {return answers;}

    public void setAnswers(Array<Answer> answers) {this.answers = answers;}

    public void setQuest(Quest quest){ this.quest = quest; }

    public Quest getQuest(){ return quest; }

}