package com.tibco.ma.test;


public class TreeTest {
	/*
	
	public static void main(String[] args) {
		List<AdminMenu> list = new ArrayList<AdminMenu>();
		AdminMenu root = new AdminMenu();
		root.setId("1");

		AdminMenu m1 = new AdminMenu();
		m1.setId("a");
		m1.setParent(root);

		
		
		AdminMenu m2 = new AdminMenu();
		m2.setId("a2");
		m2.setParent(m1);

		AdminMenu root2 = new AdminMenu();
		root2.setId("dddd");

		AdminMenu m12 = new AdminMenu();
		m12.setId("add");
		m12.setParent(root2);

		AdminMenu m22 = new AdminMenu();
		m22.setId("a2d");
		m22.setParent(m12);

		list.add(root);
		list.add(root2);
		list.add(m1);
		list.add(m2);
		list.add(m12);
		list.add(m22);

		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		List<AdminMenu> roots = buildTree(list);
		
		
		
		System.out.println(org.json.simple.JSONArray.toJSONString(roots));
		
	}

	private static List<AdminMenu> buildTree(List<AdminMenu> list) {
		List<AdminMenu> root = new ArrayList<AdminMenu>();
		for (int i = 0; i < list.size(); i++) {
			AdminMenu menu = list.get(i);

			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getParent() != null
						&& menu.getId().equals(list.get(j).getParent().getId())) {
					if (menu.getChildrens() == null) {
						menu.setChildrens(new ArrayList<AdminMenu>());
					}
					menu.getChildrens().add(list.get(j));
				}
			}

			if (menu.getParent() == null) {
				root.add(menu);
			}
		}
		return root;
	}
	*/
	public static void main(String[] args) {
		
	}
}
