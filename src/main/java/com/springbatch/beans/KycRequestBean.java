package com.springbatch.beans;

public class KycRequestBean {

    private String uniqueId;
    private String kycData;
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getKycData() {
		return kycData;
	}
	public void setKycData(String kycData) {
		this.kycData = kycData;
	}
	@Override
	public String toString() {
		return "KycRequestBean [uniqueId=" + uniqueId + ", kycData=" + kycData + "]";
	}
    
	
    
}
