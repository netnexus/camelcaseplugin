package de.netnexus.camelcase.action;

import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.actions.TextComponentEditorAction;
import de.netnexus.camelcase.core.CamelCaseEditorActionHandler;
import de.netnexus.camelcase.i18n.CamelCaseBundle;

/**
 * Switch between snake_case, SNAKE_CASE, SnakeCase, snakeCase.
 */
public class ToggleCamelCaseAction extends TextComponentEditorAction {

    public ToggleCamelCaseAction() {
        super(new CamelCaseEditorActionHandler<>());
        
        Presentation presentation = getTemplatePresentation();
        presentation.setText(CamelCaseBundle.message("action.toggleCamelCase.text"));
        presentation.setText(CamelCaseBundle.message("action.toggleCamelCase.description"));
    }

}
