package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddRawMaterialRequest;

public interface RawMaterialService {

	public HashMap<String, String> addRawMaterial(AddRawMaterialRequest addRawMaterialRequest, String customerId) throws EmpowerHerBizException;

	public HashMap<String, String> updateRawMaterial(AddRawMaterialRequest addRawMaterialRequest, String id) throws EmpowerHerBizException;

	public HashMap<String, String> deleteRawMaterial(String customerId, String id) throws EmpowerHerBizException;

}
