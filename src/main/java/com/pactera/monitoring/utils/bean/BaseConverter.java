package com.pactera.monitoring.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseConverter<DO, DTO> {
	    private static final Logger logger = LoggerFactory.getLogger(BaseConverter.class);

	    /**
	     * 单个对象转换
	     */
	    public DTO convert(DO from, Class<DTO> clazz) {
	        if (from == null) {
	            return null;
	        }
	        DTO to = null;
	        try {
	            to = clazz.newInstance();
	        } catch (Exception e) {
	            logger.error("初始化{}对象失败。", clazz, e);
	        }
	        convert(from, to);
	        return to;
	    }

	    /**
	     * 批量对象转换
	     */
	    public List<DTO> convert(List<DO> fromList, Class<DTO> clazz) {
	        if (CollectionUtils.isEmpty(fromList)) {
	            return null;
	        }
	        List<DTO> toList = new ArrayList<DTO>();
	        for (DO from : fromList) {
	            toList.add(convert(from, clazz));
	        }
	        return toList;
	    }
	    

	    /**
	     * 属性拷贝方法，有特殊需求时子类覆写此方法
	     */
	    protected void convert(DO from, DTO to) {
	        BeanUtils.copyProperties(from, to);
	    }
	    

	}
