package outlook;

import java.util.ArrayList;

import dataTypes.Item;
import android.content.Context;
import android.widget.ArrayAdapter;

public class CheckListView extends ArrayAdapter<Item>{
	private ArrayList<Item> listItems;

	public CheckListView(Context context, int resource, ArrayList<Item> listItems) {
		super(context, resource, listItems);
		this.listItems = new ArrayList<Item>();
		this.listItems.addAll(listItems);
	}
}
