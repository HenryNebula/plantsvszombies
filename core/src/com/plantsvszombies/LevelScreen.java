package com.plantsvszombies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.Queue;
import java.util.Random;

public class LevelScreen extends BaseScreen
{
    // TODO: 2019/1/2 The scale of the screen is fixed now, add support for relative size of all figures!
    private int MAX_SCREEN_WIDTH;
    private int MAX_SCREEN_HEIGHT;

    private int currentWidth;
    private int currentHeight;

    private float leftWidthOffset;
    private float rightWidthOffset;
    private float upperHeightOffset;

    private int colNum;
    private int rowNum;

    private float areaWidth;
    private float areaHeight;

    private Label displayLabel;
    private BaseActor labelActor;
    private boolean gameOver = false;
    private boolean noZombies = false;
    private Queue<String> zombieQueue;

    private float elapsedTime;
    private Random random = new Random();
    private String[] zombieNames = {"plain_zombie", "bucket_head_zombie", "football_zombie",
                                    "newspaper_zombie", "swimmer_zombie"};

    private ZombieScheduler zombieScheduler;
    private Music bgm;

    LevelScreen(MyGdxGame.LevelSetting levelSetting) {
        super();
        if (levelSetting != null){
            zombieScheduler = new ZombieScheduler(levelSetting);
        }
        else {
            zombieScheduler = new ZombieScheduler(MyGdxGame.LevelSetting.EASY);
        }
        zombieQueue = zombieScheduler.generateZombieQueue();
        bgm = BaseActor.getMusicFromFile("battle");
        bgm.setLooping(true);
        bgm.setVolume(0.2f);
        bgm.play();
    }

    private void modifySize() {
        MAX_SCREEN_WIDTH = 1200;
        MAX_SCREEN_HEIGHT = 600;

        currentWidth = 1200;
        currentHeight = 600;

        colNum = 9;
        rowNum = 6;

        leftWidthOffset =  260.0f * currentWidth / MAX_SCREEN_WIDTH;
        rightWidthOffset = 225.0f * currentWidth / MAX_SCREEN_WIDTH;
        upperHeightOffset = 90.0f * currentHeight / MAX_SCREEN_HEIGHT;

        areaHeight = (currentHeight - upperHeightOffset) / rowNum;
        areaWidth = (currentWidth - leftWidthOffset - rightWidthOffset) / colNum;
    }

    private void zombiePlacer() {
        String nextZombie = zombieQueue.peek();
        if (nextZombie == null) {
            noZombies = true;
            return;
        }
        float nextComingTime = Float.valueOf(nextZombie.split("@")[1]);

        if (elapsedTime < nextComingTime) return;
        zombieQueue.poll();
        float x = leftWidthOffset + (colNum + 0.5f) * areaWidth;
        int zombieIndex = Integer.valueOf(nextZombie.split("@")[0]);

        int row;
        if (zombieIndex != 5) {
            int[] rowSamples = {0,1,4,5};
            row = rowSamples[random.nextInt(4)];
        }
        else {
            int[] rowSamples = {2,3};
            row = rowSamples[random.nextInt(2)];
        }
        float y = row * areaHeight;

        BaseZombie zombie = zombieScheduler.makeZombie(zombieNames[zombieIndex - 1], mainStage);
        zombie.setPosition(x,y);
        zombie.setRow(row);
    }

    private void lockAllButtons(boolean lock, int sunLightAmount) {
        try {
            Class buttonClass = Class.forName("com.badlogic.gdx.scenes.scene2d.ui.TextButton");
            for (Cell cell : uiTable.getCells())
            {
                Actor button = cell.getActor();
                if ( buttonClass.isInstance(button) )
                    if (lock){
                        if (!((CardButton) button).getButtonRealText().contains("Pause")){
                            ((CardButton) button).setDisabled(true);
                            button.setTouchable(Touchable.disabled);
                        }
                    }

                    else {
                        int collectSunlightAmount = Integer.valueOf(displayLabel.getText().toString());
                        if (collectSunlightAmount >= ((CardButton) button).getSunlightDemand())
                            {
                                ((CardButton) button).setDisabled(false);
                                button.setTouchable(Touchable.enabled);
                            }
                    }
            }
            if (lock) {
                displayLabel.setText(String.valueOf(
                        Integer.valueOf(displayLabel.getText().toString()) - sunLightAmount) );
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable(String buttonName) {
        CardButton tableButton = new CardButton(buttonName);
        tableButton.setTransform(true);
        if (tableButton.getButtonRealText().equals("Slot")) {
            tableButton.setScaleY(0.8f);
            tableButton.setTouchable(Touchable.disabled);
            tableButton.getLabel().setAlignment(Align.top);
            uiTable.add(tableButton).top().padTop(30);
        }
        else if (tableButton.getButtonRealText().equals("Pause")) {
            tableButton.setScale(1.0f);
            tableButton.setText("");
            uiTable.add(tableButton).top().padLeft(50).padTop(50).expandX().expandY();
            tableButton.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setPaused(true);
                    addMenuWindow();
                    return false;
                }
            });
        }
        else{
            tableButton.setScale(0.75f, 0.5f);
            tableButton.getLabel().setAlignment(Align.top);
            tableButton.addListener(new InputListener(){
                int sunlightDecrease = tableButton.getSunlightDemand();
                String name = tableButton.getButtonRealText();
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    BasePlants basePlants;
                    if (name.equals("Sunflower")){
                        basePlants = new Sunflower(Float.MAX_VALUE, Float.MAX_VALUE, mainStage);
                    }
                    else if (name.equals("Snowpea")) {
                        basePlants = new SnowPea(Float.MAX_VALUE, Float.MAX_VALUE, mainStage, "snowpea");
                    }
                    else if (name.equals("Lilypad")) {
                        basePlants = new LilyPad(Float.MAX_VALUE, Float.MAX_VALUE, mainStage);
                    }
                    else if (name.equals("Wallnut")) {
                        basePlants = new Wallnut(Float.MAX_VALUE, Float.MAX_VALUE, mainStage);
                    }
                    else {
                        basePlants = new PeaShooter(Float.MAX_VALUE, Float.MAX_VALUE, mainStage);;
                    }

                    basePlants.setScale(0.8f, 0.6f);
                    basePlants.centerAtPosition(757, 562);
                    lockAllButtons(true, sunlightDecrease);
                    return false;
                }
            });
            uiTable.add(tableButton).top().padLeft(-10.0f);
        }
    }

    private void initUITable() {
        uiTable.padTop(-40);
        uiTable.add().width(500);

        addButtonToTable("Sunflower_50");
        addButtonToTable("Peashooter_100");
        addButtonToTable("Snowpea_175");
        addButtonToTable("Wallnut_50");
        addButtonToTable("Lilypad_25");
        addButtonToTable("Slot_0");
        addButtonToTable("Pause_0");
        uiTable.add().width(400);

//        uiTable.add().expandY();
    }

    public void initialize()
    {
        modifySize();
        BaseActor background = new BaseActor(0,0, mainStage);
        background.loadTexture( "core/assets/background.jpg" );
        background.setSize(currentWidth,currentHeight);
        BaseActor.setWorldBounds(background);

        for (int row=0; row < rowNum; row++){
            for (int col=0; col < colNum; col++){
                int leftCornerX = (int) (leftWidthOffset + col * areaWidth);
                int leftCornerY = (int) (row * areaHeight);
                MapArea mapArea;
                if (row == 2 || row == 3) {
                    mapArea = new MapArea(leftCornerX, leftCornerY, mainStage, true);
                    mapArea.moveBy(0,-20);
                }
                else {
                    mapArea = new MapArea(leftCornerX, leftCornerY, mainStage, false);
                }
                mapArea.setSize(areaWidth, areaHeight);
                mapArea.setBoundaryRectangle();
                mapArea.setRow(row);
                mapArea.setCol(col);
            }
        }

        initUITable();
        initSunLabel();
        lockAllButtons(false, 0);
    }

    private void initSunLabel() {
        labelActor = new BaseActor(240, 550, mainStage);
        labelActor.loadTexture("core/assets/cards/SunBack.png");
        labelActor.setScaleY(1.4f);
        labelActor.setBoundaryRectangle();

        displayLabel = new Label("50", new DisplayLabelStyle().getLabelStyle());
        displayLabel.setPosition(288, 557);
        displayLabel.setVisible(true);

        mainStage.addActor(displayLabel);
        lockAllButtons(true, 0);
        lockAllButtons(false,0);
    }

    private void killActors() {
        boolean noWaitingPlant = true;
        for (BaseActor actor: BaseActor.getList(mainStage, "BaseActor")) {
            try {
                if (Class.forName("com.plantsvszombies.BasePlants").isInstance(actor)
                        && !((BasePlants) actor).hasDropTarget()) {
                    if (actor.checkOutSideScreen()){
                        actor.addAction(Actions.after(
                                Actions.sequence(Actions.fadeOut(0.1f), Actions.removeActor())));
                    }
                    else
                        noWaitingPlant = false;
                }

                else if (Class.forName("com.plantsvszombies.Sunlight").isInstance(actor)
                        && actor.overlaps(labelActor)) {
                        int tempSunlightAmount = Integer.valueOf(displayLabel.getText().toString());
                        tempSunlightAmount += ((Sunlight) actor).getAddAmount();
                        displayLabel.setText(String.valueOf(tempSunlightAmount));
                        ((Sunlight) actor).setAddAmount(0);
                }

                else if (Class.forName("com.plantsvszombies.BaseZombie").isInstance(actor)) {
                    if (actor.getX() < 100) {
                        gameOver = true;
                        break;
                    }
                }

            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (! actor.isAlive()) {
                try {
                    if (Class.forName("com.plantsvszombies.BasePlants").isInstance(actor)){
                        MapArea mapArea = (MapArea) ((BasePlants) actor).getDropTarget();

                        if (Class.forName("com.plantsvszombies.LilyPad").isInstance(actor)){
                            mapArea.setLilypad(false);
                        }
//                        System.out.println(mapArea.getRow() + "|" + mapArea.getCol() + ":set free");
                        mapArea.setTargetable(true);
                    }

                    if (noZombies) {
                        if (Class.forName("com.plantsvszombies.BaseZombie").isInstance(actor))
                            actor.setOpacity(0.0f);
                    }
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                actor.addAction(Actions.after(
                        Actions.sequence(Actions.fadeOut(0.1f), Actions.removeActor())));

            }
        }

        if(noWaitingPlant) {
            lockAllButtons(false, 0);
        }
    }


    public void checkGameStatus() {
        if (gameOver) {
            showFinishLabels(false);
        }
        else {
            if (noZombies) {
                if(BaseActor.getAliveList(mainStage, "BaseZombie").size() == 0) {
                    showFinishLabels(true);
                }
            }
        }
    }

    private void showFinishLabels(boolean userWin) {
        BaseActor endLabel = new BaseActor(0,0, uiStage);
        Sound endSound;
        if (userWin) {
            endSound = BaseActor.getSoundFromFile("winmusic");
            endLabel.loadTexture("core/assets/YouWin.png");
        }
        else
        {
            endSound = BaseActor.getSoundFromFile("losemusic");
            endLabel.loadTexture("core/assets/ZombiesWon.png");
        }
        endSound.play(0.3f, 1.2f, 0.0f);
        setPaused(true);
        endLabel.setOpacity(0.0f);
        endLabel.centerAtPosition(600, 300);
        endLabel.addAction(Actions.fadeIn(5.0f));
        endLabel.addAction(Actions.after(Actions.fadeOut(2.0f)));
    }

    private void addMenuWindow() {
        bgm.pause();
        Skin skin = MyGdxGame.getSkin();
        Window pauseWindow = new Window("PAUSE", skin);

        Button continueButton = new TextButton("CONTINUE", skin);
        continueButton.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pauseWindow.addAction(Actions.after(Actions.removeActor()));
                bgm.play();
                if (!noZombies)
                    setPaused(false);
                return false;
            }
            });

        Button restartButton = new TextButton("RESTART", skin);
        restartButton.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                bgm.stop();
                pauseWindow.addAction(Actions.after(Actions.removeActor()));
                setPaused(false);
                MyGdxGame.setActiveScreen( new MenuScreen() );
                return false;
            }
        });

        pauseWindow.add(continueButton);
        pauseWindow.add().row();
        pauseWindow.add(restartButton);

        pauseWindow.setSize(mainStage.getWidth() / 2, mainStage.getHeight()/2);
        pauseWindow.setPosition(mainStage.getWidth() / 2, mainStage.getHeight()/2);
        pauseWindow.pack();
        pauseWindow.setVisible(true);
        uiStage.addActor(pauseWindow);
    }

    public void update(float dt)
    {
        elapsedTime += dt;
        zombiePlacer();
        killActors();
        checkGameStatus();
    }
}
