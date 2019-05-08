package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.editor.actionSystem.EditorAction;

/**
 * Switch between snake_case, SNAKE_CASE, SnakeCase, snakeCase.
 */
public class ToggleCamelCase extends EditorAction {

    public ToggleCamelCase() {
        super(new CamelCaseEditorActionHandler());
    }

}
