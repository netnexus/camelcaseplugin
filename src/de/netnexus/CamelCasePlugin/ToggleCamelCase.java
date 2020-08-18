package de.netnexus.CamelCasePlugin;

import com.intellij.openapi.editor.actions.TextComponentEditorAction;

/**
 * Switch between snake_case, SNAKE_CASE, SnakeCase, snakeCase.
 */
public class ToggleCamelCase extends TextComponentEditorAction {

    public ToggleCamelCase() {
        super(new CamelCaseEditorActionHandler<>());
    }

}
