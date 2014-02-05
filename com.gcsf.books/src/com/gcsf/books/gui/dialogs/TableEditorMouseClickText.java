package com.gcsf.books.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class TableEditorMouseClickText {
  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setText("Text Table Editor");

    shell.setLayout(new FillLayout());

    final Table table = new Table(shell, SWT.SINGLE | SWT.FULL_SELECTION
        | SWT.HIDE_SELECTION);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    for (int i = 0; i < 5; i++) {
      TableColumn column = new TableColumn(table, SWT.CENTER);
      column.setText("Column " + (i + 1));
      column.pack();
    }

    for (int i = 0; i < 5; i++) {
      final TableItem item = new TableItem(table, SWT.NONE);
    }

    final TableEditor editor = new TableEditor(table);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;

    table.addMouseListener(new MouseAdapter() {
      public void mouseDown(MouseEvent event) {
        Control old = editor.getEditor();
        if (old != null)
          old.dispose();

        Point pt = new Point(event.x, event.y);

        final TableItem item = table.getItem(pt);
        if (item == null) {
          return;
        }
        int column = -1;
        for (int i = 0, n = table.getColumnCount(); i < n; i++) {
          Rectangle rect = item.getBounds(i);
          if (rect.contains(pt)) {
            column = i;
            break;
          }
        }

        if (column != 1) {
          return;
        }
        final Text text = new Text(table, SWT.NONE);
        text.setForeground(item.getForeground());

        text.setText(item.getText(column));
        text.setForeground(item.getForeground());
        text.selectAll();
        text.setFocus();

        editor.minimumWidth = text.getBounds().width;

        editor.setEditor(text, item, column);

        final int col = column;
        text.addModifyListener(new ModifyListener() {
          public void modifyText(ModifyEvent event) {
            item.setText(col, text.getText());
          }
        });
      }
    });

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}
