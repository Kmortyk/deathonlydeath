package com.vanilla.death.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.model.person.interactions.*;
import com.vanilla.death.view.GraphicRepresentation;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mrkotyk on 02.07.17.
 */
public class JsonParser {

    private Json json;

//    public <TObject> TObject GetObjectJS(String name){
//
//        TObject tObject = null;
//        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal("data/"+tObject.getClass().getName()+".txt").reader());
//
//        String jsonString = bufferReader.toString();
//        tObject = (TObject) json.fromJson(tObject.getClass(),jsonString);
//
//        return tObject;
//
//    }

    public JsonParser(){
        json = new Json();
    }


//    public <TObject> ArrayList<TObject> GetGroundJS(String path){
//
//        ArrayList<TObject> objectList;
//
//
//        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal(path).reader());
//
//        String jsonString = bufferReader.toString();
//        objectList = json.fromJson(ArrayList.class,jsonString);
//
//
//        return objectList;
//
//    }


    public void createGraphicLists(){
        GraphicLists.getObject();
        json.setSerializer(GraphicLists.class, new Json.Serializer<GraphicLists>() {
                    public void write(Json json, GraphicLists graphicLists, Class knownType) {
                        json.writeObjectStart();
                        json.writeObjectEnd();
                    }

                    public GraphicLists read(Json json, JsonValue jsonData, Class type) {
                        GraphicLists.getObject().setCommonSpriteNames(jsonData.get("spriteName").asStringArray());
                        GraphicLists.getObject().setCommonFramerates(jsonData.get("animationFrame").asFloatArray());
                        GraphicLists.getObject().setCommonAnimationNames(jsonData.get("animationName").asStringArray());
                        return GraphicLists.getObject();
                    }
                });

        json.fromJson(GraphicLists.class, getJsonString("data/objects/Animations.json"));
    }



    public Array<Ground> GetGroundData(){


        //System.out.println(json.prettyPrint(jsonString));

        Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class,getJsonString("data/objects/Ground.json"));

        //Array<JsonValue> values = json.fromJson(Array.class,Gdx.files.internal("data/objects/Ground.json"));

        json.setSerializer(Ground.class, new Json.Serializer<Ground>()  {
            public void write (Json json, Ground ground, Class knownType) {
                json.writeObjectStart();
                //json.writeValue(number.name, number.number);
                json.writeObjectEnd();
            }

            public Ground read (Json json, JsonValue jsonData, Class type) {
                Ground ground = new Ground(jsonData.getString("name"));

                ground.setBOTTOMNAME(jsonData.getString("bottomname"));
                ground.setStepable(jsonData.getBoolean("stepable"));

                ground.setGroundTexture(new Texture(jsonData.getString("texture")));

                //ground.setStepSound(Gdx.files.internal(jsonData.getString("step_sound")));

                //number.setName();
                //number.setNumber(jsonData.child().asString());
                //return number;
                return ground;
            }
        });

        Array<Ground> allGround = new Array<Ground>();
        //allGround.setSize(values.size);
        //System.out.println("GROUNDED======================AZAZAZAZAZAZZZ"+values.size);

        for(JsonValue v: values){

            allGround.add(json.readValue(Ground.class, v));
        }

        //Array<Ground> allGround = json.fromJson(Array.class,Ground.class,jsonString);

        return allGround;

    }


    public Array<Mob> GetMobData(){
        Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class,getJsonString("data/objects/Mob.json"));

        json.setSerializer(Mob.class, new Json.Serializer<Mob>()  {
            public void write (Json json, Mob mob, Class knownType) {}

            public Mob read (Json json, JsonValue jsonData, Class type) {
                Mob mob = new Mob(jsonData.getString("name"));
                mob.setHP(jsonData.getInt("hp"));
                mob.setSHIELD(jsonData.getInt("shield"));
                mob.setATTACK(jsonData.getInt("attack"));
                mob.setATTACKDISTANCE(jsonData.getInt("attack_distance"));
                mob.setSPEED(jsonData.getInt("speed"));
                mob.STATE = Mob.State.valueOf(jsonData.getString("state"));
                //System.out.println(mob.STATE);
                mob.setTalking(jsonData.getBoolean("talking"));

                if(mob.isTalking()) {
                    Array<JsonValue> phrase_values = (Array<JsonValue>) json.fromJson(Array.class,getJsonString(jsonData.getString("dialogue_path")));
                    json.setSerializer(Answer.class, new Json.Serializer<Answer>() {
                        @Override
                        public void write(Json json, Answer object, Class knownType) {}

                        @Override
                        public Answer read(Json json, JsonValue jsonData, Class type) {
                            Answer answer = new Answer();
                            answer.setAnswerId(jsonData.getInt("id"));
                            answer.setNextId(jsonData.getInt("next_id"));
                            answer.setText(jsonData.getString("text"));
                            if(jsonData.has("quest_name") && jsonData.has("quest_path")) {
                                answer.setQuest(getQuest(jsonData.getString("quest_name"), jsonData.getString("quest_path")));
                            }
                            return answer;
                        }
                    });


                    json.setSerializer(Phrase.class, new Json.Serializer<Phrase>() {

                        @Override
                        public void write(Json json, Phrase object, Class knownType) {

                        }

                        @Override
                        public Phrase read(Json json, JsonValue jsonData, Class type) {
                            Phrase phrase = new Phrase();
                            phrase.setId(jsonData.getInt("id"));
                            phrase.setText(jsonData.getString("text"));
                            if (jsonData.getBoolean("isChoice")) {


                                JsonValue answer_values = jsonData.get("answers");
                                //Array<JsonValue> answer_values = (Array<JsonValue>) json.fromJson(Array.class, value.toString());

                                Array<Answer> answers = new Array<Answer>();

                                for(JsonValue v: answer_values){
                                    answers.add(json.readValue(Answer.class,v));
                                }
                                phrase.setAnswers(answers);

                            }else{
                                phrase.setNextId(jsonData.getInt("next_id"));
                                if(jsonData.has("quest_name") && jsonData.has("quest_path")) {
                                    phrase.setQuest(getQuest(jsonData.getString("quest_name"), jsonData.getString("quest_path")));
                                }
                            }
                            return phrase;
                        }
                    });

                    Array<Phrase> phrases = new Array<Phrase>();
                    for(JsonValue v: phrase_values){
                        phrases.add(json.readValue(Phrase.class, v));
                    }

                    Dialogue dialogue = new Dialogue(phrases);
                    mob.setDialogue(dialogue);
                }

                return mob;
            }
        });

        Array<Mob> allMob = new Array<Mob>();

        for(JsonValue v: values){

            allMob.add(json.readValue(Mob.class, v));
        }
        return allMob;
    }


    public Array<Item> GetItemData(){

        Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class,getJsonString("data/objects/Item.json"));

        json.setSerializer(Item.class, new Json.Serializer<Item>()  {
            public void write (Json json, Item item, Class knownType) {
                json.writeObjectStart();
                json.writeObjectEnd();
            }

            public Item read (Json json, JsonValue jsonData, Class type){
                Item item = new Item(jsonData.getString("name"));

                item.setHP(jsonData.getInt("hp"));
                item.setSHIELD(jsonData.getInt("shield"));
                item.setATTACK(jsonData.getInt("attack"));
                item.setSPEED(jsonData.getInt("speed"));
                item.setUSABLE(jsonData.getBoolean("usable"));
                item.setScriptNames(jsonData.get("script_names").asStringArray());
                //item.setType();


                return item;
            }
        });

        Array<Item> allItems = new Array<Item>();
        for(JsonValue v: values) {

            allItems.add(json.readValue(Item.class, v));
        }
        return allItems;

    }

    public Array<Hero> GetHeroData() {
        Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class, getJsonString("data/objects/Hero.json"));

        json.setSerializer(Hero.class, new Json.Serializer<Hero>() {
            public void write(Json json, Hero hero, Class knownType) {
                json.writeObjectStart();
                json.writeObjectEnd();
            }

            public Hero read(Json json, JsonValue jsonData, Class type) {
                Hero hero = new Hero(jsonData.getString("name"));
                hero.setHP(jsonData.getInt("hp"));
                hero.setSHIELD(jsonData.getInt("shield"));
                hero.setATTACK(jsonData.getInt("attack"));
                hero.setATTACKDISTANCE(jsonData.getInt("attack_distance"));
                hero.setSPEED(jsonData.getInt("speed"));
                GraphicLists.getObject().getPath().put(hero, jsonData.getString("gr_path"));
                return hero;
            }
        });

        Array<Hero> allHeroes = new Array<Hero>();
        for (JsonValue v : values) {
            allHeroes.add(json.readValue(Hero.class, v));
        }
        return allHeroes;
    }

    public void getScript(String mapName){
        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal("data/maps"+ mapName+"S.txt").reader());
        String line;
        String[] string_data;

        try {
            while((line = bufferReader.readLine())!=null) {

                string_data = line.split(" ");
                int i = Integer.parseInt(string_data[0]);
                int j = Integer.parseInt(string_data[1]);
                String name = string_data[3];
                Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class, getJsonString(string_data[2]));
                for (JsonValue v : values) {
                    if(v.getString("name").equals(name)){
                        GameScript script = new GameScript(name, i, j);
                        script.setType(v.getInt("type"));

//                        if(v.has("quest_name") && v.has("quest_path")) {
//                            script.setQuest(getQuest(v.getString("quest_name"), v.getString("quest_path")));
//                        }

                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Quest getQuest(String name, String path){
        Array<JsonValue> values = (Array<JsonValue>) json.fromJson(Array.class, getJsonString(path));
        for (JsonValue v : values) {
            if(v.getString("quest_name").equals(name)){
                Quest quest = new Quest(name);
                quest.setType(v.getInt("type"));
                quest.setMapName(v.getString("map_name"));
                quest.setI(v.getInt("i"));
                quest.setJ(v.getInt("j"));
                return  quest;
            }
        }
        return null;
    }

    private String getJsonString(String path){
        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal(path).reader());

        String string = "";

        try {
            for (String line; (line = bufferReader.readLine()) != null; string += line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }
    //
}
