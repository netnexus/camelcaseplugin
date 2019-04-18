package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import org.jetbrains.annotations.Nullable;

/**
 * Switch between snake_case, SNAKE_CASE, SnakeCase, snakeCase.
 */
public class ToggleCamelCase extends EditorAction {


    public ToggleCamelCase() {
        super(new CamelCaseEditorWriteActionHandler() {
            @Override
            protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable Object additionalParameter) {
            }
        });
    }
}
