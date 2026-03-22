package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "CamelCaseConfig",
        storages = {
                @Storage("CamelCaseConfigNew.xml")}
)
public class CamelCaseConfig implements PersistentStateComponent<CamelCaseConfig> {
    public boolean cb1State = true;
    public boolean cb2State = true;
    public boolean cb3State = true;
    public boolean cb4State = true;
    public boolean cb5State = true;
    public boolean cb6State = true;
    public boolean cb7State = true;
    public String[] model = {
            "kebab-case",
            "SNAKE_CASE",
            "CamelCase",
            "camelCase",
            "snake_case",
            "space case",
            "Camel Case",
    };
    public String[] activeModel = model;


    CamelCaseConfig() {
    }

    @Nullable
    public static CamelCaseConfig getInstance(Project project) {
        return project.getService(CamelCaseConfig.class);
    }

    public boolean getcb1State() {
        return cb1State;
    }

    public void setcb1State(boolean cb1) {
        this.cb1State = cb1;
    }

    public boolean getcb2State() {
        return cb2State;
    }

    public void setcb2State(boolean cb2) {
        this.cb2State = cb2;
    }

    public boolean getcb3State() {
        return cb3State;
    }

    public void setcb3State(boolean cb3) {
        this.cb3State = cb3;
    }

    public boolean getcb4State() {
        return cb4State;
    }

    public void setcb4State(boolean cb4) {
        this.cb4State = cb4;
    }

    public boolean getcb5State() {
        return cb5State;
    }

    public void setcb5State(boolean cb5) {
        this.cb5State = cb5;
    }

    public boolean getcb6State() {
        return cb6State;
    }

    public void setcb6State(boolean cb6) {
        this.cb6State = cb6;
    }

    public void setcb7State(boolean cb7) {
        this.cb7State = cb7;
    }

    public boolean getcb7State() {
        return cb7State;
    }

    public String[] getmodel() {
        return model;
    }
    public void setListModel(String[] model) {
        this.model = model;
    }

    public String[] getActiveModel() {
        return activeModel;
    }

    public void setActiveModel(String[] model) {
        this.activeModel = model;
    }
    @Nullable
    @Override
    public CamelCaseConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CamelCaseConfig singleFileExecutionConfig) {
        XmlSerializerUtil.copyBean(singleFileExecutionConfig, this);
    }
}