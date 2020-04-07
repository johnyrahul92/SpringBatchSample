package com.springbatch.services;

import com.springbatch.beans.KycSaveDataResponse;
import com.springbatch.dao.BatchDao;
import com.springbatch.entity.KycCustomerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BatchServices {

    @Autowired
    BatchDao batchDao;

    public KycSaveDataResponse saveData(String kycData, String cif) {

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMHHmmssSSS");
        String formattedDate = simpleDateFormat.format(date);

        KycCustomerData kycCustomerData = new KycCustomerData();
        kycCustomerData.setId(cif+formattedDate);
        kycCustomerData.setKycData(kycData);
        kycCustomerData.setCif(cif);
        kycCustomerData.setCount(1);
        kycCustomerData.setStatus("Initiated");
        kycCustomerData.setCreatedOn(new Date());

        batchDao.saveData(kycCustomerData);

        KycSaveDataResponse kycSaveDataResponse = new KycSaveDataResponse();
        kycSaveDataResponse.setStatusCode("0");
        kycSaveDataResponse.setStatusDescription("Success");
        kycSaveDataResponse.setReferenceNo(kycCustomerData.getId());
        return kycSaveDataResponse;
    }
}