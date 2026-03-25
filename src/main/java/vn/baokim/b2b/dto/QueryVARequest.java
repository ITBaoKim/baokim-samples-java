package vn.baokim.b2b.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO cho API tra cứu giao dịch VA (Host-to-Host)
 */
public class QueryVARequest {
    @SerializedName("acc_no")
    private String accNo;

    public QueryVARequest() {}

    public QueryVARequest(String accNo) {
        this.accNo = accNo;
    }

    public String getAccNo() { return accNo; }
    public void setAccNo(String accNo) { this.accNo = accNo; }
}
