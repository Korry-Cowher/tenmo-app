package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.JDBCTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
@RequestMapping("")
public class TransferController {

	private TransferDAO dao;
	
	private SingleConnectionDataSource dataSource;
	
	private SingleConnectionDataSource setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
	}
	
	public TransferController() {
		this.dao = new JDBCTransferDAO(setupDataSource());
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping( path = "/transfers", method = RequestMethod.GET)
	public List<Transfer> list() {
		return dao.list();
	}
	
	@PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@Valid @PathVariable int id) {
    	return dao.getTransfer(id);
    }
    
	@PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/Createtransfer", method = RequestMethod.POST)
    public Transfer create(@Valid @RequestBody Transfer transfer) {
    	return dao.create(transfer);
    }
    
	@PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/executeTransfer", method = RequestMethod.POST)
    public void executeTransfer(@Valid @RequestBody Transfer transfer) {
    	dao.executeTransfer(transfer);
    }
    

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping( path="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@Valid @PathVariable int id) {
    	dao.delete(id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/pendingtransfers/{userId}", method = RequestMethod.GET)
    public List<Transfer> getPendingRequests(@PathVariable Integer userId) {
    	return dao.getPendingRequests(userId);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/executeRequest", method = RequestMethod.PUT)
    public void executeRequest(@RequestBody Transfer transfer) {
    	dao.executeRequest(transfer);
    }
	
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/rejectRequest/{id}", method = RequestMethod.PUT)
    public void rejectRequest(@PathVariable int id) {
    	dao.rejectRequest(id);
    }
}
