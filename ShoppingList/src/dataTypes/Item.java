package dataTypes;

public final class Item {
	private Long id;
	private Long listId;
	private String name;
	private String date;
	private String time;
	private String dateTime;

	private Float price;
	private Float priceAsda;
	private Float priceTesco;
	private Integer quantity;
	private Integer purchased;
	
	public Item() {
		
	}
	
	public Item(Item other) {
		this.copy(other);
	}
	
	public Item(Long id, Long listId, String name, String date, String time, String dateTime) {
		this.set(id, listId, name, date, time, dateTime);
	}
	
	public Item(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Integer purchased) {
		this.set(id, listId, name, date, time, dateTime, quantity, price, purchased);
	}
	
	public Item(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Double price, Integer purchased) {
		this.set(id, listId, name, date, time, dateTime, quantity, price, purchased);
	}
	
	public Item(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Float priceAsda, Float priceTesco, Integer purchased) {
		this.set(id, listId, name, date, time, dateTime, quantity, price, priceAsda, priceTesco, purchased);
	}
	
	public Item(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Double price, Double priceAsda, Double priceTesco, Integer purchased) {
		this.set(id, listId, name, date, time, dateTime, quantity, price, priceAsda, priceTesco, purchased);
	}
	
	public boolean copy(Item other) {
		this.id = other.id;
		this.listId = other.listId;
		this.name = other.name;
		this.date = other.date;
		this.time = other.time;
		this.dateTime = other.dateTime;
		
		this.price = other.price;
		this.priceAsda = other.priceAsda;
		this.priceTesco = other.priceTesco;
		this.quantity = other.quantity;
		this.purchased = other.purchased;
		
		return true;
	}
	
	public boolean set(Long id, Long listId, String name, String date, String time, String dateTime) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		
		return true;
	}
	
	public boolean set(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Double price, Integer purchased) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.quantity = quantity;
		this.price = priceAsda.floatValue();
		this.purchased = purchased;
		
		return true;
	}
	
	public boolean set(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Integer purchased) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.quantity = quantity;
		this.price = price;
		this.purchased = purchased;
		
		return true;
	}
	
	public boolean set(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Double price, Double priceAsda, Double priceTesco, Integer purchased) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.quantity = quantity;
		this.price = price.floatValue();
		this.priceAsda = priceAsda.floatValue();
		this.priceTesco = priceTesco.floatValue();
		this.purchased = purchased;
		
		return true;
	}
	
	public boolean set(Long id, Long listId, String name, String date, String time, String dateTime, Integer quantity, Float price, Float priceAsda, Float priceTesco, Integer purchased) {
		this.id = id;
		this.listId = listId;
		this.name = name;
		this.date = date;
		this.time = time;
		this.dateTime = dateTime;
		this.quantity = quantity;
		this.price = price;
		this.priceAsda = priceAsda;
		this.priceTesco = priceTesco;
		this.purchased = purchased;
		
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price.floatValue();
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public void setPrice(String price) {
		this.price = Float.parseFloat(price);
	}

	public Float getPriceAsda() {
		return priceAsda;
	}

	public void setPriceAsda(Double priceAsda) {
		this.priceAsda = priceAsda.floatValue();
	}
	
	public void setPriceAsda(Float priceAsda) {
		this.priceAsda = priceAsda;
	}
	
	public void setPriceAsda(String priceAsda) {
		this.priceAsda = Float.parseFloat(priceAsda);
	}

	public Float getPriceTesco() {
		return priceTesco;
	}

	public void setPriceTesco(Double priceTesco) {
		this.priceTesco = priceTesco.floatValue();
	}
	
	public void setPriceTesco(Float priceTesco) {
		this.priceTesco = priceTesco;
	}
	
	public void setPriceTesco(String priceTesco) {
		this.priceTesco = Float.parseFloat(priceTesco);
	}
	
	public Integer getPurchased() {
		return purchased;
	}

	public void setPurchased(Integer purchased) {
		this.purchased = purchased;
	}
}
