package vn.baokim.b2b.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO cho API hủy thu hộ tự động (Basic/Pro)
 */
public class CancelAutoDebitRequest {
    @SerializedName("token")
    private String token;

    @SerializedName("url_success")
    private String urlSuccess;

    @SerializedName("url_fail")
    private String urlFail;

    public CancelAutoDebitRequest() {}

    public CancelAutoDebitRequest(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUrlSuccess() { return urlSuccess; }
    public void setUrlSuccess(String urlSuccess) { this.urlSuccess = urlSuccess; }

    public String getUrlFail() { return urlFail; }
    public void setUrlFail(String urlFail) { this.urlFail = urlFail; }
}
