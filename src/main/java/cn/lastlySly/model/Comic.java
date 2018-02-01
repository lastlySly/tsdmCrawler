package cn.lastlySly.model;
/**
 * @author lastlySly
 * @GitHub https://github.com/lastlySly
 * @create 2018-02-01 13:03
 **/
public class Comic {
    private String comic_id;
    private String comic_title;//标题
    private String comic_state;//状态
    private String comic_type;//类型
    private String comic_update;//更新日期
    private int comic_popularity;//人气
    private String comic_titleimgurl;//标题图片;
    private String comic_resourceurl;//资源地址;
    private String comic_sharemaker;//分享者
    private String comic_describe;//描述
    private String comic_initial;//首字母
    private String comic_keyword;//关键字

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getComic_title() {
        return comic_title;
    }

    public void setComic_title(String comic_title) {
        this.comic_title = comic_title;
    }

    public String getComic_state() {
        return comic_state;
    }

    public void setComic_state(String comic_state) {
        this.comic_state = comic_state;
    }

    public String getComic_type() {
        return comic_type;
    }

    public void setComic_type(String comic_type) {
        this.comic_type = comic_type;
    }

    public String getComic_update() {
        return comic_update;
    }

    public void setComic_update(String comic_update) {
        this.comic_update = comic_update;
    }

    public int getComic_popularity() {
        return comic_popularity;
    }

    public void setComic_popularity(int comic_popularity) {
        this.comic_popularity = comic_popularity;
    }

    public String getComic_titleimgurl() {
        return comic_titleimgurl;
    }

    public void setComic_titleimgurl(String comic_titleimgurl) {
        this.comic_titleimgurl = comic_titleimgurl;
    }

    public String getComic_resourceurl() {
        return comic_resourceurl;
    }

    public void setComic_resourceurl(String comic_resourceurl) {
        this.comic_resourceurl = comic_resourceurl;
    }

    public String getComic_sharemaker() {
        return comic_sharemaker;
    }

    public void setComic_sharemaker(String comic_sharemaker) {
        this.comic_sharemaker = comic_sharemaker;
    }

    public String getComic_describe() {
        return comic_describe;
    }

    public void setComic_describe(String comic_describe) {
        this.comic_describe = comic_describe;
    }

    public String getComic_initial() {
        return comic_initial;
    }

    public void setComic_initial(String comic_initial) {
        this.comic_initial = comic_initial;
    }

    public String getComic_keyword() {
        return comic_keyword;
    }

    public void setComic_keyword(String comic_keyword) {
        this.comic_keyword = comic_keyword;
    }
}
