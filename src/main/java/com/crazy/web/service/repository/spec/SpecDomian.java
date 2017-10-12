package com.crazy.web.service.repository.spec;

/**
 * 做一个浪超
 * 
 * @ClassName: SpecDomian
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author dave
 * @date 2017年9月27日 下午7:18:37
 */
public class SpecDomian {

	private String field;
	private String op;// eq,ne,like,in,ni
	private Object value;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
