package dataTypes;

public final class Market {
	private Long id;
	private String name;
	private String date;
	private String time;
	private String dateTime;

	private Long numItems;
	private Long totalPrice;

	public Market() {

	}
	
	public Market(Market other) {
		this.copy(other);
	}

	public Market(Long id, String name, String date, String time, String dateTime) {
		this.set(id, name, date, time, dateTime);
	}

	public Market(Long id, String name, String date, String time, String dateTime, Long numItems) {
		this.set(id, name, date, time, dateTime, numItems);
	}
	
	public Market(Long id, String name, String date, String time, String dateTime, Long numItems, Long totalPrice) {
		this.set(id, name, date, time, dateTime, numItems, totalPrice);
	}
	
	public boolean copy (Market other) {
		this.id = other.id;
		this.name = other.name;
		this.date = other.date;
		this.time = other.time;
		this.dateTime = other.dateTime;
		
		this.numItems = other.numItems;
		this.totalPrice = other.numItems;
		
		return true;
	}
	
	public boolean set(Long id, String name, String date, String time, String dateTime) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		
		return true;
	}

	public boolean set(Long id, String name, String date, String time, String dateTime, Long numItems) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.numItems = numItems;
		
		return true;
	}
	
	public boolean set(Long id, String name, String date, String time, String dateTime, Long numItems, Long totalPrice) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.numItems = numItems;
		this.totalPrice = totalPrice;
		
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id.longValue();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public Long getNumItems() {
		return numItems;
	}

	public void setNumItems(Long numItems) {
		this.numItems = numItems;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}
}
