package api;

public class Item implements Cloneable{
    private String id;
    private String title;
    private String content;
    private Integer views;
    private Integer timestamp;

    public Item(String id, String title, String content, int views, int timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Item){
            Item toCompare = (Item) o;
            return toCompare.getContent().equals(content) &&
                    toCompare.getId().equals(id) &&
                    toCompare.getTitle().equals(title) &&
                    toCompare.getTimestamp() == timestamp &&
                    toCompare.getViews() == views;
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



}
