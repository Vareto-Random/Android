package outlook;

import java.util.ArrayList;

import com.example.shoppinglist.R;

import dataTypes.DataBaseHelper;
import dataTypes.Item;
import dataTypes.Market;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.support.v4.app.NavUtils;
import android.text.InputFilter;
import android.text.InputType;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;

public class ItemActivity extends Activity implements OnClickListener {
	@SuppressLint("NewApi")
	private Button widgetAdd;
	private ListView widgetList;
	private AutoCompleteTextView widgetItem;

	private Intent global = null;
	private String message = null;
	private Market shop = null;
	private ArrayList<Item> goodList = null;
	private ArrayList<String> listOfLists = null;
	private ArrayAdapter<String> adapterOfLists = null;
	private ArrayList<String> history = null;
	private ArrayAdapter<String> adapterOfHistory = null;

	private SharedPreferences settings;
	private StringBuilder settingsList;

	private String query = null;
	private DataBaseHelper dataHelper = null;
	private SQLiteDatabase dataSet = null;
	private Cursor dataRow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_item);
		this.setupActionBar();

		// Get the message from the intent
		this.global = getIntent();
		this.message = global.getStringExtra(ListActivity.EXTRA_MESSAGE);

		this.renameComponents();
		this.fetchSettings();
		this.fetchTitleData();
		this.fetchTextAutoComplete();
		this.fetchListData();

		widgetAdd.setOnClickListener(this);
		widgetList.setOnCreateContextMenuListener(this);
		widgetList.setOnItemClickListener(onItemClick);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataSet.close();
		dataHelper.close();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add_item:
			this.insertItem();
			this.widgetItem.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		menu.setHeaderTitle(goodList.get(info.position).getName());
		menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Price");
		menu.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "Quantity");
		menu.add(Menu.NONE, Menu.FIRST + 3, Menu.NONE, "Rename");
		menu.add(Menu.NONE, Menu.FIRST + 4, Menu.NONE, "Remove");
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.options, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case android.R.id.home:
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//			break;
		case R.id.asda:
			Toast.makeText(this, "Importing data from ASDA", Toast.LENGTH_LONG).show();
			this.importAsda();
			break;
		case R.id.tesco:
			Toast.makeText(this, "Importing data from Tesco", Toast.LENGTH_LONG).show();
			this.importTesco();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1: // price
			this.onContextItemPrice(item);
			break;
		case Menu.FIRST + 2: // quantity
			this.onContextItemQuantity(item);
			break;
		case Menu.FIRST + 3: // rename
			this.onContextItemRename(item);
			break;
		case Menu.FIRST + 4: // delete
			this.onContextItemRemove(item);
			break;
		default:
			return false;
		}
		return true;
	}

	private void renameComponents() {
		this.widgetItem = (AutoCompleteTextView) findViewById(R.id.editText_add_item);
		this.widgetAdd = (Button) findViewById(R.id.button_add_item);
		this.widgetList = (ListView) findViewById(R.id.listView_add_item);
	}

	private void insertItem() {
		Item good = new Item();
		String itemName = this.widgetItem.getText().toString();
		if (itemName.length() > 0) {
			this.dataSet = this.dataHelper.getWritableDatabase();
			try {
				this.query = "INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES ("
						+ this.message + ", '" + itemName + "', 1, date('now'), time('now'), datetime('now'), 0)";
				this.dataSet.execSQL(this.query);
			} catch (SQLiteConstraintException e) {
				Toast.makeText(this, "Item '" + itemName + "' already exists", Toast.LENGTH_LONG).show();
				return;
			}
			try {
				this.query = "INSERT INTO history VALUES ('" + itemName + "')";
				this.dataSet.execSQL(this.query);
			} catch (SQLiteConstraintException e) {
				System.err.println("Item " + itemName + " was already added to history");
			}
			this.query = "SELECT _ID, listID, itemName, itemDate, itemTime, itemDateTime, itemQuantity, itemPrice, itemPurchased "
					+ "FROM items WHERE listID = " + message.toString() + " AND itemName = '" + itemName + "'";
			this.dataRow = this.dataSet.rawQuery(this.query, null);
			this.dataRow.moveToFirst();
			good.set(dataRow.getLong(0), dataRow.getLong(1), dataRow.getString(2), dataRow.getString(3), dataRow.getString(4),
					dataRow.getString(5), dataRow.getInt(6), dataRow.getFloat(7), dataRow.getInt(8));
			this.dataSet.close();
			this.compareItem(good);
		}
	}

	private void compareItem(final Item newGood) {
		if (this.settings.getBoolean("prefPriceComparison", false)) {
			final Item oldGood = new Item();
			this.query = "SELECT _ID, listID, itemName, itemDate, itemTime, itemDateTime, itemQuantity, MIN(itemPrice), itemPurchased FROM items WHERE itemName LIKE '"
					+ newGood.getName() + "' AND itemPrice != 0.0";
			this.dataSet = this.dataHelper.getReadableDatabase();
			this.dataRow = dataSet.rawQuery(this.query, null);
			this.dataRow.moveToFirst();
			if (!this.dataRow.isNull(0)) {
				oldGood.set(this.dataRow.getLong(0), this.dataRow.getLong(1), this.dataRow.getString(2), this.dataRow.getString(3),
						this.dataRow.getString(4), this.dataRow.getString(5), this.dataRow.getInt(6), this.dataRow.getFloat(7),
						this.dataRow.getInt(8));
				AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ItemActivity.this);
				deleteDialog.setTitle("Item Found");
				deleteDialog.setMessage("A similar item was previously added to another list.\nItem: " + oldGood.getName() + "\nPrice: "
						+ this.settings.getString("prefCurrencyType", "$") + oldGood.getPrice());
				System.out.println(oldGood.getId() + "\n" + oldGood.getName() + "\n" + oldGood.getQuantity() + "\n" + oldGood.getPrice());
				deleteDialog.setPositiveButton("Import item", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						query = "UPDATE items SET itemPrice = " + oldGood.getPrice() + " WHERE _ID = " + newGood.getId();
						dataSet = dataHelper.getWritableDatabase();
						dataSet.execSQL(query);
						fetchTitleData();
						fetchListData();
					}
				});
				deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						fetchListData();
						dialog.cancel();
					}
				});
				AlertDialog dialog = deleteDialog.create();
				dialog.show();
			} else {
				System.err.println("Item was not added to any list before");
			}
			this.dataSet.close();
		}
		this.fetchTitleData();
		this.fetchTextAutoComplete();
		this.fetchListData();
	}

	private void onContextItemPrice(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final EditText newPrice = new EditText(ItemActivity.this);
		newPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
		newPrice.setText(this.goodList.get(info.position).getPrice().toString());
		newPrice.setSelection(newPrice.getText().length());

		AlertDialog.Builder renameDialog = new AlertDialog.Builder(ItemActivity.this);
		renameDialog.setTitle("Price of " + this.goodList.get(info.position).getName());
		renameDialog.setView(newPrice);
		renameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Item good = goodList.get(info.position);
				query = "UPDATE items SET itemPrice = " + newPrice.getText().toString() + " WHERE _ID = " + good.getId();
				dataSet = dataHelper.getWritableDatabase();
				dataSet.execSQL(query);
				dataSet.close();
				fetchTitleData();
				fetchListData();
			}
		});
		renameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = renameDialog.create();
		dialog.show();
	}

	private void onContextItemQuantity(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final EditText newQuantity = new EditText(ItemActivity.this);
		newQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
		newQuantity.setText(this.goodList.get(info.position).getQuantity().toString());
		newQuantity.setSelection(newQuantity.getText().length());

		AlertDialog.Builder renameDialog = new AlertDialog.Builder(ItemActivity.this);
		renameDialog.setTitle("Quantity of " + this.goodList.get(info.position).getName());
		renameDialog.setView(newQuantity);
		renameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Item good = goodList.get(info.position);
				query = "UPDATE items SET itemQuantity = " + newQuantity.getText().toString() + " WHERE _ID = " + good.getId();
				dataSet = dataHelper.getWritableDatabase();
				dataSet.execSQL(query);
				dataSet.close();
				fetchTitleData();
				fetchListData();
			}
		});
		renameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = renameDialog.create();
		dialog.show();
	}

	private void onContextItemRename(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final EditText newName = new EditText(ItemActivity.this);
		newName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		newName.setText(this.goodList.get(info.position).getName());
		newName.setSelection(newName.getText().length());

		AlertDialog.Builder renameDialog = new AlertDialog.Builder(ItemActivity.this);
		renameDialog.setTitle("Rename item");
		renameDialog.setView(newName);
		renameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					Item good = goodList.get(info.position);
					query = "UPDATE items SET itemName = '" + newName.getText().toString() + "' WHERE _ID = " + good.getId();
					dataSet = dataHelper.getWritableDatabase();
					dataSet.execSQL(query);
					dataSet.close();
					fetchTitleData();
					fetchListData();
				} catch (SQLiteConstraintException e) {
					Toast.makeText(getApplicationContext(), "List '" + newName.getText().toString() + "' already exists", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		renameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = renameDialog.create();
		dialog.show();
	}

	private void onContextItemRemove(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ItemActivity.this);
		deleteDialog.setTitle("Remove " + this.goodList.get(info.position).getName());
		deleteDialog.setMessage("Are you sure?");
		deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dataSet = dataHelper.getWritableDatabase();
				query = "DELETE FROM items WHERE _ID = " + goodList.get(info.position).getId();
				dataSet.execSQL(query);
				dataSet.close();
				fetchTitleData();
				fetchListData();
			}
		});
		deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = deleteDialog.create();
		dialog.show();
	}

	private void fetchSettings() {
		this.settings = PreferenceManager.getDefaultSharedPreferences(this);
		this.settingsList = new StringBuilder();
		this.settingsList.append(this.settings.getString("prefCurrencyType", "$"));
	}

	private void fetchTitleData() {
		if (this.settings.getBoolean("prefSumItems", false)) {
			this.query = "SELECT L._ID, L.listName, L.listDate, L.listTime, L.listDateTime, COUNT(I.listID), SUM(I.itemQuantity * I.itemPrice * I.itemPurchased) "
					+ "FROM (lists AS L LEFT OUTER JOIN items as I ON L._ID = I.listID) WHERE L._ID = "
					+ message.toString()
					+ " GROUP BY I.listID ORDER BY L.listDateTime DESC";
		} else {
			this.query = "SELECT L._ID, L.listName, L.listDate, L.listTime, L.listDateTime, COUNT(I.listID), SUM(I.itemQuantity * I.itemPrice) "
					+ "FROM (lists AS L LEFT OUTER JOIN items as I ON L._ID = I.listID) WHERE L._ID = "
					+ message.toString()
					+ " GROUP BY I.listID ORDER BY L.listDateTime DESC";
		}
		this.dataHelper = new DataBaseHelper(this);
		this.dataSet = this.dataHelper.getReadableDatabase();
		this.dataRow = dataSet.rawQuery(this.query, null);

		if (this.dataRow.getCount() > 0) {
			this.dataRow.moveToFirst();
			this.shop = new Market(dataRow.getLong(0), dataRow.getString(1), dataRow.getString(2), dataRow.getString(3),
					dataRow.getString(4), dataRow.getLong(5));
		} else {
			System.err.println("Error in your database, files must be corrupted.");
			System.exit(1);
		}
		this.dataSet.close();
		this.setTitle("Shopping Lists: " + this.shop.getName().toString() + " [" + this.settings.getString("prefCurrencyType", "$") + " "
				+ dataRow.getFloat(6) + "]");
	}

	private void updateCheckbox() {
		for (int index = 0; index < this.goodList.size(); index++) {
			if (this.goodList.get(index).getPurchased() == 1) {
				this.widgetList.setItemChecked(index, true);
			}
		}
	}

	private void updateAdapter(Item good) {
		if (good.getPrice() == 0.0) {
			// this.listOfLists.add(dataRow.getString(2));
			this.listOfLists.add(good.getName() + "\n x" + good.getQuantity().toString());
		}
		if (good.getPrice() > 0.0) {
			// this.listOfLists.add(dataRow.getString(2));
			this.listOfLists.add(good.getName() + "\n x" + good.getQuantity().toString() + "\t"
					+ this.settings.getString("prefCurrencyType", "$") + " " + good.getPrice().toString());
		}
	}

	private void fetchTextAutoComplete() {
		if (this.settings.getBoolean("prefAutoComplete", false)) {
			this.query = "SELECT _ID FROM history";
			this.dataHelper = new DataBaseHelper(this);
			this.dataSet = this.dataHelper.getReadableDatabase();
			this.dataRow = dataSet.rawQuery(this.query, null);

			if (this.dataRow.getCount() > 0) {
				this.history = new ArrayList<String>();
				this.adapterOfHistory = new ArrayAdapter<String>(this, android.R.layout.test_list_item, this.history);
				this.widgetItem.setAdapter(this.adapterOfHistory);
				this.dataRow.moveToFirst();
				do {
					String item = new String(dataRow.getString(0));
					this.history.add(item);
				} while (this.dataRow.moveToNext());
			}
			this.dataSet.close();
		}
	}

	private void fetchListData() {
		if (this.settings.getBoolean("prefHideItems", false)) {
			this.query = "SELECT _ID, listID, itemName, itemDate, itemTime, itemDateTime, itemQuantity, itemPrice, itemPurchased "
					+ "FROM items WHERE listID = " + message.toString() + " AND itemPurchased = 0 ORDER BY itemPurchased, itemName";
		} else {
			this.query = "SELECT _ID, listID, itemName, itemDate, itemTime, itemDateTime, itemQuantity, itemPrice, itemPurchased "
					+ "FROM items WHERE listID = " + message.toString() + " ORDER BY itemPurchased, itemName";
		}
		this.dataHelper = new DataBaseHelper(this);
		this.dataSet = this.dataHelper.getReadableDatabase();
		this.dataRow = dataSet.rawQuery(this.query, null);

		this.goodList = new ArrayList<Item>();
		this.listOfLists = new ArrayList<String>();
		this.adapterOfLists = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, this.listOfLists);
		this.widgetList.setAdapter(this.adapterOfLists);
		this.widgetList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		if (this.dataRow.getCount() > 0) {
			this.dataRow.moveToFirst();
			do {
				Item good = new Item(dataRow.getLong(0), dataRow.getLong(1), dataRow.getString(2), dataRow.getString(3),
						dataRow.getString(4), dataRow.getString(5), dataRow.getInt(6), dataRow.getFloat(7), dataRow.getInt(8));
				this.updateAdapter(good);
				this.goodList.add(good);
			} while (this.dataRow.moveToNext());
			this.adapterOfLists.notifyDataSetChanged();
		}
		this.dataSet.close();
		this.updateCheckbox();
	}
	
	private void importAsda() {
		
	}
	
	private void importTesco() {
		
	}

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positionInt, long positionLong) {
			if (widgetList.isItemChecked(positionInt)) {
				query = "UPDATE items SET itemPurchased = 1 WHERE _ID = " + goodList.get(positionInt).getId();
				dataSet = dataHelper.getWritableDatabase();
				dataSet.execSQL(query);
				dataSet.close();
			} else {
				query = "UPDATE items SET itemPurchased = 0 WHERE _ID = " + goodList.get(positionInt).getId();
				dataSet = dataHelper.getWritableDatabase();
				dataSet.execSQL(query);
				dataSet.close();
			}
			fetchTitleData();
			fetchListData();
		}
	};
}
