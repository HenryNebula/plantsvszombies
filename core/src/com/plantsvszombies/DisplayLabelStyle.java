package com.plantsvszombies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DisplayLabelStyle {
    private Label.LabelStyle labelStyle;

    DisplayLabelStyle() {
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new CustomFontGenerator(15, Color.WHITE).getCustomFont();
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }
}
