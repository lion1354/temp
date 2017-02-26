package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tibco.ma.model.ScaleStaticsValue;

@Service
@Scope("singleton")
public class OrderDispatcher {
 
    private String[] compIdArr = null; 
    private int[] dispatchCountArr = null; 
    
    private ArrayList<ScaleStaticsValue> list;
    
    @Resource
    private ScaleService service;
    
    @PostConstruct
    public void init() throws Exception {
    	Comparator<ScaleStaticsValue> comparator = new Comparator<ScaleStaticsValue>(){
    	    public int compare(ScaleStaticsValue s1, ScaleStaticsValue s2) {
    		    if(s1.getOrderCnt()!=s2.getOrderCnt()){
    		       return s1.getOrderCnt()-s2.getOrderCnt();
    		    }
    			return 0;
    	    }
    	};
    	
    	Query query = new Query(Criteria.where("status").is(1));
    	list = (ArrayList<ScaleStaticsValue>) service.find(query, ScaleStaticsValue.class);
    	Collections.sort(list, comparator);
    	
        if (list.isEmpty()) {
        	list = new ArrayList<ScaleStaticsValue>();
        	ScaleStaticsValue value = new ScaleStaticsValue();
        	value.setName("ScheduleServer1");
        	value.setOrderCnt(0);
        	value.setStatus(1);
        	value = service.saveValue(value);
        	list.add(value);
        }
        
        compIdArr = new String[list.size()];
        dispatchCountArr = new int[list.size()];
        
        int size = list.size();
        for (int i = 0; i < size; i++) {
            compIdArr[i] = list.get(i).getName();
 
            if (i == size - 1) {
                //the last
                dispatchCountArr[i] = Integer.MAX_VALUE;
            } else {
                dispatchCountArr[i] =
                    list.get(i + 1).getOrderCnt()
                        - list.get(i).getOrderCnt();
                dispatchCountArr[i] = dispatchCountArr[i] * (i + 1);
            }
        }
    }
 
    public String getDispatchedcompId() throws Exception {
        String compId = null;
        if (dispatchCountArr == null) {
            return compId;
        }
 
        for (int i = 0; i < dispatchCountArr.length; i++) {
            if (dispatchCountArr[i] == 0) {
                continue;
            } else {
                int index = 0;
                if (i != 0) {
                    index = dispatchCountArr[i] % (i + 1);
                }
                compId = compIdArr[index];
                dispatchCountArr[i]--;
                list.get(index).setOrderCnt(list.get(index).getOrderCnt()+1);
                service.save(list.get(index));
                break;
            }
        }
        return compId;
    }
    
    public ArrayList<ScaleStaticsValue> getScaleStaticsslist(){
    	return list;
    } 
    
}