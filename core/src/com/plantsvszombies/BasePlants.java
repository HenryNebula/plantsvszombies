package com.plantsvszombies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class BasePlants extends DragAndDropActor{
    private MapArea mapArea;
    private boolean onWater;
    protected Sound plantSound;
    protected Sound errorPlantSound;

    public BasePlants(float x, float y, Stage s, boolean onWater) {
        super(x, y, s);
        this.onWater = onWater;
        errorPlantSound = getSoundFromFile("error_plant");
    }

    protected String[] generateFilesFromDirectory(String directory) {
        return super.generateFilesFromDirectory(directory, "core/assets/plants/%s/");
    }

    public void setMapArea(MapArea mapArea) {
        this.mapArea = mapArea;
    }

    public MapArea getMapArea()
    {  return mapArea;  }

    public void clearMapArea()
    {  mapArea = null;  }

    public boolean hasMapArea()
    {  return mapArea != null;  }

    // override method from DragAndDropActor class
    public void onDragStart()
    {
        if ( hasMapArea() )
        {
            MapArea mapArea = getMapArea();
            mapArea.setTargetable(true);
            clearMapArea();
        }
    }

    // override method from DragAndDropActor class
    public void onDrop()
    {
        if ( hasDropTarget() )
        {
            MapArea mapArea = (MapArea)getDropTarget();
            moveToActor(mapArea);
            setMapArea(mapArea);
            if (! onWater) {
                mapArea.setTargetable(false);
                {
                    if (mapArea.hasLilyPad()) {
                        moveToActor(new BaseActor(mapArea.getCenterX() + 10f,
                                mapArea.getCenterY() + 25f, getStage()));
                    }
                }
            }
            else mapArea.setLilypad(true);
            setCol(mapArea.getCol());
            setRow(mapArea.getRow());
            setDraggable(false);
        }

    }

    @Override
    public void findDropTarget(DragAndDropActor self) {
        // keep track of distance to closest object
        float closestDistance = Float.MAX_VALUE;
        MapArea target = null;
        for ( BaseActor actor : BaseActor.getList(self.getStage(), "MapArea") )
        {
            if ( self.overlaps(actor) )
            {
                float currentDistance =
                        Vector2.dst(self.getCenterX(),self.getCenterY(),
                                actor.getCenterX(),actor.getCenterY());

                // check if this target is even closer
                if (currentDistance < closestDistance)
                {
                    target = (MapArea) actor;
                    closestDistance = currentDistance;
                }
            }
        }
        if (target != null){
            if (target.isTargetable() && (onWater == target.checkWaterArea() || (target.hasLilyPad() && !onWater) )) {
//                System.out.println(target.getRow() + "|" + target.getCol());
                target.showSuccessTexture();
                self.setDropTarget(target);
                // return object to original size when dropped by player
                self.addAction( Actions.scaleTo(1.00f, 1.00f, 0.25f) );
            }
            else {
                errorPlantSound.play(0.3f);
                target.showFailureTexture();
                moveToSlot();
                self.addAction( Actions.scaleTo(0.8f, 0.6f, 0.25f) );
            }
        }
        else {
            moveToSlot();
            self.addAction( Actions.scaleTo(0.8f, 0.6f, 0.25f) );
        }
    }

    public void hitByZombie (float damage) {
        blood -= damage;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("(Blood: %f, OnWater: %s)", blood, onWater);
    }

    @Override
    public void setDropTarget(DropTargetActor a) {
        super.setDropTarget(a);

        if (onWater) {
            plantSound = getSoundFromFile("plant_in_water");
        }
        else plantSound = getSoundFromFile("plant");

        plantSound.play(0.5f);
    }

    @Override
    public boolean isAlive() {
        return (!hasMapArea() && super.isAlive()) ||
                (super.isAlive() && hasMapArea() && getMapArea().checkWaterArea() && getMapArea().hasLilyPad())||
                (super.isAlive() && hasMapArea() && !(getMapArea().checkWaterArea()));
    }

}
