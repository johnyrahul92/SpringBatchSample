package com.springbatch.daoImpl;

import com.springbatch.dao.BatchDao;
import com.springbatch.entity.KycCustomerData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("batchDao")
@Transactional("transactionManagerHibernate")
public class BatchDaoImpl implements BatchDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveData(KycCustomerData kycCustomerData) {
        Session session = sessionFactory.getCurrentSession();
        session.save(kycCustomerData);
    }
}
