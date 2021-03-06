package com.yellowbytestudios.spacedoctor.controllers;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public interface BasicController {

    boolean leftPressed();
    boolean rightPressed();
    boolean upPressed();
    boolean downPressed();
    boolean shootPressed();
    boolean switchGunPressed();
    boolean pausePressed();

    boolean menuUp();
    boolean menuDown();
    boolean menuSelect();
    boolean menuBack();
}
