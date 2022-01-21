package dev.slickcollections.kiwizin.database.data.container;

import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.titles.Title;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SkinsContainer extends AbstractContainer {

    public SkinsContainer(DataContainer dataContainer) {
        super(dataContainer);
    }

    public void setSkin(String name) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.put("name", name);
        selected.put("value", Manager.getSkin(name, "value"));
        selected.put("signature", Manager.getSkin(name, "signature"));
        this.dataContainer.set(selected.toString());
        selected.clear();
    }

    public String getSkin() {
        return this.dataContainer.getAsJsonObject().get("name").toString();
    }

    public String getValue() { return this.dataContainer.getAsJsonObject().get("value").toString(); }

    public String getSignature() { return this.dataContainer.getAsJsonObject().get("signature").toString(); }

    public Map<String, String> getSkins() {
        Map<String, String> skinsList = new HashMap<>();
        this.dataContainer.getAsJsonObject().forEach((key, value) ->
                skinsList.put((String) key, (String) value));
        return skinsList;
    }

    public void removeSkin(String name) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.remove(name);
        this.dataContainer.set(selected.toString());
        selected.clear();
    }

    public void addSkin(String name) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.put(name, System.currentTimeMillis() + ":" + Manager.getSkin(name, "value"));
        this.dataContainer.set(selected.toString());
        selected.clear();
    }
}
