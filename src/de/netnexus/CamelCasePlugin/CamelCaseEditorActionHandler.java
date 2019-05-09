package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CamelCaseEditorActionHandler<T> extends EditorActionHandler {

    CamelCaseEditorActionHandler() {
        super(true);
    }

    private static void replaceText(final Editor editor, final String replacement) {
        new WriteAction<Object>() {
            @Override
            protected void run(@NotNull Result<Object> result) {
                int start = editor.getSelectionModel().getSelectionStart();
                EditorModificationUtil.insertStringAtCaret(editor, replacement);
                editor.getSelectionModel().setSelection(start, start + replacement.length());
            }
        }.execute().throwException();
    }

    @Override
    protected final void doExecute(@NotNull final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {

        final Pair<Boolean, T> additionalParameter = beforeWriteAction(editor);
        if (!additionalParameter.first) {
            return;
        }

        new EditorWriteActionHandler(false) {
            @Override
            public void executeWriteAction(Editor editor1, @Nullable Caret caret1, DataContext dataContext1) { }
        }.doExecute(editor, caret, dataContext);
    }

    @NotNull
    private Pair<Boolean, T> beforeWriteAction(Editor editor) {
        Project project = editor.getProject();

        CamelCaseConfig config = CamelCaseConfig.getInstance(project);

        String text = editor.getSelectionModel().getSelectedText();
        if (text == null || text.isEmpty()) {
            editor.getSelectionModel().selectWordAtCaret(true);
            text = editor.getSelectionModel().getSelectedText();
        }

        String newText;
        assert text != null;
        if (config.getcb1State() || config.getcb2State() || config.getcb3State() || config.getcb4State() || config.getcb5State() || config.getcb6State()) {
            newText = Conversion.transform(text,
                    config.getcb6State(), // space case
                    config.getcb1State(), // kebab case
                    config.getcb2State(), // upper snake case
                    config.getcb3State(), // pascal case
                    config.getcb4State(), // camel case
                    config.getcb5State(), // lower snake case
                    config.getmodel());

            final Editor fEditor = editor;
            final String fReplacement = newText;
            Runnable runnable = () -> CamelCaseEditorActionHandler.replaceText(fEditor, fReplacement);
            ApplicationManager.getApplication().runWriteAction(getRunnableWrapper(fEditor.getProject(), runnable));
        }

        return continueExecution();
    }


    private Pair<Boolean, T> continueExecution() {
        return new Pair<>(true, null);
    }


    private Runnable getRunnableWrapper(final Project project, final Runnable runnable) {
        return () -> CommandProcessor.getInstance().executeCommand(project, runnable, "camelCase", ActionGroup.EMPTY_GROUP);
    }

}