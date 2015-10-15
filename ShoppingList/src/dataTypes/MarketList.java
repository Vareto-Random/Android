package dataTypes;

import java.util.ArrayList;

public class MarketList {
	private ArrayList<Market> list = null;

	public MarketList() {
		this.list = new ArrayList<Market>();
	}

	public MarketList(ArrayList<Market> list) {
		this.list = list;
	}
	
	public MarketList(Market shop) {
		this.list = new ArrayList<Market>();
		this.add(shop);
	}
	
	public MarketList(Long id, String name, String date, String time, String dateTime) {
		this.list = new ArrayList<Market>();
		this.add(id, name, date, time, dateTime);
	}

	public MarketList(Long id, String name, String date, String time, String dateTime, Long numItems) {
		this.list = new ArrayList<Market>();
		this.add(id, name, date, time, dateTime, numItems);
	}
	
	public MarketList(Long id, String name, String date, String time, String dateTime, Long numItems, Long totalPrice) {
		this.list = new ArrayList<Market>();
		this.add(id, name, date, time, dateTime, numItems, totalPrice);
	}
	
	public void clear() {
		this.list.clear();
	}
	
	public boolean add(Market shop) {
		if (this.list == null) {
			this.list = new ArrayList<Market>();
		}
		this.list.add(shop);
		return true;
	}
	
	public boolean add(Long id, String name, String date, String time, String dateTime) {
		Market temp = new Market(id, name, date, time, dateTime);	
		if (this.list == null) {
			this.list = new ArrayList<Market>();
		}
		this.list.add(temp);
		return true;
	}

	public boolean add(Long id, String name, String date, String time, String dateTime, Long numItems) {
		Market temp = new Market(id, name, date, time, dateTime, numItems);
		if (this.list == null) {
			this.list = new ArrayList<Market>();
		}
		this.list.add(temp);
		return true;
	}
	
	public boolean add(Long id, String name, String date, String time, String dateTime, Long numItems, Long totalPrice) {
		Market temp = new Market(id, name, date, time, dateTime, numItems, totalPrice);
		if (this.list == null) {
			this.list = new ArrayList<Market>();
		}
		this.list.add(temp);
		return true;
	}
	
	public Market get(Integer index) {
		return list.get(index);
	}
	
	public void set(Integer index, Market shop) {
		this.list.set(index, shop);
	}

	public ArrayList<Market> getList() {
		return list;
	}

	public void setList(ArrayList<Market> list) {
		this.list = list;
	}
	
	public ArrayList<Long> getIds() {
		ArrayList<Long> ids = new ArrayList<Long>();
		for(int index = 0; index < this.list.size(); index++) {
			ids.add( this.list.get(index).getId() );
		}
		return ids;
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
	
	public ArrayList<Long> getNumItems() {
		ArrayList<Long> numItems = new ArrayList<Long>();
		for(int index = 0; index < this.list.size(); index++) {
			numItems.add( this.list.get(index).getNumItems() );
		}
		return numItems;
	}	
	
	public ArrayList<Long> getTotalPrices() {
		ArrayList<Long> totalPrices = new ArrayList<Long>();
		for(int index = 0; index < this.list.size(); index++) {
			totalPrices.add( this.list.get(index).getTotalPrice() );
		}
		return totalPrices;
	}	
}
