
package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("response")
    @Expose
    private CountAndItems countAndItems;

    public CountAndItems getCountAndItems() {
        return countAndItems;
    }

    public void setCountAndItems(CountAndItems countAndItems) {
        this.countAndItems = countAndItems;
    }

    public Response withResponse(CountAndItems countAndItems) {
        this.countAndItems = countAndItems;
        return this;
    }

}
