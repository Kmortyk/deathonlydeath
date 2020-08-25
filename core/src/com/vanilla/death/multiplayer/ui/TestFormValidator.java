package com.vanilla.death.multiplayer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.vanilla.death.multiplayer.ClientController;
import com.vanilla.death.multiplayer.packet.LoginCheckPacket;
import com.vanilla.death.multiplayer.packet.RegisterCheckPacket;
import com.vanilla.death.multiplayer.packet.RegisterPacket;

/**
 * Created by hedgehog on 27.07.17.
 */
public class TestFormValidator extends VisWindow {

    private VisLabel errorLabelReg;
    private VisLabel errorLabelLog;

    public void setErrorLabelReg(String label) {
        if (label != null || label.trim().equals(""))
            errorLabelReg.setText(label);
    }

    public void setErrorLabelLog(String label) {
        if (label != null || label.trim().equals(""))
            errorLabelLog.setText(label);
    }

    public TestFormValidator(String title) {
        super(title);
    }

    public void login() {
        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        final VisTextButton loginBtn = new VisTextButton("Войти");

        final VisValidatableTextField loginTextField = new VisValidatableTextField();

        final VisValidatableTextField roomTextField = new VisValidatableTextField();

        errorLabelLog = new VisLabel();
        errorLabelLog.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabelLog).expand().fill();
        buttonTable.add(loginBtn).center();

        add(new VisLabel("Логин: "));
        add(loginTextField).expand().fill();
        row();
        add(new VisLabel("Комната: "));
        add(roomTextField).expand().fill();
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(loginBtn, errorLabelLog, "smooth");
        validator.setSuccessMessage("Всё подходит!");
        validator.notEmpty(loginTextField, "Кажется, не хватает логина");
        validator.notEmpty(roomTextField, "Безымянные комнаты в разработке");
//        validator.integerNumber(age, "age must be a number");
//        validator.valueGreaterThan(age, "you must be at least 18 years old", 18, true);

        loginBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LoginCheckPacket packet = new LoginCheckPacket();
                packet.login = loginTextField.getText();
                packet.room = roomTextField.getText();

                ClientController.getInstance().getClient().sendTCP(packet);

//                Dialogs.showOKDialog(getStage(), "message", "you made it!");
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
    }

    public void reg() {
        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton loginBtn = new VisTextButton("Register");

        final VisValidatableTextField loginTextField = new VisValidatableTextField();
        VisCheckBox termsCheckbox = new VisCheckBox("Принять соглашение(прошу, не читайте)");

        errorLabelReg = new VisLabel();
        errorLabelReg.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabelReg).expand().fill();
        buttonTable.add(loginBtn).center();

        add(new VisLabel("Логин: "));
        add(loginTextField).expand().fill();
        row();
        add(termsCheckbox).colspan(2);
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(loginBtn, errorLabelReg, "smooth");
        validator.setSuccessMessage("all good!");
        validator.notEmpty(loginTextField, "Зарезервировано для админа");
        validator.checked(termsCheckbox, "Но принять-то правила надо");

        loginBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RegisterCheckPacket packet = new RegisterCheckPacket();
                packet.login = loginTextField.getText();

                ClientController.getInstance().getClient().sendTCP(packet);

            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 10);

    }
}
