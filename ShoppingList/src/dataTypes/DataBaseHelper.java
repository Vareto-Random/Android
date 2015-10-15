/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
	
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package dataTypes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	static final String dataBaseName = "SHOPPING";

	// static final String listTable = "LISTS";
	// static final String itemTable = "ITEMS";
	// static final String listId = "listId";
	// static final String listName = "listName";
	// static final String listDate = "listDate";
	// static final String listTime = "listTime";
	// static final String itemID = "itemId";
	// static final String itemName = "itemName";
	// static final String itemQuantity = "itemQuantity";
	// static final String itemValue = "itemValue";
	// static final String itemDate = "itemDate";
	// static final String itemTime = "itemTime";
	// static final String itemPurchased = "itemPurchased";
	// static final String itemList_fk = "itemList_fk";

	public DataBaseHelper(Context context) {
		super(context, dataBaseName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String queryList = "CREATE TABLE lists (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listName TEXT NOT NULL UNIQUE, listDate TEXT NOT NULL, listTime TEXT NOT NULL, listDateTime TEXT NOT NULL);";
		String queryItem = "CREATE TABLE items (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER NOT NULL, itemName TEXT NOT NULL, itemQuantity INTEGER DEFAULT 1, itemDate TEXT NOT NULL, itemTime TEXT NOT NULL, itemDateTime TEXT NOT NULL, itemPurchased INTEGER, itemPrice REAL DEFAULT 0.00, itemPriceAsda REAL DEFAULT 0.00, itemPriceTesco REAL DEFAULT 0.00);";
		String indexItem = "CREATE UNIQUE INDEX item_index ON items (listID, itemName)";
		String queryHist = "CREATE TABLE history (_ID STRING PRIMARY KEY)";
		db.execSQL(queryList);
		db.execSQL(queryItem);
		db.execSQL(indexItem);
		db.execSQL(queryHist);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w("Data Set", "Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS lists");
		db.execSQL("DROP TABLE IF EXISTS items");
		db.execSQL("DROP TABLE IF EXISTS history");
		onCreate(db);
	}
}
