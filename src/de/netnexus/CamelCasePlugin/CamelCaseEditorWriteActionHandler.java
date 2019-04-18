package de.netnexus.CamelCasePlugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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

import java.util.Arrays;
import java.util.prefs.Preferences;


public abstract class CamelCaseEditorWriteActionHandler<T> extends EditorActionHandler {

    private CamelCaseConfig config;
    private String next;

    private static void replaceText(final Editor editor, final String replacement) {
        new WriteAction<>() {
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

        final Runnable runnable = () -> executeWriteAction(editor, caret, dataContext, additionalParameter.second);
        new EditorWriteActionHandler(false) {
            @Override
            public void executeWriteAction(Editor editor1, @Nullable Caret caret1, DataContext dataContext1) {
                runnable.run();
            }
        }.doExecute(editor, caret, dataContext);
    }

    protected abstract void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable T additionalParameter);

    @NotNull
    private Pair<Boolean, T> beforeWriteAction(Editor editor) {


        Project project = editor.getProject();

        config = CamelCaseConfig.getInstance(project);
        // kindly asking for a small donation ;-)
        Preferences userPrefs = Preferences.userNodeForPackage(this.getClass());
        int usageCount = userPrefs.getInt("usage-count", 0);
        System.out.print("anzahl: "+usageCount);
        if (usageCount == 5 || usageCount == 500) {
            com.intellij.notification.Notification n = new com.intellij.notification.Notification("CamelCase", "CamelCase Plugin", "Like this plugin? Then please consider a small <a href='https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7UDEX9ZEBNG7Q'>donation</a>.", NotificationType.INFORMATION);
            Notifications.Bus.notify(n, project);
        } else if (usageCount == 2000) {
            com.intellij.notification.Notification n = new com.intellij.notification.Notification("CamelCase", "CamelCase Plugin", "You have used this plugin about 2000x now. If you like this plugin please consider a small <a href='https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7UDEX9ZEBNG7Q'>donation</a>.", NotificationType.INFORMATION);
            Notifications.Bus.notify(n, project);
        } else if (usageCount == 10000) {
            com.intellij.notification.Notification n = new Notification("CamelCase", "CamelCase Plugin", "You have used this plugin about 10000x now. If you like this plugin please consider a small <a href='https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7UDEX9ZEBNG7Q'>donation</a>.", NotificationType.INFORMATION);
            Notifications.Bus.notify(n, project);
        }
        userPrefs.putInt("usage-count", ++usageCount);


        String text = editor.getSelectionModel().getSelectedText();
        if (text == null || text.isEmpty()) {
            editor.getSelectionModel().selectWordAtCaret(true);
            text = editor.getSelectionModel().getSelectedText();
        }

        int iterations = 0;
        String newText;
        boolean repeat;
        assert text != null;
        next = null;
        if (config.getcb1State() || config.getcb2State() || config.getcb3State() || config.getcb4State() || config.getcb5State() || config.getcb6State()) {
            do {
                newText = text;
                if (text.equals(text.toLowerCase()) && text.contains("_")) {
                    // snake_case to space case
                    if (next == null) {
                        setNext("snake_case");
                        repeat = true;
                    } else {
                        if (next.equals("space case")) {
                            repeat = !config.getcb6State();
                            setNext("space case");


                        } else {
                            repeat = true;
                        }
                    }
                    newText = text.replace('_', ' ');

                } else if (text.equals(text.toLowerCase()) && text.contains(" ")) {
                    // space case to kebab-case
                    if (next == null) {
                        setNext("space case");
                        repeat = true;
                    } else {
                        newText = text.replace(' ', '-');
                        if (next.equals("kebab-case")) {
                            repeat = !config.getcb1State();
                            setNext("kebab-case");

                        } else {
                            repeat = true;
                        }
                    }

                } else if (text.equals(text.toLowerCase()) && text.contains("-")) {
                    // kebab-case to SNAKE_CASE
                    if (next == null) {
                        setNext("kebab-case");
                        repeat = true;
                    } else {
                        newText = text.replace('-', '_').toUpperCase();
                        if (next.equals("SNAKE_CASE")) {
                            repeat = !config.getcb2State();
                            setNext("SNAKE_CASE");

                        } else {
                            repeat = true;
                        }
                    }

                } else if (text.equals(text.toUpperCase()) && text.contains("_")) {
                    // SNAKE_CASE to CamelCase
                    if (next == null) {
                        setNext("SNAKE_CASE");
                        repeat = true;
                    } else {
                        newText = toCamelCase(text.toLowerCase());
                        if (next.equals("CamelCase")) {
                            repeat = !config.getcb3State();
                            setNext("CamelCase");

                        } else {
                            repeat = true;
                        }
                    }

                } else if (!text.equals(text.toUpperCase()) && text.substring(0, 1).equals(text.substring(0, 1).toUpperCase()) && !text.contains("_")) {
                    // CamelCase to camelCase
                    if (next == null) {
                        setNext("CamelCase");
                        repeat = true;
                    } else {
                        newText = text.substring(0, 1).toLowerCase() + text.substring(1);
                        if (next.equals("camelCase")) {
                            repeat = !config.getcb4State();
                            setNext("camelCase");

                        } else {
                            repeat = true;
                        }
                    }

                } else {
                    // camelCase to snake_case
                    if (next == null) {
                        setNext("camelCase");
                        repeat = true;
                    } else {
                        newText = toSnakeCase(text);
                        if (next.equals("snake_case")) {
                            repeat = !config.getcb5State();
                            setNext("snake_case");

                        } else {
                            repeat = true;
                        }
                    }

                }
                if (iterations++ > 10) {
                    repeat = false;
                }
                text = newText;
            } while (repeat);


            final Editor fEditor = editor;
            final String fReplacement = newText;
            Runnable runnable = () -> CamelCaseEditorWriteActionHandler.replaceText(fEditor, fReplacement);
            ApplicationManager.getApplication().runWriteAction(getRunnableWrapper(fEditor.getProject(), runnable));
        }


        return continueExecution();
    }


    private Pair<Boolean, T> continueExecution() {
        return new Pair<>(true, null);
    }

    private void setNext(String conversion) {
        int index;
        String[] order = config.getmodel();
        index = Arrays.asList(order).indexOf(conversion) + 1;
        if (index < order.length) {
            next = order[index];

        } else {
            next = order[0];
        }
    }

    private Runnable getRunnableWrapper(final Project project, final Runnable runnable) {
        return () -> CommandProcessor.getInstance().executeCommand(project, runnable, "camelCase", ActionGroup.EMPTY_GROUP);
    }

    /**
     * Convert a string (CamelCase) to snake_case
     *
     * @param in CamelCase string
     * @return snake_case String
     */
    private String toSnakeCase(String in) {
        StringBuilder result = new StringBuilder("" + Character.toLowerCase(in.charAt(0)));
        for (int i = 1; i < in.length(); i++) {
            char c = in.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Convert a string (snake_case) to CamelCase
     *
     * @param in snake_case String
     * @return CamelCase string
     */
    private String toCamelCase(String in) {
        StringBuilder camelCased = new StringBuilder();
        String[] tokens = in.split("_");
        for (String token : tokens) {
            if (token.length() >= 1) {
                camelCased.append(token.substring(0, 1).toUpperCase()).append(token.substring(1));
            } else {
                camelCased.append("_");
            }
        }
        return camelCased.toString();
    }
}