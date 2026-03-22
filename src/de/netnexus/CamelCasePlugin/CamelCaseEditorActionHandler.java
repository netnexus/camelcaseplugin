package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

import static de.netnexus.CamelCasePlugin.Conversion.*;

public class CamelCaseEditorActionHandler<T> extends EditorActionHandler {

    CamelCaseEditorActionHandler() {
        super(false);
    }

    @Override
    protected final void doExecute(@NotNull final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
        final Pair<Boolean, T> additionalParameter = beforeWriteAction(editor);
        if (!additionalParameter.first) {
            return;
        }

        new EditorWriteActionHandler(false) {
            @Override
            public void executeWriteAction(@NotNull Editor editor1, @Nullable Caret caret1, DataContext dataContext1) {
            }
        }.doExecute(editor, caret, dataContext);
    }

    @NotNull
    private Pair<Boolean, T> beforeWriteAction(Editor editor) {
        String[] conversionList = ConversionList.toArray(new String[0]);
        String[] activeConversionList = ConversionList.toArray(new String[0]);
        Project project = editor.getProject();
        if (project != null) {
            CamelCaseConfig config = CamelCaseConfig.getInstance(project);
            if (config != null && (config.getcb1State() || config.getcb2State() || config.getcb3State() || config.getcb4State() || config.getcb5State() || config.getcb6State())) {
                activeConversionList = config.getActiveModel();
            }
        }

        // Determine the target case type for the selected text(s).
        // If all selected text with the same case type, target is set to the next case type in activeConversionList.
        // Otherwise, target is set to the case which at last of selected text case type.
        String target;
        {
            String commonCaseType = null;
            String lastCaseType = null;
            int lastIdx = -1;
            for (Caret caret : editor.getCaretModel().getAllCarets()) {
                String caseType = CaseType(selectedText(editor, caret));
                commonCaseType = caseType;
                int idx = Arrays.asList(conversionList).indexOf(caseType);
                if (idx > lastIdx) {
                    lastCaseType = caseType;
                    lastIdx = idx;
                }
            }
            if (lastCaseType == null) {
                assert commonCaseType != null;
                target = getNext(CaseType(commonCaseType), conversionList);
            } else {
                target = getNext(lastCaseType, conversionList);
            }

            // Make sure target is in activeConversionList
            for (int i = 0; i < conversionList.length; i++) {
                int idx = Arrays.asList(activeConversionList).indexOf(target);
                if (idx != -1) {
                    break;
                }
                target = getNext(target, conversionList);
            }
        }

        Document document = editor.getDocument();
        for (Caret caret : editor.getCaretModel().getAllCarets()) {
            String selected = caret.getSelectedText();
            if (selected != null && !selected.isEmpty()) {
                String newText = Conversion.transform(selected, target);

                Runnable runnable = () -> document.replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), newText);
                ApplicationManager.getApplication().runWriteAction(getRunnableWrapper(project, runnable));
            }
        }

        return continueExecution();
    }

    private String selectedText(Editor editor, Caret caret) {
        String text = caret.getSelectedText();
        if (text == null || text.isEmpty()) {
            int start = caret.getOffset();
            int end = start;
            boolean moveLeft = true;
            boolean moveRight = true;
            Pattern p = Pattern.compile("[^A-Za-z0-9.\\-]");

            // move caret left
            while (moveLeft && start > 0) {
                start--;
                caret.setSelection(start, end);
                String selected = caret.getSelectedText();
                if (selected == null || p.matcher(selected).find()) {
                    start++;
                    moveLeft = false;
                }
            }

            // move caret right
            while (moveRight && end < editor.getDocument().getTextLength()) {
                end++;
                caret.setSelection(start, end);
                String selected = caret.getSelectedText();
                if (selected == null || p.matcher(selected).find()) {
                    end--;
                    moveRight = false;
                }
            }

            caret.setSelection(start, end);
            text = caret.getSelectedText();
        }

        return text;
    }


    private Pair<Boolean, T> continueExecution() {
        return new Pair<>(true, null);
    }


    private Runnable getRunnableWrapper(final Project project, final Runnable runnable) {
        return () -> CommandProcessor.getInstance().executeCommand(project, runnable, "CamelCase", ActionGroup.EMPTY_GROUP);
    }

}