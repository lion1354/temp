package com.hsbc.demo.common;

import java.util.ArrayList;
import java.util.List;

public class JAXBGenericWrapper<T> {
	private List<T> items;

	public JAXBGenericWrapper() {
		items = new ArrayList<>();
	}

	public JAXBGenericWrapper(List<T> items) {
		this.items = items;
	}

	
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
