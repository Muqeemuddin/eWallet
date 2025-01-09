package com.moneyfli.user_service.service;

import com.moneyfli.user_service.kafka.CustomerKafkaProducer;
import com.moneyfli.user_service.model.Customer;
import com.moneyfli.user_service.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerDao customerDao;


    @Autowired
    private CustomerKafkaProducer customerKafkaProducer;

    public String createOrUpdateCustomer(Customer customer) {
        try {
            customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
            customerDao.save(customer);
            customerKafkaProducer.sendCustomerCreatedMessage(
                    String.format("Customer %s created successfully with mobile number %s",
                            customer.getName(), customer.getPhoneNo()));
            return "Customer created successfully";
        } catch (Exception e) {
            return "Error creating customer";
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerDao.findByPhoneNo(username);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + username);
        }
        return User.builder().username(customer.getPhoneNo()).password(customer.getPassword()).roles("USER").build();
    }

    public Customer getCustomerByPhoneNo(String phoneNo) {
        return customerDao.findByPhoneNo(phoneNo);
    }


}
