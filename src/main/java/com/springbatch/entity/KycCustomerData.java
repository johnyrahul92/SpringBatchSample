package com.springbatch.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KYCCUSTOMERDATATABLE")
public class KycCustomerData {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "KYCDATA", length = 1000000)
    private String kycData;

    @Column(name = "CIF")
    private String cif;

    @Column(name = "COUNT")
    private long count;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATEDON")
    private Date createdOn;

    @Column(name = "UPDATEDON")
    private Date updatedOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKycData() {
        return kycData;
    }

    public void setKycData(String kycData) {
        this.kycData = kycData;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

	@Override
	public String toString() {
		return "KycCustomerData [id=" + id + ", kycData=" + kycData + ", cif=" + cif + ", count=" + count + ", status="
				+ status + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + "]";
	}
    
    
}
