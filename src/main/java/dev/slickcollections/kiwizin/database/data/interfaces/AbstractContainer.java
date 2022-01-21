package dev.slickcollections.kiwizin.database.data.interfaces;

import dev.slickcollections.kiwizin.database.data.DataContainer;

public abstract class AbstractContainer {
  
  protected DataContainer dataContainer;
  
  public AbstractContainer(DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }
  
  public void gc() {
    this.dataContainer = null;
  }
}
