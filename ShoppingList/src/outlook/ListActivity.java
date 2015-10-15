package outlook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.example.shoppinglist.R;

import dataTypes.DataBaseHelper;
import dataTypes.Item;
import dataTypes.JsonList;
import dataTypes.Market;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity implements OnClickListener {
	public final static String EXTRA_MESSAGE = "com.example.shoppinglist.message";
	public final static Integer RESULT_SETTINGS = 1;

	private EditText widgetField;
	private Button widgetAdd;
	private ListView widgetList;

	private ArrayList<String> listOfLists = null;
	private ArrayList<Market> shopList = null;
	private ArrayAdapter<String> adapterOfLists = null;

	private SharedPreferences settings;
	private StringBuilder settingsList;

	private String query = null;
	private String query2 = null;
	private DataBaseHelper dataHelper = null;
	private SQLiteDatabase dataSet = null;
	private Cursor dataRow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Shopping Lists");
		this.setContentView(R.layout.activity_list);
		this.createFolder();
		this.renameComponents();
		this.fetchSettings();
		this.fetchListData();

		widgetAdd.setOnClickListener(this);
		widgetList.setOnCreateContextMenuListener(this);
		widgetList.setOnItemClickListener(onItemClick);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		this.fetchListData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataSet.close();
		dataHelper.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add_shop:
			String listName = widgetField.getText().toString();
			if (listName.length() > 0) {
				try {
					this.query = "INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('" + listName
							+ "', date('now'), time('now'), datetime('now'));'";
					this.dataSet = this.dataHelper.getWritableDatabase();
					this.dataSet.execSQL(this.query);
					this.dataSet.close();
					this.widgetField.setText("");
					this.fetchListData();
					Toast.makeText(this, "Shopping list added", Toast.LENGTH_SHORT).show();
				} catch (SQLiteConstraintException e) {
					Toast.makeText(this, "List '" + listName + "' already exists", Toast.LENGTH_LONG).show();
					widgetField.setText("");
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent global = new Intent(this, SettingsActivity.class);
			startActivityForResult(global, RESULT_SETTINGS);
			break;
		case R.id.menu_import:
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				SimpleFileDialog FileOpenDialog = new SimpleFileDialog(ListActivity.this, "FileOpen",
						new SimpleFileDialog.SimpleFileDialogListener() {
							@Override
							public void onChosenDir(String chosenDir) {
								JsonList importFile = new JsonList();
								importFile.open(ListActivity.this, chosenDir);
								importFile.generateObject();
								importFile.saveSqlObject(ListActivity.this);
								fetchListData();
							}
						});
				FileOpenDialog.Default_File_Name = "";
				FileOpenDialog.chooseFile_or_Dir();
			} else {
				Toast.makeText(ListActivity.this, "SD card not present", Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		menu.setHeaderTitle(shopList.get(info.position).getName());
		menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Details");
		menu.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "Export");
		menu.add(Menu.NONE, Menu.FIRST + 3, Menu.NONE, "Rename");
		menu.add(Menu.NONE, Menu.FIRST + 4, Menu.NONE, "Remove");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1: // details
			this.onContextItemDetail(item);
			break;
		case Menu.FIRST + 2: // export
			this.onContextItemExport(item);
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
		this.fetchListData();
		return true;
	}

	private void createFolder() {
		boolean mExternalStorageReadable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageReadable = true;
			mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageReadable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageReadable = false;
			mExternalStorageWriteable = false;
		}
		if (mExternalStorageReadable && mExternalStorageWriteable) {
			File folder = new File(Environment.getExternalStorageDirectory().toString() + "/ShoppingList");
			if (!folder.exists()) {
				folder.mkdir();
			}
		}
	}

	private void renameComponents() {
		widgetField = (EditText) findViewById(R.id.editText_add_shop);
		widgetAdd = (Button) findViewById(R.id.button_add_shop);
		widgetList = (ListView) findViewById(R.id.listView_add_shop);
	}

	/*
	 * private void onContextItemOpen(MenuItem item) { final
	 * AdapterContextMenuInfo info = (AdapterContextMenuInfo)
	 * item.getMenuInfo(); Intent global = new Intent(ListActivity.this,
	 * ItemActivity.class); global.putExtra(EXTRA_MESSAGE,
	 * String.valueOf(shopList.get(info.position).getId()));
	 * this.startActivity(global); }
	 */

	private void onContextItemDetail(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Dialog detailDialog = new Dialog(ListActivity.this);

		this.query = "SELECT I.listID, SUM(I.itemPurchased), COUNT(*), SUM(I.itemQuantity * I.itemPrice) FROM items AS I WHERE I.listID = "
				+ this.shopList.get(info.position).getId() + " GROUP BY I.listID";
		this.dataHelper = new DataBaseHelper(this);
		this.dataSet = this.dataHelper.getReadableDatabase();
		this.dataRow = dataSet.rawQuery(this.query, null);

		detailDialog.setContentView(R.layout.fragment_list_details);
		detailDialog.setTitle("List details:");

		TextView listName = (TextView) detailDialog.findViewById(R.id.textView_listName);
		TextView totPrice = (TextView) detailDialog.findViewById(R.id.textView_totalPrice);
		TextView totItems = (TextView) detailDialog.findViewById(R.id.textView_numItems);
		TextView purItems = (TextView) detailDialog.findViewById(R.id.textView_purchasedItems);
		TextView datCreat = (TextView) detailDialog.findViewById(R.id.textView_creationDate);
		TextView timCreat = (TextView) detailDialog.findViewById(R.id.textView_creationTime);

		if (this.dataRow.getCount() == 0) {
			this.dataRow.moveToFirst();
			listName.setText(this.shopList.get(info.position).getName());
			totPrice.setText("Total price: " + this.settings.getString("prefCurrencyType", "$") + "0.0");
			totItems.setText("Total items: 0");
			purItems.setText("Purchased items: 0");
			datCreat.setText("Creation date: " + this.shopList.get(info.position).getDate());
			timCreat.setText("Creation time: " + this.shopList.get(info.position).getTime());
		} else if (this.dataRow.getCount() == 1) {
			this.dataRow.moveToFirst();
			listName.setText(this.shopList.get(info.position).getName());
			totPrice.setText("Total price: " + settings.getString("prefCurrencyType", "$") + this.dataRow.getString(3));
			totItems.setText("Total items: " + this.dataRow.getString(2));
			purItems.setText("Purchased items: " + this.dataRow.getShort(1));
			datCreat.setText("Creation date: " + this.shopList.get(info.position).getDate());
			timCreat.setText("Creation time: " + this.shopList.get(info.position).getTime());
		} else {
			System.err.println("Error in your database, files must be corrupted.");
			System.exit(1);
		}
		this.dataSet.close();
		detailDialog.show();
	}

	private void onContextItemExport(MenuItem item) {
			final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			final Market shop = this.shopList.get(info.position);
			ArrayList<Item> goodList = new ArrayList<Item>();

			this.query = "SELECT _ID, listID, itemName, itemDate, itemTime, itemDateTime, itemQuantity, itemPrice, itemPurchased "
					+ "FROM items WHERE listID = " + shop.getId() + " ORDER BY itemPurchased, itemName";
			this.dataHelper = new DataBaseHelper(this);
			this.dataSet = this.dataHelper.getReadableDatabase();
			this.dataRow = dataSet.rawQuery(this.query, null);

			if (this.dataRow.getCount() > 0) {
				this.dataRow.moveToFirst();
				do {
					Item good = new Item(dataRow.getLong(0), dataRow.getLong(1), dataRow.getString(2), dataRow.getString(3),
							dataRow.getString(4), dataRow.getString(5), dataRow.getInt(6), dataRow.getFloat(7), dataRow.getInt(8));
					goodList.add(good);
				} while (this.dataRow.moveToNext());

				try {
					JsonList exportFile = new JsonList(shop, goodList);
					exportFile.generateJson();
					exportFile.save(ListActivity.this);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			this.dataSet.close();
	}

	private void onContextItemRename(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final EditText newName = new EditText(ListActivity.this);
		newName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		newName.setText(this.shopList.get(info.position).getName());
		newName.setSelection(newName.getText().length());

		AlertDialog.Builder renameDialogBuilder = new AlertDialog.Builder(ListActivity.this);
		// renameDialogBuilder.setIcon(R.drawable.ic_launcher);
		renameDialogBuilder.setTitle("Rename list");
		renameDialogBuilder.setView(newName);
		renameDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					Market shop = shopList.get(info.position);
					query = "UPDATE lists SET listName = '" + newName.getText().toString() + "' WHERE _ID = " + shop.getId();
					dataSet = dataHelper.getWritableDatabase();
					dataSet.execSQL(query);
					dataSet.close();
					fetchListData();
				} catch (SQLiteConstraintException e) {
					Toast.makeText(getApplicationContext(), "List '" + newName.getText().toString() + "' already exists", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		renameDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		this.fetchListData();
		AlertDialog renameDialog = renameDialogBuilder.create();
		renameDialog.show();
	}

	private void onContextItemRemove(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		AlertDialog.Builder removeDialogBuilder = new AlertDialog.Builder(ListActivity.this);
		removeDialogBuilder.setTitle("Remove " + this.shopList.get(info.position).getName());
		removeDialogBuilder.setMessage("Are you sure?");
		removeDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dataSet = dataHelper.getWritableDatabase();
				query = "DELETE FROM items WHERE listID = " + shopList.get(info.position).getId();
				query2 = "DELETE FROM lists WHERE _ID = " + shopList.get(info.position).getId();
				dataSet.execSQL(query);
				dataSet.execSQL(query2);
				dataSet.close();
				fetchListData();
			}
		});
		removeDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog renameDialog = removeDialogBuilder.create();
		renameDialog.show();
	}

	private void fetchSettings() {
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		settingsList = new StringBuilder();
		settingsList.append(settings.getString("prefCurrencyType", "$"));
	}

	private void fetchListData() {
		this.query = "SELECT L._ID, L.listName, L.listDate, L.listTime, L.listDateTime, COUNT(I.listID), SUM(I.itemPurchased) FROM (lists AS L LEFT OUTER JOIN items as I ON L._ID = I.listID) GROUP BY L._ID ORDER BY L.listDateTime DESC";
		this.dataHelper = new DataBaseHelper(this);
		this.dataSet = this.dataHelper.getReadableDatabase();
		this.dataRow = dataSet.rawQuery(this.query, null);

		this.shopList = new ArrayList<Market>();
		this.listOfLists = new ArrayList<String>();
		this.adapterOfLists = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfLists);
		this.widgetList.setAdapter(adapterOfLists);

		if (this.dataRow.getCount() > 0) {
			this.dataRow.moveToFirst();
			do {
				Market shop = new Market(dataRow.getLong(0), dataRow.getString(1), dataRow.getString(2), dataRow.getString(3),
						dataRow.getString(4), dataRow.getLong(5));
				this.listOfLists.add(dataRow.getString(1));
				this.shopList.add(shop);
			} while (this.dataRow.moveToNext());
			this.adapterOfLists.notifyDataSetChanged();
		}
		this.dataSet.close();
	}

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positionInt, long positionLong) {
			Intent global = new Intent(ListActivity.this, ItemActivity.class);
			global.putExtra(EXTRA_MESSAGE, String.valueOf(shopList.get(positionInt).getId()));
			startActivity(global);
			fetchListData();
		}
	};
}
