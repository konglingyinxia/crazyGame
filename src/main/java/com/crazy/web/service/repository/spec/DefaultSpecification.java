package com.crazy.web.service.repository.spec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultSpecification<T> implements Specification<T> {

	List<SpecDomian> params;

	public DefaultSpecification(){
		params = new ArrayList<>();
	}

	public void setParams(String field, String op, Object value) {
		SpecDomian spec = new SpecDomian();
		spec.setField(field);
		spec.setOp(op);
		spec.setValue(value);

		this.params.add(spec);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> pl = new ArrayList<Predicate>();
		if (params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				SpecDomian s = params.get(i);
				if (StringUtils.equals("eq", s.getOp()) && null != s.getValue()) {
					Path<String> path = root.get(s.getField());
					pl.add(cb.equal(path, s.getValue()));
				} else if (StringUtils.equals("like", s.getOp()) && null != s.getValue()) {
					Path<String> path = root.get(s.getField());
					pl.add(cb.like(path, (String) s.getValue()));
				} else if (StringUtils.equals(">", s.getOp()) && null != s.getValue()) {
					Number n = get(s.getValue());
					if (null != n) {
						Path<Number> path = root.get(s.getField());
						pl.add(cb.gt(path, n));
					} else if (null != getDate(s.getValue())) {
						Path<Date> path = root.get(s.getField());
						pl.add(cb.greaterThan(path, getDate(s.getValue())));
					}
				} else if (StringUtils.equals("<", s.getOp()) && null != s.getValue()) {
					Number n = get(s.getValue());
					if (null != n) {
						Path<Number> path = root.get(s.getField());
						pl.add(cb.gt(path, n));
					} else if (null != getDate(s.getValue())) {
						Path<Date> path = root.get(s.getField());
						pl.add(cb.lessThan(path, getDate(s.getValue())));
					}
				} else if (StringUtils.equals(">=", s.getOp()) && null != s.getValue()) {
					Number n = get(s.getValue());
					if (null != n) {
						Path<Number> path = root.get(s.getField());
						pl.add(cb.gt(path, n));
					} else if (null != getDate(s.getValue())) {
						Path<Date> path = root.get(s.getField());
						pl.add(cb.greaterThanOrEqualTo(path, getDate(s.getValue())));
					}
				} else if (StringUtils.equals("<=", s.getOp()) && null != s.getValue()) {
					Number n = get(s.getValue());
					if (null != n) {
						Path<Number> path = root.get(s.getField());
						pl.add(cb.gt(path, n));
					} else if (null != getDate(s.getValue())) {
						Path<Date> path = root.get(s.getField());
						pl.add(cb.lessThanOrEqualTo(path, getDate(s.getValue())));
					}
				}
			}
			Predicate[] pa = new Predicate[pl.size()];
			for (int i = 0; i < pl.size(); i++) {
				pa[i] = pl.get(i);
			}
			query.where(pa);
		}
		return null;
	}

	private Number get(Object param) {
		if (param instanceof Integer) {
			return ((Integer) param).intValue();
		} else if (param instanceof Double) {
			return ((Double) param).doubleValue();
		} else if (param instanceof Float) {
			return ((Float) param).floatValue();
		} else if (param instanceof Long) {
			return ((Long) param).longValue();
		}
		return null;
	}

	private Date getDate(Object param) {
		if (param instanceof Date) {
			return (Date) param;
		} else if (param instanceof String) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(String.valueOf(param));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		}
		return null;
	}
}
