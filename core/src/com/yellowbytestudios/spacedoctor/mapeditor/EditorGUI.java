package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.game.Button;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;

import java.awt.Menu;

public class EditorGUI {

    private OrthoCamera camera;
    private Vector2 touch;
    private MapManager mapManager;
    private Button zoomIn, zoomOut, moveButton, eraseButton, playMap, saveMap, exitButton;

    private Texture tileButtonSelector;
    private NinePatch bottom_bg;
    private int tileID = -1;
    private int enemyID = -1;
    private int itemID = -1;

    private ItemSideMenu sideMenu;

    public EditorGUI(MapManager mapManager) {
        this.mapManager = mapManager;
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        zoomIn = new Button(MapEditorAssets.ZOOM_IN, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 170), true);
        zoomOut = new Button(MapEditorAssets.ZOOM_OUT, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 285), true);
        moveButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON_SEL, Texture.class), new Vector2(20, 20));
        eraseButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.ERASE, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.ERASE_SEL, Texture.class), new Vector2(180, 20));
        playMap = new Button(MapEditorAssets.PLAY_MAP, new Vector2(MainGame.WIDTH - 280 - 140, 20), true);
        saveMap = new Button(MapEditorAssets.SAVE_MAP, new Vector2(MainGame.WIDTH - 280, 20), true);
        exitButton = new Button(MapEditorAssets.EXIT_EDITOR, new Vector2(MainGame.WIDTH - 140, 20), true);

        tileButtonSelector = MapEditorAssets.manager.get(MapEditorAssets.TILE_SELECTOR, Texture.class);

        bottom_bg = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.BOTTOM_BAR, Texture.class), 10, 10, 10, 10);


        sideMenu = new ItemSideMenu();
    }

    public void update(float step) {
        if (Gdx.input.isTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (zoomIn.checkTouch(touch)) { //ZOOM IN
                mapManager.zoomIn();

            } else if (zoomOut.checkTouch(touch)) { //ZOOM OUT
                mapManager.zoomOut();

            } else if (playMap.checkTouch(touch)) { //TEST PLAY MAP
                ScreenManager.setScreen(new GameScreen(mapManager.getMap()));

            } else if (saveMap.checkTouch(touch) && Gdx.input.justTouched()) { //SAVE MAP
                saveMap();

            } else if (exitButton.checkTouch(touch)) { //EXIT EDITOR
                ScreenManager.setScreen(new MainMenuScreen());
                MapEditorAssets.dispose();

            } else if (!moveButton.checkTouch(touch) && (!sideMenu.checkTouch() && touch.y > 160)) {
                //CHECK MAP FOR INTERACTION.

                if (eraseButton.isPressed()) {
                    mapManager.eraseTiles();
                }

                if (!mapManager.isHoldingObject()) {
                    if (moveButton.isPressed()) {
                        mapManager.dragMap();
                    } else if (!eraseButton.isPressed()) {
                        if(sideMenu.state.equals(sideMenu.BLOCK_STATE)) {
                            mapManager.checkForTilePlacement(tileID);
                        } else if(sideMenu.state.equals(sideMenu.ENEMY_STATE)) {
                            mapManager.addEnemy();
                        }  else if(sideMenu.state.equals(sideMenu.ITEM_STATE)) {
                            mapManager.addItem("COIN");
                        }
                    }
                }
            }
        }


        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (moveButton.checkTouch(touch)) {
                if (moveButton.isPressed()) {
                    moveButton.setPressed(false);
                    sideMenu.setShowing(true);
                } else {
                    moveButton.setPressed(true);
                    eraseButton.setPressed(false);
                    sideMenu.setShowing(false);
                }
            }

            if (eraseButton.checkTouch(touch)) {
                if (eraseButton.isPressed()) {
                    eraseButton.setPressed(false);
                } else {
                    eraseButton.setPressed(true);
                    moveButton.setPressed(false);
                    sideMenu.setShowing(true);
                }
            }
        }
    }

    private void saveMap() {
        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Save Map", "", "Enter Map Name");
    }

    public void render(SpriteBatch sb) {
        zoomIn.render(sb);
        zoomOut.render(sb);

        bottom_bg.draw(sb, 0, 0, MainGame.WIDTH, 160);
        sideMenu.render(sb);
        moveButton.render(sb);
        eraseButton.render(sb);
        playMap.render(sb);
        saveMap.render(sb);
        exitButton.render(sb);
    }


    private class MenuButton extends SpriteButton {

        private TextureRegion texture;
        private int id;
        private boolean selected = false;

        public MenuButton(TextureRegion texture, Vector2 pos, int id) {
            super(texture, pos);
            this.texture = texture;
            this.id = id;
        }

        public void render(SpriteBatch sb) {
            if (selected) {
                sb.draw(tileButtonSelector, getX() - 10, getY() - 10);
            }
            sb.draw(texture, getX(), getY());
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }
    }

    private class ItemSideMenu {

        //Menu States.
        private final String BLOCK_STATE = "BLOCK_STATE";
        private final String ENEMY_STATE = "ENEMY_STATE";
        private final String ITEM_STATE = "ITEM_STATE";

        private String state = BLOCK_STATE;

        private boolean showing = true;

        private Button blockTab, enemyTab, itemTab;

        private Array<MenuButton> tileButtons, enemyButtons, itemButtons;
        private NinePatch sideMenuBG = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.SIDE_MENU, Texture.class), 10, 10, 10, 10);

        private float bottomY = 290;

        //Side Menu Constructor.
        public ItemSideMenu() {

            blockTab = new Button(MapEditorAssets.BLOCK_TAB, new Vector2(270, 830), true);
            enemyTab = new Button(MapEditorAssets.ENEMY_TAB, new Vector2(270, 710), true);
            itemTab = new Button(MapEditorAssets.ITEM_TAB, new Vector2(270, 590), true);


            tileButtons = new Array<MenuButton>();

            Texture sheet = MapEditorAssets.manager.get(MapEditorAssets.TILESHEET, Texture.class);
            addMenuButton(tileButtons, sheet, 0, 0, TileIDs.LIGHT_PURPLE);
            addMenuButton(tileButtons, sheet, 200, 100, TileIDs.DARK_PURPLE);
            addMenuButton(tileButtons, sheet, 100, 0, TileIDs.CAGED_WALL);
            addMenuButton(tileButtons, sheet, 200, 0, TileIDs.DOWN_SPIKE);
            addMenuButton(tileButtons, sheet, 300, 200, TileIDs.UP_SPIKE);
            addMenuButton(tileButtons, sheet, 300, 100, TileIDs.RIGHT_SPIKE);
            addMenuButton(tileButtons, sheet, 400, 100, TileIDs.LEFT_SPIKE);

            tileButtons.get(0).selected = true;
            tileID = tileButtons.get(0).id;

            enemyButtons = new Array<MenuButton>();
            enemyButtons.add(new MenuButton(new TextureRegion(MapEditorAssets.manager.get(MapEditorAssets.ENEMY_ICON, Texture.class)), new Vector2(30, bottomY), 0));
            enemyButtons.get(0).selected = true;
            enemyID = enemyButtons.get(0).id;

            //Setup Item menu.
            sheet = MapEditorAssets.manager.get(MapEditorAssets.ITEM_SHEET, Texture.class);
            bottomY = 290;
            itemButtons = new Array<MenuButton>();
            addMenuButton(itemButtons, sheet, 0, 0, 0);
            addMenuButton(itemButtons, sheet, 100, 0, 1);
            addMenuButton(itemButtons, sheet, 200, 0, 2);
            addMenuButton(itemButtons, sheet, 300, 0, 3);

            itemButtons.get(0).selected = true;
            itemID = itemButtons.get(0).id;

        }

        public boolean checkTouch() {

            if (showing) {

                if (blockTab.checkTouch(touch)) {
                    state = BLOCK_STATE;
                    eraseButton.setPressed(false);
                } else if (enemyTab.checkTouch(touch)) {
                    state = ENEMY_STATE;
                    eraseButton.setPressed(false);
                } else if (itemTab.checkTouch(touch)) {
                    state = ITEM_STATE;
                    eraseButton.setPressed(false);
                }


                if (state.equals(BLOCK_STATE)) {
                    int id = getButtonSelectedId(tileButtons);
                    if(id != -1) {
                        tileID = id;
                        return true;
                    }
                } else if (state.equals(ENEMY_STATE)) {
                    int id = getButtonSelectedId(enemyButtons);
                    if(id != -1) {
                        enemyID = id;
                        return true;
                    }
                } else if (state.equals(ITEM_STATE)) {

                    int id = getButtonSelectedId(itemButtons);
                    if(id != -1) {
                        itemID = id;
                        return true;
                    }
                }
                return touch.x < 360;
            }
            return false;
        }

        private int getButtonSelectedId(Array<MenuButton> buttonArray) {
            for (MenuButton button : buttonArray) {
                if (button.checkTouch(touch)) {

                    for (MenuButton otherButtons : buttonArray) {
                        otherButtons.selected = false;
                    }
                    eraseButton.setPressed(false);
                    button.selected = true;
                    return button.id;
                }
            }
            return -1;
        }

        public void render(SpriteBatch sb) {
            if (showing) {

                sideMenuBG.draw(sb, 10, 200, 260, 760);
                blockTab.render(sb);
                enemyTab.render(sb);
                itemTab.render(sb);


                if (state.equals(BLOCK_STATE)) {
                    for (MenuButton tb : tileButtons) {
                        tb.render(sb);
                    }
                } else if (state.equals(ENEMY_STATE)) {

                    //DRAW ENEMIES
                    for (MenuButton eb : enemyButtons) {
                        eb.render(sb);
                    }

                } else if (state.equals(ITEM_STATE)) {

                    //DRAW ITEMS
                    for (MenuButton ib : itemButtons) {
                        ib.render(sb);
                    }
                }
            }
        }

        public void setShowing(boolean showing) {
            this.showing = showing;
        }

        private void addMenuButton(Array<MenuButton> array, Texture sheet, int sheetX, int sheetY, int tileID) {

            if (array.size % 2 == 0 && array.size != 0) {
                bottomY += 120;
            }

            array.add(new MenuButton(new TextureRegion(sheet, sheetX, sheetY, (int) Box2DVars.PPM, (int) Box2DVars.PPM), new Vector2(30 + (120 * (array.size % 2)), bottomY), tileID));
        }
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {

            if(MainGame.saveData.getMyMaps().size < 12) {
                MainGame.saveData.getMyMaps().add(new CustomMap(text, mapManager.getMap()));
            } else {
                MainGame.saveData.getMyMaps().set(11, new CustomMap(text, mapManager.getMap()));
            }
            MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

            //MAP SAVED MESSAGE DISPLAY HERE!!!!
        }

        @Override
        public void canceled () {
        }
    }
}
