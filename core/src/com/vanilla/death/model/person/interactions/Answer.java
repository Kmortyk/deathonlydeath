package com.vanilla.death.model.person.interactions;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mrkotyk on 26.07.17.
 */
public class Answer{
    private int answer_id, next_id;
    private String text;
    private Quest quest = null;

    public int getAnswerId() {return answer_id;}

    public void setAnswerId(int answer_id) {this.answer_id = answer_id;}

    public int getNextId() {return next_id;}

    public void setNextId(int next_id) {this.next_id = next_id;}

    public String getText() {return text;}

    public void setText(String text) {this.text = text;}

    public Quest getQuest(){ return quest; }

    public void setQuest(Quest quest){ this.quest = quest; }
}