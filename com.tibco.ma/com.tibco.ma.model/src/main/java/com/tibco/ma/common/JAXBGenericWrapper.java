package com.tibco.ma.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

public class JAXBGenericWrapper<T> {
	private List<T> items;

	public JAXBGenericWrapper() {
		items = new ArrayList<>();
	}

	public JAXBGenericWrapper(List<T> items) {
		this.items = items;
	}

	@XmlAnyElement(lax = true)//listԪ��
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return items != null ? items.toString() : "";
	}
}
