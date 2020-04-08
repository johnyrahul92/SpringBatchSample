package com.springbatch.services;

import com.springbatch.beans.KycSaveDataResponseBean;
import com.springbatch.dao.DaoClass;
import com.springbatch.entity.KycCustomerData;
import com.springbatch.utility.BatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatchServices {

    @Autowired
    DaoClass daoClass;

    public KycSaveDataResponseBean saveData(String kycData, String cif) throws Exception{

        String formattedDate = BatchUtil.getDateFormat(new Date());

        KycCustomerData kycCustomerData = new KycCustomerData();
        kycCustomerData.setId(cif+formattedDate);
        kycCustomerData.setKycData(kycData);
        kycCustomerData.setCif(cif);
        kycCustomerData.setCount(1);
        kycCustomerData.setStatus("Initiated");
        kycCustomerData.setCreatedOn(new Date());

        daoClass.save(kycCustomerData);

        KycSaveDataResponseBean kycSaveDataResponseBean = new KycSaveDataResponseBean();
        kycSaveDataResponseBean.setStatusCode("0");
        kycSaveDataResponseBean.setStatusDescription("Success");
        kycSaveDataResponseBean.setReferenceNo(kycCustomerData.getId());
        return kycSaveDataResponseBean;
    }
}
