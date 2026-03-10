package de.netnexus.camelcase.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import de.netnexus.camelcase.config.CamelCaseConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This ProjectConfigurable class appears on Settings dialog,
 * to let user to configure this plugin's behavior.
 */
public class CamelCasePluginConfigurable implements SearchableConfigurable {

    private OptionGui mGUI;
    private final CamelCaseConfig mConfig;

    @SuppressWarnings("FieldCanBeLocal")
    private final Project mProject;

    public CamelCasePluginConfigurable(@NotNull Project project) {
        mProject = project;
        mConfig = CamelCaseConfig.getInstance(project);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mGUI = new OptionGui();
        mGUI.createUI(mProject);
        return mGUI.getRootPanel();
    }

    @Override
    public void disposeUIResources() {
        mGUI = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Camel Case";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.CamelCasePluginConfigurable";
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.CamelCasePluginConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Override
    public boolean isModified() {
        return mGUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if (mGUI != null) {
            mGUI.apply();
        }
    }

    @Override
    public void reset() {

    }

}
