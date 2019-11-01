
package entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("album_id")
    @Expose
    private int albumId;
    @SerializedName("owner_id")
    @Expose
    private int ownerId;
    @SerializedName("sizes")
    @Expose
    private List<Size> sizes = null;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("date")
    @Expose
    private int date;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double _long;
    @SerializedName("post_id")
    @Expose
    private int postId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item withId(int id) {
        this.id = id;
        return this;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public Item withAlbumId(int albumId) {
        this.albumId = albumId;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Item withOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public Item withSizes(List<Size> sizes) {
        this.sizes = sizes;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Item withText(String text) {
        this.text = text;
        return this;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Item withDate(int date) {
        this.date = date;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Item withLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLong() {
        return _long;
    }

    public void setLong(double _long) {
        this._long = _long;
    }

    public Item withLong(double _long) {
        this._long = _long;
        return this;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Item withPostId(int postId) {
        this.postId = postId;
        return this;
    }

}
