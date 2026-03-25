package vn.baokim.b2b.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO cho API tra cứu đơn hàng (Basic/Pro + Direct)
 */
public class QueryOrderRequest {
    @SerializedName("mrc_order_id")
    private String mrcOrderId;

    public QueryOrderRequest() {}

    public QueryOrderRequest(String mrcOrderId) {
        this.mrcOrderId = mrcOrderId;
    }

    public String getMrcOrderId() { return mrcOrderId; }
    public void setMrcOrderId(String mrcOrderId) { this.mrcOrderId = mrcOrderId; }
}
