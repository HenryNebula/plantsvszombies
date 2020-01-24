package com.plantsvszombies;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.*;

public class ZombieScheduler {
    private MyGdxGame.LevelSetting levelSetting;
    private float shortInterval = 3.0f;
    private float longInterval = 5.0f;
    private float zombieInterval = 2.0f;

    
    ZombieScheduler(MyGdxGame.LevelSetting levelSetting) {
        this.levelSetting = levelSetting;
    }
    
    public Queue<String> generateZombieQueue() {
        Queue<String> zombieQueue = new LinkedList<>();
        String rawSequence="";
        switch (levelSetting) {
            case EASY:
                rawSequence = "0,0,1,1\n" +
                          "1,1,2\n" +
                          "1,2,5\n" +
                          "1,2,2,5,5\n" +
                          "2,3,4,5,5,5";

                // for debug only
//                rawSequence = "0,0,0\n" +
//                        "3*1\n" +
//                        "3*1\n" +
//                        "3*1\n";
                break;
            case MIDDLE:
                rawSequence = "0,0,0,1,1\n" +
                        "1,1,5\n" +
                        "1,2,2,5,5\n" +
                        "2*2,3*2,4*1,5*5\n" +
                        "1*4,2*4,3*2,4*2,5*8\n" +
                        "1*4,2*6,3*4,4*4,5*10";
                break;
            case HARD:
                rawSequence = "0,0,0,1,1\n" +
                        "1*3,5*2\n" +
                        "2*2,3*1,4*1,5*3\n" +
                        "1*3,2*3,3*3,4*3,5*8\n" +
                        "1*4,2*5,3*4,4*5,5*12\n" +
                        "1*6,2*6,3*5,5*18,4*6";
                break;
        }

        String[] lineArray = rawSequence.split("\n");
        float[] intervalTimeArray = {0,0,0,0,0,0};
        int lineCount = -1;

        // 15 seconds for preparation
        float elapsedTime = 15.0f;
        for (String line: lineArray) {
            lineCount += 1;
            if (lineCount == 0) {
                String[] intervalStringArray = line.split(",");
                intervalTimeArray = new float[intervalStringArray.length];
                for (int i = 0; i < intervalStringArray.length; i++) {
                    intervalTimeArray[i] = intervalStringArray[i].equals("0") ? shortInterval : longInterval;
                }
            }
            else {
                elapsedTime += intervalTimeArray[(lineCount - 1)];
                LinkedList<String> singleLayerZombies = new LinkedList<>();
                String[] zombieIndices = line.split(",");
                for(String zombieIndex: zombieIndices) {
                    if (zombieIndex.contains("*")){
                        String[] zombieWithCount = zombieIndex.split("\\*");
                        for (int i=0; i < Integer.valueOf(zombieWithCount[1]); i++){
                            singleLayerZombies.add(zombieWithCount[0]);
                        }
                    }
                    else singleLayerZombies.add(zombieIndex);
                }
                Collections.shuffle(singleLayerZombies);

                for (String zombieIndex: singleLayerZombies) {
                    zombieQueue.offer(String.format("%s@%.2f", zombieIndex, elapsedTime));
                    elapsedTime += zombieInterval;
                }
            }
        }

        return zombieQueue;
    }

    public BaseZombie makeZombie (String zombieName, Stage stage) {
        BaseZombie zombie;
        if (zombieName.equals("swimmer_zombie")) {
            zombie = new SwimmerZombie(Float.MAX_VALUE,Float.MAX_VALUE, stage);
            zombie.setDamage(20.0f);
            zombie.setZombieSpeed(20.0f);
        }
        else if (zombieName.equals("bucket_head_zombie")) {
            zombie = new PlainZombie(Float.MAX_VALUE,Float.MAX_VALUE, stage, zombieName,
                    20.0f, 0.1f, 0.75f);
            zombie.setDamage(20.0f);
        }
        else if (zombieName.equals("newspaper_zombie")) {
            zombie = new PlainZombie(Float.MAX_VALUE,Float.MAX_VALUE, stage, zombieName,
                    30, 0.1f, 0.75f);
            zombie.setDamage(50);
        }
        else if (zombieName.equals("football_zombie")) {
            zombie = new PlainZombie(Float.MAX_VALUE,Float.MAX_VALUE, stage, zombieName, 50,
            0.1f, 0.7f);
            zombie.setDamage(10);
        }
        else {
            zombie = new PlainZombie(Float.MAX_VALUE,Float.MAX_VALUE, stage);
            zombie.setZombieSpeed(20.0f);
        }

        return zombie;
    }
}
