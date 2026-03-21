package vn.baokim.b2b.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO cho API cập nhật VA (Host-to-Host)
 */
public class UpdateVARequest {
    @SerializedName("acc_no")
    private String accNo;

    @SerializedName("acc_name")
    private String accName;

    @SerializedName("collect_amount_min")
    private Integer collectAmountMin;

    @SerializedName("collect_amount_max")
    private Integer collectAmountMax;

    @SerializedName("expire_date")
    private String expireDate;

    @SerializedName("description")
    private String description;

    public UpdateVARequest() {}

    public UpdateVARequest(String accNo) {
        this.accNo = accNo;
    }

    public String getAccNo() { return accNo; }
    public void setAccNo(String accNo) { this.accNo = accNo; }

    public String getAccName() { return accName; }
    public void setAccName(String accName) { this.accName = accName; }

    public Integer getCollectAmountMin() { return collectAmountMin; }
    public void setCollectAmountMin(Integer collectAmountMin) { this.collectAmountMin = collectAmountMin; }

    public Integer getCollectAmountMax() { return collectAmountMax; }
    public void setCollectAmountMax(Integer collectAmountMax) { this.collectAmountMax = collectAmountMax; }

    public String getExpireDate() { return expireDate; }
    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
