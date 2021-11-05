package net.killarexe.klauncher;

import fr.trxyy.alternative.alternative_api.*;
import fr.trxyy.alternative.alternative_api.utils.*;
import fr.trxyy.alternative.alternative_api_ui.*;
import fr.trxyy.alternative.alternative_api_ui.base.*;
import javafx.scene.*;
import javafx.stage.*;
import net.killarexe.klauncher.panel.LauncherPanel;

public class Main extends AlternativeBase {

    private LauncherPreferences prefs = new LauncherPreferences("KLauncher", 1280, 720, Mover.MOVE);
    private GameFolder folder = new GameFolder("klauncher");
    private GameLinks links = new GameLinks("https://raw.githubusercontent.com/Killarexe/KLuncher/main/klauncher/web/", "1.16.5-forge-36.2.0.json");
    private GameForge forge = new GameForge(Forge.DEFAULT, "1.16.5", "1.16.5-36.2.0", "1.16.5-20210115.111550");
    private GameEngine engine = new GameEngine(folder, links, prefs, GameStyle.FORGE_1_13_HIGHER, forge);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        LauncherBase base = new LauncherBase(stage, scene, StageStyle.TRANSPARENT, engine);
        base.setIconImage(stage, "logo.png");
    }

    private Parent createContent(){
        LauncherPane pane = new LauncherPane(engine);
        new LauncherBackground(engine, "background.png", pane);
        new LauncherPanel(engine, pane);
        return pane;
    }
}
