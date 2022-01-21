package dev.slickcollections.kiwizin.database.data.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.titles.Title;
import org.json.simple.JSONArray;

@SuppressWarnings("unchecked")
public class TitlesContainer extends AbstractContainer {
  
  public TitlesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }
  
  public void add(Title title) {
    JSONArray titles = this.dataContainer.getAsJsonArray();
    titles.add(title.getId());
    this.dataContainer.set(titles.toString());
    titles.clear();
  }
  
  public boolean has(Title title) {
    return this.dataContainer.getAsJsonArray().contains(title.getId());
  }
}
