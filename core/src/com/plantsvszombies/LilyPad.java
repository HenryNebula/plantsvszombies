package com.plantsvszombies;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class LilyPad extends BasePlants{
    public LilyPad(float x, float y, Stage s) {
        super(x, y, s, true);
        loadAnimationFromFiles(generateFilesFromDirectory("lilypad"), 1.0f, true);
        setBoundaryPolygon(4);
    }
}
