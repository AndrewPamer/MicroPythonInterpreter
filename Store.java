//Store.java
//Andrew Pamer
//ajp0317


import java.util.ArrayList;
import java.util.Iterator;
class StoreType {
	protected String name;
	protected ArrayList<Integer> list;
	
	
	public StoreType(ArrayList<Integer> list, String name) {
		this.list = list;
		this.name = name;
	}
	

}



class Store {
	private ArrayList<StoreType> StoreObj;
	
	public Store() {
		StoreObj = new ArrayList<StoreType>();
	}
	
	public ArrayList<Integer> update(ArrayList<Integer> value, String name) {
		StoreType s = new StoreType(value, name);
		StoreObj.add(s);
		return value;
	}
	public ArrayList<Integer> getValue(String n) {
		ArrayList<Integer> value = new ArrayList<Integer>();
		Iterator<StoreType> it = StoreObj.iterator();
		while(it.hasNext()) {
			StoreType s = it.next();
			if(s.name.equals(n))
				value = s.list;
		}
		
		return value;
	}	
	
	public void showStore() {
		Iterator<StoreType> it = StoreObj.iterator();
		while(it.hasNext()) {
			StoreType s = it.next();
			System.out.println(s.name + " " + s.list.get(0));
		}
	}
	

}