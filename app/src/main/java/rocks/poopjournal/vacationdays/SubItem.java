package rocks.poopjournal.vacationdays;

public class SubItem {
    private String subItemTitle;
    private String start;
    private String end;
    private String monthyear;

    public SubItem(String subItemTitle, String start, String end, String monthyear) {
        this.subItemTitle = subItemTitle;
        this.start=start;
        this.end=end;
        this.monthyear=monthyear;
    }



    public String getSubItemTitle() {
        return subItemTitle;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
    public String getMonthyear() {
        return monthyear;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }
    public void setStart(String from) {
        this.start = start;
    }
    public void setEnd(String to) {
        this.end= end;
    }
    public void setMonthyear(String monthyear) {
        this.monthyear= monthyear;
    }

}
