package com.pactera.monitoring.enums;

import java.math.BigDecimal;

public enum MethodType {
	SELECT(new BigDecimal(1)),
	ADD(new BigDecimal(2)),
	UPDATE(new BigDecimal(3)),
	DELETE(new BigDecimal(4)),
	SAVE(new BigDecimal(5)),
	PASS(new BigDecimal(6)),
	REJECT(new BigDecimal(7)),
	GOBACK(new BigDecimal(8));
	
	private  MethodType(BigDecimal type){
		this.type = type;
	}
	public BigDecimal getType(){
		return type;
	}
	BigDecimal type;
	
}
