package rocks.poopjournal.myvacationdays;

import java.util.List;

public class Item {
    private String itemTitle;
    private List<rocks.poopjournal.myvacationdays.SubItem> subItemList;

    public Item(String itemTitle, List<rocks.poopjournal.myvacationdays.SubItem> subItemList) {
        this.itemTitle = itemTitle;
        this.subItemList = subItemList;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<rocks.poopjournal.myvacationdays.SubItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<rocks.poopjournal.myvacationdays.SubItem> subItemList) {
        this.subItemList = subItemList;
    }
}
