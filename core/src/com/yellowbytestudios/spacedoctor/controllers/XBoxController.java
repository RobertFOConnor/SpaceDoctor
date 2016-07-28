package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class XBoxController implements BasicController {

    private Controller controller;
    private boolean rightTriggerJustPressed, switchGunJustPressed, pauseJustPressed = false;
    private boolean menuUp, menuDown, menuSelected, menuBack = false;

    public XBoxController(int controllerNumber) {
        controller = Controllers.getControllers().get(controllerNumber);
    }

    @Override
    public boolean leftPressed() {
        return controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f;
    }

    @Override
    public boolean rightPressed() {
        return controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f;
    }

    @Override
    public boolean upPressed() {
        return controller.getButton(XBox360Pad.BUTTON_A);
    }

    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean shootPressed() {
        if (controller.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER) < -0.5f) {
            System.out.println("bang");
            if (!rightTriggerJustPressed) {
                rightTriggerJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            rightTriggerJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean pausePressed() {
        if (controller.getButton(XBox360Pad.BUTTON_START)) {
            if (!pauseJustPressed) {
                pauseJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            pauseJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean switchGunPressed() {
        if (controller.getButton(XBox360Pad.BUTTON_RB)) {
            if (!switchGunJustPressed) {
                switchGunJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            switchGunJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean menuUp() {
        if (controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -0.5f) {
            if (!menuUp) {
                menuUp = true;
                return true;
            } else {
                return false;
            }
        } else {
            menuUp = false;
            return false;
        }
    }

    @Override
    public boolean menuDown() {
        if (controller.getAxis(XBox360Pad.AXIS_LEFT_Y) > 0.5f) {
            if (!menuDown) {
                menuDown = true;
                return true;
            } else {
                return false;
            }
        } else {
            menuDown = false;
            return false;
        }
    }

    @Override
    public boolean menuSelect() {

        if (controller.getButton(XBox360Pad.BUTTON_A)) {
            if (!menuSelected) {
                menuSelected = true;
                return true;
            } else {
                return false;
            }
        } else {
            menuSelected = false;
            return false;
        }
    }

    @Override
    public boolean menuBack() {
        if (controller.getButton(XBox360Pad.BUTTON_B)) {
            if (!menuBack) {
                menuBack = true;
                return true;
            } else {
                return false;
            }
        } else {
            menuBack = false;
            return false;
        }
    }
}
