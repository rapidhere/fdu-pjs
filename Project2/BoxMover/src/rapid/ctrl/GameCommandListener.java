package rapid.ctrl;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : GameCommandListener
 * Version : 0.1
 * Usage : The listener of game events
 */
public interface GameCommandListener {
    public void onExit(GameCommandEvent e);
    public void onNewUser(GameCommandEvent e);
    public void onOldUser(GameCommandEvent e);

    public void onKeyUp(GameCommandEvent e);
    public void onKeyDown(GameCommandEvent e);
    public void onKeyLeft(GameCommandEvent e);
    public void onKeyRight(GameCommandEvent e);
    public void onBackStep(GameCommandEvent e);

    public void onLevelVictory(GameCommandEvent e);
    public void onLevelFailed(GameCommandEvent e);
    public void onBackToLevel(GameCommandEvent e);

    public void onShowAbout(GameCommandEvent e);
    public void onShowHelp(GameCommandEvent e);
    public void onShowRank(GameCommandEvent e);

    public void onChooseLevel(GameCommandEvent e);
    public void onRestartLevel(GameCommandEvent e);
    public void onSaveLevel(GameCommandEvent e);
}
