package com.springbatch.services;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springbatch.beans.KycSaveDataResponseBean;
import com.springbatch.dao.DaoClass;
import com.springbatch.entity.KycCustomerData;
import com.springbatch.utility.BatchUtil;

@Service
public class BatchServices {

	private static final Logger LOGGER = LogManager.getLogger(BatchServices.class);
	
    @Autowired
    DaoClass daoClass;
    
    @Value("${esb.retry.attempt.limit}")
    private Long attemptLimit;
    
    @Value("${esb.retry.filter.status}")
    private String filterStatus;

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

    public List<KycCustomerData> getKycFilteredData() throws Exception{
    	return daoClass.findByStatusAndCountLessThan(filterStatus, attemptLimit);
    }
}
