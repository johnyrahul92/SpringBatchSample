package com.springbatch.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springbatch.exception.PortalException;
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

	@Value("${status.message.initiated}")
    private String initiated;

    @Value("${status.message.success}")
    private String success;

    @Value("${status.message.fail}")
    private String fail;

    @Autowired
    DaoClass daoClass;
    
    @Value("${esb.retry.attempt.limit}")
    private Long attemptLimit;

    public KycSaveDataResponseBean saveOrUpdateKYCData(String kycData, Map<String, Object> headers) throws PortalException{

        KycCustomerData kycCustomerData = new KycCustomerData();

        String cif = headers.get("cif").toString();
        Object refNo = headers.get("kycrefno");

        if (refNo != null) {
            String id = refNo.toString();
            Optional<KycCustomerData> data = daoClass.findById(id);
            if (data.isPresent()) {
                LOGGER.debug("Updating the existing data.");
                KycCustomerData customerData = data.get();

                kycCustomerData.setId(id);
                if (headers.get("status").toString().equals("0")) {
                    kycCustomerData.setStatus(success);
                } else {
                    kycCustomerData.setStatus(fail);
                }
                kycCustomerData.setCount(customerData.getCount() + 1);
                kycCustomerData.setCreatedOn(customerData.getCreatedOn());
                kycCustomerData.setUpdatedOn(new Date());
            }
            else {
                throw new PortalException("KYC102");
            }
        }
        else {
            LOGGER.debug("Creating a new entry.");
            String formattedDate = BatchUtil.getDateFormat(new Date());
            kycCustomerData.setId(cif+formattedDate);
            kycCustomerData.setStatus(initiated);
            kycCustomerData.setCount(1);
            kycCustomerData.setCreatedOn(new Date());
        }

        kycCustomerData.setKycData(kycData);
        kycCustomerData.setCif(cif);

        daoClass.save(kycCustomerData);

        KycSaveDataResponseBean kycSaveDataResponseBean = new KycSaveDataResponseBean();
        kycSaveDataResponseBean.setStatusCode("0");
        kycSaveDataResponseBean.setStatusDescription("Success");
        kycSaveDataResponseBean.setReferenceNo(kycCustomerData.getId());
        return kycSaveDataResponseBean;
    }

    public List<KycCustomerData> getData() throws Exception{
    	return daoClass.findByStatusAndCountLessThan(fail, attemptLimit);
    }
}
