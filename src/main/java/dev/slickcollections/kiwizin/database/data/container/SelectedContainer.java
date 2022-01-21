package dev.slickcollections.kiwizin.database.data.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.titles.Title;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class  SelectedContainer extends AbstractContainer {
  
  public SelectedContainer(DataContainer dataContainer) {
    super(dataContainer);
  }
  
  public void setIcon(String id) {
    JSONObject selected = this.dataContainer.getAsJsonObject();
    selected.put("icon", id);
    this.dataContainer.set(selected.toString());
    selected.clear();
  }
  
  public Title getTitle() {
    return Title.getById(this.dataContainer.getAsJsonObject().get("title").toString());
  }
  
  public void setTitle(String id) {
    JSONObject selected = this.dataContainer.getAsJsonObject();
    selected.put("title", id);
    this.dataContainer.set(selected.toString());
    selected.clear();
  }
}
