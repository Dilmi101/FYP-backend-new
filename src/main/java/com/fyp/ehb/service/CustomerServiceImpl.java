package com.fyp.ehb.service;

import com.fyp.ehb.domain.Business;
import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.SignupRequest;
import com.fyp.ehb.repository.BusinessDao;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.util.EhbUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fyp.ehb.model.CustomerResponse;
import com.fyp.ehb.model.ForgotPwdRequest;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private MongoTemplate mongoTemplate;

	@Autowired
	private CustomerDao customerDao;

    @Autowired
    private BusinessDao businessDao;

    @Override
    public Customer registerCustomer(SignupRequest signupRequest) throws EmpowerHerBizException {

        Customer customer = new Customer();

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(signupRequest.getUsername()));
        Customer cust=  mongoTemplate.findOne(query, Customer.class);

        if(cust != null){
        	throw new EmpowerHerBizException(EmpowerHerBizError.USERNAME_ALREADY_EXISTS);
        }

        customer.setUsername(signupRequest.getUsername());
        customer.setName(signupRequest.getName());
        customer.setMobile(signupRequest.getMobileNo());
        customer.setEmail(signupRequest.getEmail());
        customer.setNic(signupRequest.getNic());
        customer.setLanguage(signupRequest.getLanguage());
        
        //nic validation(regex)

        String hashedPwd = EhbUtil.computeSHA256Hash(signupRequest.getPassword());
        customer.setPassword(hashedPwd);

        Business business = new Business();
        business.setBusinessName(signupRequest.getBusinessName());
        business.setBusinessAddress(signupRequest.getBusinessAddress());
        business.setBusinessRegistrationNo(signupRequest.getBusinessRegistrationNo());
        business.setTypeOfProduct(signupRequest.getTypeOfProduct());

        Business business1 = businessDao.save(business);

        customer.setBusiness(business1);

        return customerDao.save(customer);

    }
    
	@Override
	@Transactional
	public CustomerResponse login(String username, String password) throws EmpowerHerBizException {

		if(username == null || username.isEmpty()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_USERNAME);
		}
		
		if(password == null || password.isEmpty()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_PASSWORD);
		}
		
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Customer customer =  mongoTemplate.findOne(query, Customer.class);

        if(customer == null) {
        	throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_USERNAME);
        }
        
        String hashedPwd = EhbUtil.computeSHA256Hash(password);
        
        if(!customer.getPassword().equalsIgnoreCase(hashedPwd)) {
        	throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_PASSWORD);
        }
        
        CustomerResponse response = new CustomerResponse();
        response.setEmail(customer.getEmail());
        response.setId(customer.getId());
        response.setMobileNo(customer.getMobile());
        response.setName(customer.getName());
        response.setNic(customer.getNic());
        response.setUsername(customer.getUsername());
        response.setBusiness(customer.getBusiness());
        response.setPushToken(customer.getPushToken());

        return response;
		
	}

    @Override
    public CustomerResponse updateCustomerProfile(String customerId, CustomerResponse customerResponse) throws EmpowerHerBizException {
        
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}		

        Customer customerData = new Customer();

        customerData.setEmail(customerResponse.getEmail());
        customerData.setId(customerResponse.getId());
        customerData.setName(customerResponse.getName());
        customerData.setNic(customerResponse.getNic());
        customerData.setUsername(customerResponse.getUsername());

        customerData = customerDao.save(customerData);
        
        if(customerData == null) {
        	throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_DETAILS_UPDATE_FAILED);
        }
        
        CustomerResponse response = new CustomerResponse();
        response.setEmail(customerData.getEmail());
        response.setId(customerData.getId());
        response.setMobileNo(customerData.getMobile());
        response.setName(customerData.getName());
        response.setNic(customerData.getNic());
        response.setUsername(customerData.getUsername());
        response.setBusiness(customerData.getBusiness());
        
        return response;
    
    }

	@Override
	public CustomerResponse forgotPwd(ForgotPwdRequest forgotPwdRequest) throws Exception {
		
		String password = forgotPwdRequest.getPassword();
		
        Query query = new Query();
        query.addCriteria(Criteria.where("mobile").is(forgotPwdRequest.getMobileNo()));
        Customer customer = mongoTemplate.findOne(query, Customer.class);
        
        if(customer == null) {
        	throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_MOBILE);
        }
        
        String hashedPwd = EhbUtil.computeSHA256Hash(password);
        
        customer.setPassword(hashedPwd);
        
        customer = customerDao.save(customer);
        
        if(customer == null) {
        	throw new EmpowerHerBizException(EmpowerHerBizError.PWD_UPDATE_FAILED);
        }
        
        CustomerResponse response = new CustomerResponse();
        response.setEmail(customer.getEmail());
        response.setId(customer.getId());
        response.setMobileNo(customer.getMobile());
        response.setName(customer.getName());
        response.setNic(customer.getNic());
        response.setUsername(customer.getUsername());
        response.setBusiness(customer.getBusiness());

        return response;
	}
}
