package net.killarexe.klauncher.panel;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.*;
import fr.trxyy.alternative.alternative_auth.account.AccountType;
import fr.trxyy.alternative.alternative_auth.base.GameAuth;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.text.DecimalFormat;

public class LauncherPanel extends IScreen {

    private LauncherRectangle rectangle, updateRectangle;
    private LauncherLabel label, updateLabel, currentFileLabel, percentageLabel, currentStep;
    private LauncherImage logo;
    private LauncherTextField usernameField;
    private LauncherPasswordField passwordField;
    private LauncherButton loginButton, settingsButton, reduceButton, exitButton, discordButton, curseforgeButton, youtubeButton;
    private LauncherProgressBar bar;

    private Timeline line;
    private DecimalFormat format = new DecimalFormat(".#");
    private Thread updateThread;
    private GameUpdater updater = new GameUpdater();

    private GameEngine engine;
    private Pane pane;

    public LauncherPanel(GameEngine engine, Pane pane){
        this.engine = engine;
        this.pane = pane;
        setup();
    }

    public void setup(){
        rectangle = new LauncherRectangle(pane, 0, 0, engine.getWidth(), 31);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.7));

        label = new LauncherLabel(pane);
        label.setText("KLauncher");
        label.setFont(new Font("Arial", 18));
        label.setStyle("-fx-background-color: transparant; -fx-text-fill: white;");
        label.setPosition(engine.getWidth()/2 -50, -4);
        label.setOpacity(0.7);
        label.setSize(500, 40);

        logo = new LauncherImage(pane);
        logo.setImage(getResourceLocation().loadImage(engine, "logo.png"));
        logo.setSize(25, 25);
        logo.setX(engine.getWidth()/2 + 40);
        logo.setY(3);

        usernameField = new LauncherTextField(pane);
        usernameField.setPosition(engine.getWidth()/2 - 135, engine.getHeight()/2- 57);
        usernameField.setSize(270, 50);
        usernameField.setFont(new Font("Arial", 18));
        usernameField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
        usernameField.setVoidText("Account Name");

        passwordField = new LauncherPasswordField(pane);
        passwordField.setPosition(engine.getWidth()/2 - 135, engine.getHeight()/2);
        passwordField.setSize(270, 50);
        passwordField.setFont(new Font("Arial", 18));
        passwordField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
        passwordField.setVoidText("Password");

        loginButton = new LauncherButton(pane);
        loginButton.setPosition(engine.getWidth()/2 - 135, engine.getHeight()/2 - 114);
        loginButton.setSize(270, 50);
        loginButton.setFont(new Font("Arial", 18));
        loginButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        loginButton.setText("Login");
        loginButton.setOnAction(event -> {
            if(usernameField.getText().length() < 3 || usernameField.getText().isEmpty()){
                new LauncherAlert("Failed to Connect!\n(Nickname to short!)", Alert.AlertType.ERROR);
            }else if(!(usernameField.getText().length() < 3 || usernameField.getText().isEmpty()) && passwordField.getText().isEmpty()){
                GameAuth auth = new GameAuth(usernameField.getText(), passwordField.getText(), AccountType.OFFLINE);
                if(auth.isLogged()){
                    update(auth);
                }
            }else{
                GameAuth auth = new GameAuth(usernameField.getText(), passwordField.getText(), AccountType.MOJANG);
                if(auth.isLogged()){
                    update(auth);
                }else{
                    auth = new GameAuth(usernameField.getText(), passwordField.getText(), AccountType.MICROSOFT);
                    if(auth.isLogged()){
                        update(auth);
                    }else{
                        new LauncherAlert("Failed to Connect!\nWorng Password!", Alert.AlertType.ERROR);
                    }
                }
            }
        });

        settingsButton = new LauncherButton(pane);
        settingsButton.setPosition(10, 50);
        settingsButton.setSize(100, 65);
        settingsButton.setFont(new Font("Arial", 18));
        settingsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        settingsButton.setText("Settings");

        reduceButton = new LauncherButton(pane);
        reduceButton.setPosition(engine.getWidth() - 55, 0);
        reduceButton.setSize(25, 25);
        reduceButton.setFont(new Font("Arial", 15));
        reduceButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        reduceButton.setText("-");
        reduceButton.setOnAction(event -> {Stage stage = (Stage)((LauncherButton)event.getSource()).getScene().getWindow(); stage.setIconified(true);});

        exitButton = new LauncherButton(pane);
        exitButton.setPosition(engine.getWidth()-30, 0);
        exitButton.setSize(25, 25);
        exitButton.setFont(new Font("Arial", 15));
        exitButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        exitButton.setText("×");
        exitButton.setOnAction(event -> {System.exit(0);});

        discordButton = new LauncherButton(pane);
        discordButton.setPosition(engine.getWidth()/2 - 135, engine.getHeight()/2 + 100);
        discordButton.setSize(75, 75);
        discordButton.setFont(new Font("Arial", 15));
        discordButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        discordButton.setText("NULL");
        discordButton.setOnAction(event -> {openLink("https://discord.gg/xYytpBTd3r");});

        curseforgeButton = new LauncherButton(pane);
        curseforgeButton.setPosition(engine.getWidth()/2 - 35, engine.getHeight()/2 + 100);
        curseforgeButton.setSize(75, 75);
        curseforgeButton.setFont(new Font("Arial", 15));
        curseforgeButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        curseforgeButton.setText("NULL");
        curseforgeButton.setOnAction(event -> {openLink("https://www.curseforge.com/minecraft/mc-mods/negative-n");});

        youtubeButton = new LauncherButton(pane);
        youtubeButton.setPosition(engine.getWidth()/2 + 60, engine.getHeight()/2 + 100);
        youtubeButton.setSize(75, 75);
        youtubeButton.setFont(new Font("Arial", 15));
        youtubeButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        youtubeButton.setText("NULL");
        youtubeButton.setOnAction(event -> {openLink("https://www.youtube.com/channel/UC7lHA5pMQMrTTeZg1yk2hXw");});

        updateRectangle = new LauncherRectangle(pane, engine.getWidth()/2 - 175, engine.getHeight()/2-60, 350, 180);
        updateRectangle.setFill(Color.rgb(0, 0, 0, 0.7));
        updateRectangle.setArcWidth(10.0);
        updateRectangle.setArcHeight(10.0);
        updateRectangle.setVisible(false);

        updateLabel = new LauncherLabel(pane);
        updateLabel.setText("-=-=UPDATING=-=-");
        updateLabel.setAlignment(Pos.CENTER);
        updateLabel.setFont(new Font("Arial", 18));
        updateLabel.setStyle("-fx-background-color: transparant; -fx-text-fill: white;");
        updateLabel.setPosition(engine.getWidth()/2 -95, engine.getHeight()/2 -55);
        updateLabel.setOpacity(0.7);
        updateLabel.setSize(190, 40);
        updateLabel.setVisible(false);

        currentStep = new LauncherLabel(pane);
        currentStep.setText("Preparing the update...");
        currentStep.setAlignment(Pos.CENTER);
        currentStep.setFont(Font.font("Arial", FontPosture.ITALIC, 18f));
        currentStep.setStyle("-fx-background-color: transparant; -fx-text-fill: white;");
        currentStep.setPosition(engine.getWidth()/2 -160, engine.getHeight()/2 -83);
        currentStep.setOpacity(0.4);
        currentStep.setSize(320, 40);
        currentStep.setVisible(false);

        currentFileLabel = new LauncherLabel(pane);
        currentFileLabel.setText("File: ");
        currentFileLabel.setAlignment(Pos.CENTER);
        currentFileLabel.setFont(new Font("Arial", 15));
        currentFileLabel.setStyle("-fx-background-color: transparant; -fx-text-fill: white;");
        currentFileLabel.setPosition(engine.getWidth()/2 -160, engine.getHeight()/2 + 25);
        currentFileLabel.setOpacity(0.7);
        currentFileLabel.setSize(320, 40);
        currentFileLabel.setVisible(false);

        percentageLabel = new LauncherLabel(pane);
        percentageLabel.setText("0%");
        percentageLabel.setAlignment(Pos.CENTER);
        percentageLabel.setFont(new Font("Arial", 18));
        percentageLabel.setStyle("-fx-background-color: transparant; -fx-text-fill: white;");
        percentageLabel.setPosition(engine.getWidth()/2 - 50, engine.getHeight()/2 - 5);
        percentageLabel.setOpacity(0.8);
        percentageLabel.setSize(100, 40);
        percentageLabel.setVisible(false);
    }

    private void update(GameAuth auth){
        usernameField.setDisable(true);
        usernameField.setVisible(false);
        passwordField.setDisable(true);
        passwordField.setVisible(false);
        loginButton.setDisable(true);
        loginButton.setVisible(false);
        settingsButton.setDisable(true);
        settingsButton.setVisible(false);
        discordButton.setDisable(true);
        discordButton.setVisible(false);
        curseforgeButton.setDisable(true);
        curseforgeButton.setVisible(false);
        youtubeButton.setDisable(true);
        youtubeButton.setVisible(false);

        updateRectangle.setVisible(true);
        updateLabel.setVisible(true);
        currentStep.setVisible(true);
        currentFileLabel.setVisible(true);
        percentageLabel.setVisible(true);

        updater.reg(engine);
        updater.reg(auth.getSession());
        engine.reg(updater);
        updateThread = new Thread(){
            @Override
            public void run() {
                engine.getGameUpdater().run();
            }
        };
        updateThread.start();
        line = new Timeline(new KeyFrame[]{
                new KeyFrame(Duration.seconds(0.0D), e -> updateDownload(), new KeyValue[0]),
                new KeyFrame(Duration.seconds(0.1D), new KeyValue[0])
        });
        line.setCycleCount(Animation.INDEFINITE);
        line.play();
    }

    private void updateDownload() {
        if(engine.getGameUpdater().downloadedFiles > 0){
            percentageLabel.setText(format.format(engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload + "%"));
        }
        currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
        currentStep.setText(engine.getGameUpdater().getCurrentInfo());
    }
}
