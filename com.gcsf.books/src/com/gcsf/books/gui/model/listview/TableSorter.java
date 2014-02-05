package com.gcsf.books.gui.model.listview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;

public class TableSorter extends ViewerSorter {
  private int propertyIndex;

  // private static final int ASCENDING = 0;
  private static final int DESCENDING = 1;

  private int direction = DESCENDING;

  public TableSorter() {
    this.propertyIndex = 0;
    direction = DESCENDING;
  }

  public void setColumn(int column) {
    if (column == this.propertyIndex) {
      // Same column as last sort; toggle the direction
      direction = 1 - direction;
    } else {
      // New column; do an ascending sort
      this.propertyIndex = column;
      direction = DESCENDING;
    }
  }

  @Override
  public int compare(Viewer viewer, Object e1, Object e2) {
    Item p1 = (Item) e1;
    Item p2 = (Item) e2;
    int rc = 0;
    switch (propertyIndex) {
      case 0:
        rc = p1.getId().compareTo(p2.getId());
        break;
      case 1:
        rc = p1.getDetail1().compareTo(p2.getDetail1());
        break;
      case 2:
        rc = p1.getDetail2().compareTo(p2.getDetail2());
        break;
      case 3:
        if (!(e1 instanceof FolderItem) && !(e2 instanceof FolderItem)) {
          rc = p1.getYear().compareTo(p2.getYear());
        }
        break;
      default:
        rc = 0;
    }
    // If descending order, flip the direction
    if (direction == DESCENDING) {
      rc = -rc;
    }
    return rc;
  }
}
