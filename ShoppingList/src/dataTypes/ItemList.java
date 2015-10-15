package dataTypes;

import java.util.ArrayList;

public class ItemList {
	private ArrayList<Item> list = null;

	public ItemList() {
		this.list = new ArrayList<Item>();
	}

	public ItemList(ArrayList<Item> list) {
		this.list = list;
	}
	
	public ItemList(Item good) {
		this.list = new ArrayList<Item>();
		this.add(good);
	}
	
	public ItemList(Long id, Long listId, String name, String date, String time, String dateTime) {
		this.list = new ArrayList<Item>();
		this.add(id, listId, name, date, time, dateTime);
	}
	
	public ItemList(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Integer purchased) {
		this.list = new ArrayList<Item>();
		this.add(id, listId, name, date, time, dateTime, quantity, price, purchased);
	}
	
	public void clear() {
		this.list.clear();
	}
	
	public boolean add(Item shop) {
		if (this.list == null) {
			this.list = new ArrayList<Item>();
		}
		this.list.add(shop);
		return true;
	}
	
	public boolean add(Long id, Long listId, String name, String date, String time, String dateTime) {
		Item temp = new Item(id, listId, name, date, time, dateTime);	
		if (this.list == null) {
			this.list = new ArrayList<Item>();
		}
		this.list.add(temp);
		return true;
	}
	
	public boolean add(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Integer purchased) {
		Item temp = new Item(id, listId, name, date, time, dateTime, quantity, price, purchased);
		if (this.list == null) {
			this.list = new ArrayList<Item>();
		}
		this.list.add(temp);
		return true;
	}

	public ArrayList<Item> getList() {
		return list;
	}

	public void setList(ArrayList<Item> list) {
		this.list = list;
	}
	
	public ArrayList<Long> getIds() {
		ArrayList<Long> ids = new ArrayList<Long>();
		for(int index = 0; index < this.list.size(); index++) {
			ids.add( this.list.get(index).getId() );
		}
		return ids;
	}
	
	public ArrayList<Long> getListIds() {
		ArrayList<Long> listIds = new ArrayList<Long>();
		for(int index = 0; index < this.list.size(); index++) {
			listIds.add( this.list.get(index).getListId() );
		}
		return listIds;
	}
	
	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(int index = 0; index < this.list.size(); index++) {
			names.add( this.list.get(index).getName() );
		}
		return names;
	}
	
	public ArrayList<String> getDates() {
		ArrayList<String> dates = new ArrayList<String>();
		for(int index = 0; index < this.list.size(); index++) {
			dates.add( this.list.get(index).getDate() );
		}
		return dates;
	}
	
	public ArrayList<String> getTimes() {
		ArrayList<String> times = new ArrayList<String>();
		for(int index = 0; index < this.list.size(); index++) {
			times.add( this.list.get(index).getTime() );
		}
		return times;
	}
	
	public ArrayList<String> getDatesTimes() {
		ArrayList<String> datesTimes = new ArrayList<String>();
		for(int index = 0; index < this.list.size(); index++) {
			datesTimes.add( this.list.get(index).getDateTime() );
		}
		return datesTimes;
	}
	
	public ArrayList<Integer> getQuantities() {
		ArrayList<Integer> quantities = new ArrayList<Integer>();
		for(int index = 0; index < this.list.size(); index++) {
			quantities.add( this.list.get(index).getQuantity() );
		}
		return quantities;
	}
	
	public ArrayList<Float> getPrices() {
		ArrayList<Float> prices = new ArrayList<Float>();
		for(int index = 0; index < this.list.size(); index++) {
			prices.add( this.list.get(index).getPrice() );
		}
		return prices;
	}
	
	public ArrayList<Integer> getPurchaseds() {
		ArrayList<Integer> purchaseds = new ArrayList<Integer>();
		for(int index = 0; index < this.list.size(); index++) {
			purchaseds.add( this.list.get(index).getQuantity() );
		}
		return purchaseds;
	}
}
