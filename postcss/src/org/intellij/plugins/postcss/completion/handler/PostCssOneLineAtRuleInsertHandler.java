package org.intellij.plugins.postcss.completion.handler;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.css.util.CssEditorUtil;
import org.jetbrains.annotations.NotNull;

public class PostCssOneLineAtRuleInsertHandler implements InsertHandler<LookupElement> {

  @Override
  public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
    Editor editor = context.getEditor();
    CssEditorUtil.typeOrMove(editor, ' ');
    int offset = CssEditorUtil.skipWhiteSpaces(editor, editor.getCaretModel().getOffset());
    if (editor.getDocument().getCharsSequence().charAt(offset) != ';') {
      EditorModificationUtil.insertStringAtCaret(editor, ";", false, false);
    }
  }
}