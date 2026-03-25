package vn.baokim.b2b.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO cho API hủy đơn hàng Direct
 */
public class CancelOrderRequest {
    @SerializedName("mrc_order_id")
    private String mrcOrderId;

    @SerializedName("reason")
    private String reason;

    public CancelOrderRequest() {}

    public CancelOrderRequest(String mrcOrderId) {
        this.mrcOrderId = mrcOrderId;
    }

    public CancelOrderRequest(String mrcOrderId, String reason) {
        this.mrcOrderId = mrcOrderId;
        this.reason = reason;
    }

    public String getMrcOrderId() { return mrcOrderId; }
    public void setMrcOrderId(String mrcOrderId) { this.mrcOrderId = mrcOrderId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
