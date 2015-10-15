package dataTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import org.json.*;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

public final class JsonList {
	Market shop = null;
	ArrayList<Item> goodList = null;
	JSONObject jsonBuffer = null;
	String data = null;

	public JsonList() {

	}

	public JsonList(Market shop) {
		this.set(shop);
	}

	public JsonList(ArrayList<Item> goodList) {
		this.set(goodList);
	}

	public JsonList(Market shop, ArrayList<Item> goodList) {
		this.set(shop, goodList);
	}

	public boolean set(Market shop) {
		if (this.shop == null) {
			this.shop = new Market(shop);
		} else {
			this.shop.copy(shop);
		}
		return true;
	}

	public boolean set(ArrayList<Item> goodList) {
		if (this.goodList != null) {
			this.goodList.clear();
		}
		this.goodList = new ArrayList<Item>(goodList.size());
		for (Item good : goodList) {
			this.goodList.add(good);
		}
		return true;
	}

	public boolean set(Market shop, ArrayList<Item> goodList) {
		this.set(shop);
		this.set(goodList);
		return true;
	}

	public boolean generateObject() {
		try {
			this.shop = new Market();
			this.goodList = new ArrayList<Item>();
			JSONObject jsonShop = new JSONObject(this.data);
			JSONArray jsonArray = jsonShop.getJSONArray("itemList");

			this.shop.setName(jsonShop.getString("name"));
			this.shop.setDate(jsonShop.getString("date"));
			this.shop.setTime(jsonShop.getString("time"));
			this.shop.setDateTime(jsonShop.getString("dateTime"));
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonGood = jsonArray.getJSONObject(index);
				Item good = new Item();
				if (jsonGood.has("name"))
					good.setName(jsonGood.getString("name"));
				if (jsonGood.has("date"))
					good.setDate(jsonGood.getString("date"));
				if (jsonGood.has("time"))
					good.setTime(jsonGood.getString("time"));
				if (jsonGood.has("dateTime"))
					good.setDateTime(jsonGood.getString("dateTime"));
				if (jsonGood.has("price"))
					good.setPrice(jsonGood.getDouble("price"));
				if (jsonGood.has("priceAsda"))
					good.setPriceAsda(jsonGood.getDouble("priceAsda"));
				if (jsonGood.has("priceTesco"))
					good.setPriceTesco(jsonGood.getDouble("priceTesco"));
				if (jsonGood.has("purchased"))
					good.setPurchased(jsonGood.getInt("purchased"));
				if (jsonGood.has("quantity"))
					good.setQuantity(jsonGood.getInt("quantity"));
				this.goodList.add(good);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveSqlObject(Activity context) {
		try {
			String query = "INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('" + this.shop.getName() + "', '"
					+ this.shop.getDate() + "', '" + this.shop.getTime() + "', '" + this.shop.getDateTime() + "');'";
			DataBaseHelper dataHelper = new DataBaseHelper(context);
			SQLiteDatabase dataSet = dataHelper.getWritableDatabase();
			dataSet.execSQL(query);
			try {
				shop.setId(this.getAddedListId(context));
				for (int index = 0; index < this.goodList.size(); index++) {
					query = "INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES ("
							+ this.shop.getId() + ", '" + this.goodList.get(index).getName() + "', '"
							+ this.goodList.get(index).getQuantity() + "', '" + this.goodList.get(index).getDate() + "', '"
							+ this.goodList.get(index).getTime() + "', '" + this.goodList.get(index).getDateTime() + "', '"
							+ this.goodList.get(index).getPurchased() + "', '" + this.goodList.get(index).getPrice() + "')";
					dataSet.execSQL(query);
				}
				Toast.makeText(context, "List '" + this.shop.getName() + "' imported successfully", Toast.LENGTH_LONG).show();
			} catch (IndexOutOfBoundsException e) {
				Toast.makeText(context, "There is a serious error in your database", Toast.LENGTH_LONG).show();
			}
			dataSet.close();
			return true;
		} catch (SQLiteConstraintException e) {
			Toast.makeText(context, "List '" + this.shop.getName() + "' already exists", Toast.LENGTH_LONG).show();
		}
		return false;
	}

	private int getAddedListId(Activity context) throws IndexOutOfBoundsException {
		int listId = -1;
		String query = "SELECT _ID FROM lists WHERE listName = '" + this.shop.getName() + "' AND listDate = '" + this.shop.getDate()
				+ "' AND listTime = '" + this.shop.getTime() + "'";
		DataBaseHelper dataHelper = new DataBaseHelper(context);
		SQLiteDatabase dataSet = dataHelper.getReadableDatabase();
		Cursor dataRow = dataSet.rawQuery(query, null);

		if (dataRow.getCount() > 0) {
			dataRow.moveToFirst();
			listId = dataRow.getInt(0);
			dataSet.close();
		}
		if (listId < 0) {
			throw new IndexOutOfBoundsException();
		}
		return listId;
	}

	public boolean generateJson() {
		JSONArray jsonArray = new JSONArray();
		this.jsonBuffer = new JSONObject();

		try {
			for (Item good : this.goodList) {
				JSONObject jsonItem = new JSONObject();

				jsonItem.put("name", good.getName());
				jsonItem.put("date", good.getDate());
				jsonItem.put("time", good.getTime());
				jsonItem.put("dateTime", good.getDateTime());
				jsonItem.put("price", good.getPrice());
				jsonItem.put("priceAsda", good.getPriceAsda());
				jsonItem.put("priceTesco", good.getPriceTesco());
				jsonItem.put("quantity", good.getQuantity());
				jsonItem.put("purchased", good.getPurchased());

				jsonArray.put(jsonItem);
			}
			jsonBuffer.put("name", this.shop.getName());
			jsonBuffer.put("date", this.shop.getDate());
			jsonBuffer.put("time", this.shop.getTime());
			jsonBuffer.put("dateTime", this.shop.getDateTime());
			jsonBuffer.put("itemList", jsonArray);
			return true;
		} catch (JSONException e) {
			System.err.println(jsonBuffer);
			return false;
		}
	}

	@SuppressWarnings("resource")
	public boolean open(Activity context, String path) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(path);

			if (file.exists() && file.isFile()) {
				try {
					FileReader reader = new FileReader(file);
					BufferedReader buffer = new BufferedReader(reader);
					this.data = buffer.readLine();
					// StringBuilder inFile = new StringBuilder();
					// while ((line = buffer.readLine()) != null) {
					// inFile.append(line);
					// inFile.append('\n');
					// }
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(context.getApplicationContext(), "SD card not present", Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public boolean save(Activity context) throws FileNotFoundException {
		boolean mExternalStorageReadable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageReadable = true;
			mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageReadable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageReadable = false;
			mExternalStorageWriteable = false;
		}

		if (mExternalStorageReadable && mExternalStorageWriteable) {
			File folder = new File(Environment.getExternalStorageDirectory().toString() + "/ShoppingList");
			File file = new File(folder, this.shop.getName() + " " + this.shop.getDateTime().replaceAll(":", "-") + ".json");
			FileOutputStream outStream = new FileOutputStream(file);
			OutputStreamWriter outFile = new OutputStreamWriter(outStream);

			try {
				if (!folder.exists()) {
					folder.mkdir();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				outFile.write(jsonBuffer.toString());
				Toast.makeText(context.getApplicationContext(), "List '" + this.shop.getName() + "' exported successfully", Toast.LENGTH_LONG).show();
				outFile.close();
				outStream.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context.getApplicationContext(), "SD card not present", Toast.LENGTH_LONG).show();
		}
		return false;
	}
}
